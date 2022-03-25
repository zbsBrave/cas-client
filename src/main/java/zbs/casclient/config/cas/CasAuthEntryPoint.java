package zbs.casclient.config.cas;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author zhangbaisen
 * @date 2022/3/25 17:46
 */
public class CasAuthEntryPoint implements AuthenticationEntryPoint {
    private final CasProperties casProperties;
    
    public CasAuthEntryPoint(CasProperties casProperties){
        this.casProperties = casProperties;
    }
    
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        final String serviceUrl = casProperties.constructServiceUrl(request, response);
        String redirectUrl = casProperties.constructRedirectUrl(serviceUrl);
        response.sendRedirect(redirectUrl);
    }
}
