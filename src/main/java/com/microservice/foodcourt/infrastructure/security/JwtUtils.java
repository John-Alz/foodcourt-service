package com.microservice.foodcourt.infrastructure.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtUtils {

    @Value("${security.jwt.key.private}")
    private String privateKey;

    @Value("${security.jwt.user.generator}")
    private String userGenerator;

    public DecodedJWT verifyToken(String token) {
        try{
            Algorithm algorithm = Algorithm.HMAC256(this.privateKey);
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer(this.userGenerator)
                    .build();
            return verifier.verify(token);
        }catch (JWTVerificationException e) {
            throw new JWTVerificationException("Validacion fallida de JWT.");
        }
    }

    public String extractSubject(DecodedJWT decodedJWT) {
        return decodedJWT.getSubject();
    }

    public Claim extractSpecificClaim(DecodedJWT decodedJWT, String claim) {
        return decodedJWT.getClaim(claim);
    }


}
