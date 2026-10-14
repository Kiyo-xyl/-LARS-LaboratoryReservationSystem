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

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import lars.controller.LoginController;

/**
 * Student registration screen.
 * The layout code in the constructor can be edited in WindowBuilder (Design tab).
 * Validation (duplicates, ID format, passwords...) is done by LoginController.registerStudent().
 */
public class SignUpForm extends JFrame {

	private static final long serialVersionUID = 1L;

	private JPanel contentPane;
	private JPanel pnlHeader;
	private JPanel pnlForm;
	private JPanel pnlLoginRow;
	private JLabel lblHeaderIcon;
	private JLabel lblHeaderTitle;
	private JLabel lblHeaderSubtitle;
	private JLabel lblFullName;
	private JLabel lblStudentId;
	private JLabel lblCourse;
	private JLabel lblYearLevel;
	private JLabel lblSection;
	private JLabel lblUsername;
	private JLabel lblPassword;
	private JLabel lblConfirmPassword;
	private JLabel lblHaveAccount;
	private JTextField txtFullName;
	private JTextField txtStudentId;
	private JComboBox<String> cmbCourse;
	private JComboBox<String> cmbYearLevel;
	private JTextField txtSection;
	private JTextField txtUsername;
	private JPasswordField pwdPassword;
	private JPasswordField pwdConfirmPassword;
	private JButton btnSignUp;
	private JButton btnBackToLogin;

	private final LoginController loginController = new LoginController();

