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

public class ModifyFrame extends JFrame {
	private static final long serialVersionUID = 7672775197946978737L;
	private JPanel contentPane;
	private final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
	
	private int port = 9999;
	private String localhost = "127.0.0.1";
	private ObjectOutputStream oos;
	private ObjectInputStream ois;

	private JButton btnSubmit;
	private JTextField textFieldUserName;
	private JTextField textFieldTel;
	private JPasswordField passwordFieldPwd;
	private JTextField textFieldMail;
	private JComboBox<String> comboBoxSex;

	/**
	 * Create the frame.
	 */
	public ModifyFrame(ClientFrame clientFrame) {
		setTitle("\u4FEE\u6539\u7528\u6237\u4FE1\u606F");
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
		label.setBounds(43, 26, 70, 19);
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
				} else {
					btnSubmit.setEnabled(false);
				}
			}

			@Override
			public void insertUpdate(DocumentEvent e) {
				if (textFieldUserName.getText().length() > 0) {
					btnSubmit.setEnabled(true);
				} else {
					btnSubmit.setEnabled(false);
				}
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				if (textFieldUserName.getText().length() > 0) {
					btnSubmit.setEnabled(true);
				} else {
					btnSubmit.setEnabled(false);
				}
			}
		});

		JLabel label_1 = new JLabel("\u53E3   \u4EE4\uFF1A");
		label_1.setBounds(47, 62, 70, 19);
		label_1.setFont(new Font("华文楷体", Font.BOLD, 14));
		panelNorth.add(label_1);

		textFieldTel = new JTextField();
		textFieldTel.setBounds(123, 119, 168, 21);
		textFieldTel.setColumns(10);
		panelNorth.add(textFieldTel);

		JLabel label_3 = new JLabel("\u6027   \u522B\uFF1A");
		label_3.setBounds(47, 91, 70, 19);
		label_3.setFont(new Font("华文楷体", Font.BOLD, 14));
		panelNorth.add(label_3);

		JLabel label_4 = new JLabel("\u624B\u673A\u53F7\u7801\uFF1A");
		label_4.setBounds(36, 120, 81, 19);
		label_4.setFont(new Font("华文楷体", Font.BOLD, 14));
		panelNorth.add(label_4);

		passwordFieldPwd = new JPasswordField();
		passwordFieldPwd.setBounds(123, 61, 168, 21);
		panelNorth.add(passwordFieldPwd);

		JLabel label_5 = new JLabel("\u7535\u5B50\u90AE\u7BB1\uFF1A");
		label_5.setFont(new Font("华文楷体", Font.BOLD, 14));
		label_5.setBounds(36, 149, 81, 19);
		panelNorth.add(label_5);

		textFieldMail = new JTextField();
		textFieldMail.setBounds(123, 150, 168, 21);
		panelNorth.add(textFieldMail);
		textFieldMail.setColumns(10);

		comboBoxSex = new JComboBox<String>();
		comboBoxSex.setModel(new DefaultComboBoxModel<String>(new String[] { "\u7537", "\u5973", "\u672A\u77E5" }));
		comboBoxSex.setBounds(123, 90, 70, 21);
		panelNorth.add(comboBoxSex);

		JPanel panelSouth = new JPanel();
		contentPane.add(panelSouth, BorderLayout.SOUTH);

		Component horizontalStrut_2 = Box.createHorizontalStrut(20);
		panelSouth.add(horizontalStrut_2);

		btnSubmit = new JButton("\u786E\u5B9A");
		btnSubmit.addActionListener(e -> {
			// 点击确定按钮，将用户消息和用户口令存入进数据库
			String userName = textFieldUserName.getText().trim();
			String password1 = new String(passwordFieldPwd.getPassword());
			String sex = (String) comboBoxSex.getSelectedItem();
			String telPhone = textFieldTel.getText().trim();
			String mailBox = textFieldMail.getText().trim();

			// 与服务器进行连接
			try {
				SSLContext sslContext = SSLUtil.createClienSSLContext();
				SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
				SSLSocket socket = (SSLSocket) sslSocketFactory.createSocket(localhost, port);
				oos = new ObjectOutputStream(socket.getOutputStream());
				ois = new ObjectInputStream(socket.getInputStream());
			} catch (IOException e2) {
				e2.printStackTrace();
			}

			// 对用户输入的信息用正则表达式进行初始化检测，看是否符合实际情况
			if (password1.equals(password1)) {
				if (CheckUtil.checkMobileNumber(telPhone)) {
					if (CheckUtil.checkEmail(mailBox)) {

						// 对用户消息进行封装
						UserBean userBean = new UserBean();
						userBean.setUserName(userName);
						userBean.setPassword(password1);
						userBean.setSex(sex);
						userBean.setTelPhone(telPhone);
						userBean.setMailBox(mailBox);
						// 创建一个用户初始状态消息，是否成功设为false
						OriginalStateMessage originalStateMessage = new OriginalStateMessage(ClientFrame.localUserName, "Modify",
								userBean, false);
						// 发送注册消息给服务器
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
							if (originalStateMessage.isModifyMessage()) {
								if (originalStateMessage.isSucceed()) { // 修改成功
									this.setVisible(false);
									String msg = dateFormat.format(new Date()) + " 信息修改成功,请重新登录\r\n";
									FontStyle.addMsgRecord(ClientFrame.textPaneMsgRecord, msg, Color.red, 12, false,
											false);
								} else {
									this.setVisible(false);
									String msgRecord1 = dateFormat.format(new Date()) + " 信息修改失败。\r\n";
									FontStyle.addMsgRecord(ClientFrame.textPaneMsgRecord, msgRecord1, Color.red, 12,
											false, false);
								}
							}
						} catch (ClassNotFoundException | IOException e1) {
							e1.printStackTrace();
						}
					} else {
						JOptionPane.showMessageDialog(ModifyFrame.this, "邮箱格式不正确");
						return;
					}
				} else {
					JOptionPane.showMessageDialog(ModifyFrame.this, "手机号码格式不正确");
					return;
				}
			} else {
				JOptionPane.showMessageDialog(ModifyFrame.this, "两次密码不一致，请重新输入。");
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
				passwordFieldPwd.setText("");
				comboBoxSex.setSelectedItem("男");
				;
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
	
	/**
	 * 在该页面显示用户数据
	 * @param userBean
	 */

	public void showUserMessage(UserBean userBean) {
		textFieldUserName.setText(userBean.getUserName());
		passwordFieldPwd.setText(userBean.getPassword());
		if (userBean.getSex().equals("男")) {
			comboBoxSex.setSelectedIndex(0);
		}else if (userBean.getSex().equals("女")) {
			comboBoxSex.setSelectedIndex(1);
		}else {
			comboBoxSex.setSelectedIndex(2);
		}
		textFieldTel.setText(userBean.getTelPhone());
		textFieldMail.setText(userBean.getMailBox());
	}
}
