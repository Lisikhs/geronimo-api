package com.geronimo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.net.URL;
import java.net.URLClassLoader;

@SpringBootApplication
public class GeronimoApplication {

	private static final Logger logger = LoggerFactory.getLogger(GeronimoApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(GeronimoApplication.class, args);

		printClasspath();
	}

	public static void printClasspath() {
		ClassLoader cl = ClassLoader.getSystemClassLoader();
		URL[] urls = ((URLClassLoader)cl).getURLs();
		logger.info("Printing out classpath entries:");
		for(URL url: urls){
			logger.info("Classpath entry: " + url.getFile());
		}
	}
}
