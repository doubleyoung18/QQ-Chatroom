package com.qq;

import java.awt.Font;
import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.swing.*;

public class FriendListFrame {
	//1.创建窗口，面板，控件
	private User user;
	
	private JFrame frame = new JFrame("好友列表");
	private JPanel panel = new JPanel();
	
	private JLabel topLabel = new JLabel(new ImageIcon("images\\friendlist_top.jpg"));//顶部背景	
	private JLabel profileLabel = new JLabel(new ImageIcon("images\\friendlist_profile.jpg"));//用户头像
	private JLabel usernameLabel = new JLabel();//用户昵称
	private JLabel bottomLabel = new JLabel(new ImageIcon("images\\friendlist_bottom.jpg"));//底部背景
	
	private JButton groupchatButton = new JButton(new ImageIcon("images\\groupchatButton.jpg"));
	
	private DefaultListModel friendListModel = new DefaultListModel();
	private JList friendList = new JList();//好友列表
	
	private Font font1 = new Font("font1", Font.BOLD, 20);//用户昵称字体
	private Font font2 = new Font("font2", Font.BOLD, 25);//好友字体
	
	private GroupChatFrame groupchatFrame = new GroupChatFrame(0);
	private Map<Integer, ChatFrame> chatFrameMap = new HashMap<Integer, ChatFrame>();
	
	private Socket socket;

	public FriendListFrame() {
	
		initComponents();
	}
	
