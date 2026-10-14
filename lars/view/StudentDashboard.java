package lars.view;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.LocalTime;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;

import lars.controller.LoginController;
import lars.data.LARSData;
import lars.model.Computer;
import lars.model.Reservation;
import lars.model.Student;

/**
 * The student's main window: a navy sidebar on the left and a CardLayout on the right.
 * Clicking a sidebar item SWITCHES the card - no new windows are opened.
 * The window registers with LARSData, so every change (a new reservation, a cancellation,
 * an admin changing a computer) refreshes the counts and cards automatically.
 */
public class StudentDashboard extends JFrame {

	private static final long serialVersionUID = 1L;

	// names of the cards inside pnlContent
	private static final String CARD_HOME = "home";
	private static final String CARD_LAB = "laboratory";
	private static final String CARD_RESERVATION = "reservation";
	private static final String CARD_CONFIRMATION = "confirmation";
	private static final String CARD_MY_RESERVATIONS = "myReservations";
	private static final String CARD_PROFILE = "profile";

	private final LARSData data = LARSData.getInstance();
	private final LoginController loginController = new LoginController();
	private final Student student;

	private final Runnable dataChangeListener = new Runnable() {
		@Override
		public void run() {
			refreshAll();
		}
	};
	private Timer refreshTimer;

	private JPanel contentPane;
	private JPanel pnlSidebar;
	private JPanel pnlSidebarBrand;
	private JPanel pnlNav;
	private JPanel pnlContent;
	private CardLayout cardLayout;
	private JLabel lblSidebarIcon;
	private JLabel lblSidebarBrand;
	private JLabel lblSidebarUser;
	private JButton btnNavHome;
	private JButton btnNavComlab;
	private JButton btnNavIlab;
	private JButton btnNavReservations;
	private JButton btnNavProfile;
	private JButton btnNavLogout;
	private JButton[] navButtons;

	private StudentHomePanel pnlHome;
	private LaboratoryView pnlLaboratory;
	private ReservationForm pnlReservationForm;
	private ReservationConfirmation pnlConfirmation;
	private MyReservationsForm pnlMyReservations;
	private StudentProfileForm pnlProfile;

