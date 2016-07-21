package controller;


import com.jfinal.aop.Before;
import com.jfinal.core.Controller;

import dto.Result;

import com.jfinal.ext.interceptor.POST;
import com.jfinal.render.CaptchaRender;

import service.Service;

public class VerifyController extends Controller {
	Service service = new Service();

	public void index() {
	}
	//登录页面
	public void login() {
		render("/login.html");

	}
	//登录成功页面
	@Before(POST.class) // POST这个类在全局拦截器里的interceptor包里面,防止地址后面跟参数恶意攻击
	public void loginService() {
		String username = getPara("username");
		String password = getPara("password");
		String verifyCode = getPara("verifyCode");
		boolean loginSuccess = CaptchaRender.validate(this, verifyCode);

		Result result = service.login(username, password, loginSuccess);
		if (!result.getStatus().equals("登录成功")) {
			setAttr("username", username);
			setAttr("result", result);
			render("/login.html");
		} else {
			setAttr("username", username);
			render("/login_success.html");
		}

	}
	//注册页面
	public void register() {
		render("/register.html");
	}
	//注册成功页面
	@Before(POST.class) // POST这个类在全局拦截器里的interceptor包里面,防止地址后面跟参数恶意攻击
	public void registerService() {
		String username = getPara("username");
		String password = getPara("password");
		String password2 = getPara("password2");
		String verifyCode = getPara("verifyCode");
		boolean registerSuccess = CaptchaRender.validate(this, verifyCode);

		Result result = service.register(username, password, password2, registerSuccess);
		if (!result.getStatus().equals("注册成功")) {
			setAttr("result", result);
			setAttr("username", username);
			render("/register.html");
		} else {
			setAttr("username", username);
			render("/register_success.html");
		}
	}

	// 使用jfinal自带的验证码生成类生成验证码
	public void img() {
		CaptchaRender img = new CaptchaRender();
		render(img);
	}
}
