package kim.kin.config.security;

import kim.kin.config.handler.KkAccessDeniedHandler;
import kim.kin.config.handler.KkAuthenticationFailureHandler;
import kim.kin.config.handler.KkAuthenticationSucessHandler;
import kim.kin.config.handler.KkLogoutHandler;
import kim.kin.config.session.KkInvalidSessionStrategy;
import kim.kin.config.session.KkSessionInformationExpiredStrategy;
import kim.kin.service.impl.KkUserDetailService;
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

import javax.sql.DataSource;

/**
 * @author choky
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final KkAuthenticationFailureHandler kkAuthenticationFailureHandler;
    private final KkUserDetailService kkUserDetailService;
    private final KkInvalidSessionStrategy kkInvalidSessionStrategy;
    private final KkSessionInformationExpiredStrategy kkSessionInformationExpiredStrategy;
    private final KkAccessDeniedHandler kkAccessDeniedHandler;
    private final DataSource dataSource;

    public WebSecurityConfig(KkAuthenticationFailureHandler kkAuthenticationFailureHandler, KkUserDetailService kkUserDetailService, KkInvalidSessionStrategy kkInvalidSessionStrategy, KkSessionInformationExpiredStrategy kkSessionInformationExpiredStrategy, KkAccessDeniedHandler kkAccessDeniedHandler, DataSource dataSource) {

        this.kkAuthenticationFailureHandler = kkAuthenticationFailureHandler;
        this.kkUserDetailService = kkUserDetailService;
        this.kkInvalidSessionStrategy = kkInvalidSessionStrategy;
        this.kkSessionInformationExpiredStrategy = kkSessionInformationExpiredStrategy;
        this.kkAccessDeniedHandler = kkAccessDeniedHandler;
        this.dataSource = dataSource;
    }

//    @Autowired
//    public void configureGlobal(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
//        authenticationManagerBuilder.userDetailsService(userDetailsServiceImpl).passwordEncoder(passwordEncoder());
//    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        String[] anonResourcesUrl = {"/", "/css/**", "/js/**", "/fonts/**", "/img/**", "/assets/**", "*.svg", "*.png", "*.js", "*.css", "*.ico"};
        httpSecurity.exceptionHandling().accessDeniedHandler(kkAccessDeniedHandler);
        // formLogin
        httpSecurity.formLogin()
                .loginPage("/login")
                .successHandler(kkAuthenticationSucessHandler())
                .failureHandler(kkAuthenticationFailureHandler);
        // rememberMe
        httpSecurity
                .rememberMe()
                .tokenRepository(persistentTokenRepository())
                .tokenValiditySeconds(3600)
                .userDetailsService(kkUserDetailService);
        // sessionManagement
        httpSecurity.sessionManagement()
                .invalidSessionStrategy(kkInvalidSessionStrategy)
                .maximumSessions(1)
                .expiredSessionStrategy(kkSessionInformationExpiredStrategy)
                .sessionRegistry(sessionRegistry());
        //logout
        httpSecurity.logout()
                .addLogoutHandler(logoutHandler())
                .logoutUrl("/logout")
                .logoutSuccessUrl("/")
                .deleteCookies("JSESSIONID");
        // authorizeRequests
        httpSecurity.authorizeRequests() // 授权配置
                .antMatchers(anonResourcesUrl).permitAll()
                .antMatchers(
                        "/login"
                ).permitAll()
                .anyRequest().authenticated();
        // disable csrf
        httpSecurity.csrf().disable();

    }

    @Bean
    public SessionRegistry sessionRegistry() {
        return new SessionRegistryImpl();
    }

    public KkAuthenticationSucessHandler kkAuthenticationSucessHandler() {
        return new KkAuthenticationSucessHandler(sessionRegistry());
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
        KkLogoutHandler kkLogoutHandler = new KkLogoutHandler();
        kkLogoutHandler.setSessionRegistry(sessionRegistry());
        return kkLogoutHandler;
    }
}
