package com.deemo.code.service.impl;

import com.deemo.code.service.IAopService;
import org.springframework.stereotype.Service;

@Service
public class AopServiceImpl implements IAopService {

	@Override
	public void hello() {
		System.out.println("Hello AOP!");
	}

}
