package kim.kin.config.security;

import io.jsonwebtoken.ExpiredJwtException;
import kim.kin.model.UserInfo;
import kim.kin.repository.UserInfoRepository;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

/**
 * @author choky
 */
@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    private final UserInfoRepository userInfoRepository;
    private final JwtTokenUtil jwtTokenUtil;

    public JwtRequestFilter(UserInfoRepository userInfoRepository, JwtTokenUtil jwtTokenUtil) {
        this.userInfoRepository = userInfoRepository;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        final String requestTokenHeader = request.getHeader("Authorization");

        String username = null;
        String jwtToken = null;
        // JWT Token is in the form "Bearer token". Remove Bearer word and get
        // only the Token
        if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
            jwtToken = requestTokenHeader.substring(7);
            try {
                username = jwtTokenUtil.getUsernameFromToken(jwtToken);
            } catch (IllegalArgumentException e) {
                System.out.println("Unable to get JWT Token");
            } catch (ExpiredJwtException e) {
                System.out.println("JWT Token has expired");
            }
        } else {
            String requestURI = request.getRequestURI();

            logger.warn(requestURI + " :JWT Token does not begin with Bearer String");
        }

        // Once we get the token validate it.
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

//            UserDetails userDetails = userInfoRepository.findByUsername(username)
//                    .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
        // 这里需要改造成不读取数据库，解析TOKEN
            UserInfo userInfo = userInfoRepository.findByUsername(username)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found with username: "));
            List<GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority("admin"));
            UserDetails userDetails = new User(userInfo.getUsername(), userInfo.getPassword(), authorities);

            // if token is valid configure Spring Security to manually set
            // authentication
            if (jwtTokenUtil.validateToken(jwtToken, userDetails)) {
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                // After setting the Authentication in the context, we specify
                // that the current user is authenticated. So it passes the
                // Spring Security Configurations successfully.
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }
        }
        chain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        boolean result = false;
        logger.debug("getPathInfo:"+request.getPathInfo());
        logger.debug("getRequestURI:"+request.getRequestURI());
        logger.debug("getRequestURL:"+request.getRequestURL());
        logger.debug("getUserPrincipal:"+request.getUserPrincipal());
        logger.debug("getServletPath:"+request.getServletPath());
        String requestURI = request.getRequestURI();
        if (SecurityParams.LOGIN_URI.equals(requestURI)) {
            result = true;
        }
        if ("/login".equals(requestURI)) {
            result = true;
        }
        if (SecurityParams.REGISTER_URI.equals(requestURI)) {
            result = true;
        }
        if (HttpMethod.OPTIONS.toString().equals(request.getMethod())) {
            result = true;
        }
        return result;
    }

}
