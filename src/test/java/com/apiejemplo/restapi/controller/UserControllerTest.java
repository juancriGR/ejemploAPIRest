package com.apiejemplo.restapi.controller;

import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import com.apiejemplo.restapi.controller.UserController;
import com.apiejemplo.restapi.model.UserModel;
import com.apiejemplo.restapi.util.ConfigMessageProperties;
import com.apiejemplo.restapi.util.JsonUtilTest;

@RunWith(SpringRunner.class)
@SpringBootTest(properties = { "spring.jpa.database-platform=org.hibernate.dialect.H2Dialect" })
@AutoConfigureTestDatabase
public class UserControllerTest {
	
    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private ConfigMessageProperties configMessagesProp;

    @Mock
    private UserDetailsService userDetailsService;

    @Mock
    private JsonUtilTest jwtTokenUtil;

    @InjectMocks
    @Autowired
    private UserController userController;
    

    @Before
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
    }
    
    @Test
    public void testLoginOk() throws Exception {

        UserModel userModel = new UserModel("user01", "password");

        when(userDetailsService.loadUserByUsername(userModel.getUsername()))
            .thenReturn(new User(userModel.getUsername(), userModel.getPassword(), new ArrayList()));

        ResponseEntity<?> response = userController.createAuthenticationToken(userModel);

        Assert.assertNotNull("Object not null", response);
        Assert.assertEquals("Same objets : ", HttpStatus.OK.value(),
            response.getStatusCodeValue());

        LinkedHashMap<Object, String> hash = (LinkedHashMap<Object, String>) response.getBody();


        Assert.assertEquals("Same objets", "/login", hash.get("path"));

    }
    
    @Test
    public void testLoginKO() throws Exception {

        UserModel userModel = new UserModel("user02test", "password");

        when(userDetailsService.loadUserByUsername(userModel.getUsername()))
            .thenThrow(UsernameNotFoundException.class);

        ResponseEntity<?> response = userController.createAuthenticationToken(userModel);

        Assert.assertNotNull("Object not null", response);
        Assert.assertEquals("Same objets : ", HttpStatus.BAD_REQUEST.value(),
            response.getStatusCodeValue());

        LinkedHashMap<Object, String> hash = (LinkedHashMap<Object, String>) response.getBody();


        Assert.assertEquals("Same objets", "/login", hash.get("path"));

    }

}
