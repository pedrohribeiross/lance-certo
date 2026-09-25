package io.github.pedrohribeiross.lancecerto.support.security;

import org.springframework.security.oauth2.jwt.JwtClaimsSet;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public class TokenBuilder {

    private final TestTokenFactory tokenFactory;
    private final Clock clock = Clock.systemUTC();

    // valores padrões: um ‘token’ válido, sem papéis, válido por 1 hora
    private String subject = UUID.randomUUID().toString();
    private List<String> roles = List.of();
    private Instant issuedAt = clock.instant();
    private Duration expired = Duration.ofHours(1);
    private String issuer;

    public TokenBuilder(TestTokenFactory tokenFactory) {
        this.tokenFactory = tokenFactory;
    }

    public TokenBuilder subject(String subject) {
        this.subject = subject;
        return this;
    }

    public TokenBuilder roles(String... roles) {
        this.roles = List.of(roles);
        return this;
    }

    public TokenBuilder issuer(String issuer) {
        this.issuer = issuer;
        return this;
    }

    public TokenBuilder expired() {
        this.issuedAt = clock.instant().minus(Duration.ofHours(2));
        this.expired = Duration.ofHours(1);
        return this;
    }

    public String build() {
        JwtClaimsSet.Builder claims = JwtClaimsSet.builder()
                .subject(subject)
                .issuedAt(issuedAt)
                .expiresAt(issuedAt.plus(expired))
                .claim("roles", roles);

        if (issuer != null) {
            claims.issuer(issuer);
        }

        return tokenFactory.encode(claims.build());
    }
}
