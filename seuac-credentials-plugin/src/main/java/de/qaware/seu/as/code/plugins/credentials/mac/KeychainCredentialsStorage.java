/*
 *    Copyright (C) 2015 QAware GmbH
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package de.qaware.seu.as.code.plugins.credentials.mac;

import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.PointerByReference;
import de.qaware.seu.as.code.plugins.credentials.Credentials;
import de.qaware.seu.as.code.plugins.credentials.CredentialsException;
import de.qaware.seu.as.code.plugins.credentials.CredentialsExtension;
import de.qaware.seu.as.code.plugins.credentials.CredentialsStorage;
import org.apache.commons.codec.binary.Base64;

import static com.sun.jna.Native.loadLibrary;

/**
 * The platform specific implementation that uses the MacSO keychain mechanism.
 *
 * @author lreimer
 */
public class KeychainCredentialsStorage implements CredentialsStorage {

    private static final String SERVICE_NAME = "SEU-as-code";
    private static final String SEC_KEYCHAIN_ADD_GENERIC_PASSWORD = "SecKeychainAddGenericPassword";
    private static final String SEC_KEYCHAIN_FIND_GENERIC_PASSWORD = "SecKeychainFindGenericPassword";
    private static final String SEC_KEYCHAIN_ITEM_FREE_CONTENT = "SecKeychainItemFreeContent";
    private static final String SEC_KEYCHAIN_ITEM_MODIFY_CONTENT = "SecKeychainItemModifyContent";
    private static final String SEC_KEYCHAIN_ITEM_DELETE = "SecKeychainItemDelete";
    private static final String SEC_KEYCHAIN_OPEN = "SecKeychainOpen";

    private final Security security;
    private final CoreFoundation coreFoundation;
    private final String keychainFile;

    /**
     * Initialze the instance with the required native library references.
     *
     * @param extension the credentials configuration extension
     */
    public KeychainCredentialsStorage(CredentialsExtension extension) {
        this.keychainFile = extension.getKeychainFile();
        this.security = (Security) loadLibrary("Security", Security.class);
        this.coreFoundation = (CoreFoundation) loadLibrary("CoreFoundation", CoreFoundation.class);
    }

    /**
     * Initialize the instance with the given Security and CoreFoundation references.
     *
     * @param security       the MacOS security library instance
     * @param coreFoundation the MacOS core foundation library reference
     */
    KeychainCredentialsStorage(Security security, CoreFoundation coreFoundation) {
        this.keychainFile = null;
        this.security = security;
        this.coreFoundation = coreFoundation;
    }

    @Override
    public Credentials findCredentials(String service) {
        IntByReference secretLength = new IntByReference();
        PointerByReference secretPointer = new PointerByReference();

        // try to find the generic password
        Pointer keychainPointer = getKeychain();
        int status = security.SecKeychainFindGenericPassword(keychainPointer, SERVICE_NAME.length(), SERVICE_NAME.getBytes(), service.length(), service.getBytes(), secretLength, secretPointer, null);
        releaseKeychain(keychainPointer);

        if (status == Security.errSecItemNotFound) {
            // not an error, return nothing
            return null;
        } else if (status != Security.errSecSuccess) {
            throw new CredentialsException(fromNameAndStatusCode(SEC_KEYCHAIN_FIND_GENERIC_PASSWORD, status));
        }

        // convert found secret to string
        String secret = Native.toString(secretPointer.getValue().getByteArray(0, secretLength.getValue()));

        // perform cleanup and done
        status = security.SecKeychainItemFreeContent(null, secretPointer.getValue());
        if (status != Security.errSecSuccess) {
            throw new CredentialsException(fromNameAndStatusCode(SEC_KEYCHAIN_ITEM_FREE_CONTENT, status));
        }

        // and return credential from secret string
        return Credentials.fromSecret(new String(Base64.decodeBase64(secret), UTF_8));
    }

    @Override
    public void setCredentials(String service, String username, char[] password) {
        setCredentials(service, new Credentials(username, new String(password)));
    }

