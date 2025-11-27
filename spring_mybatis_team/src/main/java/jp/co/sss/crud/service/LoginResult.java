package jp.co.sss.crud.service;

import jp.co.sss.crud.entity.Employee;
import jp.co.sss.crud.util.LoginErrorType;

public class LoginResult {

	private boolean isLogin;
	private Employee loginUser;
	private String errorMsg;
	private LoginErrorType loginErrorType;

	private LoginResult(Employee loginUser) {
		this.loginUser = loginUser;
		this.isLogin = true;

	}

	private LoginResult(String errorMsg, LoginErrorType loginErrorType) {
		this.errorMsg = errorMsg;
		this.loginErrorType = loginErrorType;
	}

	public static LoginResult succeedLogin(Employee loginUser) {
		return new LoginResult(loginUser);
	}

	public static LoginResult failLogin(String errorMsg, LoginErrorType loginErrorType) {
		return new LoginResult(errorMsg, loginErrorType);
	}

	public boolean isLogin() {
		return this.isLogin;
	}

	public Employee getLoginUser() {
		return this.loginUser;
	}

	public String getErrorMsg() {
		return this.errorMsg;
	}

	public LoginErrorType getLoginErrorType() {
		return this.loginErrorType;
	}

}
