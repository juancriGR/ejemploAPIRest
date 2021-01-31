package com.apiejemplo.restapi.controller;


import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import com.apiejemplo.restapi.controller.TaskController;
import com.apiejemplo.restapi.model.TaskModel;
import com.apiejemplo.restapi.util.ConfigMessageProperties;
import com.apiejemplo.restapi.util.JsonUtilTest;

@RunWith(SpringRunner.class)
@SpringBootTest(properties = { "spring.jpa.database-platform=org.hibernate.dialect.H2Dialect" })
@AutoConfigureTestDatabase
public class TaskControllerTest {
	
    @Mock
    private AuthenticationManager authenticationManager;
    
	@Mock
	private Authentication authentication;
	
	@Mock
	private SecurityContext securityContext;

    @Mock
    private ConfigMessageProperties configMessagesProp;

    @InjectMocks
    @Autowired
    private TaskController taskController;
    

    @Before
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
        
        securityContext.setAuthentication(authentication);
        SecurityContextHolder.setContext(securityContext);
    }
    
    @Test
    public void testSaveTaskOK() {
        TaskModel taskModel = new TaskModel( "titleTest","userTest", "descriptionTest", false);

        List<TaskModel> listeTask = new ArrayList<TaskModel>();
        listeTask.add(taskModel);
        
        
        
        when(SecurityContextHolder.getContext().getAuthentication())
        .thenReturn(authentication);
        when(authentication.getPrincipal())
        .thenReturn("userTest");
                
        ResponseEntity<?> response = taskController.saveTask(listeTask);

        Assert.assertNotNull("Object not null", response);
        Assert.assertEquals("Same objets : ", HttpStatus.OK.value(),
            response.getStatusCodeValue());

        LinkedHashMap<Object, String> hash = (LinkedHashMap<Object, String>) response.getBody();


        Assert.assertEquals("Same objets", "/apiejemplo/savetask", hash.get("path"));
    }
    
    @Test
    public void testSaveTaskKO() {

        List<TaskModel> listeTask = new ArrayList<TaskModel>();
        ResponseEntity<?> response = taskController.saveTask(listeTask);

        Assert.assertNotNull("Object not null", response);
        Assert.assertEquals("Same objets : ", HttpStatus.BAD_REQUEST.value(),
            response.getStatusCodeValue());

        LinkedHashMap<Object, String> hash = (LinkedHashMap<Object, String>) response.getBody();


        Assert.assertEquals("Same objets", "/apiejemplo/savetask", hash.get("path"));
    }
    
    @Test
    @Sql("/test-h2.sql")
    public void testFinishTaskOK() {
        TaskModel taskModel = new TaskModel( "titleTest","userTest", "descriptionTest", true);
              
        ResponseEntity<?> response = taskController.finisTask(taskModel);

        Assert.assertNotNull("Object not null", response);
        Assert.assertEquals("Same objets : ", HttpStatus.OK.value(),
            response.getStatusCodeValue());

        LinkedHashMap<Object, String> hash = (LinkedHashMap<Object, String>) response.getBody();


        Assert.assertEquals("Same objets", "/apiejemplo/finishtask", hash.get("path"));
    }
    
    @Test
    public void testFinishTaskKO() {

        TaskModel taskModel = new TaskModel( "WorngtitleTest","userTest", "descriptionTest", true);
        
        ResponseEntity<?> response = taskController.finisTask(taskModel);

        Assert.assertNotNull("Object not null", response);
        Assert.assertEquals("Same objets : ", HttpStatus.BAD_REQUEST.value(),
            response.getStatusCodeValue());

        LinkedHashMap<Object, String> hash = (LinkedHashMap<Object, String>) response.getBody();


        Assert.assertEquals("Same objets", "/apiejemplo/finishtask", hash.get("path"));
    }
    
    @Test
    public void testDeleteTaskOK() {
        TaskModel taskModel = new TaskModel( "deletedTitleTest","userTest", "descriptionTest", true);
        
        ResponseEntity<?> response = taskController.deleteTask(taskModel);

        Assert.assertNotNull("Object not null", response);
        Assert.assertEquals("Same objets : ", HttpStatus.OK.value(),
            response.getStatusCodeValue());

        LinkedHashMap<Object, String> hash = (LinkedHashMap<Object, String>) response.getBody();


        Assert.assertEquals("Same objets", "/apiejemplo/deletetask", hash.get("path"));
    }
    
    @Test
    public void testDeleteTaskKO() {
       TaskModel taskModel = new TaskModel( "wrongTitle","userTest", "descriptionTest", true);
        
        ResponseEntity<?> response = taskController.deleteTask(taskModel);

        Assert.assertNotNull("Object not null", response);
        Assert.assertEquals("Same objets : ", HttpStatus.BAD_REQUEST.value(),
            response.getStatusCodeValue());

        LinkedHashMap<Object, String> hash = (LinkedHashMap<Object, String>) response.getBody();


        Assert.assertEquals("Same objets", "/apiejemplo/deletetask", hash.get("path"));
    }

    

    	

}
