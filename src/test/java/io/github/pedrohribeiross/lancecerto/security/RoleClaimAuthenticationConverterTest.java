package io.github.pedrohribeiross.lancecerto.security;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

class RoleClaimAuthenticationConverterTest {

    private final RoleClaimAuthenticationConverter converter = new RoleClaimAuthenticationConverter();

    @Test
    @DisplayName("Should convert a single 'roles' claim entry into a ROLE_ prefixed authority")
    void shouldConvertSingleRoleClaimToPrefixedAuthority() {
        var jwt = jwtWithRoles(List.of("ADMIN"));

        var auth = converter.convert(jwt);

        assertThat(auth).isNotNull();
        assertThat(roleAuthorities(auth)).containsExactly("ROLE_ADMIN");
    }

    @Test
    @DisplayName("Should convert every 'roles' claim entry into its own ROLE_ prefixed authority")
    void shouldConvertMultipleRoleClaimEntriesToPrefixedAuthorities() {
        var jwt = jwtWithRoles(List.of("ADMIN", "USER"));

        var auth = converter.convert(jwt);

        assertThat(auth).isNotNull();
        assertThat(roleAuthorities(auth))
                .containsExactlyInAnyOrder("ROLE_ADMIN", "ROLE_USER");
    }

    @Test
    @DisplayName("Should grant no role authorities when the 'roles' claim is absent")
    void shouldGrantNoRoleAuthoritiesWhenRolesClaimIsAbsent() {
        var jwt = Jwt.withTokenValue("token")
                .header("alg", "RS256")
                .subject(UUID.randomUUID().toString())
                .build();

        var auth = converter.convert(jwt);

        assertThat(auth).isNotNull();
        assertThat(roleAuthorities(auth)).isEmpty();
    }

    private static Jwt jwtWithRoles(List<String> roles) {
        return Jwt.withTokenValue("token")
                .header("alg", "RS256")
                .subject(UUID.randomUUID().toString())
                .claim("roles", roles)
                .build();
    }

    private static List<String> roleAuthorities(AbstractAuthenticationToken auth) {
        return auth.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .filter(authority -> authority.startsWith("ROLE_"))
                .toList();
    }
}