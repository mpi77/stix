package test;

import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;

import model.CsvParser;
import model.IParser;
import model.Item;

public class CsvParserTest {
	public static void main(String[] args) {
		IParser p = new CsvParser();
		try {
			ArrayList<Item> r = p.parseSPAD("bcpp_data.csv", new Date(System.currentTimeMillis() - 2000000000));
			for(int i = 0; i< r.size(); i++){
				System.out.println(r.get(i).toString());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
