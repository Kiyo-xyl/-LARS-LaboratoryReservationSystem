package lars.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import lars.controller.LoginController;

/**
 * AdminLoginForm screen.
 * The layout code in the constructor can be edited in WindowBuilder (Design tab).
 * The real work is done by LoginController - this class only collects input and shows messages.
 */
public class AdminLoginForm extends JFrame {

	private static final long serialVersionUID = 1L;

	private JPanel contentPane;
	private JPanel pnlBrand;
	private JPanel pnlForm;
	private JPanel pnlSwitchRow;
	private JLabel lblIcon;
	private JLabel lblBrandName;
	private JLabel lblBrandTagline;
	private JLabel lblBrandNote;
	private JLabel lblWelcome;
	private JLabel lblInstruction;
	private JLabel lblUsername;
	private JLabel lblPassword;
	private JLabel lblSwitchText;
	private JTextField txtAdminUsername;
	private JPasswordField pwdAdminPassword;
	private JButton btnAdminLogin;
	private JButton btnBackToStudentLogin;

	private final LoginController loginController = new LoginController();

	public AdminLoginForm() {
		setTitle("LARS - Administrator Login");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(900, 520);
		setResizable(false);
		setLocationRelativeTo(null);

		contentPane = new JPanel();
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		// ================= left side: LARS branding =================
		pnlBrand = new JPanel();
		pnlBrand.setBackground(Theme.NAVY);
		pnlBrand.setPreferredSize(new Dimension(380, 0));
		pnlBrand.setLayout(new GridBagLayout());
		contentPane.add(pnlBrand, BorderLayout.WEST);

		lblIcon = new JLabel(new ComputerIcon(110, Color.WHITE));

		lblBrandName = new JLabel("LARS");
		lblBrandName.setFont(Theme.FONT_TITLE);
		lblBrandName.setForeground(Theme.WHITE);
		lblBrandName.setFont(Theme.FONT_TITLE.deriveFont(44f));

		lblBrandTagline = new JLabel("Lab Reservation System");
		lblBrandTagline.setFont(Theme.FONT_HEADING);
		lblBrandTagline.setForeground(Theme.WHITE);

		lblBrandNote = new JLabel("Administrator portal");
		lblBrandNote.setFont(Theme.FONT_BODY);
		lblBrandNote.setForeground(Theme.UNAVAILABLE_GRAY);

		GridBagConstraints gbc_lblIcon = new GridBagConstraints();
		gbc_lblIcon.anchor = GridBagConstraints.CENTER;
		gbc_lblIcon.insets = new Insets(0, 0, 16, 0);
		gbc_lblIcon.gridx = 0;
		gbc_lblIcon.gridy = 0;
		pnlBrand.add(lblIcon, gbc_lblIcon);

		GridBagConstraints gbc_lblBrandName = new GridBagConstraints();
		gbc_lblBrandName.anchor = GridBagConstraints.CENTER;
		gbc_lblBrandName.insets = new Insets(0, 0, 4, 0);
		gbc_lblBrandName.gridx = 0;
		gbc_lblBrandName.gridy = 1;
		pnlBrand.add(lblBrandName, gbc_lblBrandName);

		GridBagConstraints gbc_lblBrandTagline = new GridBagConstraints();
		gbc_lblBrandTagline.anchor = GridBagConstraints.CENTER;
		gbc_lblBrandTagline.insets = new Insets(0, 0, 12, 0);
		gbc_lblBrandTagline.gridx = 0;
		gbc_lblBrandTagline.gridy = 2;
		pnlBrand.add(lblBrandTagline, gbc_lblBrandTagline);

		GridBagConstraints gbc_lblBrandNote = new GridBagConstraints();
		gbc_lblBrandNote.anchor = GridBagConstraints.CENTER;
		gbc_lblBrandNote.gridx = 0;
		gbc_lblBrandNote.gridy = 3;
		pnlBrand.add(lblBrandNote, gbc_lblBrandNote);

		// ================= right side: the form =================
		pnlForm = new JPanel();
		pnlForm.setBackground(Theme.WHITE);
		pnlForm.setBorder(new EmptyBorder(40, 60, 40, 60));
		pnlForm.setLayout(new GridBagLayout());
		contentPane.add(pnlForm, BorderLayout.CENTER);

		lblWelcome = new JLabel("Administrator Login");
		lblWelcome.setFont(Theme.FONT_TITLE);
		lblWelcome.setForeground(Theme.TEXT_DARK);

		lblInstruction = new JLabel("Manage computers, schedules and reservations.");
		lblInstruction.setFont(Theme.FONT_BODY);
		lblInstruction.setForeground(Theme.TEXT_MUTED);

		lblUsername = new JLabel("Administrator Username");
		lblUsername.setFont(Theme.FONT_LABEL);
		lblUsername.setForeground(Theme.TEXT_DARK);

		txtAdminUsername = new JTextField();
		txtAdminUsername.setColumns(20);
		Theme.styleTextField(txtAdminUsername);

		lblPassword = new JLabel("Password");
		lblPassword.setFont(Theme.FONT_LABEL);
		lblPassword.setForeground(Theme.TEXT_DARK);

		pwdAdminPassword = new JPasswordField();
		pwdAdminPassword.setColumns(20);
		Theme.styleTextField(pwdAdminPassword);

		btnAdminLogin = new JButton("Login as Administrator");
		Theme.stylePrimaryButton(btnAdminLogin);
		getRootPane().setDefaultButton(btnAdminLogin); // pressing Enter clicks this button

		pnlSwitchRow = new JPanel();
		pnlSwitchRow.setBackground(Theme.WHITE);
		pnlSwitchRow.setLayout(new FlowLayout(FlowLayout.CENTER, 4, 0));
		lblSwitchText = new JLabel("Not an administrator?");
		lblSwitchText.setFont(Theme.FONT_BODY);
		lblSwitchText.setForeground(Theme.TEXT_MUTED);
		pnlSwitchRow.add(lblSwitchText);
		btnBackToStudentLogin = new JButton("Student Login");
		Theme.styleLinkButton(btnBackToStudentLogin);
		pnlSwitchRow.add(btnBackToStudentLogin);

		GridBagConstraints gbc_lblWelcome = new GridBagConstraints();
		gbc_lblWelcome.anchor = GridBagConstraints.WEST;
		gbc_lblWelcome.insets = new Insets(0, 0, 4, 0);
		gbc_lblWelcome.gridx = 0;
		gbc_lblWelcome.gridy = 0;
		pnlForm.add(lblWelcome, gbc_lblWelcome);

		GridBagConstraints gbc_lblInstruction = new GridBagConstraints();
		gbc_lblInstruction.anchor = GridBagConstraints.WEST;
		gbc_lblInstruction.insets = new Insets(0, 0, 28, 0);
		gbc_lblInstruction.gridx = 0;
		gbc_lblInstruction.gridy = 1;
		pnlForm.add(lblInstruction, gbc_lblInstruction);

		GridBagConstraints gbc_lblUsername = new GridBagConstraints();
		gbc_lblUsername.anchor = GridBagConstraints.WEST;
		gbc_lblUsername.insets = new Insets(0, 0, 6, 0);
		gbc_lblUsername.gridx = 0;
		gbc_lblUsername.gridy = 2;
		pnlForm.add(lblUsername, gbc_lblUsername);

		GridBagConstraints gbc_txtAdminUsername = new GridBagConstraints();
		gbc_txtAdminUsername.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtAdminUsername.anchor = GridBagConstraints.WEST;
		gbc_txtAdminUsername.insets = new Insets(0, 0, 16, 0);
		gbc_txtAdminUsername.weightx = 1.0;
		gbc_txtAdminUsername.gridx = 0;
		gbc_txtAdminUsername.gridy = 3;
		pnlForm.add(txtAdminUsername, gbc_txtAdminUsername);

		GridBagConstraints gbc_lblPassword = new GridBagConstraints();
		gbc_lblPassword.anchor = GridBagConstraints.WEST;
		gbc_lblPassword.insets = new Insets(0, 0, 6, 0);
		gbc_lblPassword.gridx = 0;
		gbc_lblPassword.gridy = 4;
		pnlForm.add(lblPassword, gbc_lblPassword);

		GridBagConstraints gbc_pwdAdminPassword = new GridBagConstraints();
		gbc_pwdAdminPassword.fill = GridBagConstraints.HORIZONTAL;
		gbc_pwdAdminPassword.anchor = GridBagConstraints.WEST;
		gbc_pwdAdminPassword.insets = new Insets(0, 0, 24, 0);
		gbc_pwdAdminPassword.weightx = 1.0;
		gbc_pwdAdminPassword.gridx = 0;
		gbc_pwdAdminPassword.gridy = 5;
		pnlForm.add(pwdAdminPassword, gbc_pwdAdminPassword);

		GridBagConstraints gbc_btnAdminLogin = new GridBagConstraints();
		gbc_btnAdminLogin.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnAdminLogin.anchor = GridBagConstraints.WEST;
		gbc_btnAdminLogin.insets = new Insets(0, 0, 16, 0);
		gbc_btnAdminLogin.weightx = 1.0;
		gbc_btnAdminLogin.gridx = 0;
		gbc_btnAdminLogin.gridy = 6;
		pnlForm.add(btnAdminLogin, gbc_btnAdminLogin);

		GridBagConstraints gbc_pnlSwitchRow = new GridBagConstraints();
		gbc_pnlSwitchRow.fill = GridBagConstraints.HORIZONTAL;
		gbc_pnlSwitchRow.anchor = GridBagConstraints.WEST;
		gbc_pnlSwitchRow.insets = new Insets(0, 0, 4, 0);
		gbc_pnlSwitchRow.weightx = 1.0;
		gbc_pnlSwitchRow.gridx = 0;
		gbc_pnlSwitchRow.gridy = 7;
		pnlForm.add(pnlSwitchRow, gbc_pnlSwitchRow);

		// ================= button actions (only call methods; the logic is below) =================
		btnAdminLogin.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				handleLogin();
			}
		});

		btnBackToStudentLogin.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				openStudentLogin();
			}
		});

	}

	// ---------------------------------------------------------------- logic

	/** Asks LoginController to check the administrator credentials, then opens the admin dashboard. */
	private void handleLogin() {
		try {
			loginController.loginAdmin(txtAdminUsername.getText(), new String(pwdAdminPassword.getPassword()));
			new AdminDashboard().setVisible(true);
			dispose();
		} catch (IllegalArgumentException ex) {
			JOptionPane.showMessageDialog(this, ex.getMessage(), "Login Failed", JOptionPane.WARNING_MESSAGE);
			pwdAdminPassword.setText("");
		}
	}

	private void openStudentLogin() {
		new LoginForm().setVisible(true);
		dispose();
	}
}