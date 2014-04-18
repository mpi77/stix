package model;

import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;

public interface IParser {
	public ArrayList<Item> parseSPAD(String filename, Date minimalDate) throws IOException;
}
