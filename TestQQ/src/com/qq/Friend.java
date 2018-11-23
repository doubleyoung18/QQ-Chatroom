package com.qq;

import java.awt.BorderLayout;
import java.util.Random;

import javax.swing.*;

public class Friend {

	private JPanel panel = new JPanel();
	private JTextField nicknameField = new JTextField();
	private JTextField mottoTextField = new JTextField();
	private JLabel profileLabel = new JLabel();
	private String nickname;
	private String motto;
	private String profileLoc;
	
	public Friend(String nickname, String motto) {
		this.nickname = nickname;
		this.motto = motto;
		initComponents();
	}
	
	public void initComponents(){
		//2.
		Random rand = new Random();
		int i = rand.nextInt(10);
		profileLoc = "images\\" + i + ".jpg";
		nicknameField.setText(nickname);
		mottoTextField.setText(motto);
		profileLabel = new JLabel(profileLoc);
		
		//3.
		panel.setLayout(new BorderLayout());
		panel.add(profileLabel, BorderLayout.WEST);
		panel.add(nicknameField, BorderLayout.CENTER);
		panel.add(mottoTextField, BorderLayout.SOUTH);
	}
}
