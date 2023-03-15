package com.chargepoint.ringbuffer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class RingBufferApplication {
	public static void main(String[] args) {
		SpringApplication.run(RingBufferApplication.class, args);
	}
}
