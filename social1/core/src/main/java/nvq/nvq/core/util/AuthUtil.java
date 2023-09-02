package nvq.nvq.core.util;

import org.springframework.security.core.Authentication;

public class AuthUtil {
    public static String userId(Authentication authentication) {
        return (String) authentication.getPrincipal();
    }
}
