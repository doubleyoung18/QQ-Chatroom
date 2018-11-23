package com.qq;

import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Properties;

public class Login {
	
	private User user;
	private Properties prop;
	
	public Login() {

	}

	public Login(User user) {
		super();
		this.user = user;
	}
	
	public int verify() {
		prop = new Properties();
		
		String bgUsername = "";
		String bgPassword = "";
		
		FileInputStream fis = null;
		
		try {
			fis = new FileInputStream("login.dll");
			prop.load(fis);
			bgUsername = user.getUsername().trim();
			bgPassword = prop.getProperty(bgUsername);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if(fis != null) {
				try {
					fis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		if(bgPassword == null || bgPassword.equals(user.getPassword()) == false) {
			return -1;
		} else if(bgPassword.equals(user.getPassword())) {
			return 0;
		} else {
			return 1;
		} 
	}
	
	public void register(){
		String content = '\n' + user.getUsername() + " = " + user.getPassword();
		FileWriter fw = null;
		try {
			fw = new FileWriter("1.txt", true);
			fw.write(content);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
