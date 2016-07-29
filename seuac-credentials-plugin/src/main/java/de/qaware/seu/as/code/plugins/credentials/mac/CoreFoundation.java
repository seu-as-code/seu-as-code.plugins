package de.qaware.seu.as.code.plugins.credentials.mac;

import com.sun.jna.Library;
import com.sun.jna.Pointer;

/**
 * The JNA interface to access the CoreFoundation library under MacOS.
 */
public interface CoreFoundation extends Library {
    /**
     * Releases a Core Foundation object.
     * <br/>
     * void CFRelease ( CFTypeRef cf );
     *
     * @param cf A CFType object to release. This value must not be NULL.
     */
    void CFRelease(Pointer cf);

    /**
     * Returns the number (in terms of UTF-16 code pairs) of Unicode characters in a string.
     * <br/>
     * CFIndex CFStringGetLength ( CFStringRef theString );
     *
     * @param theString The string to examine.
     * @return The number (in terms of UTF-16 code pairs) of characters stored in theString.
     */
    int CFStringGetLength(Pointer theString);

    /**
     * Quickly obtains a pointer to the contents of a string as a buffer of Unicode characters.
     * <br/>
     * const UniChar * CFStringGetCharactersPtr ( CFStringRef theString );
     *
     * @param theString The string whose contents you wish to access.
     * @return A pointer to a buffer of Unicode character, or NULL if the internal storage of theString does not allow this to be returned efficiently.
     */
    Pointer CFStringGetCharactersPtr(Pointer theString);

    /**
     * Copies the character contents of a string to a local C string buffer after converting the characters to a given encoding.
     * <br/>
     * Boolean CFStringGetCString ( CFStringRef theString, char *buffer, CFIndex bufferSize, CFStringEncoding encoding );
     *
     * @param theString  The string whose contents you wish to access.
     * @param buffer     The C string buffer into which to copy the string. On return, the buffer contains the converted characters. If there is an error in conversion, the buffer contains only partial results. The buffer must be large enough to contain the converted characters and a NUL terminator. For example, if the string is Toby, the buffer must be at least 5 bytes long.
     * @param bufferSize The length of buffer in bytes.
     * @param encoding   The string encoding to which the character contents of theString should be converted. The encoding must specify an 8-bit encoding.
     * @return true upon success or false if the conversion fails or the provided buffer is too small.
     */
    boolean CFStringGetCString(Pointer theString, byte[] buffer, int bufferSize, int encoding);

    /**
     * Quickly obtains a pointer to a C-string buffer containing the characters of a string in a given encoding.
     * <br/>
     * const char * CFStringGetCStringPtr ( CFStringRef theString, CFStringEncoding encoding );
     *
     * @param theString The string whose contents you wish to access.
     * @param encoding  The string encoding to which the character contents of theString should be converted. The encoding must specify an 8-bit encoding.
     * @return A pointer to a C string or NULL if the internal storage of theString does not allow this to be returned efficiently.
     */
    Pointer CFStringGetCStringPtr(Pointer theString, int encoding);


    int kCFStringEncodingMacRoman = 0;
    int kCFStringEncodingWindowsLatin1 = 0x0500;
    int kCFStringEncodingISOLatin1 = 0x0201;
    int kCFStringEncodingNextStepLatin = 0x0B01;
    int kCFStringEncodingASCII = 0x0600;
    int kCFStringEncodingUnicode = 0x0100;
    int kCFStringEncodingUTF8 = 0x08000100;
    int kCFStringEncodingNonLossyASCII = 0x0BFF;
    int kCFStringEncodingUTF16 = 0x0100;
    int kCFStringEncodingUTF16BE = 0x10000100;
    int kCFStringEncodingUTF16LE = 0x14000100;
    int kCFStringEncodingUTF32 = 0x0c000100;
    int kCFStringEncodingUTF32BE = 0x18000100;
    int kCFStringEncodingUTF32LE = 0x1c000100;
}
