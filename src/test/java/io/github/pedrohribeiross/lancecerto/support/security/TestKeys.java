package io.github.pedrohribeiross.lancecerto.support.security;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

public final class TestKeys {

    private static final String PUBLIC = "app.pub";
    private static final String PRIVATE = "app.key";

    public static final RSAPublicKey APP_PUBLIC = RsaKeyLoader.loadPublicKey(PUBLIC);
    public static final RSAPrivateKey APP_PRIVATE = RsaKeyLoader.loadPrivateKey(PRIVATE);

    private static final KeyPair INVALID_KEY = generateKeyPair();

    public static final RSAPublicKey ROGUE_PUBLIC = (RSAPublicKey) INVALID_KEY.getPublic();
    public static final RSAPrivateKey ROGUE_PRIVATE = (RSAPrivateKey) INVALID_KEY.getPrivate();

    private static KeyPair generateKeyPair() {
        try {
            KeyPairGenerator generate = KeyPairGenerator.getInstance("RSA");
            generate.initialize(2048);
            return generate.generateKeyPair();
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("RSA não disponível nesta JVM", e);
        }
    }
}
