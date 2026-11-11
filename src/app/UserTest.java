package app;

import java.util.ArrayList;
import model.Clerk;
import model.Manager;
import model.User;

public class UserTest {

	public static void main(String[] args) {
		ArrayList<User> users = new ArrayList<>();
		users.add(new Manager("manager", "1234", "Maria Manager"));
		users.add(new Clerk("clerk1", "1111", "Carlo Clerk"));

		for (User u : users) {
			System.out.println(u.getRole() + ": " + u.getFullName()
					+ " | can manage products: " + u.canManageProducts());
		}

		User first = users.get(0);
		System.out.println("Right password: " + first.checkPassword("1234"));
		System.out.println("Wrong password: " + first.checkPassword("abcd"));
	}
}