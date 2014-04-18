package core;

import model.DataStrategy;

public class Downloader implements Runnable {

	private DataStrategy ds;
	
	public Downloader(DataStrategy ds) {
		this.ds = ds;
	}

	@Override
	public void run() {
		download();
	}
	
	private void download(){
		
	}

}
