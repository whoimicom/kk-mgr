package kim.kin.config.security;

import kim.kin.config.handler.AccessDeniedKimImpl;
import kim.kin.config.handler.AuthenticationFailureKimImpl;
import kim.kin.config.handler.AuthenticationSuccessKimImpl;
import kim.kin.config.handler.LogoutHandlerKimImpl;
import kim.kin.config.session.InvalidSessionStrategyKimImpl;
import kim.kin.config.session.SessionInformationExpiredKimImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
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
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import javax.sql.DataSource;
import java.util.*;

/**
 * @author choky
 */
@Configuration
@EnableWebSecurity
@Order(90)
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class WebSecurityKimConfigurer extends WebSecurityConfigurerAdapter {
    private static final Logger logger = LoggerFactory.getLogger(WebSecurityKimConfigurer.class);
    private final AuthenticationFailureKimImpl authenticationFailureKimImpl;
    private final UserDetailsServiceKimImpl userDetailsServiceKimImpl;
//    private final InvalidSessionStrategyKimImpl invalidSessionStrategyKimImpl;
    private final SessionInformationExpiredKimImpl sessionInformationExpiredKimImpl;
    private final AccessDeniedKimImpl accessDeniedKimImpl;
    private final DataSource dataSource;

    public WebSecurityKimConfigurer(AuthenticationFailureKimImpl authenticationFailureKimImpl, UserDetailsServiceKimImpl userDetailsServiceKimImpl, InvalidSessionStrategyKimImpl invalidSessionStrategyKimImpl, SessionInformationExpiredKimImpl sessionInformationExpiredKimImpl, AccessDeniedKimImpl accessDeniedKimImpl, @Qualifier("dataSource") DataSource dataSource) {
        this.authenticationFailureKimImpl = authenticationFailureKimImpl;
        this.userDetailsServiceKimImpl = userDetailsServiceKimImpl;
//        this.invalidSessionStrategyKimImpl = invalidSessionStrategyKimImpl;
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

        Map<RequestMappingInfo, HandlerMethod> handlerMethodMap = getApplicationContext().getBean("requestMappingHandlerMapping", RequestMappingHandlerMapping.class).getHandlerMethods();
        Map<String, Set<String>> anonymousUrls = anonymousUrls(handlerMethodMap);
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
                .invalidSessionStrategy(invalidSessionStrategy())
//                .invalidSessionUrl("/login.html")
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
                        "/testDate",
                        "/jbpm/*",
                        "testDate",
//                        "/actuator/*",
                        "/logout"
                )
                .permitAll()
                .antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                .antMatchers(HttpMethod.GET, anonymousUrls.get(HttpMethod.GET.toString()).toArray(String[]::new)).permitAll()
                .antMatchers(HttpMethod.POST, anonymousUrls.get(HttpMethod.POST.toString()).toArray(String[]::new)).permitAll()
                .antMatchers(HttpMethod.PUT, anonymousUrls.get(HttpMethod.PUT.toString()).toArray(String[]::new)).permitAll()
                .antMatchers(HttpMethod.PATCH, anonymousUrls.get(HttpMethod.PATCH.toString()).toArray(String[]::new)).permitAll()
                .antMatchers(HttpMethod.DELETE, anonymousUrls.get(HttpMethod.DELETE.toString()).toArray(String[]::new)).permitAll()
                .antMatchers(anonymousUrls.get("ALL").toArray(String[]::new)).permitAll()
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
     * setHideUserNotFoundExceptions false
     *
     * @param authenticationManagerBuilder amb
     */
//    @Autowired
    public void configureAuthentication(AuthenticationManagerBuilder authenticationManagerBuilder) {
        authenticationManagerBuilder.authenticationProvider(daoAuthenticationProvider());
    }


    private Map<String, Set<String>> anonymousUrls(Map<RequestMappingInfo, HandlerMethod> handlerMethodMap) {
        Map<String, Set<String>> anonymousUrls = new HashMap<>(6);
        Set<String> get = new HashSet<>();
        Set<String> post = new HashSet<>();
        Set<String> put = new HashSet<>();
        Set<String> patch = new HashSet<>();
        Set<String> delete = new HashSet<>();
        Set<String> all = new HashSet<>();
        for (Map.Entry<RequestMappingInfo, HandlerMethod> infoEntry : handlerMethodMap.entrySet()) {
            HandlerMethod handlerMethod = infoEntry.getValue();
            AnonymousKimAccess anonymousKimAccess = handlerMethod.getMethodAnnotation(AnonymousKimAccess.class);
            if (null != anonymousKimAccess) {
                List<RequestMethod> requestMethods = new ArrayList<>(infoEntry.getKey().getMethodsCondition().getMethods());
                if (0 != requestMethods.size()) {
                    HttpMethod httpMethod = HttpMethod.resolve(requestMethods.get(0).name());
                    switch (Objects.requireNonNull(httpMethod)) {
                        case GET -> {
                            assert infoEntry.getKey().getPatternsCondition() != null;
                            get.addAll(infoEntry.getKey().getPatternsCondition().getPatterns());
                        }
                        case POST -> {
                            assert infoEntry.getKey().getPatternsCondition() != null;
                            post.addAll(infoEntry.getKey().getPatternsCondition().getPatterns());
                        }
                        case PUT -> {
                            assert infoEntry.getKey().getPatternsCondition() != null;
                            put.addAll(infoEntry.getKey().getPatternsCondition().getPatterns());
                        }
                        case PATCH -> {
                            assert infoEntry.getKey().getPatternsCondition() != null;
                            patch.addAll(infoEntry.getKey().getPatternsCondition().getPatterns());
                        }
                        case DELETE -> {
                            assert infoEntry.getKey().getPatternsCondition() != null;
                            delete.addAll(infoEntry.getKey().getPatternsCondition().getPatterns());
                        }
                        default -> {
                        }
                    }
                } else {
                    assert infoEntry.getKey().getPatternsCondition() != null;
                    all.addAll(infoEntry.getKey().getPatternsCondition().getPatterns());
                }

            }
        }
        anonymousUrls.put(HttpMethod.GET.toString(), get);
        anonymousUrls.put(HttpMethod.POST.toString(), post);
        anonymousUrls.put(HttpMethod.PUT.toString(), put);
        anonymousUrls.put(HttpMethod.PATCH.toString(), patch);
        anonymousUrls.put(HttpMethod.DELETE.toString(), delete);
        anonymousUrls.put("ALL", all);
        logger.info(String.valueOf(anonymousUrls));
        logger.info(Arrays.toString(anonymousUrls.get("ALL").toArray(String[]::new)));
        return anonymousUrls;
    }

}
