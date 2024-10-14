package com.whoimi.config.security;


import com.whoimi.config.handler.AccessDeniedWiiImpl;
import com.whoimi.config.handler.AuthenticationFailureWiiImpl;
import com.whoimi.config.handler.AuthenticationSuccessWiiImpl;
import com.whoimi.repository.UserInfoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.condition.PathPatternsRequestCondition;
import org.springframework.web.servlet.mvc.condition.PatternsRequestCondition;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.*;

import static org.springframework.http.HttpMethod.*;

/**
 * @author whoimi
 * @see <a href="https://spring.io/blog/2022/02/21/spring-security-without-the-websecurityconfigureradapter">spring-security-without-the-websecurityconfigureradapter</a>
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class WebSecurityWiiConfigurer {
    private static final Logger log = LoggerFactory.getLogger(WebSecurityWiiConfigurer.class);
    private final AccessDeniedWiiImpl accessDeniedWiiImpl;
    private final AuthenticationFailureWiiImpl authenticationFailureWiiImpl;
    private final AuthenticationSuccessWiiImpl authenticationSuccessWiiImpl;
    private final ApplicationContext applicationContext;

    private UserInfoRepository userInfoRepository;

    public WebSecurityWiiConfigurer(AccessDeniedWiiImpl accessDeniedWiiImpl, AuthenticationFailureWiiImpl authenticationFailureWiiImpl, AuthenticationSuccessWiiImpl authenticationSuccessWiiImpl, ApplicationContext applicationContext) {
        this.accessDeniedWiiImpl = accessDeniedWiiImpl;
        this.authenticationFailureWiiImpl = authenticationFailureWiiImpl;
        this.authenticationSuccessWiiImpl = authenticationSuccessWiiImpl;
        this.applicationContext = applicationContext;
    }

    @Autowired
    public void setUserInfoRepository(UserInfoRepository userInfoRepository) {
        this.userInfoRepository = userInfoRepository;
    }

    @Bean
    public AuthenticationManager authenticationManager() {
        //不擦除认证密码，擦除会导致TokenBasedRememberMeServices因为找不到Credentials再调用UserDetailsService而抛出UsernameNotFoundException
        return new ProviderManager();
//        return new ProviderManager(Arrays.asList(EmailCodeAuthenticationToken(), daoAuthenticationProvider()));
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    //    @Bean//刷新token时自动调用
//    public PreAuthenticatedAuthenticationProvider preAuthenticatedAuthenticationProvider() {
//        PreAuthenticatedAuthenticationProvider provider = new PreAuthenticatedAuthenticationProvider();
//        provider.setPreAuthenticatedUserDetailsService(new UserDetailsByNameServiceWrapper<>(userDetailsServiceImpl));
//        return provider;
//    }
    @Bean
    DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setPasswordEncoder(new BCryptPasswordEncoder());
//        daoAuthenticationProvider.setUserDetailsService(userDetailsServiceImpl);
        return daoAuthenticationProvider;
    }


//    @Bean
//    public WebSecurityCustomizer webSecurityCustomizer() {
//        return web -> web.ignoring().requestMatchers("/register", "/")
//                .requestMatchers("/swagger**/**")
//                .requestMatchers("/webjars/**")
//                .requestMatchers("/v3/**")
//                .requestMatchers("/doc.html");
//    }

    @Order(SecurityProperties.BASIC_AUTH_ORDER)
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity, AuthenticationManager authenticationManager) throws Exception {
        Map<RequestMappingInfo, HandlerMethod> handlerMethodMap = applicationContext.getBean("requestMappingHandlerMapping", RequestMappingHandlerMapping.class).getHandlerMethods();
        Map<String, Set<String>> anonymousUrls = anonymousUrls(handlerMethodMap);
//        httpSecurity
//                .authenticationProvider(emailAuthenticationProvider())
//                .authenticationManager(authenticationManager)
/*                .addFilterBefore(emailAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(new JwtRequestFilter(authenticationManager, jwtTokenUtil), UsernamePasswordAuthenticationFilter.class)
                .addFilter(new UsernamePasswordKimFilter(authenticationManager, jwtTokenUtil));*/
        // We don't need CSRF for this example
        httpSecurity.csrf(AbstractHttpConfigurer::disable);
//        AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry auth = httpSecurity.csrf().disable().authorizeHttpRequests();

        httpSecurity.authorizeHttpRequests(authorizationManagerRequestMatcherRegistry -> {
            authorizationManagerRequestMatcherRegistry.requestMatchers(HttpMethod.OPTIONS, "/**").permitAll();
            authorizationManagerRequestMatcherRegistry.requestMatchers("/**.png").permitAll();
            log.info(Arrays.toString(anonymousUrls.get(GET.toString()).toArray(String[]::new)));
            if (!anonymousUrls.get(GET.toString()).isEmpty()) {
                authorizationManagerRequestMatcherRegistry.requestMatchers(GET, anonymousUrls.get(GET.toString()).toArray(String[]::new)).permitAll();
            }
            if (!anonymousUrls.get(POST.toString()).isEmpty()) {
                authorizationManagerRequestMatcherRegistry.requestMatchers(POST, anonymousUrls.get(POST.toString()).toArray(String[]::new)).permitAll();
            }
            if (!anonymousUrls.get(PUT.toString()).isEmpty()) {
                authorizationManagerRequestMatcherRegistry.requestMatchers(POST, anonymousUrls.get(PUT.toString()).toArray(String[]::new)).permitAll();
            }
            if (!anonymousUrls.get(PATCH.toString()).isEmpty()) {
                authorizationManagerRequestMatcherRegistry.requestMatchers(PATCH, anonymousUrls.get(PATCH.toString()).toArray(String[]::new)).permitAll();
            }
            if (!anonymousUrls.get(DELETE.toString()).isEmpty()) {
                authorizationManagerRequestMatcherRegistry.requestMatchers(DELETE, anonymousUrls.get(DELETE.toString()).toArray(String[]::new)).permitAll();
            }
            if (!anonymousUrls.get("ALL").isEmpty()) {
                authorizationManagerRequestMatcherRegistry.requestMatchers(anonymousUrls.get("ALL").toArray(String[]::new)).permitAll();
            }
            // all other requests need to be authenticated
            authorizationManagerRequestMatcherRegistry.anyRequest().authenticated();
        });


/*        auth.requestMatchers(HttpMethod.OPTIONS, "/**").permitAll();
        log.info(anonymousUrls.get(GET.toString()).toArray(String[]::new).toString());
        if (anonymousUrls.get(GET.toString()).size() > 0) {
            auth.requestMatchers(GET, anonymousUrls.get(GET.toString()).toArray(String[]::new)).permitAll();
        }
        if (anonymousUrls.get(POST.toString()).size() > 0) {
            auth.requestMatchers(POST, anonymousUrls.get(POST.toString()).toArray(String[]::new)).permitAll();
        }
        if (anonymousUrls.get(PUT.toString()).size() > 0) {
            auth.requestMatchers(POST, anonymousUrls.get(PUT.toString()).toArray(String[]::new)).permitAll();
        }
        if (anonymousUrls.get(PATCH.toString()).size() > 0) {
            auth.requestMatchers(PATCH, anonymousUrls.get(PATCH.toString()).toArray(String[]::new)).permitAll();
        }
        if (anonymousUrls.get(DELETE.toString()).size() > 0) {
            auth.requestMatchers(DELETE, anonymousUrls.get(DELETE.toString()).toArray(String[]::new)).permitAll();
        }
        if (anonymousUrls.get("ALL").size() > 0) {
            auth.requestMatchers(anonymousUrls.get("ALL").toArray(String[]::new)).permitAll();
        }
        auth.anyRequest().authenticated();*/

        // make sure we use stateless session; session won't be used to
        // store user's state.
//        httpSecurity.exceptionHandling(httpSecurityExceptionHandlingConfigurer -> httpSecurityExceptionHandlingConfigurer.authenticationEntryPoint(authenticationEntryPointKimImpl)
//                .accessDeniedHandler(accessDeniedKimImpl));
        httpSecurity.sessionManagement(httpSecuritySessionManagementConfigurer -> httpSecuritySessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
//        auth.and().exceptionHandling().authenticationEntryPoint(authenticationEntryPointKimImpl)
//                .accessDeniedHandler(accessDeniedKimImpl)
//                .and().sessionManagement()
//                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        return httpSecurity.build();

        // Add a filter to validate the tokens with every request
//        httpSecurity.addFilterBefore(new JwtRequestFilter(authenticationManager(), jwtTokenUtil), UsernamePasswordAuthenticationFilter.class);
//        httpSecurity.addFilter(new JwtRequestFilter(authenticationManager(), jwtTokenUtil));
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
            AnonymousWiiAccess anonymousWiiAccess = handlerMethod.getMethodAnnotation(AnonymousWiiAccess.class);
            if (null != anonymousWiiAccess) {
                List<RequestMethod> requestMethods = new ArrayList<>(infoEntry.getKey().getMethodsCondition().getMethods());
                if (!requestMethods.isEmpty()) {
                    HttpMethod httpMethod = HttpMethod.valueOf(requestMethods.get(0).name());
                    HttpMethod method = Objects.requireNonNull(httpMethod);
                    Set<String> path = Optional.of(infoEntry).map(Map.Entry::getKey).map(RequestMappingInfo::getPatternsCondition).map(PatternsRequestCondition::getPatterns)
                            .orElse(Optional.of(infoEntry).map(Map.Entry::getKey).map(RequestMappingInfo::getPathPatternsCondition).map(PathPatternsRequestCondition::getPatternValues).get());
                    if (method.equals(GET)) {
                        get.addAll(path);
                    } else if (method.equals(POST)) {
                        post.addAll(path);
                    } else if (method.equals(PUT)) {
                        put.addAll(path);
                    } else if (method.equals(PATCH)) {
                        patch.addAll(path);
                    } else if (method.equals(DELETE)) {
                        delete.addAll(path);
                    }
                }
//                else {
//                    all.addAll(infoEntry.getKey().getPatternsCondition().getPatterns());
//                }

            }
        }
        anonymousUrls.put(GET.toString(), get);
        anonymousUrls.put(POST.toString(), post);
        anonymousUrls.put(PUT.toString(), put);
        anonymousUrls.put(HttpMethod.PATCH.toString(), patch);
        anonymousUrls.put(HttpMethod.DELETE.toString(), delete);
        anonymousUrls.put("ALL", all);
        log.info(String.valueOf(anonymousUrls));
        log.info(Arrays.toString(anonymousUrls.get("ALL").toArray(String[]::new)));
        return anonymousUrls;
    }


}
