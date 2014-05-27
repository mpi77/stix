package test;

import static org.junit.Assert.*;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import model.CsvParser;
import model.IParser;
import model.Item;

import org.junit.Test;

/**
 * @author MPI
 * @version 27.05.2014/1.0
 */
public class CsvParserUnitTest {

	@Test
	public void testParseSpadFile() {

		ArrayList<Item> expected = new ArrayList<Item>();
		ArrayList<Item> fetched = null;
		IParser p = new CsvParser();
		
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
		java.util.Date parsed = null;
		try {
			parsed = (java.util.Date) format.parse("20130701");
		} catch (ParseException e1) {
			e1.printStackTrace();
			fail("Exception occured.");
		}
		java.sql.Date minDate = new java.sql.Date(parsed.getTime());
		
		expected.add(new Item(null, "ERBAG", minDate, new Double(
				530.0), new Double(529.0), new Double(539.0),
				new Double(518.2), new Long(109011)));
		expected.add(new Item(null, "VIG", minDate, new Double(
				935.0), new Double(944.0), new Double(947.3),
				new Double(929.0), new Long(15514)));
		
		try {
			fetched = p.parseSpad("parser_test_data.csv", minDate);
		} catch (IOException e) {
			e.printStackTrace();
			fail("Exception occured.");
		}
		assertEquals(fetched.size(), 2);
		assertEquals(expected, fetched);
	}
}
