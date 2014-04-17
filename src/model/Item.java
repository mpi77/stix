package model;

import java.sql.Date;

public class Item {
	
	public static final String DB_MAP_ID = "id";
	public static final String DB_MAP_NAME = "name";
	public static final String DB_MAP_DATE = "date";
	public static final String DB_MAP_OPEN = "open_value";
	public static final String DB_MAP_CLOSE = "close_value";
	public static final String DB_MAP_MAX = "max_value";
	public static final String DB_MAP_MIN = "min_value";
	public static final String DB_MAP_VOLUME = "volume";
	
	private String name;
	private Date date;
	private Double open;
	private Double close;
	private Double max;
	private Double min;
	private Long volume;
	
	public Item(){
		this.name = null;
		this.date = null;
		this.open = null;
		this.close = null;
		this.max = null;
		this.min = null;
		this.volume = null;
	}

	public Item(String name, Date date, Double open, Double close, Double max,
			Double min, Long volume) {
		super();
		this.name = name;
		this.date = date;
		this.open = open;
		this.close = close;
		this.max = max;
		this.min = min;
		this.volume = volume;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Double getOpen() {
		return open;
	}

	public void setOpen(Double open) {
		this.open = open;
	}

	public Double getClose() {
		return close;
	}

	public void setClose(Double close) {
		this.close = close;
	}

	public Double getMax() {
		return max;
	}

	public void setMax(Double max) {
		this.max = max;
	}

	public Double getMin() {
		return min;
	}

	public void setMin(Double min) {
		this.min = min;
	}

	public Long getVolume() {
		return volume;
	}

	public void setVolume(Long volume) {
		this.volume = volume;
	}
}
