package com.apiejemplo.restapi.configuration;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.apiejemplo.restapi.util.Constant;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;



public class JWTAuthorizationFilter extends OncePerRequestFilter {
	

	@Value("${jwt.secret}")
	private String secret;

	
	/**
	 * Do filter internal.
	 *
	 * @param request the request
	 * @param response the response
	 * @param chain the chain
	 * @throws ServletException the servlet exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
		try {
			if (existJWTToken(request, response)) {
				Claims claims = validateToken(request);
				if (claims.get("authorities") != null) {
					setUpSpringAuthentication(claims);
				} else {
					SecurityContextHolder.clearContext();
				}
			} else {
					SecurityContextHolder.clearContext();
			}
			chain.doFilter(request, response);
		} catch (ExpiredJwtException | UnsupportedJwtException | MalformedJwtException e) {
			response.setStatus(HttpServletResponse.SC_FORBIDDEN);
			((HttpServletResponse) response).sendError(HttpServletResponse.SC_FORBIDDEN, e.getMessage());
			return;
		}
	}	

	/**
	 * Validate token.
	 *
	 * @param request the request
	 * @return the claims
	 */
	private Claims validateToken(HttpServletRequest request) {
		String jwtToken = request.getHeader(Constant.REQUEST_HEADER).replace(Constant.REQUEST_PREFIX, "");
		return Jwts.parser().setSigningKey("mimacom_2020*".getBytes()).parseClaimsJws(jwtToken).getBody();
	}


	/**
	 * Sets the up spring authentication.
	 *
	 * @param claims the new up spring authentication
	 */
	private void setUpSpringAuthentication(Claims claims) {
		@SuppressWarnings("unchecked")
		List<String> authorities = (List) claims.get("authorities");

		UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(claims.getSubject(), null,
				authorities.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList()));
		SecurityContextHolder.getContext().setAuthentication(auth);

	}

	/**
	 * Exist JWT token.
	 *
	 * @param request the request
	 * @param res the res
	 * @return true, if successful
	 */
	private boolean existJWTToken(HttpServletRequest request, HttpServletResponse res) {
		String authenticationHeader = request.getHeader(Constant.REQUEST_HEADER);
		boolean exist = true;
		if (authenticationHeader == null || !authenticationHeader.startsWith(Constant.REQUEST_PREFIX)) {
			exist = false;
		}
		return exist;
	}


}
