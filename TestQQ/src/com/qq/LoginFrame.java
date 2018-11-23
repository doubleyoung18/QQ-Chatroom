package com.qq;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.*;
import javax.swing.event.AncestorListener;

public class LoginFrame {
	//1.创建窗口，控件面板，控件
	private JFrame frame = new JFrame("登录");
	private JPanel panel = new JPanel();
	
	private JLabel topLabel = new JLabel(new ImageIcon("images\\login_top.gif"));//登录界面顶部背景
	private JLabel profileLabel = new JLabel(new ImageIcon("images\\profile0.png"));//QQ头像
	private JLabel bottomLabel = new JLabel(new ImageIcon("images\\login_bottom.jpg"));//登录界面底部背景
	
	private TextField usernameTextField = new TextField(19);//账号输入框
	private TextField passwordTextField = new TextField(19);//密码输入框
	
	private JLabel registerLabel = new JLabel("注册账号");//注册账号
	private JLabel forgetLabel = new JLabel("找回密码");//找回密码
	
	private JCheckBox rememberCheckbox = new JCheckBox("记住密码");//记住密码
	private JCheckBox automaticCheckbox = new JCheckBox("自动登录");//自动登录

	private JButton loginButton = new JButton(new ImageIcon("images\\login.jpg"));//登录
	
	private JDialog dialog = null;
	private JPanel dialogPanel = new JPanel();
	private JLabel errorLabel = new JLabel();
	private JLabel dialogTopLabel = new JLabel(new ImageIcon("images\\error_top.gif"));
	private JPanel dialogCenterPanel = new JPanel();
	private JPanel dialogBottomPanel = new JPanel();
	private JButton confirmButton = new JButton("确认");
	
	private JLabel usernameLabel = new JLabel("账号");
	private JLabel passwordLabel = new JLabel("密码");
	private JTextField usernameTextField2 = new JTextField(19);
	private JTextField passwordTextField2 = new JTextField(19);
	
	private Font font1 = new Font("font1" ,Font.PLAIN, 24);//账号&密码字体
	private Font font2 = new Font("font2" ,Font.PLAIN, 15);//注册账号&找回密码&记住密码&自动登录字体
	private Font font3 = new Font("font3" ,Font.BOLD, 20);//注册账号_账号密码字体
	private Font font4 = new Font("font4" ,Font.BOLD, 24);//错误提示字体

	public LoginFrame() {
		initComponents();
	}
	
