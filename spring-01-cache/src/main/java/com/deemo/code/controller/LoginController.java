package com.deemo.code.controller;

import com.deemo.code.service.IAopService;
import com.deemo.code.service.IDeptService;
import com.deemo.code.service.IUserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

// @RestController
public class LoginController {

	@Resource
	private IUserService userService;

	@Resource
	private IDeptService deptService;

	@Resource
	private IAopService aopService;


	@GetMapping("/login")
	public String login() {
		System.out.println("userService: " + userService);
		System.out.println("deptService: " + deptService);

		System.out.println("===========================================");

		System.out.println("userService.deptService: " + userService.getService());
		System.out.println("deptService.userService: " + deptService.getService());

		System.out.println("===========================================");
		aopService.hello();

		return "Hello!";
	}

}
