package model;

import java.sql.Date;
import java.util.ArrayList;

/**
 * @author MPI
 * @version 21.04.2014/1.1
 */
public interface IDataStrategy {
	public Date getSpadLastDate();
	public ArrayList<Item> getSpadItems(Date startDate, Date endDate);
	public void insertItems(ArrayList<Item> items);
}
