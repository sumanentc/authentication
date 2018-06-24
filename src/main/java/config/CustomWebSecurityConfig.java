package config;

import org.jasig.cas.client.session.SingleSignOutFilter;
import org.jasig.cas.client.validation.Cas20ServiceTicketValidator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.cas.ServiceProperties;
import org.springframework.security.cas.authentication.CasAuthenticationProvider;
import org.springframework.security.cas.userdetails.GrantedAuthorityFromAssertionAttributesUserDetailsService;
import org.springframework.security.cas.web.CasAuthenticationEntryPoint;
import org.springframework.security.cas.web.CasAuthenticationFilter;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;

/**
 * Created by suman.das on 11/16/16.
 */

@Configuration
@EnableWebSecurity
public class CustomWebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Value("${cas.server.login}")
    private String casServerLogin;
    @Value("${cas.serviceUrl}")
    private String serviceUrl;
    @Value("${cas.prefix}")
    private String casServer;
    @Value("${cas.logout.URL}")
    private String casLogout;
    @Value("${callback.server}")
    private String callbackUrl;

    /*
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception{
        auth.inMemoryAuthentication()
                .withUser("admin")
                .password("admin")
                .roles("ADMIN","USER")
                .and()
                .withUser("user")
                .password("user")
                .roles("USER");

    }*/

    @Bean
    public ServiceProperties serviceProperties() {
        ServiceProperties serviceProperties = new ServiceProperties();
        serviceProperties.setService(serviceUrl);
        serviceProperties.setSendRenew(false);
        return serviceProperties;
    }

    @Bean
    public CasAuthenticationProvider casAuthenticationProvider() {
        CasAuthenticationProvider casAuthenticationProvider = new CasAuthenticationProvider();
        casAuthenticationProvider.setAuthenticationUserDetailsService(authenticationUserDetailsService());
        casAuthenticationProvider.setServiceProperties(serviceProperties());
        casAuthenticationProvider.setTicketValidator(cas20ServiceTicketValidator());
        casAuthenticationProvider.setKey("an_id_for_this_auth_provider_only");
        return casAuthenticationProvider;
    }

    @Bean
    public Cas20ServiceTicketValidator cas20ServiceTicketValidator() {
        return new Cas20ServiceTicketValidator(casServer);
    }


    @Bean
    public AuthenticationUserDetailsService authenticationUserDetailsService(){
        String[] role ={"role"};
        return new GrantedAuthorityFromAssertionAttributesUserDetailsService(role);
    }

    @Bean
    public CasAuthenticationFilter casAuthenticationFilter() throws Exception {
        CasAuthenticationFilter casAuthenticationFilter = new CasAuthenticationFilter();
        casAuthenticationFilter.setAuthenticationManager(authenticationManager());
        return casAuthenticationFilter;
    }

    @Bean
    public CasAuthenticationEntryPoint casAuthenticationEntryPoint() {
        CasAuthenticationEntryPoint casAuthenticationEntryPoint = new CasAuthenticationEntryPoint();
        casAuthenticationEntryPoint.setLoginUrl(casServerLogin);
        casAuthenticationEntryPoint.setServiceProperties(serviceProperties());
        return casAuthenticationEntryPoint;
    }

    @Bean
    public LogoutFilter requestSingleLogoutFilter (){

        LogoutFilter logoutFilter = new LogoutFilter(casLogout,new SecurityContextLogoutHandler());
        logoutFilter.setFilterProcessesUrl("/j_spring_cas_security_logout");
        return logoutFilter;
    }

    @Bean
    public SingleSignOutFilter singleSignOutFilter() {
        SingleSignOutFilter filter = new SingleSignOutFilter();
        filter.setCasServerUrlPrefix(casServer);
        filter.setIgnoreInitConfiguration(true);
        return filter;
    }

    private CsrfTokenRepository csrfTokenRepository()
    {
        HttpSessionCsrfTokenRepository repository = new HttpSessionCsrfTokenRepository();
        repository.setSessionAttributeName("_csrf");
        return repository;
    }


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(casAuthenticationProvider());
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().csrfTokenRepository(csrfTokenRepository());
        http.addFilterBefore(requestSingleLogoutFilter(),LogoutFilter.class);
        http.addFilterBefore(singleSignOutFilter(),CasAuthenticationFilter.class);
        http.addFilter(casAuthenticationFilter());
        http.exceptionHandling().authenticationEntryPoint(casAuthenticationEntryPoint());


        http.authorizeRequests()
                        .antMatchers("/resources/**","/","/home").permitAll()
                        .anyRequest().authenticated()
                        .and()
                        .formLogin().loginPage("/login").permitAll()
                        .and()
                        .logout().logoutUrl("/logout").invalidateHttpSession(true).deleteCookies("JSESSIONID")
                        .logoutSuccessUrl(casLogout +"?service=" + callbackUrl).permitAll();

    }


}
