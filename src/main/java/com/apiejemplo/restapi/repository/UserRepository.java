package com.apiejemplo.restapi.repository;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.apiejemplo.restapi.entity.UserDB;

@Repository("userRepository")
public interface UserRepository extends JpaRepository<UserDB, Serializable> {

	public abstract UserDB findByUsername(String username);
	
}
