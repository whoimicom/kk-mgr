package com.whoimi.rest;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com.whoimi.config.security.UserDetailsServiceWiiImpl;
import com.whoimi.log.LogWiiAnnotation;
import com.whoimi.model.MetaVO;
import com.whoimi.model.UserInfoDTO;
import com.whoimi.model.UserPermissionVO;
import com.whoimi.service.UserInfoService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.web.bind.annotation.*;


import java.io.IOException;
import java.util.*;

/**
 * @author whoimi
 */
@RestController
@CrossOrigin
public class AuthenticationController {
    private final AuthenticationManager authenticationManager;
    private final UserDetailsServiceWiiImpl userDetailsService;
    private final UserInfoService userInfoService;
    private final RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();


    public AuthenticationController(AuthenticationManager authenticationManager, UserDetailsServiceWiiImpl userDetailsService, UserInfoService userInfoService) {
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.userInfoService = userInfoService;
    }

    @RequestMapping(value = "/authenticate", method = RequestMethod.POST)
    @LogWiiAnnotation
    public void createAuthenticationToken(HttpServletRequest request, HttpServletResponse response, @RequestBody UserInfoDTO userInfoDTO) throws IOException {
        String username = userInfoDTO.getUsername();
        String password = userInfoDTO.getPassword();
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        final UserDetails userDetails = userDetailsService.loadUserByUsername(username);
//        return ResponseEntity.ok(userDetails);
        redirectStrategy.sendRedirect(request, response, "/index.html");
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    @LogWiiAnnotation
    public ResponseEntity<?> saveUser(@RequestBody UserInfoDTO user) {
        return ResponseEntity.ok(userInfoService.save(user));
    }

//    @PostMapping(value = "/user/logout")
//    @KkLog
//    public ResponseEntity<?> logout() {
//        return ResponseEntity.ok("SUCCESS");
//    }

    @PostMapping(value = "/getInfo")
    @LogWiiAnnotation
    public ResponseEntity<?> getInfo() {
        UserInfoDTO us = new UserInfoDTO();
        us.setAvatar("https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif");
        us.setEnabled(true);
        us.setIntroduction("I am a super administrator");
        us.setUsername("admin");
        us.setPassword("pwd");
        us.setRoles("admin");
//        path: 'page',
//                component: 'views/permission/page',
//                name: 'PagePermission',
//                meta: {
//                  title: 'Page Permission',
//                    roles: ['admin']
//        }

        MetaVO metaVO1 = new MetaVO("Page Permission", "lock");
        MetaVO metaVO = new MetaVO("PermissionTitle", "lock");
        UserPermissionVO userPermissionVO1 = new UserPermissionVO("PagePermission", "page", "", "permission/page", false, metaVO1, null);
        UserPermissionVO userPermissionVO2 = new UserPermissionVO("RolePermission", "role", "", "permission/role", false, metaVO1, null);
        List<UserPermissionVO> userPermissionVOS = Arrays.asList(userPermissionVO1, userPermissionVO2);
//        UserPermissionVO userPermissionVO = new UserPermissionVO("permission", "/permission", "/permission/page", "Layout", false, metaVO, Collections.singletonList(userPermissionVO1));
        UserPermissionVO userPermissionVO = new UserPermissionVO("permissionName", "/permission", "", "Layout", true, metaVO, userPermissionVOS);
        us.setVo(Collections.singletonList(userPermissionVO));

//        return ResponseEntity.ok(jwtTokenUtil.getCurrentUser());
        return ResponseEntity.ok(us);
    }

}
