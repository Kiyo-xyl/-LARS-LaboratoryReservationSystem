package lars;

import java.awt.EventQueue;

import javax.swing.JOptionPane;

import lars.data.LARSData;
import lars.view.LoginForm;

public class Main {

	public static void main(String[] args) {

		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					LARSData.getInstance();          // creates the default admin, labs, computers, schedules (once)
					new LoginForm().setVisible(true);
				} catch (RuntimeException ex) {
					JOptionPane.showMessageDialog(null, "LARS could not start: " + ex.getMessage(),
							"LARS", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
	}
}