package model;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

/**
 * @author MPI
 * @version 06.06.2014/1.2
 */
public class DerbyDatabase {

	public static final String DERBY_EMBEDDED_DRIVER = "org.apache.derby.jdbc.EmbeddedDriver";
	public static final String DERBY_PROTOCOL = "jdbc:derby:";
	public static final String DERBY_DEFAULT_USER = "";
	public static final String DERBY_DEFAULT_PASS = "";
	public static final String DERBY_SCHEMA = "STIXDB";
	public static final String DB_MAP_SPAD_TABLE = "SPAD";
	public static final String DB_MAP_COMPANY_TABLE = "COMPANY";

	private String driver;
	private String protocol;
	private String schema;
	private String dbUser;
	private String dbPassword;
	private Connection conn;

	public DerbyDatabase() throws ClassNotFoundException, SQLException {
		this.driver = DerbyDatabase.DERBY_EMBEDDED_DRIVER;
		this.protocol = DerbyDatabase.DERBY_PROTOCOL;
		this.dbUser = DerbyDatabase.DERBY_DEFAULT_USER;
		this.dbPassword = DerbyDatabase.DERBY_DEFAULT_PASS;
		this.schema = DerbyDatabase.DERBY_SCHEMA;

		connect();
	}

	public DerbyDatabase(String driver, String protocol, String dbUser,
			String dbPassword, String schema) throws ClassNotFoundException,
			SQLException {
		this.driver = driver;
		this.protocol = protocol;
		this.dbUser = dbUser;
		this.dbPassword = dbPassword;
		this.schema = schema;

		connect();
	}

	synchronized public ResultSet selectQuery(PreparedStatement ps)
			throws SQLException {
		ResultSet rs = ps.executeQuery();
		conn.commit();
		return rs;
	}

	synchronized public Integer selectQueryN(PreparedStatement ps)
			throws SQLException {
		Integer r = null;
		try (ResultSet rs = ps.executeQuery()) {
			conn.commit();
			if (rs.next()) {
				r = rs.getInt(1);
			}
		}
		return r;
	}

	synchronized public Integer actionQuery(PreparedStatement ps,
			boolean returnGeneratedKey) throws SQLException {
		Integer r = null;
		ps.executeUpdate();
		conn.commit();
		if (returnGeneratedKey) {
			try (ResultSet rs = ps.getGeneratedKeys()) {
				if (rs.next()) {
					r = rs.getInt(1);
				}
			}
		}
		return r;
	}

	public Connection getConnection() {
		return this.conn;
	}

	private void connect() throws SQLException, ClassNotFoundException {
		Class.forName(this.driver);
		Properties props = new Properties();
		props.put("user", dbUser);
		props.put("password", dbPassword);
		conn = DriverManager.getConnection(
				String.format("%s%s;create=true", protocol, schema), null);
		conn.setAutoCommit(false);

		DatabaseMetaData dbmd = conn.getMetaData();
		try (ResultSet rs = dbmd.getTables(null, null,
				DerbyDatabase.DB_MAP_SPAD_TABLE, null);) {
			if (!rs.next()) {
				initTables();
				// System.out.println("create table");
			}
		}
	}

	private void disconnect() throws SQLException {
		if (conn != null) {
			conn.close();
		}
	}

	private void initTables() throws SQLException {
		createSpad();
		createCompany();
		loadDefaultCompanies();
	}

	private void createSpad() throws SQLException {
		try (Statement s = conn.createStatement()) {
			String sql = String
					.format("CREATE TABLE %s (%s INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY "
							+ "(START WITH 1, INCREMENT BY 1) CONSTRAINT pk PRIMARY KEY, "
							+ "%s VARCHAR(10) NOT NULL, %s DATE NOT NULL, %s DOUBLE NOT NULL, "
							+ "%s DOUBLE NOT NULL, %s DOUBLE NOT NULL, %s DOUBLE NOT NULL, "
							+ "%s BIGINT NOT NULL)",
							DerbyDatabase.DB_MAP_SPAD_TABLE, Item.DB_MAP_ID,
							Item.DB_MAP_NAME, Item.DB_MAP_DATE,
							Item.DB_MAP_OPEN, Item.DB_MAP_CLOSE,
							Item.DB_MAP_MAX, Item.DB_MAP_MIN,
							Item.DB_MAP_VOLUME);
			s.execute(sql);
			conn.commit();
		}
	}

	private void createCompany() throws SQLException {
		try (Statement s = conn.createStatement()) {
			String sql = String.format(
					"CREATE TABLE %s (%s VARCHAR(10) NOT NULL CONSTRAINT ppk PRIMARY KEY,"
							+ "%s VARCHAR(50) NOT NULL)",
					DerbyDatabase.DB_MAP_COMPANY_TABLE, Company.DB_MAP_ID,
					Company.DB_MAP_NAME);
			s.execute(sql);
			conn.commit();
		}
	}

	private void loadDefaultCompanies() throws SQLException {
		String sql = String.format("INSERT INTO %s (%s,%s) VALUES (?,?)",
				DerbyDatabase.DB_MAP_COMPANY_TABLE, Company.DB_MAP_ID,
				Company.DB_MAP_NAME);
		try (PreparedStatement s = conn.prepareStatement(sql)) {
			s.setString(1, "ERBAG");
			s.setString(2, "ERBAGi");
			s.execute();
			s.setString(1, "VIG");
			s.setString(2, "VIGi");
			s.execute();
			s.setString(1, "CETV");
			s.setString(2, "CETVi");
			s.execute();
			s.setString(1, "TABAK");
			s.setString(2, "TABAKi");
			s.execute();
			s.setString(1, "CEZ");
			s.setString(2, "ČEZ");
			s.execute();
			s.setString(1, "KOMB");
			s.setString(2, "KOMBi");
			s.execute();
			s.setString(1, "UNIPE");
			s.setString(2, "UNIPEi");
			s.execute();
			s.setString(1, "TELEC");
			s.setString(2, "TELECi");
			s.execute();
			s.setString(1, "NWRUK");
			s.setString(2, "NWRUKi");
			s.execute();
			s.setString(1, "ORCO");
			s.setString(2, "ORCOi");
			s.execute();
			s.setString(1, "PEGAS");
			s.setString(2, "PEGASi");
			s.execute();
			s.setString(1, "AAA");
			s.setString(2, "AAAi");
			s.execute();
			s.setString(1, "FOREG");
			s.setString(2, "FOREGi");
			s.execute();
			s.setString(1, "TMR");
			s.setString(2, "TMRi");
			s.execute();	
			conn.commit();
		}
	}
}
