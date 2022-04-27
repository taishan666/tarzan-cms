package com.tarzan.cms.auth.dto;


import com.tarzan.cms.utils.Func;

public class ClientInfo {
    private String clientId;
    private String clientSecret;

    public boolean valid() {
        return !Func.isAnyBlank(new CharSequence[]{this.clientId, this.clientSecret});
    }

    public static ClientInfo.ClientInfoBuilder builder() {
        return new ClientInfo.ClientInfoBuilder();
    }

    public String getClientId() {
        return this.clientId;
    }

    public String getClientSecret() {
        return this.clientSecret;
    }

    public void setClientId(final String clientId) {
        this.clientId = clientId;
    }

    public void setClientSecret(final String clientSecret) {
        this.clientSecret = clientSecret;
    }


    public ClientInfo() {
    }

    public ClientInfo(final String clientId, final String clientSecret) {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
    }

    public static class ClientInfoBuilder {
        private String clientId;
        private String clientSecret;

        ClientInfoBuilder() {
        }

        public ClientInfo.ClientInfoBuilder clientId(final String clientId) {
            this.clientId = clientId;
            return this;
        }

        public ClientInfo.ClientInfoBuilder clientSecret(final String clientSecret) {
            this.clientSecret = clientSecret;
            return this;
        }

        public ClientInfo build() {
            return new ClientInfo(this.clientId, this.clientSecret);
        }

        public String toString() {
            return "ClientInfo.ClientInfoBuilder(clientId=" + this.clientId + ", clientSecret=" + this.clientSecret + ")";
        }
    }
}
