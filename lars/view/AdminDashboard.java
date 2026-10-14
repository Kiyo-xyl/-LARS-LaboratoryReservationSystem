package lars.view;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
import lars.model.Admin;

/**
 * The administrator's main window: navy sidebar + CardLayout, same style as the student window.
 * The constructor calls requireAdmin(), so a student can never open this window.
 */
public class AdminDashboard extends JFrame {

	private static final long serialVersionUID = 1L;

	private static final String CARD_HOME = "home";
	private static final String CARD_COMPUTERS = "computers";
	private static final String CARD_SCHEDULE = "schedule";
	private static final String CARD_RESERVATIONS = "reservations";

	private final LARSData data = LARSData.getInstance();
	private final LoginController loginController = new LoginController();
	private final Admin admin;

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
	private JButton btnNavDashboard;
	private JButton btnNavComputers;
	private JButton btnNavSchedule;
	private JButton btnNavReservations;
	private JButton btnNavLogout;
	private JButton[] navButtons;

	private AdminHomePanel pnlHome;
	private ComputerManagementForm pnlComputers;
	private ScheduleManagementForm pnlSchedule;
	private ReservationManagementForm pnlReservations;

	public AdminDashboard() {
		admin = data.requireAdmin(); // role check: only a logged-in administrator may open this window

		setTitle("LARS - Administrator Dashboard");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(1180, 740);
		setMinimumSize(new Dimension(1020, 680));
		setLocationRelativeTo(null);

		contentPane = new JPanel();
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		// ================= left sidebar =================
		pnlSidebar = new JPanel();
		pnlSidebar.setBackground(Theme.NAVY);
		pnlSidebar.setPreferredSize(new Dimension(260, 0));
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

		lblSidebarUser = new JLabel("Administrator");
		lblSidebarUser.setFont(Theme.FONT_SMALL);
		lblSidebarUser.setForeground(Theme.UNAVAILABLE_GRAY);
		lblSidebarUser.setAlignmentX(Component.LEFT_ALIGNMENT);
		pnlSidebarBrand.add(lblSidebarUser);

		pnlNav = new JPanel();
		pnlNav.setBackground(Theme.NAVY);
		pnlNav.setLayout(new BoxLayout(pnlNav, BoxLayout.Y_AXIS));
		pnlSidebar.add(pnlNav, BorderLayout.CENTER);

		btnNavDashboard = new JButton("Dashboard");
		btnNavComputers = new JButton("Computer Management");
		btnNavSchedule = new JButton("Schedule Management");
		btnNavReservations = new JButton("Reservation Management");
		addNavButton(btnNavDashboard);
		addNavButton(btnNavComputers);
		addNavButton(btnNavSchedule);
		addNavButton(btnNavReservations);
		navButtons = new JButton[] {btnNavDashboard, btnNavComputers, btnNavSchedule, btnNavReservations};

		btnNavLogout = new JButton("Logout");
		Theme.styleNavButton(btnNavLogout);
		pnlSidebar.add(btnNavLogout, BorderLayout.SOUTH);

		// ================= right side: pages =================
		cardLayout = new CardLayout(0, 0);
		pnlContent = new JPanel();
		pnlContent.setBackground(Theme.LIGHT_GRAY);
		pnlContent.setLayout(cardLayout);
		contentPane.add(pnlContent, BorderLayout.CENTER);

		pnlHome = new AdminHomePanel();
		pnlContent.add(pnlHome, CARD_HOME);
		pnlComputers = new ComputerManagementForm();
		pnlContent.add(pnlComputers, CARD_COMPUTERS);
		pnlSchedule = new ScheduleManagementForm();
		pnlContent.add(pnlSchedule, CARD_SCHEDULE);
		pnlReservations = new ReservationManagementForm();
		pnlContent.add(pnlReservations, CARD_RESERVATIONS);

		// ================= sidebar button actions =================
		btnNavDashboard.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				showDashboard();
			}
		});
		btnNavComputers.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				showComputerManagement();
			}
		});
		btnNavSchedule.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				showScheduleManagement();
			}
		});
		btnNavReservations.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				showReservationManagement();
			}
		});
		btnNavLogout.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				handleLogout();
			}
		});

		// ================= live updates =================
		data.addChangeListener(dataChangeListener);
		refreshTimer = new Timer(30000, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				refreshAll();
			}
		});
		refreshTimer.start();

		showDashboard();
	}

	// ---------------------------------------------------------------- navigation

	public void showDashboard() {
		pnlHome.refresh();
		selectNav(btnNavDashboard);
		cardLayout.show(pnlContent, CARD_HOME);
	}

	public void showComputerManagement() {
		pnlComputers.refresh();
		selectNav(btnNavComputers);
		cardLayout.show(pnlContent, CARD_COMPUTERS);
	}

	public void showScheduleManagement() {
		pnlSchedule.refresh();
		selectNav(btnNavSchedule);
		cardLayout.show(pnlContent, CARD_SCHEDULE);
	}

	public void showReservationManagement() {
		pnlReservations.refresh();
		selectNav(btnNavReservations);
		cardLayout.show(pnlContent, CARD_RESERVATIONS);
	}

	// ---------------------------------------------------------------- helpers

	/** Runs when shared data changes (and every 30 seconds). The schedule form is not touched, so unsaved edits stay. */
	private void refreshAll() {
		pnlHome.refresh();
		pnlComputers.refresh();
		pnlReservations.refresh();
	}

	private void addNavButton(JButton button) {
		Theme.styleNavButton(button);
		button.setAlignmentX(Component.LEFT_ALIGNMENT);
		button.setMaximumSize(new Dimension(Integer.MAX_VALUE, 48));
		pnlNav.add(button);
	}

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
		loginController.logout();
		new AdminLoginForm().setVisible(true);
		dispose();
	}

	@Override
	public void dispose() {
		if (refreshTimer != null) {
			refreshTimer.stop();
		}
		data.removeChangeListener(dataChangeListener);
		super.dispose();
	}
}