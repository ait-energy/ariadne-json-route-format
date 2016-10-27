package at.ac.ait.ariadne.routeformat.newstylexample;

public class DetailedMutableJsonClass extends MutableJsonClass {

	private String anotherDetail;

	public String getAnotherDetail() {
		return anotherDetail;
	}

	public DetailedMutableJsonClass setAnotherDetail(String anotherDetail) {
		this.anotherDetail = anotherDetail;
		return this;
	}

}
