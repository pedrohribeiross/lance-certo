package io.github.pedrohribeiross.lancecerto.support.security;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import org.springframework.security.oauth2.jose.jws.SignatureAlgorithm;
import org.springframework.security.oauth2.jwt.*;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.UUID;

public class TestTokenFactory {

    private final JwtEncoder encoder;

    public TestTokenFactory(RSAPublicKey publicKey, RSAPrivateKey privateKey) {
        RSAKey jwk = new RSAKey.Builder(publicKey)
                .privateKey(privateKey)
                .keyID(UUID.randomUUID().toString())
                .build();

        this.encoder = new NimbusJwtEncoder(new ImmutableJWKSet<>(new JWKSet(jwk)));
    }

    // Fábrica que assina com as chaves reais da aplicação.
    public static TestTokenFactory app() {
        return new TestTokenFactory(TestKeys.APP_PUBLIC, TestKeys.APP_PRIVATE);
    }

    // Fábrica que assina com o par intruso: tudo que ela emitir deve ser rejeitado.
    public static TestTokenFactory rogue() {
        return new TestTokenFactory(TestKeys.ROGUE_PUBLIC, TestKeys.ROGUE_PRIVATE);
    }

    // Recebe os claims prontos e devolve a string do JWT assinado.
    public String encode(JwtClaimsSet claims) {
        JwsHeader header = JwsHeader.with(SignatureAlgorithm.RS256).build();
        return encoder.encode(JwtEncoderParameters.from(header, claims)).getTokenValue();
    }
}
