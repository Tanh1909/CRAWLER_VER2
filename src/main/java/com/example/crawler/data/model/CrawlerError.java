package com.example.crawler.data.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CrawlerError {

    private Integer stepNumber;

    private String stepName;

    private String message;

    private Exception exception;

}
