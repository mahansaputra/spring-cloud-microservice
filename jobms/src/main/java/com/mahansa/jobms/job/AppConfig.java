package com.mahansa.jobms.job;

import org.springframework.web.client.RestTemplate;

public class AppConfig {

    public RestTemplate restTemplate(){
        return new RestTemplate();
    }
}
