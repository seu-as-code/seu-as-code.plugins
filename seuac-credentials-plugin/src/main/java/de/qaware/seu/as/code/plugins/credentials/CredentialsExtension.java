package de.qaware.seu.as.code.plugins.credentials;

/**
 * The extension class for additional configuration options.
 *
 * @author lreimer
 */
public class CredentialsExtension {
    /**
     * Name of the credentials configuration extension in the build script.
     */
    public static final String NAME = "credentials";

    private String keychainFile = null;
    private String propertyFile = null;

    public String getKeychainFile() {
        return keychainFile;
    }

    public void setKeychainFile(String keychainFile) {
        this.keychainFile = keychainFile;
    }

    public String getPropertyFile() {
        return propertyFile;
    }

    public void setPropertyFile(String propertyFile) {
        this.propertyFile = propertyFile;
    }
}
