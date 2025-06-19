package com.example.crawler.service.field;

import com.example.crawler.config.crawler.field.FieldConfig;
import com.example.crawler.config.crawler.field.FieldProcessorConfig;
import com.example.crawler.data.enums.FieldProcessorTypeEnum;
import com.example.crawler.data.enums.FieldTypeEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.example.crawler.data.enums.FieldProcessorParamEnum.*;

@Log4j2
@Service
@RequiredArgsConstructor
public class DefaultFieldProcessor implements FieldProcessor {

    @Override
    public Object process(Object rawValue, FieldProcessorConfig config) {
        if (rawValue == null) return null;
        String rawValueString = rawValue.toString();
        FieldProcessorTypeEnum type = config.getType();
        return switch (type) {
            case TRIM -> rawValue.toString();
            case UPPERCASE -> rawValueString.toUpperCase();
            case LOWERCASE -> rawValueString.toLowerCase();
            case SUBSTRING -> {
                int start = Integer.parseInt(config.getParam(START.value(), "0"));
                int end = config.getParam(END.value()) != null ?
                        Integer.parseInt(config.getParam(END.value())) : rawValueString.length();
                yield rawValueString.substring(start, end);
            }
            case REPLACE -> rawValueString.replace(
                    config.getParam(OLD.value()),
                    config.getParam(NEW.value())
            );
            case REGEX_EXTRACT -> {
                Pattern patternExtract = Pattern.compile(config.getParam(PATTERN.value()));
                Integer groupNum = config.getIntParam(GROUP.value(), 0);
                Matcher matcherExtract = patternExtract.matcher(rawValueString);
                if (matcherExtract.find()) {
                    yield matcherExtract.group(groupNum);
                }
                yield null;
            }
            case REGEX_REPLACE -> rawValueString.replaceAll(
                    config.getParam(PATTERN.value()),
                    config.getParam(REPLACEMENT.value())
            );
            case DATE_FORMAT -> {
                SimpleDateFormat inputFormat = new SimpleDateFormat(config.getParam(INPUT_FORMAT.value()));
                SimpleDateFormat outputFormat = new SimpleDateFormat(config.getParam(OUTPUT_FORMAT.value()));
                try {
                    Date date = inputFormat.parse(rawValueString);
                    yield outputFormat.format(date);
                } catch (ParseException e) {
                    yield null;
                }
            }
            case MATH_EXPRESSION -> null;
            case URL_ENCODE -> URLEncoder.encode(rawValueString, StandardCharsets.UTF_8);
            case NONE -> rawValue;
        };
    }

    @Override
    public Object convertFieldType(Object rawValue, FieldConfig fieldConfig) {
        if (rawValue == null) return null;
        FieldTypeEnum fieldType = fieldConfig.getFieldType();
        String rawValueString = rawValue.toString();
        return switch (fieldType) {
            case TEXT -> rawValueString;
            case NUMBER -> parseNumber(rawValueString);
            case BOOLEAN -> parseBoolean(rawValueString);
            case DATE -> parseToLocalDateTime(rawValueString);
        };
    }

