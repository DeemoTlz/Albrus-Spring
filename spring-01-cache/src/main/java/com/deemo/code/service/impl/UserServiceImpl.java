package com.deemo.code.service.impl;

import com.deemo.code.service.IDeptService;
import com.deemo.code.service.IUserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class UserServiceImpl implements IUserService {

	@Resource
	private IDeptService deptService;


	@Override
	public Object getService() {
		return deptService;
	}
}
