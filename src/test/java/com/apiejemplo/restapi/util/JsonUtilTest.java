package com.apiejemplo.restapi.util;

import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import com.apiejemplo.restapi.util.Constant;
import com.apiejemplo.restapi.util.JsonUtils;

public class JsonUtilTest {
	
    
    
    @Test
    public void generateResponseBodyOk() {
    	Map<String, Object> jsonBody = new LinkedHashMap<>();
    	
    	JsonUtils.generateResponseBody(jsonBody, null, null,
    			null, null);
    	
    	 Assert.assertNotNull("Object not null", jsonBody.get(Constant.TIMESTAMP_KEY));
    	 Assert.assertNull("Object null", jsonBody.get(Constant.MESAGGE_KEY));
    	 Assert.assertNull("Object null", jsonBody.get(Constant.ERROR_KEY));
    	 Assert.assertNull("Object null", jsonBody.get(Constant.PATH_KEY));
    	
    }

}
