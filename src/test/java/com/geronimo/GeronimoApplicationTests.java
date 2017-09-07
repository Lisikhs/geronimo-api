package com.geronimo;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.net.URL;
import java.net.URLClassLoader;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GeronimoApplicationTests {

	private static final Logger logger = LoggerFactory.getLogger(GeronimoApplicationTests.class);

	@Test
	public void contextLoads() {
		printClasspath();
	}

	private void printClasspath() {
		ClassLoader cl = ClassLoader.getSystemClassLoader();
		URL[] urls = ((URLClassLoader)cl).getURLs();
		logger.info("Printing out classpath entries:");
		for(URL url: urls){
			logger.info("Classpath entry: " + url.getFile());
		}
	}

}
