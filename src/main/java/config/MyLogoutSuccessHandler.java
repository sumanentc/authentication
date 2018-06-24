package config;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by suman.das on 11/16/16.
 */
@Component
public class MyLogoutSuccessHandler implements LogoutSuccessHandler {
    @Override
    public void onLogoutSuccess(HttpServletRequest request,
                                HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {
        if(authentication != null) {
            System.out.println(authentication.getName());
        }
        //perform other required operation
        //String URL = request.getContextPath() + "/app";
        String URL ="http://localhost:8080/cas/logout?service=http://localhost:9090";
        response.setStatus(HttpStatus.OK.value());
        response.sendRedirect(URL);
    }
}

