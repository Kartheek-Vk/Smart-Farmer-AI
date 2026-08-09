package com.smartfarmer.ai;

import org.springframework.boot.SpringApplication;

public class TestSmartFarmerAiApplication {

	public static void main(String[] args) {
		SpringApplication.from(SmartFarmerAiApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
