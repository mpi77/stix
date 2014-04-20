package model;

import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;

/**
 * @author MPI
 * @version 20.04.2014/1.0
 */
public interface IParser {
	public ArrayList<Item> parseSpad(String filename, Date minimalDate) throws IOException;
}
