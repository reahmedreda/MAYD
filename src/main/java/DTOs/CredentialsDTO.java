package DTOs;

public class CredentialsDTO {
    String username,password,error;
    boolean expectedToLogin;

    public CredentialsDTO(){};
    public CredentialsDTO(String username, String password, String error, boolean expectedToLogin) {
        this.username = username;
        this.password = password;
        this.error = error;
        this.expectedToLogin = expectedToLogin;
    }

    public CredentialsDTO(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public boolean isExpectedToLogin() {
        return expectedToLogin;
    }

    public void setExpectedToLogin(boolean expectedToLogin) {
        this.expectedToLogin = expectedToLogin;
    }

    public enum propertiesNames{
        username("username"),
        password("password"),
        expectedToLogin("expectedToLogin"),
        expectedErrorMessage("expectedErrorMessage");

        public String value;
        propertiesNames(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return String.valueOf(value);
        }
    }
}
