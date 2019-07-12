package com.glyfly.librarys.rsa;

/**
 * Created by zhangfaming on 2017/9/6.
 */

public class RSAKeyPair {
    private final String privateKey;

    private final String publicKey;

    public RSAKeyPair(String publicKey, String privateKey) {
        this.privateKey = privateKey;
        this.publicKey = publicKey;
    }

    public String getPrivateKey() {
        return privateKey;
    }

    public String getPublicKey() {
        return publicKey;
    }
}
