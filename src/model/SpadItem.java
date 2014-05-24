package model;

/**
 * SpadItem (data from datastrategy by company) POJO object.
 * 
 * @author MPI
 * @version 24.05.2014/1.3
 */
public class SpadItem {
	
	private String companyId;
	private String companyName;
	private Double avPrice;
	private Long avVolume;
	private Double dPriceMin;
	private Double dPriceMax;
	private Double dPriceAvg;

	/*
	 * prumerna cena akcie, prumerny prodej akcii za den, aktualni odchylka od
	 * minima, aktualni odchylka od maxima, aktualni odchylka od dlouhodobeho
	 * prumeru
	 */

	public SpadItem(String companyId, String companyName) {
		this.companyId = companyId;
		this.companyName = companyName;
		this.avPrice = null;
		this.avVolume = null;
		this.dPriceMin = null;
		this.dPriceMax = null;
		this.dPriceAvg = null;
	}

	public SpadItem(String companyId, String companyName, Double avPrice, Long avVolume,
			Double dPriceMin, Double dPriceMax, Double dPriceAvg) {
		super();
		this.companyId = companyId;
		this.companyName = companyName;
		this.avPrice = avPrice;
		this.avVolume = avVolume;
		this.dPriceMin = dPriceMin;
		this.dPriceMax = dPriceMax;
		this.dPriceAvg = dPriceAvg;
	}

	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}
	
	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public Double getAvPrice() {
		return avPrice;
	}

	public void setAvPrice(Double avPrice) {
		this.avPrice = avPrice;
	}

	public Long getAvVolume() {
		return avVolume;
	}

	public void setAvVolume(Long avVolume) {
		this.avVolume = avVolume;
	}

	public Double getdPriceMin() {
		return dPriceMin;
	}

	public void setdPriceMin(Double dPriceMin) {
		this.dPriceMin = dPriceMin;
	}

	public Double getdPriceMax() {
		return dPriceMax;
	}

	public void setdPriceMax(Double dPriceMax) {
		this.dPriceMax = dPriceMax;
	}

	public Double getdPriceAvg() {
		return dPriceAvg;
	}

	public void setdPriceAvg(Double dPriceAvg) {
		this.dPriceAvg = dPriceAvg;
	}
	
	@Override
	public String toString() {
		return "SpadItem [companyId=" + companyId + ", companyName="
				+ companyName + ", avPrice=" + avPrice + ", avVolume="
				+ avVolume + ", dPriceMin=" + dPriceMin + ", dPriceMax="
				+ dPriceMax + ", dPriceAvg=" + dPriceAvg + "]";
	}
}
