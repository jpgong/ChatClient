package com.chatTest.Jframe;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import com.chatTest.bean.UserBean;
import com.chatTest.message.OriginalStateMessage;
import com.chatTest.util.CheckUtil;
import com.chatTest.util.FontStyle;
import com.chatTest.util.SSLUtil;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.JButton;
import java.awt.Component;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.swing.Box;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.awt.event.ActionEvent;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;

public class RegisterFrame extends JFrame {

	private static final long serialVersionUID = -5375662544067424164L;
	private JPanel contentPane;
	private final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
	
	private int port = 9999;
	private String localhost = "127.0.0.1";
	private ObjectOutputStream oos;
	private ObjectInputStream ois;

	private JButton btnSubmit;
	private JTextField textFieldUserName;
	private JTextField textFieldTel;
	private JPasswordField passwordFieldPwd1;
	private JPasswordField passwordFieldPwd2;
	private JTextField textFieldMail;
	private JComboBox<String> comboBoxSex;

	/**
	 * Create the frame.
	 */
	public RegisterFrame(ClientFrame clientFrame) {
		setTitle("\u65B0\u7528\u6237\u6CE8\u518C");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 417, 307);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		JPanel panelNorth = new JPanel();
		contentPane.add(panelNorth, BorderLayout.CENTER);
		panelNorth.setLayout(null);
		
		JLabel label = new JLabel("\u7528\u6237\u540D\uFF1A");
		label.setFont(new Font("华文楷体", Font.BOLD, 14));
		label.setBounds(47, 23, 70, 19);
		panelNorth.add(label);
		
		textFieldUserName = new JTextField();
		textFieldUserName.setBounds(123, 25, 168, 21);
		panelNorth.add(textFieldUserName);
		textFieldUserName.setColumns(10);
		textFieldUserName.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void removeUpdate(DocumentEvent e) {
				if (textFieldUserName.getText().length() > 0) {
					btnSubmit.setEnabled(true);		
				}else {
					btnSubmit.setEnabled(false);		
				}
			}
			
			@Override
			public void insertUpdate(DocumentEvent e) {
				if (textFieldUserName.getText().length() > 0) {
					btnSubmit.setEnabled(true);		
				}else {
					btnSubmit.setEnabled(false);		
				}		
			}
			
