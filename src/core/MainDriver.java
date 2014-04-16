package core;

public class MainDriver {

	public static void main(String[] args) throws ClassNotFoundException {
		Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
		System.out.println("ok");
	}

}
