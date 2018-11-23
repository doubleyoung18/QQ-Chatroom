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
	//1.�������ڣ��ؼ���壬�ؼ�
	private JFrame frame = new JFrame("��¼");
	private JPanel panel = new JPanel();
	
	private JLabel topLabel = new JLabel(new ImageIcon("images\\login_top.gif"));//��¼���涥������
	private JLabel profileLabel = new JLabel(new ImageIcon("images\\profile0.png"));//QQͷ��
	private JLabel bottomLabel = new JLabel(new ImageIcon("images\\login_bottom.jpg"));//��¼����ײ�����
	
	private TextField usernameTextField = new TextField(19);//�˺������
	private TextField passwordTextField = new TextField(19);//���������
	
	private JLabel registerLabel = new JLabel("ע���˺�");//ע���˺�
	private JLabel forgetLabel = new JLabel("�һ�����");//�һ�����
	
	private JCheckBox rememberCheckbox = new JCheckBox("��ס����");//��ס����
	private JCheckBox automaticCheckbox = new JCheckBox("�Զ���¼");//�Զ���¼

	private JButton loginButton = new JButton(new ImageIcon("images\\login.jpg"));//��¼
	
	private JDialog dialog = null;
	private JPanel dialogPanel = new JPanel();
	private JLabel errorLabel = new JLabel();
	private JLabel dialogTopLabel = new JLabel(new ImageIcon("images\\error_top.gif"));
	private JPanel dialogCenterPanel = new JPanel();
	private JPanel dialogBottomPanel = new JPanel();
	private JButton confirmButton = new JButton("ȷ��");
	
	private JLabel usernameLabel = new JLabel("�˺�");
	private JLabel passwordLabel = new JLabel("����");
	private JTextField usernameTextField2 = new JTextField(19);
	private JTextField passwordTextField2 = new JTextField(19);
	
	private Font font1 = new Font("font1" ,Font.PLAIN, 24);//�˺�&��������
	private Font font2 = new Font("font2" ,Font.PLAIN, 15);//ע���˺�&�һ�����&��ס����&�Զ���¼����
	private Font font3 = new Font("font3" ,Font.BOLD, 20);//ע���˺�_�˺���������
	private Font font4 = new Font("font4" ,Font.BOLD, 24);//������ʾ����

	public LoginFrame() {
		initComponents();
	}
	
	public void initComponents() {
		//2.
		
		//���ñ���ͼƬ����
		topLabel.setBounds(0, 0, 539, 187);
		bottomLabel.setBounds(0, 188, 539, 187);
	
		//����QQͷ������
		profileLabel.setBounds(50, 209, 100, 100);

		//�����˺����������Լ��¼�������
		usernameTextField.setBounds(167, 206, 243, 37);
		usernameTextField.setText("QQ����/�ֻ�/����");
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
					usernameTextField.setText("QQ����/�ֻ�/����");
					usernameTextField.setForeground(new Color(127, 127, 127));
				}	
			}
		});
		
		//�����������������Լ��¼�������
		passwordTextField.setBounds(167, 242, 243, 37);
		passwordTextField.setText("����");
		passwordTextField.setFont(font1);
		passwordTextField.setForeground(new Color(127, 127, 127));
		passwordTextField.addFocusListener(new FocusAdapter(){
			@Override
			public void focusGained(FocusEvent arg0) {
				passwordTextField.setText("");
				passwordTextField.setForeground(Color.BLACK);
				passwordTextField.setEchoChar('*'); ///
			}
			@Override
			public void focusLost(FocusEvent arg0) {
				if(passwordTextField.getText().equals("")){
					passwordTextField.setText("����");
					passwordTextField.setForeground(new Color(127, 127, 127));
					passwordTextField.setEchoChar((char)0);
				}	
			}
		});
		
		//����ע���˺�����
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
				
				dialog.setTitle("ע��");
				dialog.setVisible(true);
			}
		});
			
		//����������������
		forgetLabel.setBounds(422, 255, 63, 16);
		forgetLabel.setFont(font2);
		forgetLabel.setForeground(new Color(38, 133, 227));
		
		//���õ�¼��ť���Լ��¼�������
		loginButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				String username = usernameTextField.getText().trim();
				String password = passwordTextField.getText().trim();
				User user = new User(username, password);
				Login login = new Login(user);
				switch(login.verify()) {
				case -1:
					initDialog();
					errorLabel.setText("       ��������˺Ż����벻��ȷ\n");
					errorLabel.setFont(font4);
					dialog.setTitle("����");

					//ȷ�ϰ�ť�¼�������
					confirmButton.addActionListener(new ActionListener(){
						public void actionPerformed(ActionEvent arg0) {
							dialogPanel.remove(errorLabel);
							dialog.dispose();
						}
					});
					
					dialogPanel.add(errorLabel, BorderLayout.CENTER);
					dialog.setTitle("����");
					dialog.setVisible(true);
					break;
				case 0:
					FriendListFrame friendListFrame = new FriendListFrame(user);
					frame.dispose();
					break;
				}
			}
		});
		
		//���ü�ס��������
		rememberCheckbox.setBounds(167, 290, 85, 16);
		rememberCheckbox.setFont(font2);
		rememberCheckbox.setBackground(new Color(235, 242, 249));
		
		//�����Զ���¼����
		automaticCheckbox.setBounds(326, 290, 85, 16);
		automaticCheckbox.setFont(font2);
		automaticCheckbox.setBackground(new Color(235, 242, 249));
		
		//���õ�¼��ť����
		loginButton.setBounds(167, 327, 242, 37);
		loginButton.setContentAreaFilled(false);
		loginButton.setBorderPainted(false);
		
		//3.���
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
		
		//4.���ô�������
		frame.setBackground(new Color(235, 242, 249));
		frame.setSize(540, 415);//���ڴ�С
		frame.setResizable(false);//���ڴ�С���ɱ�	
		frame.setLocationRelativeTo(null);//���ڿ���ʱ����
		frame.setAlwaysOnTop(true);//�������ڶ���
		//frame.setUndecorated(true);//ȥ�����ܴ���
		frame.setVisible(true);//���ڿɼ�	
	}
	
	/**
	 * ���ûỰ��
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
