package xyz.moonrabbit.bittrex;

import java.io.IOException;
import java.io.InputStream;

public class BittrexBuilder {

    public static final int DEFAULT_RETRY_ATTEMPTS = 1;
    public static final int DEFAULT_RETRY_DELAY = 15;

    private String apikey;
    private String secret;
    private int retryAttempts;
    private int retryDelaySeconds;
    private InputStream keyFileStream;

    public BittrexBuilder setApikey(String apikey) {
        this.apikey = apikey;
        return this;
    }

    public BittrexBuilder setSecret(String secret) {
        this.secret = secret;
        return this;
    }

    public BittrexBuilder setRetryAttempts(int retryAttempts) {
        this.retryAttempts = retryAttempts == 0 ? DEFAULT_RETRY_ATTEMPTS : retryAttempts;
        return this;
    }

    public BittrexBuilder setRetryDelaySeconds(int retryDelaySeconds) {
        this.retryDelaySeconds = retryDelaySeconds == 0 ? DEFAULT_RETRY_DELAY : retryDelaySeconds;
        return this;
    }

    public BittrexBuilder setKeyFileStream(InputStream keyFileStream) {
        this.keyFileStream = keyFileStream;
        return this;
    }

    public Bittrex build() throws IOException {
        return new Bittrex(apikey, secret, retryAttempts, retryDelaySeconds, keyFileStream);
    }
}