	public void initComponents() {
		//2.
		
		//设置背景图片属性
		topLabel.setBounds(0, 0, 539, 187);
		bottomLabel.setBounds(0, 188, 539, 187);
	
		//设置QQ头像属性
		profileLabel.setBounds(50, 209, 100, 100);

		//设置账号输入栏属性及事件监听器
		usernameTextField.setBounds(167, 206, 243, 37);
		usernameTextField.setText("QQ号码/手机/邮箱");
		usernameTextField.setFont(font1);
		usernameTextField.setForeground(new Color(127, 127, 127));
		usernameTextField.addFocusListener(new FocusAdapter(){
			@Override
			public void focusGained(FocusEvent arg0) {
				usernameTextField.setText("");
				usernameTextField.setForeground(Color.BLACK);
			}
			@Override
			public void focusLost(FocusEvent arg0) {
				if(usernameTextField.getText().equals("")){
					usernameTextField.setText("QQ号码/手机/邮箱");
					usernameTextField.setForeground(new Color(127, 127, 127));
				}	
			}
		});
		
		//设置密码输入栏属性及事件监听器
		passwordTextField.setBounds(167, 242, 243, 37);
		passwordTextField.setText("密码");
		passwordTextField.setFont(font1);
		passwordTextField.setForeground(new Color(127, 127, 127));
		passwordTextField.addFocusListener(new FocusAdapter(){
			@Override
			public void focusGained(FocusEvent arg0) {
				passwordTextField.setText("");
				passwordTextField.setForeground(Color.BLACK);
				passwordTextField.setEchoChar('●');
			}
			@Override
			public void focusLost(FocusEvent arg0) {
				if(passwordTextField.getText().equals("")){
					passwordTextField.setText("密码");
					passwordTextField.setForeground(new Color(127, 127, 127));
					passwordTextField.setEchoChar((char)0);
				}	
			}
		});
		
		//设置注册账号属性
		registerLabel.setBounds(422, 220, 63, 16);	
		registerLabel.setFont(font2);
		registerLabel.setForeground(new Color(38, 133, 227));
		registerLabel.addMouseListener(new MouseAdapter(){
			@Override
			public void mouseClicked(MouseEvent arg0) {
				initDialog();
				usernameLabel.setFont(font3);
				usernameLabel.setBounds(50, 45, 50, 50);
				usernameLabel.setFont(font1);
				usernameTextField2.setBounds(110, 50, 200, 38);
				passwordLabel.setFont(font3);
				passwordLabel.setBounds(50, 100, 50, 50);
				passwordLabel.setFont(font1);
				passwordTextField2.setBounds(110, 100, 200, 38);
				
				confirmButton.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent arg0) {
						String username = usernameTextField2.getText().trim();
						String password = passwordTextField2.getText().trim();
						User user = new User(username, password);
						Register reg = new Register(user);
						reg.register();
						usernameTextField2.setText("");
						passwordTextField2.setText("");
						dialogPanel.remove(dialogCenterPanel);
						dialog.dispose();
					}
				});
				
				
				dialogPanel.add(dialogCenterPanel, BorderLayout.CENTER);
				
				dialogCenterPanel.add(usernameLabel);
				dialogCenterPanel.add(usernameTextField2);
				dialogCenterPanel.add(passwordLabel);
				dialogCenterPanel.add(passwordTextField2);
				
				dialogCenterPanel.setLayout(null);
				dialogCenterPanel.setOpaque(false);
				
				dialog.setTitle("注册");
				dialog.setVisible(true);
			}
		});
			
		//设置忘记密码属性
		forgetLabel.setBounds(422, 255, 63, 16);
		forgetLabel.setFont(font2);
		forgetLabel.setForeground(new Color(38, 133, 227));
		
		//设置登录按钮属性及事件监听器
		loginButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				String username = usernameTextField.getText().trim();
				String password = passwordTextField.getText().trim();
				User user = new User(username, password);
				Login login = new Login(user);
				switch(login.verify()) {
				case -1:
					initDialog();
					errorLabel.setText("       你输入的账号或密码不正确\n");
					errorLabel.setFont(font4);
					dialog.setTitle("错误");

					//确认按钮事件监听器
					confirmButton.addActionListener(new ActionListener(){
						public void actionPerformed(ActionEvent arg0) {
							dialogPanel.remove(errorLabel);
							dialog.dispose();
						}
					});
					
					dialogPanel.add(errorLabel, BorderLayout.CENTER);
					dialog.setTitle("错误");
					dialog.setVisible(true);
					break;
				case 0:
					FriendListFrame friendListFrame = new FriendListFrame(user);
					frame.dispose();
					break;
				}
			}
		});
		
		//设置记住密码属性
		rememberCheckbox.setBounds(167, 290, 85, 16);
		rememberCheckbox.setFont(font2);
		rememberCheckbox.setBackground(new Color(235, 242, 249));
		
		//设置自动登录属性
		automaticCheckbox.setBounds(326, 290, 85, 16);
		automaticCheckbox.setFont(font2);
		automaticCheckbox.setBackground(new Color(235, 242, 249));
		
		//设置登录按钮属性
		loginButton.setBounds(167, 327, 242, 37);
		loginButton.setContentAreaFilled(false);
		loginButton.setBorderPainted(false);
		
		//3.组件
		panel.setOpaque(false);
		panel.setLayout(null);
		panel.setBackground(new Color(235, 242, 249));
		
		panel.add(topLabel);
		panel.add(loginButton);
		panel.add(profileLabel);
		panel.add(usernameTextField);
		panel.add(passwordTextField);
		panel.add(registerLabel);
		panel.add(forgetLabel);
		panel.add(rememberCheckbox);
		panel.add(automaticCheckbox);
		
		JPanel contentPanel = (JPanel) frame.getContentPane();
		contentPanel.setOpaque(false);

		frame.getLayeredPane().add(bottomLabel, new Integer(Integer.MIN_VALUE));
		frame.add(panel);
		
		//4.设置窗口属性
		frame.setBackground(new Color(235, 242, 249));
		frame.setSize(540, 415);//窗口大小
		frame.setResizable(false);//窗口大小不可变	
		frame.setLocationRelativeTo(null);//窗口开启时居中
		frame.setAlwaysOnTop(true);//窗口置于顶层
		//frame.setUndecorated(true);//去除四周窗体
		frame.setVisible(true);//窗口可见	
	}
	
	/**
	 * 设置会话框
	 */
	public void initDialog(){
		dialog = new JDialog(frame, "", true);
		dialogPanel.setLayout(new BorderLayout());
		dialogBottomPanel.setBackground(new Color(205, 226, 242));
		confirmButton.setBackground(new Color(244, 244, 244));
		confirmButton.setBorderPainted(false);
		
		dialogPanel.add(dialogTopLabel, BorderLayout.NORTH);
		dialogPanel.add(dialogBottomPanel, BorderLayout.SOUTH);
		dialogBottomPanel.add(confirmButton);
		
		dialog.add(dialogPanel);
		
		dialogPanel.setBackground(new Color(235, 242, 249));
		dialog.setSize(400, 320);
		dialog.setResizable(false);
		dialog.setLocationRelativeTo(null);
		dialog.setAlwaysOnTop(true);
	}
	
	public static void main(String[] args) {
		LoginFrame test = new LoginFrame();
	}
}
