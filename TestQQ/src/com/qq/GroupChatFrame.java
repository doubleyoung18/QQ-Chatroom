package com.qq;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Date;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.StyledDocument;

public class GroupChatFrame {
	//1.创建窗体，面板，控件
	private JFrame frame = new JFrame();
	private JPanel panel = new JPanel();
	private JPanel middlePanel = new JPanel();
	
	private JScrollPane scrollPane = null;//滚动条
	
	private JLabel topLabel = new JLabel(new ImageIcon("images\\chat_top.jpg"));
	private JLabel showLabel = new JLabel(new ImageIcon("images\\chat_show.jpg"));
	
	private JTextPane receiveTextPane = new JTextPane();//输入框
	private JTextArea sendTextArea = new JTextArea();//接收框
	
	private JButton imageButton = new JButton("图片");//发送图片按钮
	private JButton fileButton = new JButton("文件");//发送文件按钮
	private JButton sendButton = new JButton(new ImageIcon("images\\chat_send.jpg"));//发送按钮
	private JButton closeButton = new JButton(new ImageIcon("images\\chat_close.jpg"));//关闭按钮
	
	private Socket socket;
	
	private Font font1 = new Font("font1", Font.BOLD, 15);//好友昵称字体
	private Font font2 = new Font("font2", Font.BOLD, 20);//发送窗口字体
	private Font font3 = new Font("font3", Font.BOLD, 18);//分隔菜单字体
	
	private JLabel fontNameLabel = new JLabel("字体");
	private JLabel fontSizeLabel = new JLabel("字号");
	private JLabel fontStyleLabel = new JLabel("样式");
	private JLabel fontColorLabel = new JLabel("颜色");
	
	private JComboBox fontNameBox = null;//字体
	private JComboBox fontSizeBox = null;//字号
	private JComboBox fontStyleBox = null;//样式
	private JComboBox fontColorBox = null;//颜色
	
	private StyledDocument doc = null;//文字样式
	
	private Integer openFlag = 0;//单开标识位
	
	public GroupChatFrame(){
		initComponents();
	}
	
	public GroupChatFrame(Integer openFlag) {
		this.openFlag = openFlag;
	}

	public GroupChatFrame(Socket socket, Integer openFlag) {
		this.socket = socket;
		this.openFlag = openFlag;
		initComponents();
	}
	
	public Integer getOpenFlag() {
		return openFlag;
	}
	
	public JTextPane getReceiveTextPane() {
		return receiveTextPane;
	}
	
	public JFrame getFrame() {
		return frame;
	}

	public void initComponents(){
		//设置Windows的界面风格
		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		} catch (Exception e) {
			e.printStackTrace();
		}
		doc = receiveTextPane.getStyledDocument();
		
		//2.控件属性
		//设置滚动条属性
		scrollPane = new JScrollPane(receiveTextPane);
		scrollPane.setBounds(558, 70, 15, 400);
		scrollPane.setBackground(new Color(191, 195, 199));
		scrollPane.setVerticalScrollBarPolicy( JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);//设置滚动条总是可见
		
		//设置背景属性
		topLabel.setBounds(0, 0, 747, 63);
		
		//设置接收框属性
		receiveTextPane.setBounds(5, 70, 573, 400);
		receiveTextPane.setBackground(new Color(238, 243, 248));
		receiveTextPane.setEditable(false);//设为不可写
		
		//设置分隔菜单属性
		middlePanel.setBounds(0, 470, 573, 42);
		middlePanel.setBackground(new Color(238, 243, 249));
		
		fontNameLabel.setFont(font3);
		String[] str_name = {"宋体", "黑体", "楷体", "行楷"};
		fontNameBox = new JComboBox(str_name);
		
		fontSizeLabel.setFont(font3);
		String[] str_Size = {"20", "22", "24", "26", "28", "30"};
		fontSizeBox = new JComboBox(str_Size);

		fontStyleLabel.setFont(font3);
		String[] str_Style = {"常规", "斜体", "粗体", "粗斜体"};
		fontStyleBox = new JComboBox(str_Style);
		
		fontColorLabel.setFont(font3);
		String[] str_Color = {"黑色", "红色", "蓝色", "黄色", "绿色"};
		fontColorBox = new JComboBox(str_Color);
		
