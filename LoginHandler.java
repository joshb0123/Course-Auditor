package termProj;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

/**
 * This class represents a handler of both the Login window and the database
 * connection
 * 
 * @author brown8jt - Josh Brown
 * @since 4/20/2019
 * 
 */
public class LoginHandler {

	private LoginView view; // login window
	private MySqlConnection con; // database connection
	private JButton loginBtn; // login button on the window

	private String title, content, status; // content for the pop up window after login button is pressed

	/**
	 * Public constructor the handler. Connects to the database and then attempts to
	 * log student into system
	 */
	public LoginHandler() {

		// initialize connection class, window, and login button
		this.con = MySqlConnection.getInstance();
		this.view = new LoginView();
		this.loginBtn = view.getLoginBtn();

		// connect to the database
		status = con.connect();

		// unsuccessful connection
		if (!status.equals("MSC.c001")) {

			title = "Error: " + status + "!";
			content = "";

			if (status.equals("MSC.c002")) {

				// driver not found
				content = "Could not find JDBC driver!";

			} else {

				// unsuccessful database connection attempt
				content = "Could not connect to database!";

			}

			PopUpHandler pu = new PopUpHandler(title, content, status, con);

		}

		// add action listener to button
		this.loginBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {

				// get username and password
				String userName = view.getUserName();
				String password = view.getPassword();

				status = con.login(userName, password); // login and get status code
				title = "";
				content = "";

				// successful login
				if (status.equals("MSC.li001")) {

					title = "Logged in!";
					content = "Welcome " + con.getStudentLastFirstName() + "!";

				}

				// no username or password
				if (status.equals("MSC.li002")) {

					title = "Error: " + status + "!";
					content = "Could not find student with given username/password!";

				}

				// student already logged in
				if (status.equals("MSC.li003")) {

					title = "Error: " + status + "!";
					content = "There is a student already logged in!";

				}

				// launch popup window
				PopUpHandler pu = new PopUpHandler(title, content, status, con);

			}

		});

	}

}
