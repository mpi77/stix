package model;

import java.sql.Date;

/**
 * Item (raw data from bcpp) POJO object.
 * 
 * @author MPI
 * @version 27.05.2014/1.2
 */
public class Item {

	public static final String DB_MAP_ID = "id";
	public static final String DB_MAP_NAME = "name";
	public static final String DB_MAP_DATE = "date";
	public static final String DB_MAP_OPEN = "open_value";
	public static final String DB_MAP_CLOSE = "close_value";
	public static final String DB_MAP_MAX = "max_value";
	public static final String DB_MAP_MIN = "min_value";
	public static final String DB_MAP_VOLUME = "volume";
	
	private Integer id;
	private String name;
	private Date date;
	private Double open;
	private Double close;
	private Double max;
	private Double min;
	private Long volume;
	
	public Item(){
		this.id = null;
		this.name = null;
		this.date = null;
		this.open = null;
		this.close = null;
		this.max = null;
		this.min = null;
		this.volume = null;
	}

	public Item(Integer id, String name, Date date, Double open, Double close, Double max,
			Double min, Long volume) {
		this.id = id;
		this.name = name;
		this.date = date;
		this.open = open;
		this.close = close;
		this.max = max;
		this.min = min;
		this.volume = volume;
	}
	
	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id){
		this.id = id;
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
	
	@Override
	public String toString() {
		return "Item [id=" + id + ", name=" + name + ", date=" + date
				+ ", open=" + open + ", close=" + close + ", max=" + max
				+ ", min=" + min + ", volume=" + volume + "]";
	}

	@Override
	public boolean equals(Object obj) {
		boolean r = false;
		if (obj instanceof Item){
			Item item = (Item)obj;
			r = (this.getId() == item.getId() && this.getName().equals(item.getName()) 
					&& this.getOpen().doubleValue() == item.getOpen().doubleValue()
					&& this.getClose().doubleValue() == item.getClose().doubleValue()
					&& this.getMax().doubleValue() == item.getMax().doubleValue()
					&& this.getMin().doubleValue() == item.getMin().doubleValue()
					&& this.getVolume().longValue() == item.getVolume().longValue());
		} 
	    return r;
	}
}