	public StudentDashboard() {
		student = data.requireStudent(); // role check: only a logged-in student may open this window

		setTitle("LARS - Student Dashboard");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(1140, 740);
		setMinimumSize(new Dimension(1000, 680));
		setLocationRelativeTo(null);

		contentPane = new JPanel();
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		// ================= left sidebar =================
		pnlSidebar = new JPanel();
		pnlSidebar.setBackground(Theme.NAVY);
		pnlSidebar.setPreferredSize(new Dimension(240, 0));
		pnlSidebar.setLayout(new BorderLayout(0, 0));
		contentPane.add(pnlSidebar, BorderLayout.WEST);

		pnlSidebarBrand = new JPanel();
		pnlSidebarBrand.setBackground(Theme.NAVY);
		pnlSidebarBrand.setBorder(new EmptyBorder(28, 28, 24, 20));
		pnlSidebarBrand.setLayout(new BoxLayout(pnlSidebarBrand, BoxLayout.Y_AXIS));
		pnlSidebar.add(pnlSidebarBrand, BorderLayout.NORTH);

		lblSidebarIcon = new JLabel(new ComputerIcon(48, Color.WHITE));
		lblSidebarIcon.setAlignmentX(Component.LEFT_ALIGNMENT);
		pnlSidebarBrand.add(lblSidebarIcon);

		lblSidebarBrand = new JLabel("LARS");
		lblSidebarBrand.setFont(Theme.FONT_TITLE);
		lblSidebarBrand.setForeground(Theme.WHITE);
		lblSidebarBrand.setAlignmentX(Component.LEFT_ALIGNMENT);
		pnlSidebarBrand.add(lblSidebarBrand);

		lblSidebarUser = new JLabel(student.getFullName());
		lblSidebarUser.setFont(Theme.FONT_SMALL);
		lblSidebarUser.setForeground(Theme.UNAVAILABLE_GRAY);
		lblSidebarUser.setAlignmentX(Component.LEFT_ALIGNMENT);
		pnlSidebarBrand.add(lblSidebarUser);

		pnlNav = new JPanel();
		pnlNav.setBackground(Theme.NAVY);
		pnlNav.setLayout(new BoxLayout(pnlNav, BoxLayout.Y_AXIS));
		pnlSidebar.add(pnlNav, BorderLayout.CENTER);

		btnNavHome = new JButton("Home");
		btnNavComlab = new JButton("COMLAB");
		btnNavIlab = new JButton("ILAB");
		btnNavReservations = new JButton("My Reservations");
		btnNavProfile = new JButton("My Profile");
		addNavButton(btnNavHome);
		addNavButton(btnNavComlab);
		addNavButton(btnNavIlab);
		addNavButton(btnNavReservations);
		addNavButton(btnNavProfile);
		navButtons = new JButton[] {btnNavHome, btnNavComlab, btnNavIlab, btnNavReservations, btnNavProfile};

		btnNavLogout = new JButton("Logout");
		Theme.styleNavButton(btnNavLogout);
		pnlSidebar.add(btnNavLogout, BorderLayout.SOUTH);

		// ================= right side: pages (CardLayout) =================
		cardLayout = new CardLayout(0, 0);
		pnlContent = new JPanel();
		pnlContent.setBackground(Theme.LIGHT_GRAY);
		pnlContent.setLayout(cardLayout);
		contentPane.add(pnlContent, BorderLayout.CENTER);

		pnlHome = new StudentHomePanel();
		pnlHome.setDashboard(this);
		pnlContent.add(pnlHome, CARD_HOME);

		pnlLaboratory = new LaboratoryView();
		pnlLaboratory.setDashboard(this);
		pnlContent.add(pnlLaboratory, CARD_LAB);

		pnlReservationForm = new ReservationForm();
		pnlReservationForm.setDashboard(this);
		pnlContent.add(pnlReservationForm, CARD_RESERVATION);

		pnlConfirmation = new ReservationConfirmation();
		pnlConfirmation.setDashboard(this);
		pnlContent.add(pnlConfirmation, CARD_CONFIRMATION);

		pnlMyReservations = new MyReservationsForm();
		pnlContent.add(pnlMyReservations, CARD_MY_RESERVATIONS);

		pnlProfile = new StudentProfileForm();
		pnlContent.add(pnlProfile, CARD_PROFILE);

		// ================= sidebar button actions =================
		btnNavHome.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				showHome();
			}
		});
		btnNavComlab.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				showLaboratory("COMLAB");
			}
		});
		btnNavIlab.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				showLaboratory("ILAB");
			}
		});
		btnNavReservations.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				showMyReservations();
			}
		});
		btnNavProfile.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				showProfile();
			}
		});
		btnNavLogout.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				handleLogout();
			}
		});

		// ================= live updates =================
		data.addChangeListener(dataChangeListener); // refresh whenever shared data changes
		// Statuses also change as time passes (a reservation starts or ends), so refresh every 30 seconds too.
		refreshTimer = new Timer(30000, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				refreshAll();
			}
		});
		refreshTimer.start();

		showHome();
	}

	// ---------------------------------------------------------------- navigation (also used by the panels)

	public void showHome() {
		pnlHome.refresh();
		selectNav(btnNavHome);
		cardLayout.show(pnlContent, CARD_HOME);
	}

	public void showLaboratory(String laboratoryName) {
		pnlLaboratory.setLaboratory(laboratoryName);
		selectNav(laboratoryName.equals("COMLAB") ? btnNavComlab : btnNavIlab);
		cardLayout.show(pnlContent, CARD_LAB);
	}

	public void showReservationForm(Computer computer, LocalDate date, LocalTime start, LocalTime end) {
		pnlReservationForm.prepare(computer, date, start, end);
		cardLayout.show(pnlContent, CARD_RESERVATION);
	}

	public void showConfirmation(Reservation reservation) {
		pnlConfirmation.showReservation(reservation);
		cardLayout.show(pnlContent, CARD_CONFIRMATION);
	}

	public void showMyReservations() {
		pnlMyReservations.refresh();
		selectNav(btnNavReservations);
		cardLayout.show(pnlContent, CARD_MY_RESERVATIONS);
	}

	public void showProfile() {
		pnlProfile.refresh();
		selectNav(btnNavProfile);
		cardLayout.show(pnlContent, CARD_PROFILE);
	}

	// ---------------------------------------------------------------- helpers

	/** Called whenever shared data changes (and every 30 seconds). Does not touch forms being edited. */
	private void refreshAll() {
		lblSidebarUser.setText(student.getFullName());
		pnlHome.refresh();
		pnlLaboratory.refresh();
		pnlMyReservations.refresh();
	}

	private void addNavButton(JButton button) {
		Theme.styleNavButton(button);
		button.setAlignmentX(Component.LEFT_ALIGNMENT);
		button.setMaximumSize(new Dimension(Integer.MAX_VALUE, 48));
		pnlNav.add(button);
	}

	/** Highlights the current page in the sidebar. */
	private void selectNav(JButton active) {
		for (JButton button : navButtons) {
			Theme.setNavSelected(button, button == active);
		}
	}

	private void handleLogout() {
		int answer = JOptionPane.showConfirmDialog(this, "Do you want to log out of LARS?",
				"Logout", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
		if (answer != JOptionPane.YES_OPTION) {
			return;
		}
		loginController.logout();          // clears the session only; all data stays in memory
		new LoginForm().setVisible(true);
		dispose();
	}

	/** Stop the timer and unregister from LARSData so closed windows do not keep refreshing. */
	@Override
	public void dispose() {
		if (refreshTimer != null) {
			refreshTimer.stop();
		}
		data.removeChangeListener(dataChangeListener);
		super.dispose();
	}
}