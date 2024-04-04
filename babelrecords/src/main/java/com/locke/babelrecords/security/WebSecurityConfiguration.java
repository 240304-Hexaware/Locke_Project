package com.locke.babelrecords.security;

import com.locke.babelrecords.utils.RSAKeyProperties;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableMethodSecurity
public class WebSecurityConfiguration {
  private final RSAKeyProperties keys;

  WebSecurityConfiguration(RSAKeyProperties keys) {
    this.keys = keys;
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    String idForEncode = "bcrypt";
    Map encoders = new HashMap<>();
    encoders.put(idForEncode, new BCryptPasswordEncoder());
    return new DelegatingPasswordEncoder(idForEncode, encoders);
  }

  @Bean
  public AuthenticationManager authenticationManager(UserDetailsService detailsService) {
    var daoAuthenticationProvider = new DaoAuthenticationProvider();
    daoAuthenticationProvider.setUserDetailsService(detailsService);
    return new ProviderManager(daoAuthenticationProvider);
  }

  @Bean
  public SecurityFilterChain defaultSecurityFilterChair(HttpSecurity http) throws Exception {
    return http
        .authorizeHttpRequests(auth -> auth
            .requestMatchers("/api/v1/users/**").authenticated()
            .requestMatchers("/api/v1/admin/**").hasRole("ADMIN")
            .requestMatchers("/api/v1/files/**").authenticated()
            .requestMatchers("api/v1/auth/**").permitAll()
        )
        .oauth2ResourceServer(oauth2 -> oauth2
            .jwt(jwtConfigurer -> jwtConfigurer
                .jwtAuthenticationConverter(jwtAuthenticationConverter())
            )
        )
        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .csrf(AbstractHttpConfigurer::disable)
        .cors(Customizer.withDefaults())
        .build();
  }

  @Bean
  public JwtDecoder jwtDecoder() {
    return NimbusJwtDecoder.withPublicKey(keys.getRsaPublicKey()).build();
  }

  @Bean
  public JwtEncoder jwtEncoder() {
    JWK jwk = new RSAKey.Builder(keys.getRsaPublicKey()).privateKey(keys.getRsaPrivateKey()).build();
    JWKSource<SecurityContext> jwkSource = new ImmutableJWKSet<>(new JWKSet(jwk));
    return new NimbusJwtEncoder(jwkSource);
  }

  @Bean
  public JwtAuthenticationConverter jwtAuthenticationConverter() {
    var jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
    jwtGrantedAuthoritiesConverter.setAuthoritiesClaimName("roles");
    jwtGrantedAuthoritiesConverter.setAuthorityPrefix("ROLE_");
    var jwtConverter = new JwtAuthenticationConverter();
    jwtConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);
    return jwtConverter;
  }
}
