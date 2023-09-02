package nvq.nvq.core.config.exception;

import com.google.gson.Gson;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nvq.nvq.common.response.DfResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

import static nvq.nvq.common.constant.StatusRp.UNAUTHORIZED;

@Component
public class AuthEntryPoint implements AuthenticationEntryPoint {
    @Autowired
    private Gson gson;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(UNAUTHORIZED.code());
        response.getOutputStream().println(gson.toJson(DfResponse.status(UNAUTHORIZED)));
    }
}
