package com.whoimi.config.security;

import jakarta.servlet.http.HttpSession;
import com.whoimi.model.UserInfo;
import com.whoimi.repository.UserInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * @author xxm
 * @date 2021/3/30 10:39 自定义UserDetailsService 接口
 */
@Service
public class CustomUserServiceImpl implements UserDetailsService {

  @Autowired
  private UserInfoRepository userInfoRepository;

  // 授权过程
  @Override
  /** 根据数据库获得用户信息，并且查询出权限，返回带有权限的用户信息。 */
  public UserDetails loadUserByUsername(String username) {

    UserInfo userInfo = userInfoRepository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException(username));
    if (userInfo != null) {
      HttpServletRequest request =
          ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
      HttpSession session = request.getSession();
      session.setAttribute("username", username);
      List<String> permissionCodess = null;
      List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
      for (String permissionCode : permissionCodess) {
        if (permissionCode != null && permissionCode != "") {
          GrantedAuthority grantedAuthority =
                  new SimpleGrantedAuthority(permissionCode);
          grantedAuthorities.add(grantedAuthority);
        }
      }
      // 返回带有权限的user
      return new User(userInfo.getUsername(), userInfo.getPassword(), grantedAuthorities);
    } else {
      throw new UsernameNotFoundException("admin: " + username + " do not exist!");
    }
  }
}
