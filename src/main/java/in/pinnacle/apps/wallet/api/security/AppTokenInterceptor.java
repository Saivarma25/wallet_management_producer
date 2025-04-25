package in.pinnacle.apps.wallet.api.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AppTokenInterceptor implements HandlerInterceptor {

    @Value("${wallet.app.token}")
    private String expectedAppToken;

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest,
             @NonNull HttpServletResponse httpServletResponse, @NonNull Object handler) throws Exception {

        String appToken = httpServletRequest.getHeader("App-Token");
        if (expectedAppToken.equals(appToken))
            return true;

        httpServletResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        httpServletResponse.getWriter().write("Invalid or missing App-Token");
        return false;
    }
}
