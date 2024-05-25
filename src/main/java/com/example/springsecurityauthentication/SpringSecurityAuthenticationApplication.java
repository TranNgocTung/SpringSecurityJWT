package com.example.springsecurityauthentication;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringSecurityAuthenticationApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringSecurityAuthenticationApplication.class, args);
        int [] arr = {1,2,3,4,5,6,7,8,9,10};
        for (int i = 0; i < arr.length; i++) {
            System.out.println(arr[i]);
        }

    }

}
