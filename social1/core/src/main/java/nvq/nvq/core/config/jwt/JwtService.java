package nvq.nvq.core.config.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import nvq.nvq.common.dto.user.AuthDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;

import static nvq.nvq.core.util.JsonUtil.jsonToObject;
import static nvq.nvq.core.util.JsonUtil.objectToJson;
import static nvq.nvq.core.util.TimeUtil.currentMillis;

@Service
public class JwtService {
    @Value("${spring.security.jwt.secret-key}")
    private String secretKey;
    @Value("${spring.security.jwt.expired-in}")
    private Long expiredIn;

    public String generateToken(AuthDTO authDTO) {
        return Jwts.builder()
                .setClaims(new HashMap<>() {{
                    put("data", objectToJson(authDTO));
                }})
                .setSubject(authDTO.getUserId())
                .setExpiration(new Date(currentMillis() + expiredIn))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }
    public Claims extractClaims(String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            System.out.println("ERROR: parsing jwt token failed!");
        }
        return null;
    }

    public Authentication extractAuth(String token) {
        AuthDTO authDTO = jsonToObject(
                (String) extractClaims(token)
                        .get("data"),
                AuthDTO.class);
        return new UsernamePasswordAuthenticationToken(
                authDTO.getUserId(),
                authDTO,
                authDTO.getRoles()
                        .stream()
                        .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                        .toList());
    }

    public Boolean isExpired(String token) {
        Claims claims = extractClaims(token);
        return claims == null || claims
                .getExpiration()
                .before(new Date());
    }
}
