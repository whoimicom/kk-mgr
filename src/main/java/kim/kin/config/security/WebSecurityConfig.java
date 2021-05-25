package kim.kin.config.security;

import kim.kin.config.handler.AccessDeniedHandlerImpl;
import kim.kin.config.handler.AuthenticationFailureHandlerImpl;
import kim.kin.config.handler.AuthenticationSuccessHandlerImpl;
import kim.kin.config.handler.LogoutHandlerImpl;
import kim.kin.config.session.InvalidSessionStrategyImpl;
import kim.kin.config.session.SessionInformationExpiredStrategyImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.session.InvalidSessionStrategy;
import org.springframework.security.web.session.SimpleRedirectInvalidSessionStrategy;

import javax.sql.DataSource;

/**
 * @author choky
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final AuthenticationFailureHandlerImpl authenticationFailureHandlerImpl;
    private final UserDetailsServiceImpl userDetailsServiceImpl;
    private final InvalidSessionStrategyImpl invalidSessionStrategyImpl;
    private final SessionInformationExpiredStrategyImpl sessionInformationExpiredStrategyImpl;
    private final AccessDeniedHandlerImpl accessDeniedHandlerImpl;
    private final DataSource dataSource;

    public WebSecurityConfig(AuthenticationFailureHandlerImpl authenticationFailureHandlerImpl, UserDetailsServiceImpl userDetailsServiceImpl, InvalidSessionStrategyImpl invalidSessionStrategyImpl, SessionInformationExpiredStrategyImpl sessionInformationExpiredStrategyImpl, AccessDeniedHandlerImpl accessDeniedHandlerImpl, DataSource dataSource) {
        this.authenticationFailureHandlerImpl = authenticationFailureHandlerImpl;
        this.userDetailsServiceImpl = userDetailsServiceImpl;
        this.invalidSessionStrategyImpl = invalidSessionStrategyImpl;
        this.sessionInformationExpiredStrategyImpl = sessionInformationExpiredStrategyImpl;
        this.accessDeniedHandlerImpl = accessDeniedHandlerImpl;
        this.dataSource = dataSource;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public InvalidSessionStrategy invalidSessionStrategy() {
        return new SimpleRedirectInvalidSessionStrategy("/login.html");
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        String[] anonResourcesUrl = {"/css/**", "/js/**", "/fonts/**", "/img/**", "*.svg", "*.png", "*.js", "*.css", "*.ico"};
        httpSecurity.exceptionHandling().accessDeniedHandler(accessDeniedHandlerImpl);
        // formLogin
        httpSecurity.formLogin()
                .loginPage("/login.html")
                .loginProcessingUrl("/signin")
                .successHandler(authenticationSucessHandler())
                .failureHandler(authenticationFailureHandlerImpl);
        // rememberMe
        httpSecurity
                .rememberMe()
                .tokenRepository(persistentTokenRepository())
                .tokenValiditySeconds(3600)
                .userDetailsService(userDetailsServiceImpl);
        // sessionManagement
        httpSecurity.sessionManagement()
//                .invalidSessionStrategy(invalidSessionStrategy())
                .invalidSessionUrl("/login.html")
                .maximumSessions(1)
                .expiredSessionStrategy(sessionInformationExpiredStrategyImpl)
                .sessionRegistry(sessionRegistry());
        //logout
        httpSecurity.logout()
                .addLogoutHandler(logoutHandler())
                .logoutUrl("/logout")
                .logoutSuccessUrl("/")
                .deleteCookies("JSESSIONID");
        // authorizeRequests
        httpSecurity.authorizeRequests()
                .antMatchers(anonResourcesUrl).permitAll()
                .antMatchers(
                        "/login.html",
                        "/authenticate",
                        "/logout.html",
                        "signin",
                        "/logout"
                ).permitAll()
                .anyRequest().authenticated();
        // disable csrf
        httpSecurity.csrf().disable();

    }

    @Bean
    public SessionRegistry sessionRegistry() {
        return new SessionRegistryImpl();
    }

    public AuthenticationSuccessHandlerImpl authenticationSucessHandler() {
        return new AuthenticationSuccessHandlerImpl(sessionRegistry());
    }

    @Bean
    public PersistentTokenRepository persistentTokenRepository() {
        JdbcTokenRepositoryImpl jdbcTokenRepository = new JdbcTokenRepositoryImpl();
        jdbcTokenRepository.setDataSource(dataSource);
        jdbcTokenRepository.setCreateTableOnStartup(false);
        return jdbcTokenRepository;
    }

    @Bean
    public LogoutHandler logoutHandler() {
        LogoutHandlerImpl logoutHandlerImpl = new LogoutHandlerImpl();
        logoutHandlerImpl.setSessionRegistry(sessionRegistry());
        return logoutHandlerImpl;
    }
}
