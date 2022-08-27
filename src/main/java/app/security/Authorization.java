package app.security;

import app.domain.role.Role;
import app.domain.user.User;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class Authorization {
    private static final List<String> invalidatedTokens = new ArrayList<>();
    static Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());
    static JWTVerifier verifier = JWT.require(algorithm).build();
    public static List<String> getAuthorizedRoles(String token) throws JWTVerificationException {
        if (!isValid(token))
            throw new JWTVerificationException("Token was invalidated");

        DecodedJWT decodedJWT = verifier.verify(token);
        return decodedJWT.getClaim("roles").asList(String.class);
    }
    public static boolean checkUserRole(String token, String role) {
        if (!isValid(token))
            throw new JWTVerificationException("Token was invalidated");

        DecodedJWT decodedJWT = verifier.verify(token);
        return decodedJWT.getClaim("roles").asList(String.class).contains(role);
    }
    public static void invalidate(String token) throws JWTVerificationException {
        verifier.verify(token);
        invalidatedTokens.add(token);
    }

    public static boolean verify(String token) {
        try {
            DecodedJWT verify = verifier.verify(token);
        } catch (JWTVerificationException e) {
            return false;
        }
        return true;
    }

    public static boolean isValid(String token) {
        return !invalidatedTokens.contains(token) && verify(token);
    }

    public static String authorize(User authenticatedUser) {
        return JWT.create()
                .withSubject(authenticatedUser.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + 2 * 60 * 1000))
                .withClaim("roles", authenticatedUser.getRoles().stream().map(Role::getName).collect(Collectors.toList()))
                .sign(Algorithm.HMAC256("secret".getBytes()));
    }
}
