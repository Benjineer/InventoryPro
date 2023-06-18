package com.alamu817group4.inventorypro.configurations;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;

@Component
public class RSAKeyGenerator {

    private PrivateKey privateKey;
    private PublicKey publicKey;

    @PostConstruct
    public void generateKeys() throws Exception {
        KeyPair keyPair = generateRSAKeyPair();
        privateKey = keyPair.getPrivate();
        publicKey = keyPair.getPublic();
    }

    private KeyPair generateRSAKeyPair() throws Exception {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048); // Adjust key size as per your requirements
        return keyPairGenerator.generateKeyPair();
    }

    public PrivateKey getPrivateKey() {
        return privateKey;
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }
}
