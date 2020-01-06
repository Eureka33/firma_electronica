package com.meve.firma.digital.ws.core;

import mx.eurk.firma.digital.bean.AppContext;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class AppContextProvider implements ApplicationContextAware {

    @Override
    public void setApplicationContext( ApplicationContext ctx) throws BeansException {
        AppContext.setApplicationContext(ctx);
    }
}