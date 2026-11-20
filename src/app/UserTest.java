package app;

import java.util.ArrayList;
import model.User;
import util.PodSystem;

/***********************************************************
 * Console test for the User classes (Manager and Clerk):
 * roles, permissions, the password check and the login.
 *
 * @author CHARLES GIAN L. SANTOS
 * @created_date 2026-11-11 
 ***********************************************************/

public class UserTest {

	public static void main(String[] args) {
		// the fixed accounts come from the PodSystem class
		ArrayList<User> users = PodSystem.getUsers();

		for (User u : users) {
			System.out.println(u.getRole() + ": " + u.getFullName()
					+ " | can manage products: " + u.canManageProducts()
					+ " | can void: " + u.canVoidTransactions());
		}

		User first = users.get(0);
		System.out.println("Right password: " + first.checkPassword("1234"));
		System.out.println("Wrong password: " + first.checkPassword("abcd"));

		// the login check
		System.out.println("Login manager/1234: " + (PodSystem.login("manager", "1234") != null));
		System.out.println("Login manager/abcd: " + (PodSystem.login("manager", "abcd") != null));
		System.out.println("Login nobody/1234: " + (PodSystem.login("nobody", "1234") != null));
		System.out.println("Clerks: " + PodSystem.getClerks().size());
	}
}
