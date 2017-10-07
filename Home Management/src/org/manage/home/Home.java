package org.manage.home;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Observable;
import java.util.Observer;
import java.util.Properties;

import javax.imageio.ImageIO;
import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.event.ConnectionEvent;
import javax.mail.event.ConnectionListener;
import javax.mail.event.TransportEvent;
import javax.mail.event.TransportListener;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;

import org.manage.home.mail.Cred;
import org.manage.home.mail.Mailer;
import org.manage.home.mail.Messages;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.Color;
import javax.swing.SwingConstants;
import java.awt.SystemColor;
import javax.swing.UIManager;
import javax.swing.border.BevelBorder;

public class Home implements ConnectionListener, TransportListener, Observer {

	private JFrame frame;
	private JTextField textVideo;
	private JTextField textImage;
	private JTextField textFileName;
	private JTextField textQuality;
	private JLabel lblResponse;
	private JTextArea textArea;
	private JLabel lblImage;
	@SuppressWarnings("unused")
	private ClipboardScanner clipboardScanner;
	private static Home window;
	private JTextField txtPrefix;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					window = new Home();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

	}

	/**
	 * Create the application.
	 */
	public Home() {
		initialize();

		clipboardScanner = new ClipboardScanner();
		clipboardScanner.addObserver(this);
		new Thread(clipboardScanner).start();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 1280, 741);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JButton btnClose = new JButton("Close");
		btnClose.setBounds(1163, 660, 89, 23);
		btnClose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				frame.dispose();
			}
		});
		frame.getContentPane().setLayout(null);
		frame.getContentPane().add(btnClose);

		JLabel lblHomeManagementSystem = new JLabel("Home Management System");
		lblHomeManagementSystem.setBounds(10, 11, 270, 14);
		frame.getContentPane().add(lblHomeManagementSystem);

		JPanel panel = new JPanel();
		panel.setBounds(10, 30, 1242, 619);
		frame.getContentPane().add(panel);
		panel.setLayout(null);

		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBounds(0, 0, 666, 407);
		panel.add(tabbedPane);

		JPanel panel_2 = new JPanel();
		tabbedPane.addTab("Entertainment", null, panel_2, null);
		panel_2.setLayout(null);

		JLabel lblVideoUrl = new JLabel("Video URL");
		lblVideoUrl.setBounds(10, 25, 121, 14);
		panel_2.add(lblVideoUrl);

		JLabel lblImageUrl = new JLabel("Image ID");
		lblImageUrl.setBounds(10, 50, 108, 14);
		panel_2.add(lblImageUrl);

		JLabel lblFileName = new JLabel("File Name");
		lblFileName.setBounds(10, 75, 121, 14);
		panel_2.add(lblFileName);

		JLabel lblQualityFactor = new JLabel("Quality Factor");
		lblQualityFactor.setBounds(10, 100, 121, 14);
		panel_2.add(lblQualityFactor);

		textVideo = new JTextField();
		textVideo.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent arg0) {

				try {
					//searchVideo();

				} catch (Exception e) {
					textArea.setText(e.getMessage());
					e.printStackTrace();
				}

				grabTheVideoToken();

			}
		});
		textVideo.setBounds(141, 22, 515, 20);
		panel_2.add(textVideo);
		textVideo.setColumns(10);

		textImage = new JTextField();
		textImage.setBounds(141, 47, 515, 20);
		panel_2.add(textImage);
		textImage.setColumns(10);

		textFileName = new JTextField();
		textFileName.addFocusListener(new FocusAdapter() {

			@Override
			public void focusLost(FocusEvent arg0) {
				textFileName.setText(textFileName.getText().replaceAll(" ", "_"));
			}
		});
		textFileName.setBounds(285, 72, 254, 20);
		panel_2.add(textFileName);
		textFileName.setColumns(10);

		JButton btnTemplate = new JButton("Template");
		btnTemplate.setBounds(551, 71, 105, 23);
		panel_2.add(btnTemplate);

		textQuality = new JTextField();
		textQuality.setBounds(141, 97, 86, 20);
		panel_2.add(textQuality);
		textQuality.setColumns(10);

		JButton btnSendToQueue = new JButton("Send to Queue");
		btnSendToQueue.setBounds(141, 128, 136, 23);
		btnSendToQueue.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				textArea.setText("");
				Properties props = new Properties();
				props.put("mail.smtp.host", Messages.mailer_smtp_host);
				props.put("mail.smtp.socketFactory.port", Messages.mailer_smtp_socket_port);
				props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
				props.put("mail.smtp.auth", Messages.mailer_stmp_auth);
				props.put("mail.smtp.port", Messages.mailer_smtp_port);

				Session session = Session.getDefaultInstance(props, new javax.mail.Authenticator() {
					protected PasswordAuthentication getPasswordAuthentication() {
						return new PasswordAuthentication(Cred.UN, Cred.PWD);
					}
				});

				String command = "1";
				String videoUrl = textVideo.getText();
				String imageUrl = textImage.getText();
				String fileName = txtPrefix.getText() + "_" + textFileName.getText();
				String quality = textQuality.getText();
				Transport trans = null;
				try {
					Message message = new MimeMessage(session);
					message.setFrom(new InternetAddress(Messages.mailer_from));
					message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(Messages.mailer_to));
					message.setSubject("command");
					message.setText("{ \"command\": \"" + command + "\" ," + "\"V\":\"" + videoUrl + "\","
							+ "\"P\":\"https://i.ytimg.com/vi/" + imageUrl + "/hqdefault.jpg\" ," + "\"F\":\""
							+ fileName + "\",\"Q\":\"" + quality + "\"}");

					// Transport.send(message);
					message.saveChanges();

					trans = session.getTransport(session.getProvider("smtp"));
					trans.addConnectionListener(window);
					trans.addTransportListener(window);

					trans.connect();
					Address[] addresses = new Address[1];
					addresses[0] = new InternetAddress(Messages.mailer_to);
					trans.sendMessage(message, addresses);
					trans.close();

					textVideo.setText("");
					textImage.setText("");
					textFileName.setText("");
					textQuality.setText("");
					textArea.setText("Queued:" +  fileName);

				} catch (NoSuchProviderException e) {
					// TODO Auto-generated catch block
					textArea.setText(e.getMessage());
					e.printStackTrace();
				} catch (AddressException e) {
					// TODO Auto-generated catch block
					textArea.setText(e.getMessage());
					e.printStackTrace();
				} catch (MessagingException e) {
					// TODO Auto-generated catch block
					textArea.setText(e.getMessage());
					e.printStackTrace();
				} finally {
				}

			}
		});
		panel_2.add(btnSendToQueue);

		lblResponse = new JLabel("Details");
		lblResponse.setBounds(10, 162, 646, 97);
		panel_2.add(lblResponse);
		
		txtPrefix = new JTextField();
		txtPrefix.setBounds(141, 73, 136, 19);
		panel_2.add(txtPrefix);
		txtPrefix.setColumns(10);

		JPanel panel_1 = new JPanel();
		tabbedPane.addTab("General", null, panel_1, null);

		JPanel panel_3 = new JPanel();
		tabbedPane.addTab("Configuration", null, panel_3, null);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(676, 21, 556, 587);
		panel.add(scrollPane);

		textArea = new JTextArea();
		scrollPane.setViewportView(textArea);
		textArea.setWrapStyleWord(true);
		textArea.setBackground(Color.BLACK);
		textArea.setForeground(Color.GREEN);
		
		JPanel panel_4 = new JPanel();
		panel_4.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		panel_4.setBounds(10, 409, 285, 199);
		panel.add(panel_4);
		panel_4.setLayout(null);
		
		lblImage = new JLabel("");
		lblImage.setBounds(0, 0, 285, 199);
		panel_4.add(lblImage);
		lblImage.setForeground(UIManager.getColor("Button.disabledShadow"));
		lblImage.setHorizontalAlignment(SwingConstants.CENTER);
		lblImage.setBackground(SystemColor.window);
	}

	protected void searchVideo() {
		VideoInformationExtractor extractor = new VideoInformationExtractor(textVideo.getText());
		extractor.addObserver(this);
		new Thread(extractor).start();
		
		FileNameExtractor fileNameExtractor = new FileNameExtractor(textVideo.getText());
		fileNameExtractor.addObserver(this);
		new Thread(fileNameExtractor).start();;

		textFileName.setText("");
	}

	protected void grabTheVideoToken() {
		try {
			String key = textVideo.getText().split("=")[1];
			textImage.setText(key);
		} catch (Exception e) {
			// Print the error, is not required here
		}

	}

	@Override
	public void messageDelivered(TransportEvent arg0) {
		lblResponse.setText("Delivered");
	}

	@Override
	public void messageNotDelivered(TransportEvent arg0) {
		lblResponse.setText("Not Delivered");
	}

	@Override
	public void messagePartiallyDelivered(TransportEvent arg0) {
		lblResponse.setText("Partially Delivered");
	}

	@Override
	public void closed(ConnectionEvent arg0) {
		lblResponse.setText("Connection Closed");
	}

	@Override
	public void disconnected(ConnectionEvent arg0) {
		lblResponse.setText("Disconnected");
	}

	@Override
	public void opened(ConnectionEvent arg0) {
		lblResponse.setText("Delivered");
	}

	@Override
	public void update(Observable o, Object arg) {
		if (o != null) {
			if (o instanceof ClipboardScanner) {
				textVideo.setText(((ClipboardScanner) o).getUrl());
				grabTheVideoToken();
				searchVideo();
			} else if (o instanceof VideoInformationExtractor) {
				textArea.setText(((VideoInformationExtractor) o).getOutput().toString());
			} else if (o instanceof FileNameExtractor) {
				textFileName.setText(removeSpecialChars(((FileNameExtractor) o).getOutput().toString().trim().replaceAll(" ", "_")));
			}
		}
		
		try {
			String path = "http://i.ytimg.com/vi/"+ textImage.getText().trim() + "/hqdefault.jpg";
			URL imageUrl = new URL(path);
			BufferedImage image = ImageIO.read(imageUrl);
			lblImage.setIcon(new ImageIcon(new Util().getScaledImage(image, 280, 195)));
			lblImage.repaint();
		} catch (IOException e) {
			textArea.setText(e.getMessage());
		}
	}
	
	private String removeSpecialChars(String name){
		String specialChars[] = {"&", ";", "|", "\\*", "\\?", "\'", "\"", "'", "\\[", "\\]", "\\(", "\\)", "$", "<", ">", "\\{", "\\}", "#", "/", "!", "~"};
		for(String specialStr : specialChars){

			name = name.replaceAll(specialStr, "");
		}
		return name;
	}
}
