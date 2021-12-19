package com.example.springproject.service.impl;

import com.example.springproject.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import static org.springframework.context.i18n.LocaleContextHolder.getLocale;

@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {

    private final MessageSource messageSource;

    @Override
    public String getLocalMessage(String messageType) {

        return messageSource.getMessage(messageType, null, getLocale());
    }

}
