package net.slc.dv.enums;

public enum Days {
	SUN("Sun"),
	MON("Mon"),
	TUE("Tue"),
	WED("Wed"),
	THU("Thu"),
	FRI("Fri"),
	SAT("Sat");

	private String day;

	Days(String day) {
		this.day = day;
	}

	public String getDay() {
		return day;
	}

}
