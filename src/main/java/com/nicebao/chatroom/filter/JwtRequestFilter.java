package com.nicebao.chatroom.filter;

import com.nicebao.chatroom.model.CustomUserDetails;
import com.nicebao.chatroom.service.UserService;
import com.nicebao.chatroom.utils.JWTUtils;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;

/**
 * @name: JwtRequestFilter
 * @author: IhaveBB
 * @date: 2024-09-01 17:53
 **/
@Component
public class JwtRequestFilter extends OncePerRequestFilter {
	@Autowired
	private UserService userService;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		String token = null;
		if (request.getCookies() != null) {
			for (Cookie cookie : request.getCookies()) {
				if ("token".equals(cookie.getName())) {
					token = cookie.getValue();
				}
			}
		}

		if (token != null) {
			Claims claims = JWTUtils.parseJWT(token);
			if (claims != null) {
				Integer userId = claims.get("userId", Integer.class);
				String username = claims.get("username", String.class);

				// 创建一个包含用户名和角色的认证对象
				UsernamePasswordAuthenticationToken authentication =
						new UsernamePasswordAuthenticationToken(
								new CustomUserDetails(userId, username),
								null,
								Collections.emptyList()
						);
				SecurityContextHolder.getContext().setAuthentication(authentication);
			} else {
				response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
				return;
			}
		}

		filterChain.doFilter(request, response);
	}
}

