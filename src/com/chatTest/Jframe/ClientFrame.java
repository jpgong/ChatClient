package com.chatTest.Jframe;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.JProgressBar;
import javax.swing.JButton;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;

import java.awt.Component;
import java.awt.Dimension;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.swing.Box;
import javax.swing.JSplitPane;
import javax.swing.JTextPane;
import javax.swing.ProgressMonitor;
import javax.swing.SwingWorker;
import javax.swing.Timer;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.BadLocationException;
import javax.swing.text.StyledDocument;

import com.chatTest.bean.FileBean;
import com.chatTest.bean.UserBean;
import com.chatTest.message.ChatMessage;
import com.chatTest.message.FileStateMessage;
import com.chatTest.message.ImageMessage;
import com.chatTest.message.Message;
import com.chatTest.message.OriginalStateMessage;
import com.chatTest.message.UserStateMessage;
import com.chatTest.util.FontStyle;
import com.chatTest.util.SSLUtil;

import java.awt.Font;
import java.awt.HeadlessException;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.swing.JCheckBox;
import javax.swing.JFileChooser;

import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ActionEvent;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JScrollPane;

public class ClientFrame extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField textFieldUserName;
	private JPasswordField passwordFieldPwd;
	private JTextField textFieldMsgToSend;

	private int port = 9999;
	private String localhost = "127.0.0.1";
	private SSLSocket socket;
	private ObjectOutputStream oos;
	private ObjectInputStream ois;
	// 在线用户名
	public static String localUserName;
	// 在线用户口令
	private String localUserPwd;
	// 在线用户列表
	private DefaultListModel<String> onlineUserDlm = new DefaultListModel<>();
	// 用于控制时间信息显示格式
	// private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd
	// HH:mm:ss");
	private final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
	private JButton btnLogin;
	private JButton btnSendFile;
	private JButton btnSendMsg;
	private JCheckBox chckbxPrivate;
	private JButton btnRegister;
	private JLabel label;
	@SuppressWarnings("rawtypes")
	private JComboBox comboBoxSetting;
	private Component horizontalStrut_1;
	private JSplitPane splitPane;
	private JScrollPane scrollPaneMsgRecord;
	private JScrollPane scrollPaneOnlineUser;
	public static JTextPane textPaneMsgRecord;
	private JList<String> listOnlineUsers;
	private JCheckBox chckbxPicture;
	private JProgressBar progressBar;
	private JPanel panel;
	private ProgressMonitor progressMonitor;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ClientFrame frame = new ClientFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public ClientFrame() {
//		try { // 使用Windows的界面风格
//			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		setTitle("\u5BA2\u6237\u7AEF");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 574, 360);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));

		JPanel panelNorth = new JPanel();
		contentPane.add(panelNorth, BorderLayout.NORTH);
		panelNorth.setLayout(new BoxLayout(panelNorth, BoxLayout.X_AXIS));

		JLabel lblNewLabel = new JLabel("\u7528\u6237\u540D\uFF1A");
		lblNewLabel.setFont(new Font("华文楷体", Font.BOLD, 14));
		panelNorth.add(lblNewLabel);

		textFieldUserName = new JTextField();
		panelNorth.add(textFieldUserName);
		textFieldUserName.setColumns(10);

		Component horizontalStrut = Box.createHorizontalStrut(20);
		panelNorth.add(horizontalStrut);

		JLabel lblNewLabel_1 = new JLabel("\u53E3\u4EE4\uFF1A");
		lblNewLabel_1.setFont(new Font("华文楷体", Font.BOLD, 14));
		panelNorth.add(lblNewLabel_1);

		passwordFieldPwd = new JPasswordField();
		passwordFieldPwd.setColumns(10);
		panelNorth.add(passwordFieldPwd);

		Component horizontalStrut_4 = Box.createHorizontalStrut(20);
		panelNorth.add(horizontalStrut_4);

		btnLogin = new JButton("\u767B\u5F55");
		btnLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (btnLogin.getText().equals("登录")) {
					localUserName = textFieldUserName.getText().trim();
					localUserPwd = new String(passwordFieldPwd.getPassword());

					// 在"消息记录"文本框用红色填写"添加成功"、用户账号和在线时长
					String msgRecord = dateFormat.format(new Date()) + " 正在登陆……\r\n";
					FontStyle.addMsgRecord(textPaneMsgRecord, msgRecord, Color.red, 12, false, false);

					UserBean userBean = new UserBean();
					userBean.setUserName(localUserName);
					userBean.setPassword(localUserPwd);

					try {
						SSLContext sslContext = SSLUtil.createClienSSLContext();
						SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
						socket = (SSLSocket) sslSocketFactory.createSocket(localhost, port);
						oos = new ObjectOutputStream(socket.getOutputStream());
						ois = new ObjectInputStream(socket.getInputStream());
					} catch (UnknownHostException e1) {
						JOptionPane.showMessageDialog(ClientFrame.this, "网络不通，请检查你的防火墙");
						e1.printStackTrace();
					} catch (IOException e1) {
						JOptionPane.showMessageDialog(ClientFrame.this, "服务器未启动");
						e1.printStackTrace();
					}

					// 创建一个用户初始状态消息，是否成功设为false
					OriginalStateMessage originalStateMessage = new OriginalStateMessage(localUserName, "Login",
							userBean, false);
					// 发送注册消息给服务器
					try {
						oos.writeObject(originalStateMessage);
						oos.flush();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					try {
						originalStateMessage = (OriginalStateMessage) ois.readObject();
						if (originalStateMessage.isSucceed()) { // 登录成功
							// 在"消息记录"文本框用红色填写"添加成功"、用户账号和在线时长
							String msgRecord1 = dateFormat.format(new Date()) + " 登陆成功\r\n";
							FontStyle.addMsgRecord(textPaneMsgRecord, msgRecord1, Color.red, 12, false, false);

							// 创建一个用户上线消息对象，将用户状态设为true
							UserStateMessage userStateMessage = new UserStateMessage(localUserName, "", true);
							try {
								oos.writeObject(userStateMessage);
								oos.flush();
							} catch (IOException e1) {
								JOptionPane.showMessageDialog(ClientFrame.this, "网络已经断开");
								e1.printStackTrace();
							}
							textPaneMsgRecord.setForeground(Color.RED);

							// 将两个发送按钮设置为可选
							btnSendMsg.setEnabled(true);
							btnSendFile.setEnabled(true);
							btnRegister.setEnabled(false);

							// 创建并启动后台监听线程
							new Thread(new ListeningHandle()).start();
							btnLogin.setText("退出");

							setTitle("客户端，当前用户：" + localUserName);

						} else {
							if (originalStateMessage.isRepeat()) {
								String msgRecord1 = dateFormat.format(new Date()) + " 您已登录，请不要重复登录\r\n";
								FontStyle.addMsgRecord(ClientFrame.textPaneMsgRecord, msgRecord1, Color.red, 12, false,
										false);
								JOptionPane.showMessageDialog(ClientFrame.this, "请您不要重复登录！");
							}else {
								String msgRecord1 = dateFormat.format(new Date()) + " 登录失败，用户名或密码错误\r\n";
								FontStyle.addMsgRecord(ClientFrame.textPaneMsgRecord, msgRecord1, Color.red, 12, false,
										false);
							}
						}
					} catch (ClassNotFoundException | IOException e1) {
						e1.printStackTrace();
					}
				} else { // 当用户登录成功时，此时登录按钮为退出按钮，应执行退出操作
					if (JOptionPane.showConfirmDialog(ClientFrame.this, "是否退出", "退出确定",
							JOptionPane.YES_NO_OPTION) == JOptionPane.OK_OPTION) {
						// 将用户状态修改为下线状态，即false态
						UserStateMessage userStateMessage = new UserStateMessage(localUserName, " ", false);
						// 在"消息记录"文本框用红色填写下线成功消息
						String msgRecord = dateFormat.format(new Date()) + " 下线成功。";
						FontStyle.addMsgRecord(textPaneMsgRecord, msgRecord, Color.red, 12, false, false);
						try {
							synchronized (oos) {
								oos.writeObject(userStateMessage);
								oos.flush();
							}
							// 用户退出后，清空用户列表中的内容
							onlineUserDlm.clear();
							btnLogin.setText("登录");
							System.exit(0);
						} catch (IOException e1) {
							e1.printStackTrace();
						}
					}
				}
			}
		});
		
		label = new JLabel("\u8BBE\u7F6E\uFF1A");
		label.setFont(new Font("华文楷体", Font.BOLD, 14));
		panelNorth.add(label);
		
		comboBoxSetting = new JComboBox();
		comboBoxSetting.addItemListener(e-> {
			if (e.getStateChange() == ItemEvent.SELECTED) {
				int index = comboBoxSetting.getSelectedIndex();
				if (index == 1) {  //处理修改资料功能
					//从服务器端把用户消息接收过来的
					UserBean userBean = getMessageFromServer(localUserName);
					//在修改页面显示该用户内容
					ModifyFrame modifyFrame = new ModifyFrame(this);
					// 使窗口可见
					modifyFrame.setVisible(true);
					modifyFrame.setSize(new Dimension(417, 307));
					modifyFrame.showUserMessage(userBean);
				}else if (index == 2) {  //处理更改头像功能
					System.out.println("头像更改");
				}
			}
		});
		comboBoxSetting.setModel(new DefaultComboBoxModel(new String[] {"", "\u7F16\u8F91\u8D44\u6599", "\u8BBE\u7F6E\u5934\u50CF"}));
		panelNorth.add(comboBoxSetting);
		
		horizontalStrut_1 = Box.createHorizontalStrut(20);
		panelNorth.add(horizontalStrut_1);
		
		btnLogin.setFont(new Font("楷体", Font.BOLD, 15));
		panelNorth.add(btnLogin);

		Component horizontalStrut_5 = Box.createHorizontalStrut(20);
		panelNorth.add(horizontalStrut_5);

		btnRegister = new JButton("\u6CE8\u518C");
		btnRegister.addActionListener(e -> {
			RegisterFrame registerFrame = new RegisterFrame(this);
			// 使窗口可见
			registerFrame.setVisible(true);
			registerFrame.setSize(new Dimension(417, 307));
		});
		btnRegister.setFont(new Font("楷体", Font.BOLD, 15));
		panelNorth.add(btnRegister);

		JPanel panelMid = new JPanel();
		contentPane.add(panelMid, BorderLayout.CENTER);
		panelMid.setLayout(new BoxLayout(panelMid, BoxLayout.X_AXIS));
		
		splitPane = new JSplitPane();
		splitPane.setResizeWeight(1.0);
		panelMid.add(splitPane);
		
		scrollPaneMsgRecord = new JScrollPane();
		scrollPaneMsgRecord.setBorder(new TitledBorder(null, "\u6D88\u606F\u8BB0\u5F55", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		splitPane.setLeftComponent(scrollPaneMsgRecord);
		
		textPaneMsgRecord = new JTextPane();
		scrollPaneMsgRecord.setViewportView(textPaneMsgRecord);
		
		panel = new JPanel();
		scrollPaneMsgRecord.setColumnHeaderView(panel);
		
		progressBar = new JProgressBar(0,100);
		progressBar.setPreferredSize(new Dimension(250, 14));
		panel.add(progressBar);
		
		scrollPaneOnlineUser = new JScrollPane();
		scrollPaneOnlineUser.setBorder(new TitledBorder(null, "\u5728\u7EBF\u7528\u6237", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		splitPane.setRightComponent(scrollPaneOnlineUser);
		
		listOnlineUsers = new JList<String>(onlineUserDlm);
		scrollPaneOnlineUser.setViewportView(listOnlineUsers);

		JPanel panelSouth = new JPanel();
		contentPane.add(panelSouth, BorderLayout.SOUTH);
		panelSouth.setLayout(new BoxLayout(panelSouth, BoxLayout.X_AXIS));

		textFieldMsgToSend = new JTextField();
		panelSouth.add(textFieldMsgToSend);
		textFieldMsgToSend.setColumns(10);

		chckbxPrivate = new JCheckBox("\u79C1\u804A");
		chckbxPrivate.setFont(new Font("华文楷体", Font.BOLD, 14));
		panelSouth.add(chckbxPrivate);
		
		chckbxPicture = new JCheckBox("\u56FE\u7247");
		chckbxPicture.setFont(new Font("华文楷体", Font.BOLD, 14));
		panelSouth.add(chckbxPicture);

		Component horizontalStrut_2 = Box.createHorizontalStrut(20);
		panelSouth.add(horizontalStrut_2);

		btnSendMsg = new JButton("\u53D1\u9001\u6D88\u606F");
		btnSendMsg.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String msgContent = textFieldMsgToSend.getText();
				// 消息框中有消息再进行发送
				if (msgContent.length() > 0) {
					// 如果私聊选项被选中且在列表中选发件人，则构建一个私聊消息
					if (chckbxPrivate.isSelected() && !listOnlineUsers.isSelectionEmpty()) {
						// 获取私聊消息的接收者
						String dstUser = (String) listOnlineUsers.getSelectedValue();
						ChatMessage chatMessage = new ChatMessage(localUserName, dstUser, msgContent);
						try {
							synchronized (oos) {
								oos.writeObject(chatMessage);
								oos.flush();
							}
						} catch (IOException e1) {
							e1.printStackTrace();
						}
						// 在“消息记录”文本框中用蓝色显示发送的消息及发送时间
						String msgRecord = dateFormat.format(new Date()) + " 对" + dstUser + "说：" + msgContent + "\r\n";
						FontStyle.addMsgRecord(textPaneMsgRecord, msgRecord, Color.BLUE, 12, false, false);
					} else {
						// 构造一个聊天消息，实现公聊，广播给其他用户
						ChatMessage chatMessage = new ChatMessage(localUserName, "", msgContent);
						try {
							synchronized (oos) {
								oos.writeObject(chatMessage);
								oos.flush();
							}
						} catch (IOException e1) {
							e1.printStackTrace();
						}
						// 在“消息记录”文本框中用蓝色显示发送的消息及发送时间
						String msgRecord = dateFormat.format(new Date()) + " 向大家说：" + msgContent + "\r\n";
						FontStyle.addMsgRecord(textPaneMsgRecord, msgRecord, Color.BLUE, 12, false, false);
					}
				} else if (chckbxPicture.isSelected()) { // 发送图片消息
					JFileChooser chooser = new JFileChooser("此电脑");
					chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
					// 添加过滤器，只选择图片
					FileNameExtensionFilter filter = new FileNameExtensionFilter("图片(jpg,gif,png……)", "gif", "jpg","png","ico","webp","bmp");
					chooser.setFileFilter(filter);
					StyledDocument doc =  textPaneMsgRecord.getStyledDocument();
					// 判断是私聊发送照片还是公聊发送照片
					if (chckbxPrivate.isSelected() && !listOnlineUsers.isSelectionEmpty()) { // 是私聊发图片
						// 获取私聊消息的接收者
						String dstUser = (String) listOnlineUsers.getSelectedValue();
						String msgRecord = dateFormat.format(new Date()) + " 对" + dstUser + "发照片：\r\n";
						FontStyle.addMsgRecord(textPaneMsgRecord, msgRecord, Color.BLUE, 12, false, false);
						if (chooser.showOpenDialog(ClientFrame.this) == JFileChooser.APPROVE_OPTION) {
							textPaneMsgRecord.setCaretPosition(doc.getLength()); // 设置插入位置
							ImageIcon imageIcon = new ImageIcon(chooser.getSelectedFile().toString());
							textPaneMsgRecord.insertIcon(imageIcon);
							//私聊发送照片
							ImageMessage imageMessage = new ImageMessage(localUserName, dstUser, imageIcon);
							try {
								synchronized (oos) {
									oos.writeObject(imageMessage);
									oos.flush();
								}
							} catch (IOException e1) {
								e1.printStackTrace();
							}
						}
						try {
							doc.insertString(doc.getLength(), "\r\n", null);
						} catch (BadLocationException e1) {
							e1.printStackTrace();
						}
					} else {    //是向全体人员发图片消息
						String msgRecord = dateFormat.format(new Date()) + " 对全体发照片：\r\n";
						FontStyle.addMsgRecord(textPaneMsgRecord, msgRecord, Color.BLUE, 12, false, false);
						if (chooser.showOpenDialog(ClientFrame.this) == JFileChooser.APPROVE_OPTION) {
							textPaneMsgRecord.setCaretPosition(doc.getLength()); // 设置插入位置
							ImageIcon imageIcon = new ImageIcon(chooser.getSelectedFile().toString());
							textPaneMsgRecord.insertIcon(imageIcon);
							//向全体成员发送照片消息
							ImageMessage imageMessage = new ImageMessage(localUserName, "", imageIcon);
							try {
								synchronized (oos) {
									oos.writeObject(imageMessage);
									oos.flush();
								}
							} catch (IOException e1) {
								e1.printStackTrace();
							}
						}
						try {
							doc.insertString(doc.getLength(), "\r\n", null);
						} catch (BadLocationException e1) {
							e1.printStackTrace();
						}
					}
				} else {
					JOptionPane.showMessageDialog(ClientFrame.this, "请填写您要发送的消息内容。");
				}
				textFieldMsgToSend.setText("");
			}
		});
		btnSendMsg.setFont(new Font("楷体", Font.BOLD, 15));
		panelSouth.add(btnSendMsg);

		Component horizontalStrut_3 = Box.createHorizontalStrut(20);
		panelSouth.add(horizontalStrut_3);

		btnSendFile = new JButton("\u53D1\u9001\u6587\u4EF6");
		btnSendFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!listOnlineUsers.isSelectionEmpty()) { // 选中要发送的目标，则进行文件的发送
					JFileChooser fileChooser = new JFileChooser("此电脑");
					String dstUser = (String) listOnlineUsers.getSelectedValue();
					// 允许用户选择文件和目录
					fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
					if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
						String filePath = fileChooser.getSelectedFile().getAbsolutePath();
//						System.out.println(filePath);
						File file = fileChooser.getSelectedFile();
						// 发送文件前的准备工作，需要询问接受者是否要接收该文件
						sendPrepare(file, dstUser, filePath);
					}

				} else { // 如果没有发送目标，则进行提示
					JOptionPane.showMessageDialog(ClientFrame.this, "请先选择发送目标。");
				}
			}
		});
		btnSendFile.setFont(new Font("楷体", Font.BOLD, 15));
		panelSouth.add(btnSendFile);

		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				int i = JOptionPane.showConfirmDialog(ClientFrame.this, "确定要退出系统吗？", "退出系统", JOptionPane.YES_NO_OPTION);
				if (i == JOptionPane.YES_OPTION) {
					System.exit(0);
				}
			}
		});

		btnSendMsg.setEnabled(false);
		btnSendFile.setEnabled(false);
		progressBar.setEnabled(false);
		
	}

	/**
	 * 从服务器把该用户的消息给接收过来的，然后进行修改
	 * @return
	 */
	protected UserBean getMessageFromServer(String userName) {
		UserBean userBean = new UserBean();
		userBean.setUserName(userName);
		//与服务器进行连接
		try {
			SSLContext sslContext = SSLUtil.createClienSSLContext();
			SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
			SSLSocket socket = (SSLSocket) sslSocketFactory.createSocket(localhost, port);
			oos = new ObjectOutputStream(socket.getOutputStream());
			ois = new ObjectInputStream(socket.getInputStream());
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		//创建一个用户初始状态消息，是否成功设为false
		OriginalStateMessage originalStateMessage = new OriginalStateMessage(userName, "ModifyPrepare", userBean, false);
		//发送注册消息给服务器
		try {
			synchronized (oos) {
				oos.writeObject(originalStateMessage);
				oos.flush();
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		try {
			originalStateMessage = (OriginalStateMessage) ois.readObject();
			if (originalStateMessage.isModifyPreMessage()) {   //返回修改信息
				if (originalStateMessage.isSucceed()) {
					userBean = originalStateMessage.getUserBean();   //返回数据库中该该用户信息
				}else {
					System.out.println("没有返回该消息内容");
				}
			}else {
				System.out.println("返回消息内容有误。");
			}
		} catch (ClassNotFoundException | IOException e1) {
			e1.printStackTrace();
		}
		return userBean;
	}

	/**
	 * 发送文件前要做的准备工作 通过服务器来询问接收者是否需要接收该文件
	 * 
	 * @param fileName
	 *            文件名称
	 * @param dstUser
	 *            接收者
	 * @return 如果返回true,则表示接收者要接收，如果返回false，则拒绝接收
	 */
	protected void sendPrepare(File sendFile, String dstUser, String filePath) {
		String fileName = sendFile.getName();
		// 在消息记录中用蓝色字提示发送文件的请求
		String msg = dateFormat.format(new Date()) + " 准备向用户" + dstUser + "发送文件：" + fileName + "\r\n";
		FontStyle.addMsgRecord(textPaneMsgRecord, msg, Color.BLUE, 12, false, false);

		try {
			FileInputStream fis = new FileInputStream(sendFile);
			long size = fis.available();

			// 向收件人发送文件发送请求消息，定义一个发送文件协议码
			FileBean fileBean = new FileBean();
			fileBean.setFileName(fileName);
			fileBean.setFileSize(size);
			fileBean.setFilePath(filePath);
			FileStateMessage fileSendStateMessage = new FileStateMessage(localUserName, dstUser, fileBean, false, "请求");
			try {
				synchronized (oos) {
					oos.writeObject(fileSendStateMessage);
					oos.flush();
				}
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			fis.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * 后台监听线程
	 * 
	 * @author jpgong
	 *
	 */
	class ListeningHandle implements Runnable {

		@Override
		public void run() {
			while (true) {
				Message message = null;
				try {
					synchronized (ois) {
						message = (Message) ois.readObject();
					}
					if (message instanceof UserStateMessage) { // 如果是用户状态消息
						processUserStateMessage((UserStateMessage) message);

					} else if (message instanceof ChatMessage) { // 如果是聊天消息
						processChatMessage((ChatMessage) message);

					} else if (message instanceof FileStateMessage) {   //处理文件请求和响应消息
						processFileStateMessage((FileStateMessage)message);
					} else if (message instanceof ImageMessage) {    //处理图片消息
						processImageMessage((ImageMessage)message);
					}else {
						// 意味收到了错误格式的消息
						JOptionPane.showMessageDialog(ClientFrame.this, "消息格式不正确！");

					}
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		/**
		 * 处理图片消息
		 * @param message
		 */
		private void processImageMessage(ImageMessage message) {
			String srcUser = message.getSrcUser();
			ImageIcon imageIcon = message.getImageIcon();
			if (onlineUserDlm.contains(srcUser)) {
				// 如果是公聊发送照片
				if (message.isPublicMessage()) {
					String msg = dateFormat.format(new Date()) + " " + srcUser + " 对全体发照片：\r\n";
					FontStyle.addMsgRecord(textPaneMsgRecord, msg, Color.BLACK, 12, false, false);
					StyledDocument doc = textPaneMsgRecord.getStyledDocument();
					textPaneMsgRecord.setCaretPosition(doc.getLength()); // 设置插入位置
					textPaneMsgRecord.insertIcon(imageIcon);
					try {
						doc.insertString(doc.getLength(), "\r\n", null);
					} catch (BadLocationException e1) {
						e1.printStackTrace();
					}
				} else {
					// 该消息是私聊消息
					String msg = dateFormat.format(new Date()) + " " + srcUser + " 给您发送照片：\r\n";
					FontStyle.addMsgRecord(textPaneMsgRecord, msg, Color.BLACK, 12, false, false);
					StyledDocument doc = textPaneMsgRecord.getStyledDocument();
					textPaneMsgRecord.setCaretPosition(doc.getLength()); // 设置插入位置
					textPaneMsgRecord.insertIcon(imageIcon);
					try {
						doc.insertString(doc.getLength(), "\r\n", null);
					} catch (BadLocationException e1) {
						e1.printStackTrace();
					}
				}
			} else {
				System.out.println("此消息是幽灵消息！");
			}
		}
		/**
		 * 处理文件发送请求消息和文件响应请求消息
		 * @param message
		 */
		private void processFileStateMessage(FileStateMessage message) {
			String srcName = message.getSrcUser();
			String dstName = message.getDstUser();
			FileBean fileBean = message.getFileBean();
			String fileName = fileBean.getFileName();
			long fileSize = fileBean.getFileSize();
			if (message.isRequest()) {   //是文件发送请求消息,则需要用户来进行判断
				
				int i = JOptionPane.showConfirmDialog(ClientFrame.this, "用户" + srcName + "请求发送文件！\r\n" 
				                                                      + "文件名：" + fileName + "\r\n" 
				                                                      + "文件大小： " + fileSize + "\r\n"
				                                                      + "您是否要接收？" + "\r\n"
				                                                      , "用户" + dstName, JOptionPane.YES_NO_OPTION);
				if (i == JOptionPane.YES_OPTION) {    //接收者处理想要接收该文件的请求消息
					message.setType("响应");   //处理完之后
					message.setState(true);
					try {
						synchronized (oos) {
							oos.writeObject(message);
							oos.flush();
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
					
					try {
						//创建一个接收文件的线程
						SSLContext context = SSLUtil.createServerSSLContext();
						SSLServerSocketFactory factory = context.getServerSocketFactory();
						SSLServerSocket serverSocket = (SSLServerSocket) factory.createServerSocket(8888);
						SSLSocket socket = (SSLSocket) serverSocket.accept();
						
						String msg = dateFormat.format(new Date()) + " 正准备接收" + srcName + "传过来的文件：" + fileName + "\r\n";
						FontStyle.addMsgRecord(textPaneMsgRecord, msg, Color.blue, 12, false, false);
						
						ReceiveActivity receiveActivity = new ReceiveActivity(socket,srcName,fileName,fileSize);
						receiveActivity.execute();
						
						
						new Timer(500, e ->{
							if (progressMonitor.isCanceled()) {
								receiveActivity.cancel(true);
							}else if (receiveActivity.isDone()) {
								progressMonitor.close();
							}else {
								progressMonitor.setProgress(receiveActivity.getProgress());
							}
						}).start();
						
					} catch (IOException e) {
						e.printStackTrace();
					}
				}else {   //拒绝接收该文件
					message.setType("响应");
					message.setState(false);
					try {
						synchronized (oos) {
							oos.writeObject(message);
							oos.flush();
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			} else if (message.isResponse()) {   //如果是响应消息
				if (message.isState()) {     //接收者同意接收该文件，则发送者创建一个发送文件的线程来发送文件
					try {
						//创建一个发送文件的线程   
						SSLContext sslContext = SSLUtil.createClienSSLContext();
						SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
						SSLSocket socket = (SSLSocket) sslSocketFactory.createSocket(fileBean.getAddress(), 8888);
						
						//创建进度条
						System.out.println("文件大小为：" + fileSize);
						progressBar.setStringPainted(true);
						
						//启动发送文件进程
						SendActivity sendActivity = new SendActivity(socket, fileBean.getFilePath(),fileSize,dstName,fileName);
						sendActivity.execute();
						
					} catch (IOException e) {
						e.printStackTrace();
					}
				}else {    //发送者处理拒绝接收文件的响应消息
					String msg = dateFormat.format(new Date()) + " 用户" + dstName + "拒绝接收文件：" + fileName + "的请求。"
							+ "\r\n";
					FontStyle.addMsgRecord(textPaneMsgRecord, msg, Color.blue, 12, false, false);
				}
			} else {
				System.out.println("该消息不是文件消息类型");
				return;
			}
		}

		/**
		 * 处理用户状态消息的函数 判断用户状态是上线还是下线
		 */
		public void processUserStateMessage(UserStateMessage userStateMessage) {
			String srcUser = userStateMessage.getSrcUser();
			String dstUser = userStateMessage.getDstUser();
			if (userStateMessage.isUserOnline()) {
				if (userStateMessage.isPubUserStateMessage()) { // 有新用户上线
					// 用绿色文字将用户名和用户上线时间添加到“消息记录”文本框中
					String msg = dateFormat.format(new Date()) + " " + srcUser + " 上线了!" + "\r\n";
					FontStyle.addMsgRecord(textPaneMsgRecord, msg, Color.GREEN, 12, false, false);
					onlineUserDlm.addElement(srcUser);
				}
				if (dstUser.equals(localUserName)) { // 收到其他在线用户的消息
					onlineUserDlm.addElement(srcUser);
				}
			} else { // 处理用户下线消息
				if (onlineUserDlm.contains(srcUser)) {
					// 用绿色文字将用户名和用户下线时间添加到“消息记录”文本框中
					String msg = dateFormat.format(new Date()) + " " + srcUser + " 下线了!" + "\r\n";
					FontStyle.addMsgRecord(textPaneMsgRecord, msg, Color.GREEN, 12, false, false);
					onlineUserDlm.removeElement(srcUser);
				} else {
					System.out.println("见鬼了");
				}
			}
		}

		/**
		 * 处理聊天消息的函数 判断是公聊还是私聊
		 */
		public void processChatMessage(ChatMessage chatMessage) {
			String srcUser = chatMessage.getSrcUser();
			String msgContent = chatMessage.getMsgContent();

			if (onlineUserDlm.contains(srcUser)) {
				// 如果是公聊消息
				if (chatMessage.isPublicMessage()) {
					// 用黑色文字将收到消息的时间、发送消息的用户名和消息内容添加到“消息记录”文本框中
					String msg = dateFormat.format(new Date()) + " " + srcUser + " 对大家说：" + msgContent + "\r\n";
					FontStyle.addMsgRecord(textPaneMsgRecord, msg, Color.BLACK, 12, false, false);
				} else {
					// 该消息是私聊消息
					String msg = dateFormat.format(new Date()) + " " + srcUser + " 说：" + msgContent + "\r\n";
					FontStyle.addMsgRecord(textPaneMsgRecord, msg, Color.BLACK, 12, false, false);
				}
			} else {
				System.out.println("此消息是幽灵消息！");
			}
		}
	}
	
	/**
	 * 接收文件，并显示进度条的状态
	 * @author jpgong
	 *
	 */
	class ReceiveActivity extends SwingWorker<Void, Long>{
		SSLSocket socket;
		String srcName;
		String fileName;
		long fileSize;
		
		public ReceiveActivity(SSLSocket socket, String srcName, String fileName, long fileSize) {
			this.socket = socket;
			this.srcName = srcName;
			this.fileName = fileName;
			this.fileSize = fileSize;
			progressMonitor = new ProgressMonitor(ClientFrame.this, "Waiting for a moment……", null, 0, 100);
			System.out.println("开始接收文件");
		}
		@Override
		protected Void doInBackground() throws Exception {
			try {
				// 选择文件保存目录
				JFileChooser jFileChooser = new JFileChooser("此电脑");
				jFileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				if (jFileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
					String path = jFileChooser.getSelectedFile().getPath();
					System.out.println(path + fileName);
					File file = new File(path + fileName);
					// 创建本地的文件输出流，用来保存接收到的文件数据
					FileOutputStream fos = new FileOutputStream(file);
					// 设置缓冲区大小
					byte[] buff = new byte[4096];

					// 定义文件输入流，用来接收传过来的数据
					InputStream is = socket.getInputStream();
					// 定义每次接收到的数据
					int size = 0;
					// 定义已接收到的文件大小
					long fileCount = 0;
					while (fileCount < fileSize) {
						// 每次读到的大小
						size = is.read(buff);

						// 将读到的数据写入本地文件夹中
						fos.write(buff, 0, size);
						fos.flush();
						fileCount += size;
						setProgress((int) ((fileCount*100)/fileSize));
					}
					System.out.println("接收完毕");
					String msg = dateFormat.format(new Date()) + " 用户" + srcName + "传来的文件：" + fileName + "已接收完毕。\r\n";
					FontStyle.addMsgRecord(textPaneMsgRecord, msg, Color.red, 12, false, false);
					fos.close();
					is.close();
				}
			} catch (HeadlessException e) {
				e.printStackTrace();
			}catch (FileNotFoundException e) {
				System.err.println("服务器写文件失败。");
			} catch (IOException e) {
				System.err.println("与客户端断开连接。");
			} finally {
				if (socket != null) {
					try {
						socket.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
			return null;
		}
		@Override
		protected void process(List<Long> chunks) {
			for (Long num : chunks) {
				progressBar.setValue((int) ((num*100)/fileSize));
			}
		}
	}
	
	/**
	 * 发送文件线程，并且显示进度条的状态
	 * @author jpgong
	 *
	 */
	class SendActivity extends SwingWorker<Void, Long>{
		SSLSocket socket;
		String filePath;
		long fileSize;
		String dstName;
		String fileName;

		public SendActivity(SSLSocket socket, String filePath, long fileSize, String dstName, String fileName) {
			this.socket = socket;
			this.filePath = filePath;
			this.fileSize = fileSize;
			this.dstName = dstName;
			this.fileName = fileName;
			System.out.println("开始发送文件");
		}
		@Override
		protected Void doInBackground() throws Exception {
			try {
				System.out.println(filePath);
				FileInputStream fis = new FileInputStream(new File(filePath));
				byte[] buff = new byte[4096];
				//定义输出流，对数据包进行输出
				OutputStream os = socket.getOutputStream();
				//记录每次读文件的大小
				int size = 0;
				//记录已读文件的大小
				long fileCount = 0;
				//使用while循环读取文件，直到文件读取结束
				while((size = fis.read(buff)) != -1){
				    //向输出流中写入刚刚读到的数据包
				    os.write(buff, 0, size);
				    os.flush();
				    
				    fileCount += size;
				    publish(fileCount);
				}
				String msg = dateFormat.format(new Date()) + " 已向用户" + dstName + "发送完整个文件：" + fileName + "\r\n";
				FontStyle.addMsgRecord(textPaneMsgRecord, msg, Color.red, 12, false, false);
				fis.close();
				os.close();
			} catch (FileNotFoundException e) {
				System.err.println("客户端读文件文件失败。");
			} catch (IOException e) {
				System.err.println("与服务器断开连接。");
			}
			return null;
		}
		
		@Override
		protected void process(List<Long> chunks) {
			for (Long num : chunks) {
				progressBar.setValue((int) ((num*100)/fileSize));
			}
		}
	}
}
