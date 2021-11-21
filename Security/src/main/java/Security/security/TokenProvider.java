package Security.security;

import Security.cache.CacheManagement;
import Security.config.AppProperties;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.time.*;
import java.util.Date;

@Service
public class TokenProvider {
    @Autowired
    private CacheManagement cacheManagement;
    private static final Logger logger = LoggerFactory.getLogger(TokenProvider.class);
    @Autowired
    private AppProperties appProperties;

    public TokenProvider(AppProperties appProperties) {
        this.appProperties = appProperties;
    }

    public String createToken(Authentication authentication) {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

        Date expiryDate = makeDateInture(appProperties.getAuth().getTokenExpirationMsec());
        String token = Jwts.builder()
                .setSubject(Long.toString(userPrincipal.getId()))
                .setIssuedAt(new Date())
                .setExpiration(expiryDate)
                .signWith(getKey(appProperties.getAuth().getTokenSecret()), SignatureAlgorithm.HS512)
                .compact();
        cacheManagement.putElement(token, expiryDate, userPrincipal);
        return token;
    }

    public Long getUserIdFromToken(String token) {

        Claims claims = Jwts.parserBuilder().
                setSigningKey(getKey(appProperties.getAuth().getTokenSecret())).
                build().parseClaimsJws(token).getBody();
        return Long.parseLong(claims.getSubject());
    }

    public boolean validateToken(String authToken) {
        try {
            Jwts.parserBuilder().
                    setSigningKey(getKey(appProperties.getAuth().getTokenSecret())).
                    build().parseClaimsJws(authToken);
            return true;
        } catch (MalformedJwtException ex) {
            logger.error("Invalid JWT token");
        } catch (ExpiredJwtException ex) {
            logger.error("Expired JWT token");
        } catch (UnsupportedJwtException ex) {
            logger.error("Unsupported JWT token");
        } catch (IllegalArgumentException ex) {
            logger.error("JWT claims string is empty.");
        }
        return false;
    }


    private Date makeDateInture(long days) {
        LocalDate now = LocalDate.now().plusDays(days);
        //Creating ZonedDateTime instance
        ZonedDateTime zdt = now.atStartOfDay(ZoneId.systemDefault());
        //Creating Instant instance
        Instant instant = zdt.toInstant();
        //Creating Date instance using instant instance.
        return Date.from(instant);

    }

    private Key getKey(String secret) {

        // HMAC key to sign/verify JWT used for email purpose
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
    }
}