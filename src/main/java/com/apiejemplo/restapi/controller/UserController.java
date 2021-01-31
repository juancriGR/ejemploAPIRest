package com.apiejemplo.restapi.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.apiejemplo.restapi.model.UserModel;
import com.apiejemplo.restapi.util.ConfigMessageProperties;
import com.apiejemplo.restapi.util.JsonUtils;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;




@RestController
@RequestMapping("/")
public class UserController {
	
	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private UserDetailsService userDetailsService;
	
	@Autowired
	private ConfigMessageProperties configMessageProperties;
	
	@Value("${jwt.secret}")
	private String secret;

	@Value("${jwt.expiration}")
	private long tokenValidity;
	
	private static final Log LOG = LogFactory.getLog(UserController.class);

	/**
	 * Creates the authentication token.
	 *
	 * @param userModel the user model
	 * @return the response entity
	 * @throws Exception the exception
	 */
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public ResponseEntity<?> createAuthenticationToken(@RequestBody UserModel userModel) throws Exception {
		LOG.info("METHOD: createAuthenticationToken()"+ " --PARAMS : "+userModel.toString());
		Map<String, Object> jsonBody = new LinkedHashMap<>();
		ResponseEntity<Map<String, Object>> response;
		
		
		authenticate(userModel.getUsername(), userModel.getPassword());
		try {
			final UserDetails userDetails = userDetailsService.loadUserByUsername(userModel.getUsername());
			final String token = generateToken(userDetails);
			
			JsonUtils.generateResponseBody(jsonBody,configMessageProperties.getConfigValue("user.auth.ok"), null,
					"/login", token);
			response = ResponseEntity.ok(jsonBody);
		}catch(UsernameNotFoundException e) {
			JsonUtils.generateResponseBody(jsonBody,null, configMessageProperties.getConfigValue("user.auth.ko"),
					"/login", null);
			response = ResponseEntity.badRequest().body(jsonBody);
		}

	

		return response;
	}

	/**
	 * Authenticate.
	 *
	 * @param username the username
	 * @param password the password
	 * @throws Exception the exception
	 */
	private void authenticate(String username, String password) throws Exception {
		
		LOG.info("METHOD: authenticate()"+ " --PARAMS : "+username);
		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
		} catch (DisabledException e) {
			throw new Exception("USER_DISABLED", e);
		} catch (BadCredentialsException e) {
			throw new Exception("INVALID_CREDENTIALS", e);
		}
	}
	
	
	/**
	 * Generate token.
	 *
	 * @param userDetails the user details
	 * @return the string
	 */
	private String generateToken(UserDetails userDetails) {
		LOG.info("METHOD: generateToken()"+ " --PARAMS : "+userDetails.toString());
		
		Map<String, Object> claims = new HashMap<>();
		List<GrantedAuthority> grantedAuthorities = AuthorityUtils
				.commaSeparatedStringToAuthorityList("ROLE_USER");
		claims.put("authorities", grantedAuthorities.stream()
				.map(GrantedAuthority::getAuthority)
				.collect(Collectors.toList()));
		return doGenerateToken(claims, userDetails.getUsername());
	}

	/**
	 * Do generate token.
	 *
	 * @param claims the claims
	 * @param subject the subject
	 * @return the string
	 */
	private String doGenerateToken(Map<String, Object> claims, String subject) {
		LOG.info("METHOD: generateToken()");
		return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + tokenValidity*1000)).signWith(SignatureAlgorithm.HS512, secret.getBytes()).compact();
	}

}
