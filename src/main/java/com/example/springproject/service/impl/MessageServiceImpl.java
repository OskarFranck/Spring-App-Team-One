package com.example.springproject.service.impl;

import com.example.springproject.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import static org.springframework.context.i18n.LocaleContextHolder.getLocale;

@Service
public class MessageServiceImpl implements MessageService {

    @Autowired
    private MessageSource messageSource;

    @Override
    public String getLocalMessage(String messageType) {

        return messageSource.getMessage(messageType, null, getLocale());
    }

}
