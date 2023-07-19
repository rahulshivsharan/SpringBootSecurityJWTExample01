package com.sm.repository;

import com.sm.entity.UserEntity;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserEntityRepository extends JpaRepository<UserEntity, Integer>{
	UserEntity findByUsername(String username);
	
	List<UserEntity> findAll();
}
