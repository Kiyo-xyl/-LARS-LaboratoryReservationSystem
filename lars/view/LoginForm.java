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
 * LoginForm screen.
 * The layout code in the constructor can be edited in WindowBuilder (Design tab).
 * The real work is done by LoginController - this class only collects input and shows messages.
 */
public class LoginForm extends JFrame {

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
	private JTextField txtUsernameOrId;
	private JPasswordField pwdPassword;
	private JButton btnLogin;
	private JButton btnSignUp;
	private JButton btnAdminLogin;

	private final LoginController loginController = new LoginController();

	public LoginForm() {
		setTitle("LARS - Student Login");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(900, 560);
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

		lblBrandNote = new JLabel("Reserve a computer in COMLAB or ILAB");
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

		lblWelcome = new JLabel("Welcome back");
		lblWelcome.setFont(Theme.FONT_TITLE);
		lblWelcome.setForeground(Theme.TEXT_DARK);

		lblInstruction = new JLabel("Log in to reserve a computer.");
		lblInstruction.setFont(Theme.FONT_BODY);
		lblInstruction.setForeground(Theme.TEXT_MUTED);

		lblUsername = new JLabel("Username or Student ID");
		lblUsername.setFont(Theme.FONT_LABEL);
		lblUsername.setForeground(Theme.TEXT_DARK);

		txtUsernameOrId = new JTextField();
		txtUsernameOrId.setColumns(20);
		Theme.styleTextField(txtUsernameOrId);

		lblPassword = new JLabel("Password");
		lblPassword.setFont(Theme.FONT_LABEL);
		lblPassword.setForeground(Theme.TEXT_DARK);

		pwdPassword = new JPasswordField();
		pwdPassword.setColumns(20);
		Theme.styleTextField(pwdPassword);

		btnLogin = new JButton("Login");
		Theme.stylePrimaryButton(btnLogin);
		getRootPane().setDefaultButton(btnLogin); // pressing Enter clicks this button

		pnlSwitchRow = new JPanel();
		pnlSwitchRow.setBackground(Theme.WHITE);
		pnlSwitchRow.setLayout(new FlowLayout(FlowLayout.CENTER, 4, 0));
		lblSwitchText = new JLabel("Don't have an account?");
		lblSwitchText.setFont(Theme.FONT_BODY);
		lblSwitchText.setForeground(Theme.TEXT_MUTED);
		pnlSwitchRow.add(lblSwitchText);
		btnSignUp = new JButton("Sign Up");
		Theme.styleLinkButton(btnSignUp);
		pnlSwitchRow.add(btnSignUp);

		btnAdminLogin = new JButton("Administrator login");
		Theme.styleLinkButton(btnAdminLogin);

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

		GridBagConstraints gbc_txtUsernameOrId = new GridBagConstraints();
		gbc_txtUsernameOrId.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtUsernameOrId.anchor = GridBagConstraints.WEST;
		gbc_txtUsernameOrId.insets = new Insets(0, 0, 16, 0);
		gbc_txtUsernameOrId.weightx = 1.0;
		gbc_txtUsernameOrId.gridx = 0;
		gbc_txtUsernameOrId.gridy = 3;
		pnlForm.add(txtUsernameOrId, gbc_txtUsernameOrId);

		GridBagConstraints gbc_lblPassword = new GridBagConstraints();
		gbc_lblPassword.anchor = GridBagConstraints.WEST;
		gbc_lblPassword.insets = new Insets(0, 0, 6, 0);
		gbc_lblPassword.gridx = 0;
		gbc_lblPassword.gridy = 4;
		pnlForm.add(lblPassword, gbc_lblPassword);

		GridBagConstraints gbc_pwdPassword = new GridBagConstraints();
		gbc_pwdPassword.fill = GridBagConstraints.HORIZONTAL;
		gbc_pwdPassword.anchor = GridBagConstraints.WEST;
		gbc_pwdPassword.insets = new Insets(0, 0, 24, 0);
		gbc_pwdPassword.weightx = 1.0;
		gbc_pwdPassword.gridx = 0;
		gbc_pwdPassword.gridy = 5;
		pnlForm.add(pwdPassword, gbc_pwdPassword);

		GridBagConstraints gbc_btnLogin = new GridBagConstraints();
		gbc_btnLogin.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnLogin.anchor = GridBagConstraints.WEST;
		gbc_btnLogin.insets = new Insets(0, 0, 16, 0);
		gbc_btnLogin.weightx = 1.0;
		gbc_btnLogin.gridx = 0;
		gbc_btnLogin.gridy = 6;
		pnlForm.add(btnLogin, gbc_btnLogin);

		GridBagConstraints gbc_pnlSwitchRow = new GridBagConstraints();
		gbc_pnlSwitchRow.fill = GridBagConstraints.HORIZONTAL;
		gbc_pnlSwitchRow.anchor = GridBagConstraints.WEST;
		gbc_pnlSwitchRow.insets = new Insets(0, 0, 4, 0);
		gbc_pnlSwitchRow.weightx = 1.0;
		gbc_pnlSwitchRow.gridx = 0;
		gbc_pnlSwitchRow.gridy = 7;
		pnlForm.add(pnlSwitchRow, gbc_pnlSwitchRow);

		GridBagConstraints gbc_btnAdminLogin = new GridBagConstraints();
		gbc_btnAdminLogin.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnAdminLogin.anchor = GridBagConstraints.CENTER;
		gbc_btnAdminLogin.weightx = 1.0;
		gbc_btnAdminLogin.gridx = 0;
		gbc_btnAdminLogin.gridy = 8;
		pnlForm.add(btnAdminLogin, gbc_btnAdminLogin);

		// ================= button actions (only call methods; the logic is below) =================
		btnLogin.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				handleLogin();
			}
		});

		btnSignUp.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				openSignUp();
			}
		});

		btnAdminLogin.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				openAdminLogin();
			}
		});
	}

	// ---------------------------------------------------------------- logic

	/** Asks LoginController to check the credentials, then opens the student dashboard. */
	private void handleLogin() {
		try {
			loginController.loginStudent(txtUsernameOrId.getText(), new String(pwdPassword.getPassword()));
			new StudentDashboard().setVisible(true);
			dispose(); // close the login window
		} catch (IllegalArgumentException ex) {
			JOptionPane.showMessageDialog(this, ex.getMessage(), "Login Failed", JOptionPane.WARNING_MESSAGE);
			pwdPassword.setText("");
		}
	}

	private void openSignUp() {
		new SignUpForm().setVisible(true);
		dispose();
	}

	private void openAdminLogin() {
		new AdminLoginForm().setVisible(true);
		dispose();
	}
}