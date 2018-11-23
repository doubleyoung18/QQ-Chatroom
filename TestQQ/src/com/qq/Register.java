package com.qq;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Register {

	private User user;

	public Register(User user) {
		this.user = user;
	}
	
	public boolean register(){
		String username = user.getUsername();
		String password = user.getPassword();
		System.out.println("username:" + username);
		System.out.println("password:" + password);
		//(1)创建模式
		//用户名要求3-8位数字或字母
		Pattern username_pattern = Pattern.compile("\\w{3,10}");
		Pattern password_pattern = Pattern.compile("\\w{3,12}");
		
		//(2)创建面匹配对象
		Matcher user_matcher = username_pattern.matcher(username);
		Matcher password_matcher = password_pattern.matcher(password);
		
		//(3)判断是否匹配
		boolean b = user_matcher.matches() && password_matcher.matches();
		
		if(b){
			String content = user.getUsername() + " = " + user.getPassword() +"\r\n";

			try {
				BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("login.dll", true)));
				bw.write(content);
				bw.flush();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return true;
		} else {
			return false;
		}
	}
}
