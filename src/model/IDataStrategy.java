package model;

import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * @author MPI
 * @version 27.05.2014/1.10
 */
public interface IDataStrategy {
	/**
	 * Get first date in SPAD table.
	 * 
	 * @return Date
	 * @throws SQLException
	 */
	public Date getSpadFirstDate() throws SQLException;

	/**
	 * Get last date in SPAD table.
	 * 
	 * @return Date
	 * @throws SQLException
	 */
	public Date getSpadLastDate() throws SQLException;

	/**
	 * Get SPAD items (companies in index) data in given date interval.
	 * 
	 * @param startDate
	 * @param endDate
	 * @return ArrayList with SpadItems
	 * @throws SQLException
	 */
	public ArrayList<SpadItem> getSpadItems(Date startDate, Date endDate)
			throws SQLException;

	/**
	 * Get SPAD purchase recommendation.
	 * 
	 * @return String[] with keys ordered DESC by best item to buy
	 * @throws SQLException
	 */
	public String[] getPurchaseRecommendation() throws SQLException;
	
	/**
	 * Get all companies in company table.
	 * 
	 * @return ArrayList with Company item
	 * @throws SQLException
	 */
	public ArrayList<Company> getCompanies() throws SQLException;
	
	/**
	 * Get long-term average price for given company.
	 * 
	 * @param companyId
	 * @return Double
	 * @throws SQLException
	 */
	public Double getLongTermAveragePrice(String companyId) throws SQLException;
	
	/**
	 * Get items (raw item data) from SPAD in given date interval.
	 * 
	 * @param startDate
	 * @param endDate
	 * @param companyId
	 * @return ArrayList with Items
	 * @throws SQLException
	 */
	public ArrayList<Item> getItems(Date startDate, Date endDate, String companyId)
			throws SQLException;

	/**
	 * Insert Item into SPAD.
	 * 
	 * @param item
	 * @throws SQLException
	 */
	public void insertItem(Item item) throws SQLException;

	/**
	 * Insert Items into SPAD.
	 * 
	 * @param items
	 * @throws SQLException
	 */
	public void insertItems(ArrayList<Item> items) throws SQLException;
}
