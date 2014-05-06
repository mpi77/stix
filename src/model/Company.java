package model;

/**
 * Company (data from Company table) POJO object.
 * 
 * @author MPI
 * @version 06.05.2014/1.0
 */
public class Company {
	public static final String DB_MAP_ID = "id";
	public static final String DB_MAP_NAME = "name";
	
	private String id;
	private String name;
	
	public Company(String id) {
		super();
		this.id = id;
		this.name = id;
	}
	
	public Company(String id, String name) {
		super();
		this.id = id;
		this.name = name;
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "Company [id=" + id + ", name=" + name + "]";
	}
	
}
