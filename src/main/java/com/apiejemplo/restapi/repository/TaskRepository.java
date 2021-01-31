package com.apiejemplo.restapi.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.apiejemplo.restapi.entity.Task;

@Repository("taskRepository")
public interface TaskRepository extends JpaRepository<Task, Serializable>{
	
    @Query(value="FROM Task where username=:username and ended=false")
    List<Task> findAllByUsername(@Param("username") String username);

}
