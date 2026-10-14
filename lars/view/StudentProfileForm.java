package lars.view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import lars.controller.LoginController;
import lars.data.LARSData;
import lars.model.Student;

/**
 * The student's profile. Name, course, year level and section can be edited.
 * Student ID and username are locked because they identify the account.
 */
public class StudentProfileForm extends JPanel {

	private static final long serialVersionUID = 1L;

	private final LoginController loginController = new LoginController();

	private JPanel pnlCard;
	private JPanel pnlButtons;
	private JLabel lblFormTitle;
	private JLabel lblFormSubtitle;
	private JLabel lblFullName;
	private JLabel lblStudentId;
	private JLabel lblCourse;
	private JLabel lblYearLevel;
	private JLabel lblSection;
	private JLabel lblUsername;
	private JTextField txtFullName;
	private JTextField txtStudentId;
	private JComboBox<String> cmbCourse;
	private JComboBox<String> cmbYearLevel;
	private JTextField txtSection;
	private JTextField txtUsername;
	private JButton btnSaveChanges;
	private JButton btnReset;

	public StudentProfileForm() {
		setBackground(Theme.LIGHT_GRAY);
		setBorder(new EmptyBorder(24, 28, 24, 28));
		setLayout(new BorderLayout(0, 0));

		pnlCard = new JPanel();
		pnlCard.setBackground(Theme.WHITE);
		pnlCard.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createLineBorder(Theme.BORDER_GRAY),
				BorderFactory.createEmptyBorder(24, 32, 24, 32)));
		pnlCard.setLayout(new GridBagLayout());
		add(pnlCard, BorderLayout.NORTH);

		lblFormTitle = new JLabel("My Profile");
		lblFormTitle.setFont(Theme.FONT_HEADING);
		lblFormTitle.setForeground(Theme.NAVY);

		lblFormSubtitle = new JLabel("Student ID and username cannot be changed.");
		lblFormSubtitle.setFont(Theme.FONT_BODY);
		lblFormSubtitle.setForeground(Theme.TEXT_MUTED);

		lblFullName = new JLabel("Full Name");
		lblFullName.setFont(Theme.FONT_LABEL);
		lblFullName.setForeground(Theme.TEXT_DARK);

		txtFullName = new JTextField();
		txtFullName.setColumns(18);
		Theme.styleTextField(txtFullName);

		lblStudentId = new JLabel("Student ID (locked)");
		lblStudentId.setFont(Theme.FONT_LABEL);
		lblStudentId.setForeground(Theme.TEXT_DARK);

		txtStudentId = new JTextField();
		txtStudentId.setColumns(18);
		txtStudentId.setEditable(false);
		Theme.styleTextField(txtStudentId);
		txtStudentId.setBackground(Theme.LIGHT_GRAY);

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

		lblSection = new JLabel("Section");
		lblSection.setFont(Theme.FONT_LABEL);
		lblSection.setForeground(Theme.TEXT_DARK);

		txtSection = new JTextField();
		txtSection.setColumns(18);
		Theme.styleTextField(txtSection);

		lblUsername = new JLabel("Username (locked)");
		lblUsername.setFont(Theme.FONT_LABEL);
		lblUsername.setForeground(Theme.TEXT_DARK);

		txtUsername = new JTextField();
		txtUsername.setColumns(18);
		txtUsername.setEditable(false);
		Theme.styleTextField(txtUsername);
		txtUsername.setBackground(Theme.LIGHT_GRAY);

		pnlButtons = new JPanel();
		pnlButtons.setBackground(Theme.WHITE);
		pnlButtons.setLayout(new FlowLayout(FlowLayout.RIGHT, 12, 0));

		btnSaveChanges = new JButton("Save Changes");
		Theme.stylePrimaryButton(btnSaveChanges);

		btnReset = new JButton("Reset");
		Theme.styleSecondaryButton(btnReset);

		pnlButtons.add(btnReset);
		pnlButtons.add(btnSaveChanges);

		GridBagConstraints gbc_lblFormTitle = new GridBagConstraints();
		gbc_lblFormTitle.gridwidth = 2;
		gbc_lblFormTitle.anchor = GridBagConstraints.WEST;
		gbc_lblFormTitle.insets = new Insets(0, 0, 2, 0);
		gbc_lblFormTitle.gridx = 0;
		gbc_lblFormTitle.gridy = 0;
		pnlCard.add(lblFormTitle, gbc_lblFormTitle);

		GridBagConstraints gbc_lblFormSubtitle = new GridBagConstraints();
		gbc_lblFormSubtitle.gridwidth = 2;
		gbc_lblFormSubtitle.anchor = GridBagConstraints.WEST;
		gbc_lblFormSubtitle.insets = new Insets(0, 0, 20, 0);
		gbc_lblFormSubtitle.gridx = 0;
		gbc_lblFormSubtitle.gridy = 1;
		pnlCard.add(lblFormSubtitle, gbc_lblFormSubtitle);

		GridBagConstraints gbc_lblFullName = new GridBagConstraints();
		gbc_lblFullName.anchor = GridBagConstraints.WEST;
		gbc_lblFullName.insets = new Insets(0, 0, 6, 24);
		gbc_lblFullName.gridx = 0;
		gbc_lblFullName.gridy = 2;
		pnlCard.add(lblFullName, gbc_lblFullName);

		GridBagConstraints gbc_txtFullName = new GridBagConstraints();
		gbc_txtFullName.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtFullName.anchor = GridBagConstraints.WEST;
		gbc_txtFullName.insets = new Insets(0, 0, 14, 24);
		gbc_txtFullName.weightx = 1.0;
		gbc_txtFullName.gridx = 0;
		gbc_txtFullName.gridy = 3;
		pnlCard.add(txtFullName, gbc_txtFullName);

		GridBagConstraints gbc_lblStudentId = new GridBagConstraints();
		gbc_lblStudentId.anchor = GridBagConstraints.WEST;
		gbc_lblStudentId.insets = new Insets(0, 0, 6, 0);
		gbc_lblStudentId.gridx = 1;
		gbc_lblStudentId.gridy = 2;
		pnlCard.add(lblStudentId, gbc_lblStudentId);

		GridBagConstraints gbc_txtStudentId = new GridBagConstraints();
		gbc_txtStudentId.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtStudentId.anchor = GridBagConstraints.WEST;
		gbc_txtStudentId.insets = new Insets(0, 0, 14, 0);
		gbc_txtStudentId.weightx = 1.0;
		gbc_txtStudentId.gridx = 1;
		gbc_txtStudentId.gridy = 3;
		pnlCard.add(txtStudentId, gbc_txtStudentId);

		GridBagConstraints gbc_lblCourse = new GridBagConstraints();
		gbc_lblCourse.anchor = GridBagConstraints.WEST;
		gbc_lblCourse.insets = new Insets(0, 0, 6, 24);
		gbc_lblCourse.gridx = 0;
		gbc_lblCourse.gridy = 4;
		pnlCard.add(lblCourse, gbc_lblCourse);

		GridBagConstraints gbc_cmbCourse = new GridBagConstraints();
		gbc_cmbCourse.fill = GridBagConstraints.HORIZONTAL;
		gbc_cmbCourse.anchor = GridBagConstraints.WEST;
		gbc_cmbCourse.insets = new Insets(0, 0, 14, 24);
		gbc_cmbCourse.weightx = 1.0;
		gbc_cmbCourse.gridx = 0;
		gbc_cmbCourse.gridy = 5;
		pnlCard.add(cmbCourse, gbc_cmbCourse);

		GridBagConstraints gbc_lblYearLevel = new GridBagConstraints();
		gbc_lblYearLevel.anchor = GridBagConstraints.WEST;
		gbc_lblYearLevel.insets = new Insets(0, 0, 6, 0);
		gbc_lblYearLevel.gridx = 1;
		gbc_lblYearLevel.gridy = 4;
		pnlCard.add(lblYearLevel, gbc_lblYearLevel);

		GridBagConstraints gbc_cmbYearLevel = new GridBagConstraints();
		gbc_cmbYearLevel.fill = GridBagConstraints.HORIZONTAL;
		gbc_cmbYearLevel.anchor = GridBagConstraints.WEST;
		gbc_cmbYearLevel.insets = new Insets(0, 0, 14, 0);
		gbc_cmbYearLevel.weightx = 1.0;
		gbc_cmbYearLevel.gridx = 1;
		gbc_cmbYearLevel.gridy = 5;
		pnlCard.add(cmbYearLevel, gbc_cmbYearLevel);

		GridBagConstraints gbc_lblSection = new GridBagConstraints();
		gbc_lblSection.anchor = GridBagConstraints.WEST;
		gbc_lblSection.insets = new Insets(0, 0, 6, 24);
		gbc_lblSection.gridx = 0;
		gbc_lblSection.gridy = 6;
		pnlCard.add(lblSection, gbc_lblSection);

		GridBagConstraints gbc_txtSection = new GridBagConstraints();
		gbc_txtSection.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtSection.anchor = GridBagConstraints.WEST;
		gbc_txtSection.insets = new Insets(0, 0, 14, 24);
		gbc_txtSection.weightx = 1.0;
		gbc_txtSection.gridx = 0;
		gbc_txtSection.gridy = 7;
		pnlCard.add(txtSection, gbc_txtSection);

		GridBagConstraints gbc_lblUsername = new GridBagConstraints();
		gbc_lblUsername.anchor = GridBagConstraints.WEST;
		gbc_lblUsername.insets = new Insets(0, 0, 6, 0);
		gbc_lblUsername.gridx = 1;
		gbc_lblUsername.gridy = 6;
		pnlCard.add(lblUsername, gbc_lblUsername);

		GridBagConstraints gbc_txtUsername = new GridBagConstraints();
		gbc_txtUsername.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtUsername.anchor = GridBagConstraints.WEST;
		gbc_txtUsername.insets = new Insets(0, 0, 14, 0);
		gbc_txtUsername.weightx = 1.0;
		gbc_txtUsername.gridx = 1;
		gbc_txtUsername.gridy = 7;
		pnlCard.add(txtUsername, gbc_txtUsername);

		GridBagConstraints gbc_pnlButtons = new GridBagConstraints();
		gbc_pnlButtons.gridwidth = 2;
		gbc_pnlButtons.fill = GridBagConstraints.HORIZONTAL;
		gbc_pnlButtons.anchor = GridBagConstraints.WEST;
		gbc_pnlButtons.insets = new Insets(8, 0, 0, 0);
		gbc_pnlButtons.weightx = 1.0;
		gbc_pnlButtons.gridx = 0;
		gbc_pnlButtons.gridy = 8;
		pnlCard.add(pnlButtons, gbc_pnlButtons);

		btnSaveChanges.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				handleSave();
			}
		});

		btnReset.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				refresh();
			}
		});

	}

	/** Loads the logged-in student's current details into the fields (also used by the Reset button). */
	public void refresh() {
		Student student = LARSData.getInstance().requireStudent();
		txtFullName.setText(student.getFullName());
		txtStudentId.setText(student.getStudentId());
		FormOptions.selectValue(cmbCourse, student.getCourse());
		FormOptions.selectValue(cmbYearLevel, student.getYearLevel());
		txtSection.setText(student.getSection());
		txtUsername.setText(student.getUsername());
	}

	/** LoginController validates and updates the in-memory student account immediately. */
	private void handleSave() {
		try {
			loginController.updateProfile(txtFullName.getText(),
					FormOptions.valueOrEmpty(cmbCourse),
					FormOptions.valueOrEmpty(cmbYearLevel),
					txtSection.getText());
			JOptionPane.showMessageDialog(this, "Your profile has been updated.",
					"Profile Saved", JOptionPane.INFORMATION_MESSAGE);
			refresh();
		} catch (IllegalArgumentException | IllegalStateException | SecurityException ex) {
			JOptionPane.showMessageDialog(this, ex.getMessage(), "Profile Not Saved", JOptionPane.WARNING_MESSAGE);
		}
	}
}