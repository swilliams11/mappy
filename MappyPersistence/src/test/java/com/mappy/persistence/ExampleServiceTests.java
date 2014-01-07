package com.mappy.persistence;

import static org.junit.Assert.*;

import org.junit.Test;

import com.mappy.persistence.ExampleService;


public class ExampleServiceTests{

	private ExampleService service = new ExampleService();
	
	@Test
	public void testReadOnce() throws Exception {
		assertEquals("Hello world!", service.getMessage());
	}

}
