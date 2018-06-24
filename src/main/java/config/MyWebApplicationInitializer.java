package config;

import org.jasig.cas.client.session.SingleSignOutHttpSessionListener;
import org.springframework.boot.web.servlet.ServletContextInitializer;

import javax.servlet.ServletContext;

/**
 * Created by suman.das on 11/17/16.
 */
//@Configuration
public class MyWebApplicationInitializer implements ServletContextInitializer{
    @Override
    public void onStartup(ServletContext servletContext) {
        servletContext.addListener(new SingleSignOutHttpSessionListener());
    }
}
