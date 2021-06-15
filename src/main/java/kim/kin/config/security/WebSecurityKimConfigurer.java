package kim.kin.config.security;

import kim.kin.config.handler.AccessDeniedKimImpl;
import kim.kin.config.handler.AuthenticationFailureKimImpl;
import kim.kin.config.handler.AuthenticationSuccessKimImpl;
import kim.kin.config.handler.LogoutHandlerKimImpl;
import kim.kin.config.session.InvalidSessionStrategyKimImpl;
import kim.kin.config.session.SessionInformationExpiredKimImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
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
public class WebSecurityKimConfigurer extends WebSecurityConfigurerAdapter {

    private final AuthenticationFailureKimImpl authenticationFailureKimImpl;
    private final UserDetailsServiceKimImpl userDetailsServiceKimImpl;
    private final InvalidSessionStrategyKimImpl invalidSessionStrategyKimImpl;
    private final SessionInformationExpiredKimImpl sessionInformationExpiredKimImpl;
    private final AccessDeniedKimImpl accessDeniedKimImpl;
    private final DataSource dataSource;

    public WebSecurityKimConfigurer(AuthenticationFailureKimImpl authenticationFailureKimImpl, UserDetailsServiceKimImpl userDetailsServiceKimImpl, InvalidSessionStrategyKimImpl invalidSessionStrategyKimImpl, SessionInformationExpiredKimImpl sessionInformationExpiredKimImpl, AccessDeniedKimImpl accessDeniedKimImpl, DataSource dataSource) {
        this.authenticationFailureKimImpl = authenticationFailureKimImpl;
        this.userDetailsServiceKimImpl = userDetailsServiceKimImpl;
        this.invalidSessionStrategyKimImpl = invalidSessionStrategyKimImpl;
        this.sessionInformationExpiredKimImpl = sessionInformationExpiredKimImpl;
        this.accessDeniedKimImpl = accessDeniedKimImpl;
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
        httpSecurity.exceptionHandling().accessDeniedHandler(accessDeniedKimImpl);
        // formLogin
        httpSecurity.formLogin()
                .loginPage("/login.html")
                .loginProcessingUrl("/signin")
                .successHandler(authenticationSucessHandler())
                .failureHandler(authenticationFailureKimImpl);
        // rememberMe
        httpSecurity
                .rememberMe()
                .tokenRepository(persistentTokenRepository())
                .tokenValiditySeconds(3600)
                .userDetailsService(userDetailsServiceKimImpl);
        // sessionManagement
        httpSecurity.sessionManagement()
//                .invalidSessionStrategy(invalidSessionStrategy())
                .invalidSessionUrl("/login.html")
                .maximumSessions(1)
                .expiredSessionStrategy(sessionInformationExpiredKimImpl)
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

    public AuthenticationSuccessKimImpl authenticationSucessHandler() {
        return new AuthenticationSuccessKimImpl(sessionRegistry());
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
        LogoutHandlerKimImpl logoutHandlerKimImpl = new LogoutHandlerKimImpl();
        logoutHandlerKimImpl.setSessionRegistry(sessionRegistry());
        return logoutHandlerKimImpl;
    }

    /**
     * setHideUserNotFoundExceptions false
     *
     * @return AuthenticationProvider
     */
    @Bean
    public AuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(userDetailsServiceKimImpl);
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        daoAuthenticationProvider.setHideUserNotFoundExceptions(false);
        return daoAuthenticationProvider;
    }

    /**
     * @param authenticationManagerBuilder
     * @throws Exception
     */
    @Autowired
    public void configureAuthentication(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder.authenticationProvider(daoAuthenticationProvider());
    }
}
