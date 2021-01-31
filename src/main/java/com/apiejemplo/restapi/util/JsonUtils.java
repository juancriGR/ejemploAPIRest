package com.apiejemplo.restapi.util;

import java.time.LocalDateTime;
import java.util.Map;


public class JsonUtils {
	
	/**
	 * Generate response body.
	 *
	 * @param jsonBody the json body
	 * @param messageKey the message key
	 * @param errorKey the error key
	 * @param pathKey the path key
	 * @param tokenKey the token key
	 */
	public static void generateResponseBody(Map<String, Object> jsonBody, Object messageKey, String errorKey,
			String pathKey, String tokenKey) {
		jsonBody.put(Constant.TIMESTAMP_KEY, LocalDateTime.now());
		if (messageKey != null) {
			jsonBody.put(Constant.MESAGGE_KEY, messageKey);
		}
		if (errorKey != null) {
			jsonBody.put(Constant.ERROR_KEY, errorKey);
		}
		
		if (tokenKey != null) {
			jsonBody.put(Constant.TOKEN_KEY, tokenKey);
		}
		jsonBody.put(Constant.PATH_KEY, pathKey);
	}

}
