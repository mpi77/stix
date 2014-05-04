package model;

import java.sql.Date;
import java.util.ArrayList;

/**
 * @author MPI
 * @version 04.05.2014/1.3
 */
public interface IDataStrategy {
	public Date getSpadLastDate();
	public ArrayList<SpadItem> getSpadItems(Date startDate, Date endDate);
	public ArrayList<Item> getItems(Date startDate, Date endDate);
	public void insertItem(Item item);
	public void insertItems(ArrayList<Item> items);
}
