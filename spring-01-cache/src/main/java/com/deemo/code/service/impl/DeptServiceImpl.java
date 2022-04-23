package com.deemo.code.service.impl;

import com.deemo.code.service.IDeptService;
import com.deemo.code.service.IUserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class DeptServiceImpl implements IDeptService {

	@Resource
	private IUserService userService;

	@Override
	public Object getService() {
		return userService;
	}
}