    @Override
    public void setCredentials(String service, Credentials credentials) {
        PointerByReference itemRef = new PointerByReference();
        String secret = credentials.toSecret();
        String password = Base64.encodeBase64String(secret.getBytes(UTF_8));

        Pointer keychainPointer = getKeychain();
        int status = security.SecKeychainFindGenericPassword(keychainPointer, SERVICE_NAME.length(), SERVICE_NAME.getBytes(), service.length(), service.getBytes(), null, null, itemRef);
        releaseKeychain(keychainPointer);

        if (status == Security.errSecItemNotFound) {
            // then we perform an add operation of a generic password
            keychainPointer = getKeychain();
            status = security.SecKeychainAddGenericPassword(keychainPointer, SERVICE_NAME.length(), SERVICE_NAME.getBytes(), service.length(), service.getBytes(), password.length(), password.getBytes(), null);
            releaseKeychain(keychainPointer);

            if (status != Security.errSecSuccess) {
                throw new CredentialsException(fromNameAndStatusCode(SEC_KEYCHAIN_ADD_GENERIC_PASSWORD, status));
            }
        } else if (status == Security.errSecSuccess) {
            // then we perform an update operation if the generic password
            status = security.SecKeychainItemModifyContent(itemRef.getValue(), null, password.length(), password.getBytes());
            coreFoundation.CFRelease(itemRef.getValue());
            if (status != Security.errSecSuccess) {
                throw new CredentialsException(fromNameAndStatusCode(SEC_KEYCHAIN_ITEM_MODIFY_CONTENT, status));
            }
        } else {
            // something else went wrong
            throw new CredentialsException(fromNameAndStatusCode(SEC_KEYCHAIN_FIND_GENERIC_PASSWORD, status));
        }
    }

    @Override
    public void clearCredentials(String service) {
        PointerByReference itemRef = new PointerByReference();

        Pointer keychainPointer = getKeychain();
        int status = security.SecKeychainFindGenericPassword(keychainPointer, SERVICE_NAME.length(), SERVICE_NAME.getBytes(), service.length(), service.getBytes(), null, null, itemRef);
        releaseKeychain(keychainPointer);

        if (status == Security.errSecItemNotFound) {
            // we skip here, no error and no message
            return;
        } else if (status != Security.errSecSuccess) {
            throw new CredentialsException(fromNameAndStatusCode(SEC_KEYCHAIN_FIND_GENERIC_PASSWORD, status));
        }

        status = security.SecKeychainItemDelete(itemRef.getValue());
        coreFoundation.CFRelease(itemRef.getValue());
        if (status != Security.errSecSuccess) {
            throw new CredentialsException(fromNameAndStatusCode(SEC_KEYCHAIN_ITEM_DELETE, status));
        }
    }

    private Pointer getKeychain() {
        PointerByReference keychainPointer = new PointerByReference();
        if (keychainFile != null) {
            int status = security.SecKeychainOpen(keychainFile, keychainPointer);
            if (status != Security.errSecSuccess) {
                throw new CredentialsException(fromNameAndStatusCode(SEC_KEYCHAIN_OPEN, status));
            }
        }
        return keychainPointer.getValue();
    }

    private void releaseKeychain(Pointer keychainPointer) {
        if (keychainPointer != null) {
            coreFoundation.CFRelease(keychainPointer);
        }
    }

    private String fromNameAndStatusCode(String method, int status) {
        StringBuilder result = new StringBuilder();
        result.append(getClass().getName()).append(": ");
        result.append("Calling ").append(method).append(" returned status code ").append(status);
        String description = translateStatusCode(status);
        if (description != null) {
            result.append(" which translates to '").append(description).append("'");
        }
        return result.toString();
    }

    private String translateStatusCode(int status) {
        Pointer message = security.SecCopyErrorMessageString(status, null);
        int lengthInChars = coreFoundation.CFStringGetLength(message);
        int potentialLengthInBytes = 3 * lengthInChars + 1;
        byte[] buffer = new byte[potentialLengthInBytes];
        boolean res = coreFoundation.CFStringGetCString(message, buffer, potentialLengthInBytes, CoreFoundation.kCFStringEncodingUTF8);
        coreFoundation.CFRelease(message);
        return res ? Native.toString(buffer, UTF_8.name()) : "";
    }
}
