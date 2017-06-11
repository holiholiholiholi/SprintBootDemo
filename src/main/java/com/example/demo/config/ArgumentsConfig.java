package com.example.demo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class ArgumentsConfig {
	@Autowired
    public ArgumentsConfig(ApplicationArguments args) {
        boolean debug = args.containsOption("debug");
        log.info("debug: {}, source args: {}, option names: {}, non option names: {}", 
        		debug, args.getSourceArgs(), args.getOptionNames(), args.getNonOptionArgs());
	}

}
