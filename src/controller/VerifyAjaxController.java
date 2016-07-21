package controller;


import java.util.List;

import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import dto.Result;
import entity.User;

import com.jfinal.ext.interceptor.POST;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.render.CaptchaRender;
import service.Service;
//页面ajax请求处理控制器
public class VerifyAjaxController extends Controller {
	Service service = new Service();

	public void index() {
	}
	//检查用户名是否存在
	@Before(POST.class)
	public void checkUsername(){
		Result result = new Result();
		String username = getPara("username");
		System.out.println(username);
		List<Record> records = User.dao.findByUsername(username);
		if (records.size() > 0) {
			result.setStatus("该用户名已被注册");
			setAttr("status",result.getStatus());
			renderJson();
	}else{
		
		result.setStatus("该用户名可以使用");
		setAttr("status",result.getStatus());
		renderJson();
		}
	}
	//检查验证码是否正确
	@Before(POST.class)
	public void checkVerifyCode(){
		Result result = new Result();
		String verifyCode = getPara("verifyCode");
		boolean registerSuccess = CaptchaRender.validate(this, verifyCode);
		if(registerSuccess){
			
			result.setStatus("验证码正确");
		}else{
			result.setStatus("验证码错误");
		}
		setAttr("status",result.getStatus());
		renderJson();
	}
	
	// 使用jfinal自带的验证码生成类生成验证码
	public void img() {
		CaptchaRender img = new CaptchaRender();
		render(img);
	}
}