    private Date parseDate(String value) {
        String format;

        if (value.matches("\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}"))
            format = "yyyy-MM-dd HH:mm:ss";       // 2024-06-18 10:30:00
        else if (value.matches("\\d{2}/\\d{2}/\\d{4} \\d{2}:\\d{2}:\\d{2}"))
            format = "dd/MM/yyyy HH:mm:ss"; // 18/06/2024 10:30:00
        else if (value.matches("\\d{2}-\\d{2}-\\d{4} \\d{2}:\\d{2}:\\d{2}"))
            format = "dd-MM-yyyy HH:mm:ss"; // 18-06-2024 10:30:00
        else if (value.matches("\\d{4}/\\d{2}/\\d{2} \\d{2}:\\d{2}:\\d{2}"))
            format = "yyyy/MM/dd HH:mm:ss"; // 2024/06/18 10:30:00
        else if (value.matches("\\d{2}\\.\\d{2}\\.\\d{4} \\d{2}:\\d{2}:\\d{2}"))
            format = "dd.MM.yyyy HH:mm:ss"; // 18.06.2024 10:30:00

        else if (value.matches("\\d{4}-\\d{2}-\\d{2}")) format = "yyyy-MM-dd";
        else if (value.matches("\\d{2}/\\d{2}/\\d{4}")) format = "dd/MM/yyyy";
        else if (value.matches("\\d{2}-\\d{2}-\\d{4}")) format = "dd-MM-yyyy";
        else if (value.matches("\\d{2}\\.\\d{2}\\.\\d{4}")) format = "dd.MM.yyyy";
        else if (value.matches("\\d{4}/\\d{2}/\\d{2}")) format = "yyyy/MM/dd";
        else if (value.matches("\\d{2}\\s[a-zA-Z]+\\s\\d{4}")) format = "dd MMM yyyy"; // 18 Jun 2024
        else if (value.matches("[a-zA-Z]+\\s\\d{2},\\s\\d{4}")) format = "MMMM dd, yyyy"; // June 18, 2024
        else if (value.matches("\\d{4}\\d{2}\\d{2}")) format = "yyyyMMdd"; // 20240618
        else return null;

        try {
            return new SimpleDateFormat(format).parse(value);
        } catch (Exception e) {
            log.error("cannot parseDate: {} because: {}", value, e.getMessage());
            return null;
        }
    }

    public static LocalDateTime parseToLocalDateTime(String value) {
        DateTimeFormatter formatter;

        // LocalDateTime formats (có cả ngày + giờ)
        if (value.matches("\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}"))
            formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        else if (value.matches("\\d{2}/\\d{2}/\\d{4} \\d{2}:\\d{2}:\\d{2}"))
            formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        else if (value.matches("\\d{2}-\\d{2}-\\d{4} \\d{2}:\\d{2}:\\d{2}"))
            formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        else if (value.matches("\\d{4}/\\d{2}/\\d{2} \\d{2}:\\d{2}:\\d{2}"))
            formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        else if (value.matches("\\d{2}\\.\\d{2}\\.\\d{4} \\d{2}:\\d{2}:\\d{2}"))
            formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");

            // LocalDate only (nếu không có thời gian, sẽ gán mặc định giờ = 00:00:00)
        else if (value.matches("\\d{4}-\\d{2}-\\d{2}"))
            formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        else if (value.matches("\\d{2}/\\d{2}/\\d{4}"))
            formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        else if (value.matches("\\d{2}-\\d{2}-\\d{4}"))
            formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        else if (value.matches("\\d{2}\\.\\d{2}\\.\\d{4}"))
            formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        else if (value.matches("\\d{4}/\\d{2}/\\d{2}"))
            formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        else if (value.matches("\\d{2}\\s[a-zA-Z]+\\s\\d{4}"))
            formatter = DateTimeFormatter.ofPattern("dd MMM yyyy", Locale.ENGLISH);
        else if (value.matches("[a-zA-Z]+\\s\\d{2},\\s\\d{4}"))
            formatter = DateTimeFormatter.ofPattern("MMMM dd, yyyy", Locale.ENGLISH);
        else if (value.matches("\\d{4}\\d{2}\\d{2}"))
            formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        else
            return null;

        try {
            if (formatter.toString().contains("H")) {
                return LocalDateTime.parse(value, formatter);
            } else {
                LocalDate date = LocalDate.parse(value, formatter);
                return date.atStartOfDay(); // mặc định giờ 00:00:00
            }
        } catch (DateTimeParseException e) {
            log.error("Cannot parse localDateTime: {} - {}", value, e.getMessage());
            return null;
        }
    }


    private Number parseNumber(String value) {
        try {
            value = value.replaceAll("[^\\d.,-]", "");
            if (value.contains(",") && value.contains(".")) {
                value = value.replace(".", "").replace(",", ".");
            } else if (value.contains(",")) {
                value = value.replace(",", "");
            }
            return Double.parseDouble(value);
        } catch (Exception e) {
            log.error("cannot parseNumber: {} because: {}", value, e.getMessage());
            return null;
        }
    }

    private Boolean parseBoolean(String value) {
        return "true".equalsIgnoreCase(value) ||
                "1".equals(value) ||
                "yes".equalsIgnoreCase(value);
    }
}
