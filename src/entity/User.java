package entity;

import java.util.List;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Record;
//表映射的实体类及其与数据库交互的方法
public class User extends Model<User> {
	private static final long serialVersionUID = 7133877486558649607L;
	public static User dao = new User();
	//查找username
	public List<Record> findByUsername(String username) {
		String sql = "select username from user_tb where username = ?";
		List<Record> records = Db.find(sql, username);
		return records;
	}
	//插入用户
	public void insertUser(String username, String password) {
		String sql = "insert into user_tb(username,password,create_time,login_error_time) values(?,md5(?),now(),now())";
		Db.update(sql, username, password);
	}
	//查找对应用户的密码
	public List<Record> findPasswordByUsername(String username, String password) {
		String sql = "select password from user_tb where username = ? and password= md5(?)";
		List<Record> records = Db.find(sql, username,password);
		return records;
	}
	//查找对应用户的登录错误次数
	public List<Record> findLoginErrorByUsername(String username) {

		String sql = "select login_error from user_tb where username= ?";
		List<Record> records = Db.find(sql, username);
		return records;
	}
	//是对应用户的登录错误次数加1
	public void addLoginError(String username) {
		String sql = "update user_tb set login_error=login_error+1 where username= ?";
		Db.update(sql, username);
	}
	//设置对应用户的登录错误次数
	public void updateLoginError(int loginError,String username) {
		String sql = "update user_tb set login_error=? where username= ?";
		Db.update(sql, loginError,username);
	}
	//清除对应用户的登录错误次数
	public void removeLoginError(String username) {
		String sql = "update user_tb set login_error=0 where username= ?";
		Db.update(sql, username);
	}
	//添加对应用户的无法登录时间惩罚时间
	public void updateLoginErrorTime(String time, String username) {
		String sql = "update user_tb set login_error_time = ? WHERE username = ?";
		Db.update(sql, time, username);
	}
	//获取对应用户的无法登录惩罚时间
	public List<Record> findLoginErrorTimeByUsername(String username) {

		String sql = "select login_error_time from user_tb where username= ?";
		List<Record> records = Db.find(sql, username);
		return records;
	}
}