	public SignUpForm() {
		setTitle("LARS - Student Sign Up");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(780, 740);
		setResizable(false);
		setLocationRelativeTo(null);

		contentPane = new JPanel();
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		// ================= top header =================
		pnlHeader = new JPanel();
		pnlHeader.setBackground(Theme.NAVY);
		pnlHeader.setPreferredSize(new Dimension(0, 96));
		pnlHeader.setBorder(new EmptyBorder(0, 32, 0, 32));
		pnlHeader.setLayout(new GridBagLayout());
		contentPane.add(pnlHeader, BorderLayout.NORTH);

		lblHeaderIcon = new JLabel(new ComputerIcon(56, Color.WHITE));
		lblHeaderTitle = new JLabel("Create your LARS account");
		lblHeaderTitle.setFont(Theme.FONT_HEADING);
		lblHeaderTitle.setForeground(Theme.WHITE);
		lblHeaderSubtitle = new JLabel("Fill in your details to start reserving computers.");
		lblHeaderSubtitle.setFont(Theme.FONT_BODY);
		lblHeaderSubtitle.setForeground(Theme.UNAVAILABLE_GRAY);

		GridBagConstraints gbc_lblHeaderIcon = new GridBagConstraints();
		gbc_lblHeaderIcon.gridheight = 2;
		gbc_lblHeaderIcon.anchor = GridBagConstraints.WEST;
		gbc_lblHeaderIcon.insets = new Insets(0, 0, 0, 16);
		gbc_lblHeaderIcon.gridx = 0;
		gbc_lblHeaderIcon.gridy = 0;
		pnlHeader.add(lblHeaderIcon, gbc_lblHeaderIcon);

		GridBagConstraints gbc_lblHeaderTitle = new GridBagConstraints();
		gbc_lblHeaderTitle.anchor = GridBagConstraints.WEST;
		gbc_lblHeaderTitle.gridx = 1;
		gbc_lblHeaderTitle.gridy = 0;
		pnlHeader.add(lblHeaderTitle, gbc_lblHeaderTitle);

		GridBagConstraints gbc_lblHeaderSubtitle = new GridBagConstraints();
		gbc_lblHeaderSubtitle.anchor = GridBagConstraints.WEST;
		gbc_lblHeaderSubtitle.gridx = 1;
		gbc_lblHeaderSubtitle.gridy = 1;
		pnlHeader.add(lblHeaderSubtitle, gbc_lblHeaderSubtitle);

		// ================= form =================
		pnlForm = new JPanel();
		pnlForm.setBackground(Theme.WHITE);
		pnlForm.setBorder(new EmptyBorder(24, 48, 24, 48));
		pnlForm.setLayout(new GridBagLayout());
		contentPane.add(pnlForm, BorderLayout.CENTER);

		lblFullName = new JLabel("Full Name");
		lblFullName.setFont(Theme.FONT_LABEL);
		lblFullName.setForeground(Theme.TEXT_DARK);
		txtFullName = new JTextField();
		txtFullName.setColumns(18);
		Theme.styleTextField(txtFullName);

		lblStudentId = new JLabel("Student ID (YYYY-NNNNN)");
		lblStudentId.setFont(Theme.FONT_LABEL);
		lblStudentId.setForeground(Theme.TEXT_DARK);
		txtStudentId = new JTextField();
		txtStudentId.setColumns(18);
		Theme.styleTextField(txtStudentId);

		lblCourse = new JLabel("Program / Course");
		lblCourse.setFont(Theme.FONT_LABEL);
		lblCourse.setForeground(Theme.TEXT_DARK);
		cmbCourse = new JComboBox<String>();
		cmbCourse.setModel(new DefaultComboBoxModel<String>(FormOptions.getCourses()));
		Theme.styleComboBox(cmbCourse);

		lblYearLevel = new JLabel("Year Level");
		lblYearLevel.setFont(Theme.FONT_LABEL);
		lblYearLevel.setForeground(Theme.TEXT_DARK);
		cmbYearLevel = new JComboBox<String>();
		cmbYearLevel.setModel(new DefaultComboBoxModel<String>(FormOptions.getYearLevels()));
		Theme.styleComboBox(cmbYearLevel);

		lblSection = new JLabel("Section (example: A)");
		lblSection.setFont(Theme.FONT_LABEL);
		lblSection.setForeground(Theme.TEXT_DARK);
		txtSection = new JTextField();
		txtSection.setColumns(18);
		Theme.styleTextField(txtSection);

		lblUsername = new JLabel("Username");
		lblUsername.setFont(Theme.FONT_LABEL);
		lblUsername.setForeground(Theme.TEXT_DARK);
		txtUsername = new JTextField();
		txtUsername.setColumns(18);
		Theme.styleTextField(txtUsername);

		lblPassword = new JLabel("Password (at least 6 characters)");
		lblPassword.setFont(Theme.FONT_LABEL);
		lblPassword.setForeground(Theme.TEXT_DARK);
		pwdPassword = new JPasswordField();
		pwdPassword.setColumns(18);
		Theme.styleTextField(pwdPassword);

		lblConfirmPassword = new JLabel("Confirm Password");
		lblConfirmPassword.setFont(Theme.FONT_LABEL);
		lblConfirmPassword.setForeground(Theme.TEXT_DARK);
		pwdConfirmPassword = new JPasswordField();
		pwdConfirmPassword.setColumns(18);
		Theme.styleTextField(pwdConfirmPassword);

		GridBagConstraints gbc_lblFullName = new GridBagConstraints();
		gbc_lblFullName.anchor = GridBagConstraints.WEST;
		gbc_lblFullName.insets = new Insets(0, 0, 6, 24);
		gbc_lblFullName.gridx = 0;
		gbc_lblFullName.gridy = 0;
		pnlForm.add(lblFullName, gbc_lblFullName);

		GridBagConstraints gbc_txtFullName = new GridBagConstraints();
		gbc_txtFullName.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtFullName.anchor = GridBagConstraints.WEST;
		gbc_txtFullName.insets = new Insets(0, 0, 16, 24);
		gbc_txtFullName.weightx = 1.0;
		gbc_txtFullName.gridx = 0;
		gbc_txtFullName.gridy = 1;
		pnlForm.add(txtFullName, gbc_txtFullName);

		GridBagConstraints gbc_lblStudentId = new GridBagConstraints();
		gbc_lblStudentId.anchor = GridBagConstraints.WEST;
		gbc_lblStudentId.insets = new Insets(0, 0, 6, 24);
		gbc_lblStudentId.gridx = 0;
		gbc_lblStudentId.gridy = 2;
		pnlForm.add(lblStudentId, gbc_lblStudentId);

		GridBagConstraints gbc_txtStudentId = new GridBagConstraints();
		gbc_txtStudentId.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtStudentId.anchor = GridBagConstraints.WEST;
		gbc_txtStudentId.insets = new Insets(0, 0, 16, 24);
		gbc_txtStudentId.weightx = 1.0;
		gbc_txtStudentId.gridx = 0;
		gbc_txtStudentId.gridy = 3;
		pnlForm.add(txtStudentId, gbc_txtStudentId);

		GridBagConstraints gbc_lblCourse = new GridBagConstraints();
		gbc_lblCourse.anchor = GridBagConstraints.WEST;
		gbc_lblCourse.insets = new Insets(0, 0, 6, 24);
		gbc_lblCourse.gridx = 0;
		gbc_lblCourse.gridy = 4;
		pnlForm.add(lblCourse, gbc_lblCourse);

		GridBagConstraints gbc_cmbCourse = new GridBagConstraints();
		gbc_cmbCourse.fill = GridBagConstraints.HORIZONTAL;
		gbc_cmbCourse.anchor = GridBagConstraints.WEST;
		gbc_cmbCourse.insets = new Insets(0, 0, 16, 24);
		gbc_cmbCourse.weightx = 1.0;
		gbc_cmbCourse.gridx = 0;
		gbc_cmbCourse.gridy = 5;
		pnlForm.add(cmbCourse, gbc_cmbCourse);

		GridBagConstraints gbc_lblYearLevel = new GridBagConstraints();
		gbc_lblYearLevel.anchor = GridBagConstraints.WEST;
		gbc_lblYearLevel.insets = new Insets(0, 0, 6, 24);
		gbc_lblYearLevel.gridx = 0;
		gbc_lblYearLevel.gridy = 6;
		pnlForm.add(lblYearLevel, gbc_lblYearLevel);

		GridBagConstraints gbc_cmbYearLevel = new GridBagConstraints();
		gbc_cmbYearLevel.fill = GridBagConstraints.HORIZONTAL;
		gbc_cmbYearLevel.anchor = GridBagConstraints.WEST;
		gbc_cmbYearLevel.insets = new Insets(0, 0, 16, 24);
		gbc_cmbYearLevel.weightx = 1.0;
		gbc_cmbYearLevel.gridx = 0;
		gbc_cmbYearLevel.gridy = 7;
		pnlForm.add(cmbYearLevel, gbc_cmbYearLevel);

		GridBagConstraints gbc_lblSection = new GridBagConstraints();
		gbc_lblSection.anchor = GridBagConstraints.WEST;
		gbc_lblSection.insets = new Insets(0, 0, 6, 0);
		gbc_lblSection.gridx = 1;
		gbc_lblSection.gridy = 0;
		pnlForm.add(lblSection, gbc_lblSection);

		GridBagConstraints gbc_txtSection = new GridBagConstraints();
		gbc_txtSection.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtSection.anchor = GridBagConstraints.WEST;
		gbc_txtSection.insets = new Insets(0, 0, 16, 0);
		gbc_txtSection.weightx = 1.0;
		gbc_txtSection.gridx = 1;
		gbc_txtSection.gridy = 1;
		pnlForm.add(txtSection, gbc_txtSection);

		GridBagConstraints gbc_lblUsername = new GridBagConstraints();
		gbc_lblUsername.anchor = GridBagConstraints.WEST;
		gbc_lblUsername.insets = new Insets(0, 0, 6, 0);
		gbc_lblUsername.gridx = 1;
		gbc_lblUsername.gridy = 2;
		pnlForm.add(lblUsername, gbc_lblUsername);

		GridBagConstraints gbc_txtUsername = new GridBagConstraints();
		gbc_txtUsername.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtUsername.anchor = GridBagConstraints.WEST;
		gbc_txtUsername.insets = new Insets(0, 0, 16, 0);
		gbc_txtUsername.weightx = 1.0;
		gbc_txtUsername.gridx = 1;
		gbc_txtUsername.gridy = 3;
		pnlForm.add(txtUsername, gbc_txtUsername);

		GridBagConstraints gbc_lblPassword = new GridBagConstraints();
		gbc_lblPassword.anchor = GridBagConstraints.WEST;
		gbc_lblPassword.insets = new Insets(0, 0, 6, 0);
		gbc_lblPassword.gridx = 1;
		gbc_lblPassword.gridy = 4;
		pnlForm.add(lblPassword, gbc_lblPassword);

		GridBagConstraints gbc_pwdPassword = new GridBagConstraints();
		gbc_pwdPassword.fill = GridBagConstraints.HORIZONTAL;
		gbc_pwdPassword.anchor = GridBagConstraints.WEST;
		gbc_pwdPassword.insets = new Insets(0, 0, 16, 0);
		gbc_pwdPassword.weightx = 1.0;
		gbc_pwdPassword.gridx = 1;
		gbc_pwdPassword.gridy = 5;
		pnlForm.add(pwdPassword, gbc_pwdPassword);

		GridBagConstraints gbc_lblConfirmPassword = new GridBagConstraints();
		gbc_lblConfirmPassword.anchor = GridBagConstraints.WEST;
		gbc_lblConfirmPassword.insets = new Insets(0, 0, 6, 0);
		gbc_lblConfirmPassword.gridx = 1;
		gbc_lblConfirmPassword.gridy = 6;
		pnlForm.add(lblConfirmPassword, gbc_lblConfirmPassword);

		GridBagConstraints gbc_pwdConfirmPassword = new GridBagConstraints();
		gbc_pwdConfirmPassword.fill = GridBagConstraints.HORIZONTAL;
		gbc_pwdConfirmPassword.anchor = GridBagConstraints.WEST;
		gbc_pwdConfirmPassword.insets = new Insets(0, 0, 16, 0);
		gbc_pwdConfirmPassword.weightx = 1.0;
		gbc_pwdConfirmPassword.gridx = 1;
		gbc_pwdConfirmPassword.gridy = 7;
		pnlForm.add(pwdConfirmPassword, gbc_pwdConfirmPassword);

		btnSignUp = new JButton("Sign Up");
		Theme.stylePrimaryButton(btnSignUp);
		getRootPane().setDefaultButton(btnSignUp);

		pnlLoginRow = new JPanel();
		pnlLoginRow.setBackground(Theme.WHITE);
		pnlLoginRow.setLayout(new FlowLayout(FlowLayout.CENTER, 4, 0));
		lblHaveAccount = new JLabel("Already have an account?");
		lblHaveAccount.setFont(Theme.FONT_BODY);
		lblHaveAccount.setForeground(Theme.TEXT_MUTED);
		pnlLoginRow.add(lblHaveAccount);
		btnBackToLogin = new JButton("Back to Login");
		Theme.styleLinkButton(btnBackToLogin);
		pnlLoginRow.add(btnBackToLogin);

		GridBagConstraints gbc_btnSignUp = new GridBagConstraints();
		gbc_btnSignUp.gridwidth = 2;
		gbc_btnSignUp.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnSignUp.anchor = GridBagConstraints.WEST;
		gbc_btnSignUp.insets = new Insets(12, 0, 12, 0);
		gbc_btnSignUp.weightx = 1.0;
		gbc_btnSignUp.gridx = 0;
		gbc_btnSignUp.gridy = 8;
		pnlForm.add(btnSignUp, gbc_btnSignUp);

		GridBagConstraints gbc_pnlLoginRow = new GridBagConstraints();
		gbc_pnlLoginRow.gridwidth = 2;
		gbc_pnlLoginRow.fill = GridBagConstraints.HORIZONTAL;
		gbc_pnlLoginRow.anchor = GridBagConstraints.WEST;
		gbc_pnlLoginRow.weightx = 1.0;
		gbc_pnlLoginRow.gridx = 0;
		gbc_pnlLoginRow.gridy = 9;
		pnlForm.add(pnlLoginRow, gbc_pnlLoginRow);

		// ================= button actions =================
		btnSignUp.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				handleSignUp();
			}
		});

		btnBackToLogin.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				openLogin();
			}
		});
	}

	// ---------------------------------------------------------------- logic

	/** Sends the typed values to LoginController, which validates everything and creates the account. */
	private void handleSignUp() {
		try {
			loginController.registerStudent(
					txtFullName.getText(),
					txtStudentId.getText(),
					FormOptions.valueOrEmpty(cmbCourse),      // "" if the user did not choose
					FormOptions.valueOrEmpty(cmbYearLevel),
					txtSection.getText(),
					txtUsername.getText(),
					new String(pwdPassword.getPassword()),
					new String(pwdConfirmPassword.getPassword()));

			JOptionPane.showMessageDialog(this,
					"Account created successfully! You can now log in.\n"
					+ "Note: accounts are kept in memory only and are cleared when LARS closes.",
					"Sign Up Successful", JOptionPane.INFORMATION_MESSAGE);
			openLogin();
		} catch (IllegalArgumentException ex) {
			JOptionPane.showMessageDialog(this, ex.getMessage(), "Sign Up Failed", JOptionPane.WARNING_MESSAGE);
		}
	}

	private void openLogin() {
		new LoginForm().setVisible(true);
		dispose();
	}
}