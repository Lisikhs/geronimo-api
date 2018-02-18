package com.geronimo.config.security.jwt;

import com.geronimo.config.security.UserDetails;
import com.geronimo.util.DateUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Clock;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.DefaultClock;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
@Slf4j
public class JwtTokenUtil implements Serializable {

    public static final String AUDIENCE_WEB = "web";

    private Clock clock = DefaultClock.INSTANCE;

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private Long expiration;

    @Value("${jwt.scheme}")
    private String scheme;

    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    public Date getIssuedAtDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getIssuedAt);
    }

    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    public String getAudienceFromToken(String token) {
        return getClaimFromToken(token, Claims::getAudience);
    }

    private <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();
    }

    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(clock.now());
    }

    private Boolean isIssuedBeforeLastPasswordReset(Date issuedAt, Date lastPasswordReset) {
        return (lastPasswordReset != null && issuedAt.before(lastPasswordReset));
    }

    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        return doGenerateToken(claims, userDetails.getUsername());
    }

    private String doGenerateToken(Map<String, Object> claims, String subject) {
        final Date issuedAt = clock.now();
        final Date expirationDate = calculateExpirationDate(issuedAt);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setAudience(AUDIENCE_WEB)
                .setIssuedAt(issuedAt)
                .setExpiration(expirationDate)
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    public Boolean canTokenBeRefreshed(String token, Date lastPasswordReset) {
        final Date issuedAt = getIssuedAtDateFromToken(token);
        return !isIssuedBeforeLastPasswordReset(issuedAt, lastPasswordReset)
                && !isTokenExpired(token);
    }

    public Boolean canTokenBeRefreshed(String token, LocalDateTime lastPasswordReset) {
        return canTokenBeRefreshed(token,
                lastPasswordReset == null ? null : DateUtils.convertToDate(lastPasswordReset));
    }

    public String refreshToken(String token) {
        final Date issuedAt = clock.now();
        final Date expirationDate = calculateExpirationDate(issuedAt);

        final Claims claims = getAllClaimsFromToken(token);
        claims.setIssuedAt(issuedAt);
        claims.setExpiration(expirationDate);

        return Jwts.builder()
                .setClaims(claims)
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = getUsernameFromToken(token);
        final Date issuedAt = getIssuedAtDateFromToken(token);

        Date lastPasswordReset = null;
        if (userDetails.getLastPasswordReset() != null) {
            lastPasswordReset = DateUtils.convertToDate(userDetails.getLastPasswordReset());
        }

        return username.equals(userDetails.getUsername())
                && !isTokenExpired(token)
                && !isIssuedBeforeLastPasswordReset(issuedAt, lastPasswordReset);
    }

    private Date calculateExpirationDate(Date issuedAt) {
        return new Date(issuedAt.getTime() + expiration * 1000);
    }

    public Boolean isValidTokenHeader(String tokenHeader) {
        return tokenHeader.startsWith(scheme);
    }

    public String extractTokenFromHeader(String tokenHeader) {
        return tokenHeader.substring(scheme.length()).trim();
    }
}
