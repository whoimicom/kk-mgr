package kim.kin.config.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import kim.kin.exception.ReqException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.*;
import java.util.function.Function;

/**
 * @author choky
 */
@Component
public class JwtTokenUtil implements Serializable {

    private static final long serialVersionUID = -2550185165626007488L;

    /**
     * jwt base64EncodedSecretKey
     */
    @Value("${jwt.base64EncodedSecretKey}")
    private String base64EncodedSecretKey;

    /**
     * jwt expiration time (s)
     */
    @Value("${jwt.expiration}")
    private Long expiration;

    private final UserDetailsServiceImpl userDetailsServiceImpl;


    public JwtTokenUtil(UserDetailsServiceImpl userDetailsServiceImpl) {
        this.userDetailsServiceImpl = userDetailsServiceImpl;
    }

    /**
     * 获取当前登录的用户
     *
     * @return UserDetails
     */
    public UserDetails getCurrentUser() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            throw new ReqException(HttpStatus.UNAUTHORIZED, "token timeout");
        }
        if (authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String username = userDetails.getUsername();
            return userDetailsServiceImpl.loadUserByUsername(username);
        } else {
            throw new ReqException(HttpStatus.UNAUTHORIZED, "UNAUTHORIZED ");
        }
    }

    /**
     * retrieve username from jwt token
     *
     * @param token token
     * @return getUsernameFromToken
     */
    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    /**
     * retrieve expiration date from jwt token
     *
     * @param token token
     * @return getExpirationDateFromToken
     */
    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        Jws<Claims> claimsJws = Jwts.parser().setSigningKey(base64EncodedSecretKey).parseClaimsJws(token);
        Object scope = claimsJws.getBody().get("scope");
        System.out.println(scope);
        Claims claims = claimsJws.getBody();
        System.out.println("ID: " + claims.getId());
        System.out.println("Subject: " + claims.getSubject());
        System.out.println("Issuer: " + claims.getIssuer());
        System.out.println("Expiration: " + claims.getExpiration());
        System.out.println("getNotBefore: " + claims.getNotBefore());
        System.out.println("getAudience: " + claims.getAudience());
        System.out.println("getIssuedAt: " + claims.getIssuedAt());
        return claimsResolver.apply(claims);
    }


    /**
     * check if the token has expired
     *
     * @param token token
     * @return isTokenExpired
     */
    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    /**
     * while creating the token -
     * 1. Define  claims of the token, like Issuer, Expiration, Subject, and the ID
     * 2. Sign the JWT using the HS512 algorithm and secret key.
     * 3. According to JWS Compact Serialization(https://tools.ietf.org/html/draft-ietf-jose-json-web-signature-41#section-3.1)
     * compaction of the JWT to a URL-safe string
     *
     * @param userDetails userDetails
     * @return token
     */
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>(10);
        String username = userDetails.getUsername();
        List<String> strings = Arrays.asList("/admin", "/index", "/index.html");
        claims.put("scope", strings);
        return Jwts.builder().setClaims(claims).setSubject(username).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration * 1000))
                .signWith(SignatureAlgorithm.HS512, base64EncodedSecretKey).compact();
    }


    /**
     * validate token
     *
     * @param token       token
     * @param userDetails userDetails
     * @return result
     */
    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = getUsernameFromToken(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
}
