package com.glyfly.librarys.rsa;

import java.math.BigInteger;

/**
 * Created by zhangfaming on 2017/9/6.
 */

public class PublicKey {

    private final BigInteger n;

    private final BigInteger b;

    public PublicKey(BigInteger n, BigInteger b) {
        this.n = n;
        this.b = b;
    }

    public BigInteger getN() {
        return n;
    }

    public BigInteger getB() {
        return b;
    }
}
