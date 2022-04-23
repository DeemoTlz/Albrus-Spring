package com.deemo.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class DeemoListenerServiceImpl {

    @EventListener(classes = {ApplicationEvent.class})
    public void listener(ApplicationEvent event) {
        log.info("receive event: {}.", event);
    }

}
