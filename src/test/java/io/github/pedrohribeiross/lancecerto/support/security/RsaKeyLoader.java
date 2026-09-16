package io.github.pedrohribeiross.lancecerto.support.security;

import org.springframework.core.io.ClassPathResource;
import org.springframework.security.converter.RsaKeyConverters;

import java.io.IOException;
import java.io.InputStream;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

// É uma classe só de métodos estáticos. Marcar com final e construtor privado comunica "não instancie, não herde".
public final class RsaKeyLoader {

    private RsaKeyLoader() {
    }

    public static RSAPublicKey loadPublicKey(String classpathLocation) {
        // ClassPathResource -> procura o arquivo no classpath. O caminho é relativo à raiz de resources.
        try (InputStream in = new ClassPathResource(classpathLocation).getInputStream()) {
            return RsaKeyConverters.x509().convert(in);
        } catch (IOException e) {
            throw new IllegalStateException(
                    "Não consegui ler a chave pública em: " + classpathLocation, e
            );
        }
    }

    public static RSAPrivateKey loadPrivateKey(String classpathLocation) {
        // try-with-resources fecha o arquivo sozinho no fim. Cada converter consome o stream, então cada chamada abre o seu
        try (InputStream in = new ClassPathResource(classpathLocation).getInputStream()) {
            return RsaKeyConverters.pkcs8().convert(in);
        } catch (IOException e) {
            throw new IllegalStateException(
                    "Não consegui ler a chave privada em: " + classpathLocation, e
            );
        }
    }
}
