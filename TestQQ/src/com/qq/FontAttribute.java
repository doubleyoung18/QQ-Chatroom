package com.qq;
import java.awt.Color;
import java.io.Serializable;

import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

public class FontAttribute implements Serializable{
	public static final int GENERAL = 0;//常规

	public static final int BOLD = 1;//粗体

	public static final int ITALIC = 2;//斜体

	public static final int BOLD_ITALIC = 3;//粗斜体

	private SimpleAttributeSet attrSet = null;//属性集

	private String name = "宋体";//字体
	private int style = 0;//样式
	private int size = 20;//字号
	private Color color = new Color(128, 128, 170);//颜色

	public FontAttribute() {
	
	}

	public SimpleAttributeSet getAttrSet() {
		//设置字体
		attrSet = new SimpleAttributeSet();
		if (name != null){
			StyleConstants.setFontFamily(attrSet, name);
		}
		
		//设置字号
		StyleConstants.setFontSize(attrSet, size);
		
		//设置样式
		if (style == FontAttribute.GENERAL) {
			StyleConstants.setBold(attrSet, false);
			StyleConstants.setItalic(attrSet, false);
		} else if (style == FontAttribute.BOLD) {
			StyleConstants.setBold(attrSet, true);
			StyleConstants.setItalic(attrSet, false);
		} else if (style == FontAttribute.ITALIC) {
			StyleConstants.setBold(attrSet, false);
			StyleConstants.setItalic(attrSet, true);
		} else if (style == FontAttribute.BOLD_ITALIC) {
			StyleConstants.setBold(attrSet, true);
			StyleConstants.setItalic(attrSet, true);
		}
		
		//设置颜色
		if (color != null)
			StyleConstants.setForeground(attrSet, color);
		return attrSet;
	}

	public void setAttrSet(SimpleAttributeSet attrSet) {
		this.attrSet = attrSet;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getStyle() {
		return style;
	}

	public void setStyle(int style) {
		this.style = style;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}
}
