package com.edel;

import org.apache.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.edel.controllers.ImageController;

@SpringBootApplication
public class Application {

	private	final static Logger log = Logger.getLogger(ImageController.class);
	
	public static void main(String[] args) throws Exception {
		log.info("Spring application starting...");
		SpringApplication.run(Application.class, args);
		log.info("Spring application started.");
	}

}
