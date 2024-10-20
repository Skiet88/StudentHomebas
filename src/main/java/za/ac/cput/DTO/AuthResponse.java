package za.ac.cput.DTO;

import java.io.Serializable;

public class AuthResponse implements Serializable {
    private String token;
    private String role;
    private String redirectUrl;

    protected AuthResponse() {
    }

    // Private constructor to force the use of the builder
    private AuthResponse(Builder builder) {
        this.token = builder.token;
        this.redirectUrl = builder.redirectUrl;
        this.role = builder.role;
    }

    public String getToken() {
        return token;
    }

    public String getRole() {
        return role;
    }

    public String getRedirectUrl() {
        return redirectUrl;
    }

    // Static nested Builder class
    public static class Builder {
        private String token;
        private String role;
        private String redirectUrl;

        public Builder setToken(String token) {
            this.token = token;
            return this;
        }

        public Builder setRole(String role) {
            this.role = role;
            return this;
        }

        public Builder setRedirectUrl(String redirectUrl) {
            this.redirectUrl = redirectUrl;
            return this;
        }

        public AuthResponse build() {
            return new AuthResponse(this);
        }
    }
}
