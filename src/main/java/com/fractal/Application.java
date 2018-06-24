package com.fractal;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Created by suman.das on 11/15/16.
 */
@Configuration
@EnableAutoConfiguration
@ComponentScan({"com.fractal","com.fractal.controller","com.fractal.model","config"})
public class Application {
    public static void main(String[] args){
        SpringApplication.run(Application.class,args);
    }


}
