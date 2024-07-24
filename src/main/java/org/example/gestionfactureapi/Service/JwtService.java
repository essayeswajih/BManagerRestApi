package org.example.gestionfactureapi.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.example.gestionfactureapi.Entity.User;
import org.example.gestionfactureapi.Repository.TokenRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;
import javax.crypto.SecretKey;
import java.util.Date;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class JwtService {
    private final String SEKRET_KEY = "988eba5189a453b4a7a2ecf8bd670e3e2c4e545a6006205bf19b43224577b85f";
    @Value("${application.security.jwt.access-token-expiration}")
    private long accessTokenExpire;

    @Value("${application.security.jwt.refresh-token-expiration}")
    private long refreshTokenExpire;
    private final TokenRepository tokenRepository;
    public String extractUserName(String token){
        return extractClaim(token,Claims::getSubject);
    }
    public boolean isValid(String token, UserDetails user){
        String userName = extractUserName(token);
        return userName.equals(user.getUsername())&& !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token,Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims,T> resolver){
        Claims claims = extractAllClaims(token);
        return resolver.apply(claims);
    }

    public String generateAccessToken(User user) {
        return generateToken(user, accessTokenExpire);
    }

    public String generateRefreshToken(User user) {
        return generateToken(user, refreshTokenExpire );
    }
    public boolean isValidRefreshToken(String token, User user) {
        String username = extractUserName(token);

        boolean validRefreshToken = tokenRepository
                .findByRefreshToken(token)
                .map(t -> !t.isLoggedOut())
                .orElse(false);

        return (username.equals(user.getUsername())) && !isTokenExpired(token) && validRefreshToken;
    }

    private Claims extractAllClaims(String token){
        return  Jwts
                .parser()
                .verifyWith(getSignInKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public String generateToken(User user, long accessTokenExpire){
        String token = Jwts
                .builder()
                .subject(user.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 24 * 60 * 60*1000))
                .signWith(getSignInKey())
                .compact();
        return token;
    }
    private SecretKey getSignInKey(){
        byte[] keyBytes = Decoders.BASE64.decode(SEKRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
