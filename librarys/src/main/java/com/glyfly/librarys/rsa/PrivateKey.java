package com.glyfly.librarys.rsa;

import java.math.BigInteger;

/**
 * Created by zhangfaming on 2017/9/6.
 */

public class PrivateKey {

    private final BigInteger n;

    private final BigInteger a;

    public PrivateKey(BigInteger n, BigInteger a) {
        this.n = n;
        this.a = a;
    }

    public BigInteger getN() {
        return n;
    }

    public BigInteger getA() {
        return a;
    }
}
