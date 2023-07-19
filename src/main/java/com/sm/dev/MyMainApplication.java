package com.sm.dev;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.sm.entity.UserEntity;
import com.sm.jwt.util.JSONWebTokenUtil;
import com.sm.repository.UserEntityRepository;

/**
 * Hello world!
 *
 */

@SpringBootApplication
@EntityScan("com.sm.entity")
@ComponentScan({"com.sm.*"})
@EnableJpaRepositories("com.sm.repository")
public class MyMainApplication {
	
	@Autowired
	private UserEntityRepository repository;

	@PostConstruct
	public void initUser() {
		System.out.println("Spring application initiated");
		
		List<UserEntity> userList = Stream.of(	new UserEntity(101, "testingrahul", "testingrahul@gmail.com", 	"password"),
												new UserEntity(102, "testingsam", 	"testingsam@gmail.com", 	"password01"),	
												new UserEntity(103, "testingtim", 	"testingtim@gmail.com", 	"password02")
									).collect(Collectors.toList());
		
		repository.saveAll(userList);
	}
	
    public static void main( String[] args ){
    	JSONWebTokenUtil util = new JSONWebTokenUtil();
    	String token = util.generateToken("testingrahul");
    	SpringApplication.run(MyMainApplication.class, args);
        System.out.println( "Hello World ! from Spring App  " + token);
    }
}
