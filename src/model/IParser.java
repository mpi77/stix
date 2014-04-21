package model;

import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;

/**
 * @author MPI
 * @version 21.04.2014/1.1
 */
public interface IParser {
	public ArrayList<Item> parseSpad(String filePath, Date minimalDate) throws IOException;
}
