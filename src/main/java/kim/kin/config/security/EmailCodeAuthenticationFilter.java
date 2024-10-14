package kim.kin.config.security;

import jakarta.servlet.ServletException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

/**
 * @Author: crush
 * @Date: 2021-09-08 21:13
 * version 1.0
 */
public class EmailCodeAuthenticationFilter  extends AbstractAuthenticationProcessingFilter {
	/**
	 * 前端传来的 参数名 - 用于request.getParameter 获取
	 */
	private final String DEFAULT_EMAIL_NAME="email";

	private final String DEFAULT_EMAIL_CODE="e_code";

	@Autowired
	@Override
	public void setAuthenticationManager(AuthenticationManager authenticationManager) {
		super.setAuthenticationManager(authenticationManager);
	}
	/**
	 * 是否 仅仅post方式
	 */
	private boolean postOnly = true;

	/**
	 * 通过 传入的 参数 创建 匹配器
	 * 即 Filter过滤的url
	 */
	public EmailCodeAuthenticationFilter() {
		super(new AntPathRequestMatcher("/email/login","POST"));
	}


	/**
	 * filter 获得 用户名（邮箱） 和 密码（验证码） 装配到 token 上 ，
	 * 然后把token 交给 provider 进行授权
	 */
	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
		if(postOnly && !request.getMethod().equals("POST") ){
			throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
		}else{
			String email = getEmail(request);
			if(email == null){
				email = "";
			}
			email = email.trim();
			//如果 验证码不相等 故意让token出错 然后走springsecurity 错误的流程
			boolean flag = checkCode(request);
			//封装 token
			EmailCodeAuthenticationToken token = new EmailCodeAuthenticationToken(email,new ArrayList<>());
			this.setDetails(request,token);
			//交给 manager 发证
			return this.getAuthenticationManager().authenticate(token);
		}
	}

	/**
	 * 获取 头部信息 让合适的provider 来验证他
	 */
	public void setDetails(HttpServletRequest request , EmailCodeAuthenticationToken token ){
		token.setDetails(this.authenticationDetailsSource.buildDetails(request));
	}

	/**
	 * 获取 传来 的Email信息
	 */
	public String getEmail(HttpServletRequest request ){
		String result=  request.getParameter(DEFAULT_EMAIL_NAME);
		return result;
	}

	/**
	 * 判断 传来的 验证码信息 以及 session 中的验证码信息
	 */
	public boolean checkCode(HttpServletRequest request ){
		String code1 = request.getParameter(DEFAULT_EMAIL_CODE);
		System.out.println("code1**********"+code1);
		// TODO 另外再写一个链接 生成 验证码 那个验证码 在生成的时候  存进redis 中去
		//TODO  这里的验证码 写在Redis中， 到时候取出来判断即可 验证之后 删除验证码
		if(code1.equals("123456")){
			return true;
		}
		return false;
	}
	// set、get方法...
}
