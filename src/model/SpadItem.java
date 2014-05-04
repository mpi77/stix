package model;

/**
 * @author MPI
 * @version 04.05.2014/1.0
 */
public class SpadItem {
	private String companyId;
	private Double avPrice;
	private Double avVolume;
	private Double dPriceMin;
	private Double dPriceMax;
	private Double dPriceAvg;

	/*
	 * prumerna cena akcie, prumerny prodej akcii za den, aktualni odchylka od
	 * minima, aktualni odchylka od maxima, aktualni odchylka od dlouhodobeho
	 * prumeru
	 */

	public SpadItem(String companyId) {
		this.companyId = companyId;
		this.avPrice = null;
		this.avVolume = null;
		this.dPriceMin = null;
		this.dPriceMax = null;
		this.dPriceAvg = null;
	}

	public SpadItem(String companyId, Double avPrice, Double avVolume,
			Double dPriceMin, Double dPriceMax, Double dPriceAvg) {
		super();
		this.companyId = companyId;
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

	public Double getAvPrice() {
		return avPrice;
	}

	public void setAvPrice(Double avPrice) {
		this.avPrice = avPrice;
	}

	public Double getAvVolume() {
		return avVolume;
	}

	public void setAvVolume(Double avVolume) {
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

}
