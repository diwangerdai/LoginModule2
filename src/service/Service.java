package service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.jfinal.plugin.activerecord.Record;
import dto.Result;
import entity.User;
//业务逻辑类
public class Service {
	/*
	 * 用户注册方法
	 */
	public Result register(String username, String password, String password2, boolean registerSuccess) {
		// 结果包装类初始化
		Result result = new Result();
		// 检查用户名是否为空
		if (username == null || "".equals(username)) {
			result.setStatus("请输入用户名");
			return result;
		}
		// 使用正则表达式检查用户名格式
		Pattern p = Pattern.compile("^[a-zA-Z][a-zA-Z0-9_]{5,15}$");
		Matcher m = p.matcher(username);
		if (!m.matches()) {
			result.setStatus("用户名格式不对");
			return result;
		}
		// 查询用户名是否被注册
		List<Record> records = User.dao.findByUsername(username);
		if (records.size() > 0) {
			result.setStatus("该用户名已被注册");
			return result;
		}
		// 检查密码是否为空
		if (password == null || "".equals(password)) {
			result.setStatus("请输入密码");
			return result;
		}
		// 使用正则表达式检查用户密码格式
		Pattern p2 = Pattern.compile("^[a-zA-Z][a-zA-Z0-9_]{6,22}$");
		Matcher m2 = p2.matcher(password);
		if (!m2.matches()) {
			result.setStatus("密码格式不对");
			return result;
		}
		// 检查用户名和密码的相似度
		String temp = username.substring(0, 5);
		String temp2 = password.substring(0, 5);
		if (temp.equals(temp2)) {
			result.setStatus("账号和密码不能太相似");
			return result;
		}

		if (!password.equals(password2)) {
			result.setStatus("两次密码输入不同");
			return result;
		} else if (!registerSuccess) {
			result.setStatus("验证码输入错误");
			return result;
		}
		// 向数据库增加用户
		User.dao.insertUser(username, password);
		result.setStatus("注册成功");
		return result;

	}

	/*
	 * 用户登录业务方法
	 */
	public Result login(String username, String password, boolean loginSuccess) {
		// 初始化结果包装类
		Result result = new Result();
		// 检查用户名和密码是否为空
		if (username == null || "".equals(username)) {
			result.setStatus("用户名不能为空");
			return result;
		} else if (password == null || "".equals(password)) {
			result.setStatus("密码不能为空");
			return result;
		}
		// 检查用户名是否存在
		List<Record> records = User.dao.findByUsername(username);
		if (records.size() <= 0) {
			result.setStatus("用户名错误");
			return result;
		}

		// 检查已存在的用户名对应的密码是否正确
		List<Record> records2 = User.dao.findPasswordByUsername(username, password);
		if (records2.size() <= 0) {
			// 向数据库插入密码错误次数,并在结果类里放入密码错误次数
			User.dao.addLoginError(username);
			result = verify(result, username, loginSuccess);
			if (result.getStatus() != null && !"".equals(result.getStatus())) {
				return result;
			}
			result.setStatus("密码错误");
			return result;
		} else {
			result = verify(result, username, loginSuccess);
			if (result.getStatus() != null && !"".equals(result.getStatus())) {
				return result;
			}
			// 登录成功登录错误次数归0
			User.dao.removeLoginError(username);
			result.setStatus("登录成功");
			return result;
		}

	}

	private Result verify(Result result, String username, boolean loginSuccess) {
		result.setLoginError(findLoginErrorByUsername(username));
		// 错误次数如果大于6则添加不可1次登录时间惩罚
		if (result.getLoginError() > 6) {
			// 检查不可登录时间是否过去
			Date loginErrorTime = findLoginErrorTimeByUsername(username);
			if (loginErrorTime.getTime() > new Date().getTime()) {
				result.setStatus("由于密码错误多次,请1分钟后再登录");
				return result;
			} else {
				// 不可登录时间以过去,将登录错误减至验证码阶段
				User.dao.updateLoginError(4, username);

			}
			// 向数据库插入1分钟的不可登录时间
			addLoginErrorTime(username);
		}
		// 登录错误次数大于3需要验证验证码
		if (result.getLoginError() > 3) {
			System.out.println(loginSuccess);
			if (!loginSuccess) {
				// 向数据库插入密码错误次数,并在结果类里放入密码错误次数
				User.dao.addLoginError(username);
				result.setStatus("验证码错误");
				return result;
			}
		}
		return result;
	}

	// 向数据库查询登录错误次数
	private int findLoginErrorByUsername(String username) {
		int loginError = 0;
		List<Record> records = User.dao.findLoginErrorByUsername(username);
		for (Record r : records) {
			loginError = r.getInt("login_error");
		}
		return loginError;
	}

	// 添加不可登录时间惩罚
	private void addLoginErrorTime(String username) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date afterDate = new Date(new Date().getTime() + 60000);
		String time = sdf.format(afterDate);
		User.dao.updateLoginErrorTime(time, username);
	}

	// 获取不可登录时间
	private Date findLoginErrorTimeByUsername(String username) {
		Date loginErrorTime = null;
		List<Record> lists = User.dao.findLoginErrorTimeByUsername(username);
		for (Record r : lists) {
			loginErrorTime = r.getDate("login_error_time");
		}
		return loginErrorTime;
	}
}
