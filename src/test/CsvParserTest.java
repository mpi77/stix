package test;

import java.io.IOException;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import model.CsvParser;
import model.IParser;
import model.Item;

/**
 * @author MPI
 * @version 20.04.2014/1.1
 */
public class CsvParserTest {
	public static void main(String[] args) {
		IParser p = new CsvParser();
		try {
			SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
			java.util.Date parsed = (java.util.Date) format.parse("20140320");
			java.sql.Date minDate = new java.sql.Date(parsed.getTime());
			
			ArrayList<Item> r = p.parseSpad("bcpp_data.csv", minDate);
			for(int i = 0; i< r.size(); i++){
				System.out.println(r.get(i).toString());
			}
		} catch (IOException | ParseException e) {
			e.printStackTrace();
		}
	}
}
