package model;

import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * @author MPI
 * @version 06.05.2014/1.4
 */
public interface IDataStrategy {
	public Date getSpadFirstDate() throws SQLException;
	public Date getSpadLastDate() throws SQLException;
	public ArrayList<SpadItem> getSpadItems(Date startDate, Date endDate) throws SQLException;
	public ArrayList<Item> getItems(Date startDate, Date endDate) throws SQLException;
	public void insertItem(Item item) throws SQLException;
	public void insertItems(ArrayList<Item> items) throws SQLException;
}
