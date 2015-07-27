/**
 *    Copyright 2015 QAware GmbH
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
package de.qaware.seu.as.code.plugins.credentials.impl;

import de.qaware.seu.as.code.plugins.credentials.Credentials;
import org.apache.commons.codec.binary.Base64;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Properties;

/**
 * Stores the credentials in a property file.
 */
public class PropertyCredentials implements Credentials {
    /**
     * UTF-8 charset.
     */
    private static final Charset UTF_8 = Charset.forName("UTF-8");
    /**
     * Properties.
     */
    private final Properties properties;
    /**
     * Properties file.
     */
    private final File credentialsFile;
    /**
     * Encryptor.
     */
    private final Encryptor encryptor;

    /**
     * Creates a new instance.
     *
     * @param credentialsFile Properties file.
     * @param encryptor       Encryptor.
     * @throws IOException If something went wrong while loading the properties file.
     */
    public PropertyCredentials(File credentialsFile, Encryptor encryptor) throws IOException {
        this.credentialsFile = credentialsFile;
        this.encryptor = encryptor;

        properties = new Properties();
        if (credentialsFile.exists()) {
            loadProperties(credentialsFile);
        }
    }

    /**
     * Loads the properties from the given file.
     *
     * @param file File.
     * @throws IOException If something went wrong while loading the properties file.
     */
    private void loadProperties(File file) throws IOException {
        FileInputStream fileInputStream = new FileInputStream(file);
        try {
            properties.load(fileInputStream);
        } finally {
            fileInputStream.close();
        }
    }

    @Override
    public String get(String key) {
        String encodedValue = properties.getProperty(key);
        if (encodedValue == null) {
            return null;
        }

        byte[] encryptedValue = Base64.decodeBase64(encodedValue);
        byte[] decryptedValue = encryptor.decrypt(encryptedValue);

        return new String(decryptedValue, UTF_8);
    }

    @Override
    public void set(String key, String value) {
        byte[] encryptedValue = encryptor.encrypt(value.getBytes(UTF_8));
        String encodedValue = Base64.encodeBase64String(encryptedValue);

        properties.setProperty(key, encodedValue);
    }

    @Override
    public void save() throws IOException {
        FileOutputStream fileOutputStream = new FileOutputStream(credentialsFile);
        try {
            properties.store(fileOutputStream, "");
        } finally {
            fileOutputStream.close();
        }
    }
}
