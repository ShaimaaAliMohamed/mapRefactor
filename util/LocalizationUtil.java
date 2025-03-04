package com.sadeem.smap.util;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class LocalizationUtil {

    private final MessageSource messageSource;

    public LocalizationUtil(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    public String getMessage(String key, Locale locale) {
        return messageSource.getMessage(key, null, locale);
    }
}