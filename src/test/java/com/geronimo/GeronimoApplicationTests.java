package com.geronimo;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import java.net.URL;
import java.net.URLClassLoader;


@RunWith(SpringRunner.class)
@GeronimoSpringTest
@Slf4j
public class GeronimoApplicationTests {

	@Test
	public void contextLoads() {
		printClasspath();
	}

	private void printClasspath() {
		ClassLoader cl = ClassLoader.getSystemClassLoader();
		URL[] urls = ((URLClassLoader)cl).getURLs();
		log.info("Printing out classpath entries:");
		for(URL url: urls){
			log.info("Classpath entry: " + url.getFile());
		}
	}

}
