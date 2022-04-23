package com.deemo.bean.service.impi;

import com.deemo.bean.entity.Car;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

@Lazy
@Service
public class DeemoServiceImpl {

    @Lazy
    @Autowired
    private Car car;

    public Car getCar() {
        return car;
    }
}
