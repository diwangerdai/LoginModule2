package dto;
//结果包装类
public class Result {
	//验证结构描述
	private String status;
	//登录错误次数
	private int loginError;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public int getLoginError() {
		return loginError;
	}

	public void setLoginError(int loginError) {
		this.loginError = loginError;
	}

}
