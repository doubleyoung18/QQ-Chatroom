package com.qq;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import javax.swing.*;

public class QQServer {

	private JFrame frame = new JFrame("服务器");
	private JPanel panel = new JPanel();
	
	private JTextArea receiveArea = new JTextArea(50, 32);
	private JButton cleanButton = new JButton("clean");
	
	private Map<Integer, Socket> socketMap = new HashMap<Integer, Socket>();
	
	public QQServer(){
		initConnection t = new initConnection();
		t.start();
		initComponents();
	}
	
	public void initComponents(){
		
		cleanButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				receiveArea.setText("");
			}
		});
		
		panel.add(receiveArea);
		panel.add(cleanButton);
		frame.add(panel);
		frame.setSize(370, 1000);
		frame.setVisible(true);
	}
	
	/**
	 * 好友上线修改
	 */
	public void addFriendList(){
		Set keySet = socketMap.keySet();
		Iterator<Integer> ite = keySet.iterator();
		while(ite.hasNext()) {
			//获取当前已登录客户端的socket
			Socket socket = socketMap.get(ite.next());
			//向这些客户端广播当前已登录客户端的端口号
			Set keySet2 = socketMap.keySet();
			Iterator<Integer> ite2 = keySet2.iterator();
			while(ite2.hasNext()){
				Integer port_src = ite2.next();
				try {
					ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
					
					Message msg = new Message();
					msg.setPort_src(port_src);
					msg.setFlag(1);
					
					oos.writeObject(msg);
					oos.flush();
					System.out.println("已向" + socket.getPort() + "添加" + port_src);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * 好友下线修改
	 */
	public void minusFriendList(Integer port_src) {
		Set keySet = socketMap.keySet();
		Iterator<Integer> ite = keySet.iterator();
		while(ite.hasNext()) {
			//获取当前已登录客户端的socket
			Socket socket = socketMap.get(ite.next());
			//向这些客户端广播退出的客户端端口号
			try{
				ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
					
				Message msg = new Message();
				msg.setPort_src(port_src);
				msg.setFlag(0);
					
				oos.writeObject(msg);
				oos.flush();
				System.out.println("已从" + socket.getPort() + "删除" + port_src);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 发送群聊消息
	 */
	public void sendGroupMsg(Message msg) {
		Integer port_src = msg.getPort_src();
		Set keySet = socketMap.keySet();
		Iterator<Integer> ite = keySet.iterator();
		while(ite.hasNext()) {
			Integer port_dest = ite.next();
			if(port_src.equals(port_dest) == false){
				Socket socket = socketMap.get(port_dest);
				try {
					ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());			
					oos.writeObject(msg);
					oos.flush();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * 发送私聊消息
	 */
	public void sendMsg(Message msg) {
		Integer port_dest = msg.getPort_dest();
		Socket socket = socketMap.get(port_dest);
		try {
			ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());			
			oos.writeObject(msg);
			oos.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}
	
	/**
	 * 接收消息线程
	 */
	class ReceiveMsgThread extends Thread {
		
		private Socket socket;
		
		public ReceiveMsgThread(Socket socket) {
			this.socket = socket;
		}
		
		@Override
		public void run() {
			try {
				while(true) {
					ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
					
					Message msg = (Message) ois.readObject();
					String message = msg.getMessage();
					Integer port_src = msg.getPort_src();
					Integer port_dest = msg.getPort_dest();
					Integer flag = msg.getFlag();
					
					receiveArea.append("port_src:" + port_src + '\n'
											+ "port_dest:" + port_dest + '\n'
											+ "message:" + message + '\n'
											+ "flag:" + flag + "\n\n");
					
					switch(flag) {
						case 0:
							socketMap.remove(port_src);
							minusFriendList(port_src);
							addFriendList();
							break;
						case 1://发送更新后的list
							addFriendList();
							break;
						case 2://发送群聊消息
						case 3:
							sendGroupMsg(msg);
							break;
						case 4://发送私聊消息
						case 5:
							sendMsg(msg);
						default:
							break;
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 建立连接线程
	 */
	class initConnection extends Thread {
		@Override
		public void run() {
			try {
				ServerSocket server = new ServerSocket(9990);
				while(true) {
					Socket socket = server.accept();
					InetSocketAddress isa = (InetSocketAddress) socket.getRemoteSocketAddress();
					Integer port = isa.getPort();
					socketMap.put(port, socket);
					
					ReceiveMsgThread t1 = new ReceiveMsgThread(socket);
					t1.start();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void main(String[] args) {
		QQServer test = new QQServer();
	}
}
