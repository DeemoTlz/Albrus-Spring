package com.deemo.calculator;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DeemoCalculator {

    public double div(int a, int b) {
        log.info("div running...");

        if (b == 0) {
            throw new RuntimeException("b can not be 0!");
        }

        return a * 1.0 / b;
    }

}
