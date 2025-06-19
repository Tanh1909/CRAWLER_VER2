package com.example.crawler.data.enums;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum FieldProcessorTypeEnum {
    TRIM("trim"),
    UPPERCASE("uppercase"),
    LOWERCASE("lowercase"),
    SUBSTRING("substring"),
    REPLACE("replace"),
    REGEX_EXTRACT("regex_extract"),
    REGEX_REPLACE("regex_replace"),
    DATE_FORMAT("date_format"),
    MATH_EXPRESSION("math_expression"),
    URL_ENCODE("url_encode"),
    NONE("none");
    private final String value;


}
