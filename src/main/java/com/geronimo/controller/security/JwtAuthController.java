package com.geronimo.controller.security;

import com.geronimo.config.security.UserDetails;
import com.geronimo.config.security.jwt.JwtTokenUtil;
import com.geronimo.model.security.JwtAuth;
import com.geronimo.model.security.JwtToken;
import com.geronimo.service.UserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@RestController
public class JwtAuthController {

    @Value("${jwt.header}")
    private String tokenHeaderName;

    @Value("${jwt.scheme}")
    private String scheme;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private UserDetailsService userDetailsService;

    @PostMapping(value = "${jwt.route.auth.path}")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtAuth auth) throws AuthenticationException {

        // Perform the security
        final Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        auth.getUsername(),
                        auth.getPassword()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Reload password post-security so we can generate token
        final UserDetails userDetails = userDetailsService.loadUserByUsername(auth.getUsername());
        final String token = jwtTokenUtil.generateToken(userDetails);
        final Date expiresAt = jwtTokenUtil.getExpirationDateFromToken(token);

        // Return the token
        return ResponseEntity.ok(new JwtToken(token, String.valueOf(expiresAt.getTime())));
    }

    @GetMapping(value = "${jwt.route.auth.refresh}")
    public ResponseEntity<?> refreshAndGetAuthenticationToken(HttpServletRequest request) {
        String tokenHeader = request.getHeader(tokenHeaderName);

        final String token = jwtTokenUtil.extractTokenFromHeader(tokenHeader);
        String username = jwtTokenUtil.getUsernameFromToken(token);

        UserDetails user = userDetailsService.loadUserByUsername(username);

        if (jwtTokenUtil.canTokenBeRefreshed(token, user.getLastPasswordReset())) {
            String refreshedToken = jwtTokenUtil.refreshToken(token);
            Date expiresAt = jwtTokenUtil.getExpirationDateFromToken(refreshedToken);
            return ResponseEntity.ok(new JwtToken(refreshedToken, String.valueOf(expiresAt.getTime())));
        } else {
            return ResponseEntity.badRequest().body(null);
        }
    }
}