		imageButton.addActionListener(new ActionListener() {//插入图片事件
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser f = new JFileChooser();//查找文件
				f.showOpenDialog(null);
				Message msg = new Message();
				String message = f.getSelectedFile().toString();
				Date date = new Date();
				FontAttribute attr = new FontAttribute();
				insert("\t\t\t\t" + date.toLocaleString() + '\n', attr);
				insert("\t\t\t\t", attr);
				insertIcon(message);//插入图片
				msg.setMessage(message);
				msg.setDate(date);
				msg.setFlag(3);
				sendMessage(msg);
			}
		});
		
		//设置发送框属性及事件监听器
		sendTextArea.setBounds(5, 520, 573, 59);
		sendTextArea.setFont(font2);
		sendTextArea.setBackground(new Color(238, 243, 248));
		sendTextArea.setLineWrap(true);
		sendTextArea.setWrapStyleWord(true);
		
		//设置关闭按钮属性及事件监听器
		closeButton.setBounds(340, 590, 110, 29);
		closeButton.setContentAreaFilled(false);
		closeButton.setBorderPainted(false);
		closeButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				openFlag = 0;
				frame.dispose();
			}
		});
		
		//设置窗口关闭事件
		frame.addWindowListener(new WindowAdapter(){
			@Override
			public void windowClosing(WindowEvent arg0) {
				openFlag = 0;
				frame.dispose();
			}
		});
		
		//设置发送按钮属性及监听器事件
		sendButton.setBounds(443, 590, 110, 29);
		sendButton.setContentAreaFilled(false);
		sendButton.setBorderPainted(false);
		sendButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				Message msg = new Message();
				
				String message = sendTextArea.getText();
				Date date = new Date();
				FontAttribute attr = new FontAttribute();
				insert("\t\t\t\t" + date.toLocaleString() + '\n', attr);
				attr = getFontAttribute();
				insert("\t\t\t\t" + message + '\n', attr);
				
				msg.setMessage(message);
				msg.setAttribute(attr);
				msg.setDate(date);
				msg.setFlag(2);
				sendMessage(msg);
			}
		});
		
		//设置QQ秀属性
		showLabel.setBounds(578, 70, 174, 569);
		
		//3.组件
		panel.setLayout(null);
		panel.setBackground(new Color(238, 243, 249));
		
		panel.add(topLabel);
		panel.add(receiveTextPane);
		
		panel.add(middlePanel);
		middlePanel.add(fontNameLabel);
		middlePanel.add(fontNameBox);
		middlePanel.add(fontSizeLabel);
		middlePanel.add(fontSizeBox);
		middlePanel.add(fontStyleLabel);
		middlePanel.add(fontStyleBox);
		middlePanel.add(fontColorLabel);	
		middlePanel.add(fontColorBox);
		middlePanel.add(imageButton);
		middlePanel.add(fileButton);
		
		panel.add(sendTextArea);
		panel.add(closeButton);
		panel.add(sendButton);
		panel.add(showLabel);
		
		frame.getContentPane().add(scrollPane);
		frame.add(panel);
		
		//4.设置窗口属性
		frame.setSize(760, 680);//窗口大小
		frame.setTitle("群聊");
		frame.setFont(font1);
		frame.setResizable(false);//窗口大小不可变	
		frame.setLocationRelativeTo(null);//窗口开启时居中
		//frame.setAlwaysOnTop(true);//窗口置于顶层
		//frame.setUndecorated(true);//去除四周窗体
		frame.setVisible(true);//窗口可见
	}
	
	/**
	 * 发送消息方法
	 */
	public void sendMessage(Message msg){
		try {
			ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
			Integer port_src = socket.getLocalPort();
			msg.setPort_src(port_src);		
			oos.writeObject(msg);
			oos.flush();
			sendTextArea.setText("");			
		} catch (IOException e) {
			e.printStackTrace();
		}				
	}
	
	/**
	 * 插入文本
	 * @param message 消息内容
	 * @param attr 文字属性
	 */
	public void insert(String message, FontAttribute attr) {
		try {
			doc.insertString(doc.getLength(), message, attr.getAttrSet());
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 插入图片
	 * @param image 待插图片
	 */
	public void insertIcon(String loc) {
		File image = new File(loc);
		receiveTextPane.setCaretPosition(doc.getLength());//设置插入位置
		receiveTextPane.insertIcon(new ImageIcon(image.getPath()));//插入图片
		insert("\n", new FontAttribute());//换行
	}

	/**
	 * 获取消息文字属性
	 * @return 属性集
	 */
	public FontAttribute getFontAttribute() {
		FontAttribute attr = new FontAttribute();
		attr.setName((String) fontNameBox.getSelectedItem());
		
		attr.setSize(Integer.parseInt((String) fontSizeBox.getSelectedItem()));
		
		String temp_style = (String) fontStyleBox.getSelectedItem();
		if (temp_style.equals("常规")) {
			attr.setStyle(FontAttribute.GENERAL);
		} else if (temp_style.equals("粗体")) {
			attr.setStyle(FontAttribute.BOLD);
		} else if (temp_style.equals("斜体")) {
			attr.setStyle(FontAttribute.ITALIC);
		} else if (temp_style.equals("粗斜体")) {
			attr.setStyle(FontAttribute.BOLD_ITALIC);
		}
		
		String temp_color = (String) fontColorBox.getSelectedItem();
		if (temp_color.equals("黑色")) {
			attr.setColor(new Color(0, 0, 0));
		} else if (temp_color.equals("红色")) {
			attr.setColor(new Color(255, 0, 0));
		} else if (temp_color.equals("蓝色")) {
			attr.setColor(new Color(0, 0, 255));
		} else if (temp_color.equals("黄色")) {
			attr.setColor(new Color(255, 255, 0));
		} else if (temp_color.equals("绿色")) {
			attr.setColor(new Color(0, 255, 0));
		}
        return attr;
    }
	
	public static void main(String[] args) {
		GroupChatFrame gcf = new GroupChatFrame();
	}
}