	public FriendListFrame(User user) {
		try {
			socket = new Socket("127.0.0.1", 9990);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.user = user;
		initComponents();
		receiveMsgThread t = new receiveMsgThread();
		t.start();
	}
	
	/**
	 * 界面初始化
	 */
	public void initComponents(){
		//2.
		//设置顶部背景属性
		topLabel.setBounds(0, 0, 361, 203);
		bottomLabel.setBounds(0, 630, 361, 49);
		
		//设置用户头像属性
		profileLabel.setBounds(15, 26, 78, 78);
		
		//设置用户昵称属性
		usernameLabel.setText(user.getUsername());
		
		usernameLabel.setFont(font1);
		usernameLabel.setForeground(new Color(255, 255, 255));
		usernameLabel.setBounds(110, 26, 74, 25);
		
		//设置群聊按钮属性及事件监听器
		groupchatButton.setBorderPainted(false);
		groupchatButton.setBounds(180, 157, 91, 45);	
		groupchatButton.addMouseListener(new MouseAdapter(){
			@Override
			public void mouseClicked(MouseEvent e) {
				if(e.getClickCount() == 2 && groupchatFrame.getOpenFlag() == 0) {
					groupchatFrame = new GroupChatFrame(socket, 1);
				}
			}
		});
		
		//设置好友列表属性及事件监听器
		friendList.setModel(friendListModel);
		friendList.setBounds(0, 202, 365, 400);
		friendList.setFont(font2);
		friendList.setFixedCellWidth(365);
		friendList.setBackground(new Color(255, 255, 255));
		friendList.addMouseListener(new MouseAdapter(){
			@Override
			public void mouseClicked(MouseEvent e) {
				Integer port = (Integer) friendList.getSelectedValue();
				if(e.getClickCount() == 2 && port != socket.getLocalPort()) {
					//判断与该好友回话窗口是否开启
					boolean isExist = false;
					if(chatFrameMap.get(port) != null && chatFrameMap.get(port).getOpenFlag() == 1) {
						isExist = true;
					}			
					if(isExist == false) {
						String username = port.toString();
						ChatFrame chatFrame = new ChatFrame(socket, username, 1);
						chatFrameMap.put(port, chatFrame);
					}
				}
			}
		});
		
		//设置界面关闭事件
		frame.addWindowListener(new WindowAdapter(){
			//登录
			@Override
			public void windowOpened(WindowEvent arg0) {
				try {
					
					Message msg = new Message();
					String username = user.getUsername();
					Integer port_src = socket.getLocalPort();
					Integer flag = 1;
					msg.setMessage(username);
					msg.setPort_src(port_src);
					msg.setFlag(flag);
					
					System.out.println(port_src + "open");
					
					ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
					oos.writeObject(msg);
					oos.flush();
				} catch (UnknownHostException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
			//退出
			@Override
			public void windowClosing(WindowEvent arg0) {
				try {
					Message msg = new Message();
					String username = user.getUsername();
					Integer port_src = socket.getLocalPort();
					Integer flag = 0;
					msg.setMessage(username);
					msg.setPort_src(port_src);
					msg.setFlag(flag);
					
					ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
					oos.writeObject(msg);
					oos.flush();
				} catch (UnknownHostException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				
				frame.disable();
				groupchatFrame.getFrame().dispose();
				Set keySet = chatFrameMap.keySet();
				Iterator<Integer> ite = keySet.iterator();
				while(ite.hasNext()) {
					Integer keyPort = ite.next();
					ChatFrame chatFrame = chatFrameMap.get(keyPort);
					chatFrame.getFrame().dispose();
				}
			}
		});
			
		//3.组件
		panel.setLayout(null);
		
		frame.getLayeredPane().add(topLabel, new Integer(Integer.MIN_VALUE));
		JPanel contentPanel = (JPanel) frame.getContentPane();
		contentPanel.setOpaque(false);
		
		panel.setOpaque(false);
		
		panel.add(bottomLabel);
		panel.add(profileLabel);
		panel.add(usernameLabel);
		panel.add(groupchatButton);
		panel.add(friendList);
		frame.add(panel);
		
		//4.设置窗口属性
		frame.setBackground(new Color(255, 255, 255));
		frame.setSize(365, 710);//窗口大小
		frame.setResizable(false);//窗口大小不可变	
		frame.setAlwaysOnTop(true);//窗口置于顶层
		//frame.setUndecorated(true);//去除四周窗体
		frame.setVisible(true);//窗口可见
	}
	
	/**
	 * 接收消息线程
	 */
	class receiveMsgThread extends Thread {
		@Override
		public void run() {
			try {
				while(true) {
					ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
					Message msg = (Message) ois.readObject();
					
					String message = msg.getMessage();
					Integer port_src = msg.getPort_src();//获取发送方端口号
					Integer flag = msg.getFlag();
					
					if(flag == 0 && friendListModel.contains(port_src) == true) {//好友下线，更新列表
						friendListModel.removeElement(port_src);
					} else if(flag == 1 && friendListModel.contains(port_src) == false) {//好友上线，更新列表
						friendListModel.addElement(port_src);
					} else if(flag == 2 || flag == 3) {
						//接收群聊消息时自动打开群聊窗口
						if(groupchatFrame.getOpenFlag() == 0) {
							groupchatFrame = new GroupChatFrame(socket, 1);
						}
						Date date = msg.getDate();
						FontAttribute attr = new FontAttribute();
						groupchatFrame.insert(date.toLocaleString() + "   ", attr);
						groupchatFrame.insert(port_src.toString() + ":\n", attr);
						if(flag == 2) {
							attr = msg.getAttr();
							groupchatFrame.insert(message + '\n', attr);
						} else {
							groupchatFrame.insertIcon(message);
						}
						
					} else if(flag == 4 || flag == 5) {
						ChatFrame chatFrame = chatFrameMap.get(port_src);
						//接收私聊消息时自动打开私聊窗口
						if(chatFrame == null || chatFrame.getOpenFlag() == 0) {
							String username = port_src.toString();
							chatFrame = new ChatFrame(socket, username, 1);
							chatFrameMap.put(port_src, chatFrame); 
						}
						Date date = msg.getDate();
						FontAttribute attr = new FontAttribute();
						chatFrame.insert(date.toLocaleString() + '\n', attr);
						if(flag == 4) {
							attr = msg.getAttr();
							chatFrame.insert(message + '\n', attr);
						} else {
							System.out.println("++++" + message);
							chatFrame.insertIcon(message);
						}
					} else {
						
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}		
	}
}