			@Override
			public void changedUpdate(DocumentEvent e) {
				if (textFieldUserName.getText().length() > 0) {
					btnSubmit.setEnabled(true);		
				}else {
					btnSubmit.setEnabled(false);		
				}
			}
		});
		
		JLabel label_1 = new JLabel("\u53E3   \u4EE4\uFF1A");
		label_1.setBounds(47, 52, 70, 19);
		label_1.setFont(new Font("华文楷体", Font.BOLD, 14));
		panelNorth.add(label_1);
		
		JLabel label_2 = new JLabel("\u786E\u5B9A\u53E3\u4EE4\uFF1A");
		label_2.setBounds(32, 81, 81, 19);
		label_2.setFont(new Font("华文楷体", Font.BOLD, 14));
		panelNorth.add(label_2);
		
		textFieldTel = new JTextField();
		textFieldTel.setBounds(123, 139, 168, 21);
		textFieldTel.setColumns(10);
		panelNorth.add(textFieldTel);
		
		JLabel label_3 = new JLabel("\u6027   \u522B\uFF1A");
		label_3.setBounds(47, 110, 70, 19);
		label_3.setFont(new Font("华文楷体", Font.BOLD, 14));
		panelNorth.add(label_3);
		
		JLabel label_4 = new JLabel("\u624B\u673A\u53F7\u7801\uFF1A");
		label_4.setBounds(32, 140, 81, 19);
		label_4.setFont(new Font("华文楷体", Font.BOLD, 14));
		panelNorth.add(label_4);
		
		passwordFieldPwd1 = new JPasswordField();
		passwordFieldPwd1.setBounds(123, 51, 168, 21);
		panelNorth.add(passwordFieldPwd1);
		
		passwordFieldPwd2 = new JPasswordField();
		passwordFieldPwd2.setBounds(123, 80, 168, 21);
		panelNorth.add(passwordFieldPwd2);
		
		JLabel label_5 = new JLabel("\u7535\u5B50\u90AE\u7BB1\uFF1A");
		label_5.setFont(new Font("华文楷体", Font.BOLD, 14));
		label_5.setBounds(32, 169, 81, 19);
		panelNorth.add(label_5);
		
		textFieldMail = new JTextField();
		textFieldMail.setBounds(123, 170, 168, 21);
		panelNorth.add(textFieldMail);
		textFieldMail.setColumns(10);
		
		comboBoxSex = new JComboBox<String>();
		comboBoxSex.setModel(new DefaultComboBoxModel<String>(new String[] {"\u7537", "\u5973", "\u672A\u77E5"}));
		comboBoxSex.setBounds(123, 109, 70, 21);
		panelNorth.add(comboBoxSex);

		JPanel panelSouth = new JPanel();
		contentPane.add(panelSouth, BorderLayout.SOUTH);

		Component horizontalStrut_2 = Box.createHorizontalStrut(20);
		panelSouth.add(horizontalStrut_2);

		btnSubmit = new JButton("\u786E\u5B9A");
		btnSubmit.addActionListener(e -> {
				//点击确定按钮，将用户消息和用户口令存入进数据库
				String userName = textFieldUserName.getText().trim();
				String password1 = new String(passwordFieldPwd1.getPassword());
				String password2 = new String(passwordFieldPwd2.getPassword());
				String sex = (String) comboBoxSex.getSelectedItem();
				String telPhone = textFieldTel.getText().trim();
				String mailBox = textFieldMail.getText().trim();
				
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
				
				//对用户输入的信息用正则表达式进行初始化检测，看是否符合实际情况
				if (password1.equals(password2)) {
					if (CheckUtil.checkMobileNumber(telPhone)) {
						if (CheckUtil.checkEmail(mailBox)) {
							
							//对用户消息进行封装
							UserBean userBean = new UserBean();
							userBean.setUserName(userName);
							userBean.setPassword(password1);
							userBean.setSex(sex);
							userBean.setTelPhone(telPhone);
							userBean.setMailBox(mailBox);
							// 在"消息记录"文本框用红色填写"添加成功"、用户账号和在线时长
							String msgRecord = dateFormat.format(new Date()) + " 正在向服务器发送注册请求……\r\n";
							FontStyle.addMsgRecord(ClientFrame.textPaneMsgRecord,msgRecord, Color.red, 12, false, false);
							//创建一个用户初始状态消息，是否成功设为false
							OriginalStateMessage originalStateMessage = new OriginalStateMessage(userName, "Regist", userBean, false);
							//发送注册消息给服务器
							try {
								synchronized(oos) {
									oos.writeObject(originalStateMessage);
									oos.flush();
								}
							} catch (IOException e1) {
								e1.printStackTrace();
							}
							
							try {
								originalStateMessage = (OriginalStateMessage) ois.readObject();
								if (originalStateMessage.isSucceed()) {   //注册成功
									this.setVisible(false);
									String msg = dateFormat.format(new Date()) + " 注册成功，请登录\r\n";
									FontStyle.addMsgRecord(ClientFrame.textPaneMsgRecord, msg, Color.red, 12, false, false);
									
								}else {
									this.setVisible(false);
									String msgRecord1 = dateFormat.format(new Date()) + " 注册失败，该用户信息已存在\r\n";
								    FontStyle.addMsgRecord(ClientFrame.textPaneMsgRecord,msgRecord1, Color.red, 12, false, false);
								}
							} catch (ClassNotFoundException | IOException e1) {
								e1.printStackTrace();
							}
						}else {
							JOptionPane.showMessageDialog(RegisterFrame.this, "邮箱格式不正确");
							return;
						}
					}else {
						JOptionPane.showMessageDialog(RegisterFrame.this, "手机号码格式不正确");
						return;
					}
				}else { 
					JOptionPane.showMessageDialog(RegisterFrame.this, "两次密码不一致，请重新输入。");
					return;
				}
		});
		btnSubmit.setFont(new Font("楷体", Font.BOLD, 15));
		panelSouth.add(btnSubmit);

		Component horizontalStrut = Box.createHorizontalStrut(20);
		panelSouth.add(horizontalStrut);

		JButton btnDelete = new JButton("\u91CD\u7F6E");
		btnDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// 点击重置按钮，清空两个文本框
				textFieldUserName.setText("");
				passwordFieldPwd1.setText("");
				passwordFieldPwd2.setText("");
				comboBoxSex.setSelectedItem("男");;
				textFieldMail.setText("");
				textFieldTel.setText("");
			}
		});
		btnDelete.setFont(new Font("楷体", Font.BOLD, 15));
		panelSouth.add(btnDelete);

		Component horizontalStrut_1 = Box.createHorizontalStrut(20);
		panelSouth.add(horizontalStrut_1);

		JButton btnBack = new JButton("\u53D6\u6D88");
		btnBack.addActionListener(e -> {
				this.setVisible(false);
		});
		btnBack.setFont(new Font("楷体", Font.BOLD, 15));
		panelSouth.add(btnBack);
		
		btnSubmit.setEnabled(false);
	}
}
