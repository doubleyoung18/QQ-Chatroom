package com.qq;

import java.awt.Color;
import java.awt.Font;
import java.io.Serializable;
import java.util.Date;

public class Message implements Serializable {

	private String message;
	private FontAttribute attribute;
	private Date date;
	private Integer port_src;
	private Integer port_dest;
	private Integer flag;

	public Message() {
		
	}
	
	public String getMessage() {
		return message;
	}
	
	public void setMessage(String message) {
		this.message = message;
	}
	
	public FontAttribute getAttr() {
		return attribute;
	}

	public void setAttribute(FontAttribute attr) {
		this.attribute = attr;
	}

	public Date getDate() {
		return date;
	}
	
	public void setDate(Date date) {
		this.date = date;
	}
	
	public Integer getPort_src() {
		return port_src;
	}
	
	public void setPort_src(Integer port_src) {
		this.port_src = port_src;
	}
	
	public Integer getPort_dest() {
		return port_dest;
	}
	
	public void setPort_dest(Integer port_dest) {
		this.port_dest = port_dest;
	}
	
	public Integer getFlag() {
		return flag;
	}
	
	public void setFlag(Integer flag) {
		this.flag = flag;
	}
}
