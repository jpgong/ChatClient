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
	// �����û���
	public static String localUserName;
	// �����û�����
	private String localUserPwd;
	// �����û��б�
	private DefaultListModel<String> onlineUserDlm = new DefaultListModel<>();
	// ���ڿ���ʱ����Ϣ��ʾ��ʽ
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
//		try { // ʹ��Windows�Ľ�����
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
		lblNewLabel.setFont(new Font("���Ŀ���", Font.BOLD, 14));
		panelNorth.add(lblNewLabel);

		textFieldUserName = new JTextField();
		panelNorth.add(textFieldUserName);
		textFieldUserName.setColumns(10);

		Component horizontalStrut = Box.createHorizontalStrut(20);
		panelNorth.add(horizontalStrut);

		JLabel lblNewLabel_1 = new JLabel("\u53E3\u4EE4\uFF1A");
		lblNewLabel_1.setFont(new Font("���Ŀ���", Font.BOLD, 14));
		panelNorth.add(lblNewLabel_1);

		passwordFieldPwd = new JPasswordField();
		passwordFieldPwd.setColumns(10);
		panelNorth.add(passwordFieldPwd);

		Component horizontalStrut_4 = Box.createHorizontalStrut(20);
		panelNorth.add(horizontalStrut_4);

		btnLogin = new JButton("\u767B\u5F55");
		btnLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (btnLogin.getText().equals("��¼")) {
					localUserName = textFieldUserName.getText().trim();
					localUserPwd = new String(passwordFieldPwd.getPassword());

					// ��"��Ϣ��¼"�ı����ú�ɫ��д"��ӳɹ�"���û��˺ź�����ʱ��
					String msgRecord = dateFormat.format(new Date()) + " ���ڵ�½����\r\n";
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
						JOptionPane.showMessageDialog(ClientFrame.this, "���粻ͨ��������ķ���ǽ");
						e1.printStackTrace();
					} catch (IOException e1) {
						JOptionPane.showMessageDialog(ClientFrame.this, "������δ����");
						e1.printStackTrace();
					}

					// ����һ���û���ʼ״̬��Ϣ���Ƿ�ɹ���Ϊfalse
					OriginalStateMessage originalStateMessage = new OriginalStateMessage(localUserName, "Login",
							userBean, false);
					// ����ע����Ϣ��������
					try {
						oos.writeObject(originalStateMessage);
						oos.flush();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					try {
						originalStateMessage = (OriginalStateMessage) ois.readObject();
						if (originalStateMessage.isSucceed()) { // ��¼�ɹ�
							// ��"��Ϣ��¼"�ı����ú�ɫ��д"��ӳɹ�"���û��˺ź�����ʱ��
							String msgRecord1 = dateFormat.format(new Date()) + " ��½�ɹ�\r\n";
							FontStyle.addMsgRecord(textPaneMsgRecord, msgRecord1, Color.red, 12, false, false);

							// ����һ���û�������Ϣ���󣬽��û�״̬��Ϊtrue
							UserStateMessage userStateMessage = new UserStateMessage(localUserName, "", true);
							try {
								oos.writeObject(userStateMessage);
								oos.flush();
							} catch (IOException e1) {
								JOptionPane.showMessageDialog(ClientFrame.this, "�����Ѿ��Ͽ�");
								e1.printStackTrace();
							}
							textPaneMsgRecord.setForeground(Color.RED);

							// ���������Ͱ�ť����Ϊ��ѡ
							btnSendMsg.setEnabled(true);
							btnSendFile.setEnabled(true);
							btnRegister.setEnabled(false);

							// ������������̨�����߳�
							new Thread(new ListeningHandle()).start();
							btnLogin.setText("�˳�");

							setTitle("�ͻ��ˣ���ǰ�û���" + localUserName);

						} else {
							if (originalStateMessage.isRepeat()) {
								String msgRecord1 = dateFormat.format(new Date()) + " ���ѵ�¼���벻Ҫ�ظ���¼\r\n";
								FontStyle.addMsgRecord(ClientFrame.textPaneMsgRecord, msgRecord1, Color.red, 12, false,
										false);
								JOptionPane.showMessageDialog(ClientFrame.this, "������Ҫ�ظ���¼��");
							}else {
								String msgRecord1 = dateFormat.format(new Date()) + " ��¼ʧ�ܣ��û������������\r\n";
								FontStyle.addMsgRecord(ClientFrame.textPaneMsgRecord, msgRecord1, Color.red, 12, false,
										false);
							}
						}
					} catch (ClassNotFoundException | IOException e1) {
						e1.printStackTrace();
					}
				} else { // ���û���¼�ɹ�ʱ����ʱ��¼��ťΪ�˳���ť��Ӧִ���˳�����
					if (JOptionPane.showConfirmDialog(ClientFrame.this, "�Ƿ��˳�", "�˳�ȷ��",
							JOptionPane.YES_NO_OPTION) == JOptionPane.OK_OPTION) {
						// ���û�״̬�޸�Ϊ����״̬����false̬
						UserStateMessage userStateMessage = new UserStateMessage(localUserName, " ", false);
						// ��"��Ϣ��¼"�ı����ú�ɫ��д���߳ɹ���Ϣ
						String msgRecord = dateFormat.format(new Date()) + " ���߳ɹ���";
						FontStyle.addMsgRecord(textPaneMsgRecord, msgRecord, Color.red, 12, false, false);
						try {
							synchronized (oos) {
								oos.writeObject(userStateMessage);
								oos.flush();
							}
							// �û��˳�������û��б��е�����
							onlineUserDlm.clear();
							btnLogin.setText("��¼");
							System.exit(0);
						} catch (IOException e1) {
							e1.printStackTrace();
						}
					}
				}
			}
		});
		
		label = new JLabel("\u8BBE\u7F6E\uFF1A");
		label.setFont(new Font("���Ŀ���", Font.BOLD, 14));
		panelNorth.add(label);
		
		comboBoxSetting = new JComboBox();
		comboBoxSetting.addItemListener(e-> {
			if (e.getStateChange() == ItemEvent.SELECTED) {
				int index = comboBoxSetting.getSelectedIndex();
				if (index == 1) {  //�����޸����Ϲ���
					//�ӷ������˰��û���Ϣ���չ�����
					UserBean userBean = getMessageFromServer(localUserName);
					//���޸�ҳ����ʾ���û�����
					ModifyFrame modifyFrame = new ModifyFrame(this);
					// ʹ���ڿɼ�
					modifyFrame.setVisible(true);
					modifyFrame.setSize(new Dimension(417, 307));
					modifyFrame.showUserMessage(userBean);
				}else if (index == 2) {  //�������ͷ����
					System.out.println("ͷ�����");
				}
			}
		});
		comboBoxSetting.setModel(new DefaultComboBoxModel(new String[] {"", "\u7F16\u8F91\u8D44\u6599", "\u8BBE\u7F6E\u5934\u50CF"}));
		panelNorth.add(comboBoxSetting);
		
		horizontalStrut_1 = Box.createHorizontalStrut(20);
		panelNorth.add(horizontalStrut_1);
		
		btnLogin.setFont(new Font("����", Font.BOLD, 15));
		panelNorth.add(btnLogin);

		Component horizontalStrut_5 = Box.createHorizontalStrut(20);
		panelNorth.add(horizontalStrut_5);

		btnRegister = new JButton("\u6CE8\u518C");
		btnRegister.addActionListener(e -> {
			RegisterFrame registerFrame = new RegisterFrame(this);
			// ʹ���ڿɼ�
			registerFrame.setVisible(true);
			registerFrame.setSize(new Dimension(417, 307));
		});
		btnRegister.setFont(new Font("����", Font.BOLD, 15));
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
		chckbxPrivate.setFont(new Font("���Ŀ���", Font.BOLD, 14));
		panelSouth.add(chckbxPrivate);
		
		chckbxPicture = new JCheckBox("\u56FE\u7247");
		chckbxPicture.setFont(new Font("���Ŀ���", Font.BOLD, 14));
		panelSouth.add(chckbxPicture);

		Component horizontalStrut_2 = Box.createHorizontalStrut(20);
		panelSouth.add(horizontalStrut_2);

		btnSendMsg = new JButton("\u53D1\u9001\u6D88\u606F");
		btnSendMsg.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String msgContent = textFieldMsgToSend.getText();
				// ��Ϣ��������Ϣ�ٽ��з���
				if (msgContent.length() > 0) {
					// ���˽��ѡ�ѡ�������б���ѡ�����ˣ��򹹽�һ��˽����Ϣ
					if (chckbxPrivate.isSelected() && !listOnlineUsers.isSelectionEmpty()) {
						// ��ȡ˽����Ϣ�Ľ�����
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
						// �ڡ���Ϣ��¼���ı���������ɫ��ʾ���͵���Ϣ������ʱ��
						String msgRecord = dateFormat.format(new Date()) + " ��" + dstUser + "˵��" + msgContent + "\r\n";
						FontStyle.addMsgRecord(textPaneMsgRecord, msgRecord, Color.BLUE, 12, false, false);
					} else {
						// ����һ��������Ϣ��ʵ�ֹ��ģ��㲥�������û�
						ChatMessage chatMessage = new ChatMessage(localUserName, "", msgContent);
						try {
							synchronized (oos) {
								oos.writeObject(chatMessage);
								oos.flush();
							}
						} catch (IOException e1) {
							e1.printStackTrace();
						}
						// �ڡ���Ϣ��¼���ı���������ɫ��ʾ���͵���Ϣ������ʱ��
						String msgRecord = dateFormat.format(new Date()) + " ����˵��" + msgContent + "\r\n";
						FontStyle.addMsgRecord(textPaneMsgRecord, msgRecord, Color.BLUE, 12, false, false);
					}
				} else if (chckbxPicture.isSelected()) { // ����ͼƬ��Ϣ
					JFileChooser chooser = new JFileChooser("�˵���");
					chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
					// ��ӹ�������ֻѡ��ͼƬ
					FileNameExtensionFilter filter = new FileNameExtensionFilter("ͼƬ(jpg,gif,png����)", "gif", "jpg","png","ico","webp","bmp");
					chooser.setFileFilter(filter);
					StyledDocument doc =  textPaneMsgRecord.getStyledDocument();
					// �ж���˽�ķ�����Ƭ���ǹ��ķ�����Ƭ
					if (chckbxPrivate.isSelected() && !listOnlineUsers.isSelectionEmpty()) { // ��˽�ķ�ͼƬ
						// ��ȡ˽����Ϣ�Ľ�����
						String dstUser = (String) listOnlineUsers.getSelectedValue();
						String msgRecord = dateFormat.format(new Date()) + " ��" + dstUser + "����Ƭ��\r\n";
						FontStyle.addMsgRecord(textPaneMsgRecord, msgRecord, Color.BLUE, 12, false, false);
						if (chooser.showOpenDialog(ClientFrame.this) == JFileChooser.APPROVE_OPTION) {
							textPaneMsgRecord.setCaretPosition(doc.getLength()); // ���ò���λ��
							ImageIcon imageIcon = new ImageIcon(chooser.getSelectedFile().toString());
							textPaneMsgRecord.insertIcon(imageIcon);
							//˽�ķ�����Ƭ
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
					} else {    //����ȫ����Ա��ͼƬ��Ϣ
						String msgRecord = dateFormat.format(new Date()) + " ��ȫ�巢��Ƭ��\r\n";
						FontStyle.addMsgRecord(textPaneMsgRecord, msgRecord, Color.BLUE, 12, false, false);
						if (chooser.showOpenDialog(ClientFrame.this) == JFileChooser.APPROVE_OPTION) {
							textPaneMsgRecord.setCaretPosition(doc.getLength()); // ���ò���λ��
							ImageIcon imageIcon = new ImageIcon(chooser.getSelectedFile().toString());
							textPaneMsgRecord.insertIcon(imageIcon);
							//��ȫ���Ա������Ƭ��Ϣ
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
					JOptionPane.showMessageDialog(ClientFrame.this, "����д��Ҫ���͵���Ϣ���ݡ�");
				}
				textFieldMsgToSend.setText("");
			}
		});
		btnSendMsg.setFont(new Font("����", Font.BOLD, 15));
		panelSouth.add(btnSendMsg);

		Component horizontalStrut_3 = Box.createHorizontalStrut(20);
		panelSouth.add(horizontalStrut_3);

		btnSendFile = new JButton("\u53D1\u9001\u6587\u4EF6");
		btnSendFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!listOnlineUsers.isSelectionEmpty()) { // ѡ��Ҫ���͵�Ŀ�꣬������ļ��ķ���
					JFileChooser fileChooser = new JFileChooser("�˵���");
					String dstUser = (String) listOnlineUsers.getSelectedValue();
					// �����û�ѡ���ļ���Ŀ¼
					fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
					if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
						String filePath = fileChooser.getSelectedFile().getAbsolutePath();
//						System.out.println(filePath);
						File file = fileChooser.getSelectedFile();
						// �����ļ�ǰ��׼����������Ҫѯ�ʽ������Ƿ�Ҫ���ո��ļ�
						sendPrepare(file, dstUser, filePath);
					}

				} else { // ���û�з���Ŀ�꣬�������ʾ
					JOptionPane.showMessageDialog(ClientFrame.this, "����ѡ����Ŀ�ꡣ");
				}
			}
		});
		btnSendFile.setFont(new Font("����", Font.BOLD, 15));
		panelSouth.add(btnSendFile);

		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				int i = JOptionPane.showConfirmDialog(ClientFrame.this, "ȷ��Ҫ�˳�ϵͳ��", "�˳�ϵͳ", JOptionPane.YES_NO_OPTION);
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
	 * �ӷ������Ѹ��û�����Ϣ�����չ����ģ�Ȼ������޸�
	 * @return
	 */
	protected UserBean getMessageFromServer(String userName) {
		UserBean userBean = new UserBean();
		userBean.setUserName(userName);
		//���������������
		try {
			SSLContext sslContext = SSLUtil.createClienSSLContext();
			SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
			SSLSocket socket = (SSLSocket) sslSocketFactory.createSocket(localhost, port);
			oos = new ObjectOutputStream(socket.getOutputStream());
			ois = new ObjectInputStream(socket.getInputStream());
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		//����һ���û���ʼ״̬��Ϣ���Ƿ�ɹ���Ϊfalse
		OriginalStateMessage originalStateMessage = new OriginalStateMessage(userName, "ModifyPrepare", userBean, false);
		//����ע����Ϣ��������
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
			if (originalStateMessage.isModifyPreMessage()) {   //�����޸���Ϣ
				if (originalStateMessage.isSucceed()) {
					userBean = originalStateMessage.getUserBean();   //�������ݿ��иø��û���Ϣ
				}else {
					System.out.println("û�з��ظ���Ϣ����");
				}
			}else {
				System.out.println("������Ϣ��������");
			}
		} catch (ClassNotFoundException | IOException e1) {
			e1.printStackTrace();
		}
		return userBean;
	}

	/**
	 * �����ļ�ǰҪ����׼������ ͨ����������ѯ�ʽ������Ƿ���Ҫ���ո��ļ�
	 * 
	 * @param fileName
	 *            �ļ�����
	 * @param dstUser
	 *            ������
	 * @return �������true,���ʾ������Ҫ���գ��������false����ܾ�����
	 */
	protected void sendPrepare(File sendFile, String dstUser, String filePath) {
		String fileName = sendFile.getName();
		// ����Ϣ��¼������ɫ����ʾ�����ļ�������
		String msg = dateFormat.format(new Date()) + " ׼�����û�" + dstUser + "�����ļ���" + fileName + "\r\n";
		FontStyle.addMsgRecord(textPaneMsgRecord, msg, Color.BLUE, 12, false, false);

		try {
			FileInputStream fis = new FileInputStream(sendFile);
			long size = fis.available();

			// ���ռ��˷����ļ�����������Ϣ������һ�������ļ�Э����
			FileBean fileBean = new FileBean();
			fileBean.setFileName(fileName);
			fileBean.setFileSize(size);
			fileBean.setFilePath(filePath);
			FileStateMessage fileSendStateMessage = new FileStateMessage(localUserName, dstUser, fileBean, false, "����");
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
	 * ��̨�����߳�
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
					if (message instanceof UserStateMessage) { // ������û�״̬��Ϣ
						processUserStateMessage((UserStateMessage) message);

					} else if (message instanceof ChatMessage) { // �����������Ϣ
						processChatMessage((ChatMessage) message);

					} else if (message instanceof FileStateMessage) {   //�����ļ��������Ӧ��Ϣ
						processFileStateMessage((FileStateMessage)message);
					} else if (message instanceof ImageMessage) {    //����ͼƬ��Ϣ
						processImageMessage((ImageMessage)message);
					}else {
						// ��ζ�յ��˴����ʽ����Ϣ
						JOptionPane.showMessageDialog(ClientFrame.this, "��Ϣ��ʽ����ȷ��");

					}
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		/**
		 * ����ͼƬ��Ϣ
		 * @param message
		 */
		private void processImageMessage(ImageMessage message) {
			String srcUser = message.getSrcUser();
			ImageIcon imageIcon = message.getImageIcon();
			if (onlineUserDlm.contains(srcUser)) {
				// ����ǹ��ķ�����Ƭ
				if (message.isPublicMessage()) {
					String msg = dateFormat.format(new Date()) + " " + srcUser + " ��ȫ�巢��Ƭ��\r\n";
					FontStyle.addMsgRecord(textPaneMsgRecord, msg, Color.BLACK, 12, false, false);
					StyledDocument doc = textPaneMsgRecord.getStyledDocument();
					textPaneMsgRecord.setCaretPosition(doc.getLength()); // ���ò���λ��
					textPaneMsgRecord.insertIcon(imageIcon);
					try {
						doc.insertString(doc.getLength(), "\r\n", null);
					} catch (BadLocationException e1) {
						e1.printStackTrace();
					}
				} else {
					// ����Ϣ��˽����Ϣ
					String msg = dateFormat.format(new Date()) + " " + srcUser + " ����������Ƭ��\r\n";
					FontStyle.addMsgRecord(textPaneMsgRecord, msg, Color.BLACK, 12, false, false);
					StyledDocument doc = textPaneMsgRecord.getStyledDocument();
					textPaneMsgRecord.setCaretPosition(doc.getLength()); // ���ò���λ��
					textPaneMsgRecord.insertIcon(imageIcon);
					try {
						doc.insertString(doc.getLength(), "\r\n", null);
					} catch (BadLocationException e1) {
						e1.printStackTrace();
					}
				}
			} else {
				System.out.println("����Ϣ��������Ϣ��");
			}
		}
		/**
		 * �����ļ�����������Ϣ���ļ���Ӧ������Ϣ
		 * @param message
		 */
		private void processFileStateMessage(FileStateMessage message) {
			String srcName = message.getSrcUser();
			String dstName = message.getDstUser();
			FileBean fileBean = message.getFileBean();
			String fileName = fileBean.getFileName();
			long fileSize = fileBean.getFileSize();
			if (message.isRequest()) {   //���ļ�����������Ϣ,����Ҫ�û��������ж�
				
				int i = JOptionPane.showConfirmDialog(ClientFrame.this, "�û�" + srcName + "�������ļ���\r\n" 
				                                                      + "�ļ�����" + fileName + "\r\n" 
				                                                      + "�ļ���С�� " + fileSize + "\r\n"
				                                                      + "���Ƿ�Ҫ���գ�" + "\r\n"
				                                                      , "�û�" + dstName, JOptionPane.YES_NO_OPTION);
				if (i == JOptionPane.YES_OPTION) {    //�����ߴ�����Ҫ���ո��ļ���������Ϣ
					message.setType("��Ӧ");   //������֮��
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
						//����һ�������ļ����߳�
						SSLContext context = SSLUtil.createServerSSLContext();
						SSLServerSocketFactory factory = context.getServerSocketFactory();
						SSLServerSocket serverSocket = (SSLServerSocket) factory.createServerSocket(8888);
						SSLSocket socket = (SSLSocket) serverSocket.accept();
						
						String msg = dateFormat.format(new Date()) + " ��׼������" + srcName + "���������ļ���" + fileName + "\r\n";
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
				}else {   //�ܾ����ո��ļ�
					message.setType("��Ӧ");
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
			} else if (message.isResponse()) {   //�������Ӧ��Ϣ
				if (message.isState()) {     //������ͬ����ո��ļ��������ߴ���һ�������ļ����߳��������ļ�
					try {
						//����һ�������ļ����߳�   
						SSLContext sslContext = SSLUtil.createClienSSLContext();
						SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
						SSLSocket socket = (SSLSocket) sslSocketFactory.createSocket(fileBean.getAddress(), 8888);
						
						//����������
						System.out.println("�ļ���СΪ��" + fileSize);
						progressBar.setStringPainted(true);
						
						//���������ļ�����
						SendActivity sendActivity = new SendActivity(socket, fileBean.getFilePath(),fileSize,dstName,fileName);
						sendActivity.execute();
						
					} catch (IOException e) {
						e.printStackTrace();
					}
				}else {    //�����ߴ���ܾ������ļ�����Ӧ��Ϣ
					String msg = dateFormat.format(new Date()) + " �û�" + dstName + "�ܾ������ļ���" + fileName + "������"
							+ "\r\n";
					FontStyle.addMsgRecord(textPaneMsgRecord, msg, Color.blue, 12, false, false);
				}
			} else {
				System.out.println("����Ϣ�����ļ���Ϣ����");
				return;
			}
		}

		/**
		 * �����û�״̬��Ϣ�ĺ��� �ж��û�״̬�����߻�������
		 */
		public void processUserStateMessage(UserStateMessage userStateMessage) {
			String srcUser = userStateMessage.getSrcUser();
			String dstUser = userStateMessage.getDstUser();
			if (userStateMessage.isUserOnline()) {
				if (userStateMessage.isPubUserStateMessage()) { // �����û�����
					// ����ɫ���ֽ��û������û�����ʱ����ӵ�����Ϣ��¼���ı�����
					String msg = dateFormat.format(new Date()) + " " + srcUser + " ������!" + "\r\n";
					FontStyle.addMsgRecord(textPaneMsgRecord, msg, Color.GREEN, 12, false, false);
					onlineUserDlm.addElement(srcUser);
				}
				if (dstUser.equals(localUserName)) { // �յ����������û�����Ϣ
					onlineUserDlm.addElement(srcUser);
				}
			} else { // �����û�������Ϣ
				if (onlineUserDlm.contains(srcUser)) {
					// ����ɫ���ֽ��û������û�����ʱ����ӵ�����Ϣ��¼���ı�����
					String msg = dateFormat.format(new Date()) + " " + srcUser + " ������!" + "\r\n";
					FontStyle.addMsgRecord(textPaneMsgRecord, msg, Color.GREEN, 12, false, false);
					onlineUserDlm.removeElement(srcUser);
				} else {
					System.out.println("������");
				}
			}
		}

		/**
		 * ����������Ϣ�ĺ��� �ж��ǹ��Ļ���˽��
		 */
		public void processChatMessage(ChatMessage chatMessage) {
			String srcUser = chatMessage.getSrcUser();
			String msgContent = chatMessage.getMsgContent();

			if (onlineUserDlm.contains(srcUser)) {
				// ����ǹ�����Ϣ
				if (chatMessage.isPublicMessage()) {
					// �ú�ɫ���ֽ��յ���Ϣ��ʱ�䡢������Ϣ���û�������Ϣ������ӵ�����Ϣ��¼���ı�����
					String msg = dateFormat.format(new Date()) + " " + srcUser + " �Դ��˵��" + msgContent + "\r\n";
					FontStyle.addMsgRecord(textPaneMsgRecord, msg, Color.BLACK, 12, false, false);
				} else {
					// ����Ϣ��˽����Ϣ
					String msg = dateFormat.format(new Date()) + " " + srcUser + " ˵��" + msgContent + "\r\n";
					FontStyle.addMsgRecord(textPaneMsgRecord, msg, Color.BLACK, 12, false, false);
				}
			} else {
				System.out.println("����Ϣ��������Ϣ��");
			}
		}
	}
	
	/**
	 * �����ļ�������ʾ��������״̬
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
			progressMonitor = new ProgressMonitor(ClientFrame.this, "Waiting for a moment����", null, 0, 100);
			System.out.println("��ʼ�����ļ�");
		}
		@Override
		protected Void doInBackground() throws Exception {
			try {
				// ѡ���ļ�����Ŀ¼
				JFileChooser jFileChooser = new JFileChooser("�˵���");
				jFileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				if (jFileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
					String path = jFileChooser.getSelectedFile().getPath();
					System.out.println(path + fileName);
					File file = new File(path + fileName);
					// �������ص��ļ������������������յ����ļ�����
					FileOutputStream fos = new FileOutputStream(file);
					// ���û�������С
					byte[] buff = new byte[4096];

					// �����ļ����������������մ�����������
					InputStream is = socket.getInputStream();
					// ����ÿ�ν��յ�������
					int size = 0;
					// �����ѽ��յ����ļ���С
					long fileCount = 0;
					while (fileCount < fileSize) {
						// ÿ�ζ����Ĵ�С
						size = is.read(buff);

						// ������������д�뱾���ļ�����
						fos.write(buff, 0, size);
						fos.flush();
						fileCount += size;
						setProgress((int) ((fileCount*100)/fileSize));
					}
					System.out.println("�������");
					String msg = dateFormat.format(new Date()) + " �û�" + srcName + "�������ļ���" + fileName + "�ѽ�����ϡ�\r\n";
					FontStyle.addMsgRecord(textPaneMsgRecord, msg, Color.red, 12, false, false);
					fos.close();
					is.close();
				}
			} catch (HeadlessException e) {
				e.printStackTrace();
			}catch (FileNotFoundException e) {
				System.err.println("������д�ļ�ʧ�ܡ�");
			} catch (IOException e) {
				System.err.println("��ͻ��˶Ͽ����ӡ�");
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
	 * �����ļ��̣߳�������ʾ��������״̬
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
			System.out.println("��ʼ�����ļ�");
		}
		@Override
		protected Void doInBackground() throws Exception {
			try {
				System.out.println(filePath);
				FileInputStream fis = new FileInputStream(new File(filePath));
				byte[] buff = new byte[4096];
				//����������������ݰ��������
				OutputStream os = socket.getOutputStream();
				//��¼ÿ�ζ��ļ��Ĵ�С
				int size = 0;
				//��¼�Ѷ��ļ��Ĵ�С
				long fileCount = 0;
				//ʹ��whileѭ����ȡ�ļ���ֱ���ļ���ȡ����
				while((size = fis.read(buff)) != -1){
				    //���������д��ոն��������ݰ�
				    os.write(buff, 0, size);
				    os.flush();
				    
				    fileCount += size;
				    publish(fileCount);
				}
				String msg = dateFormat.format(new Date()) + " �����û�" + dstName + "�����������ļ���" + fileName + "\r\n";
				FontStyle.addMsgRecord(textPaneMsgRecord, msg, Color.red, 12, false, false);
				fis.close();
				os.close();
			} catch (FileNotFoundException e) {
				System.err.println("�ͻ��˶��ļ��ļ�ʧ�ܡ�");
			} catch (IOException e) {
				System.err.println("��������Ͽ����ӡ�");
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
