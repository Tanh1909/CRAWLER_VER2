package com.example.crawler.config.crawler;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ApiConfig {

    private List<String> urlPatterns = new ArrayList<>();

    private Integer timeOutSeconds = 30;

}
