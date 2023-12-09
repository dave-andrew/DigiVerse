package net.slc.dv.enums;

import net.slc.dv.resources.Text;
import net.slc.dv.storage.TextStorage;

public enum TestStatus {
	NOT_SUBMITTED("Not Submitted"),
	SUBMITTED("Submitted"),
	DONE("Done"),
	NOT_DONE("Not Done"),
	NOT_ATTEMPTED("Not Attempted");

	private final String status;

	TestStatus(String status) {
		this.status = status;
	}

	public String getString() {
		return status;
	}

	public String getStatusLanguage() {
		if(this.equals(NOT_SUBMITTED)) {
			return TextStorage.getText(Text.NOT_SUBMITTED);
		}
		if(this.equals(SUBMITTED)) {
			return TextStorage.getText(Text.SUBMITTED);
		}
		if(this.equals(DONE)) {
			return TextStorage.getText(Text.DONE);
		}
		if(this.equals(NOT_DONE)) {
			return TextStorage.getText(Text.NOT_DONE);
		}
		if(this.equals(NOT_ATTEMPTED)) {
			return TextStorage.getText(Text.NOT_ATTEMPTED);
		}
		return null;
	}

	public static TestStatus getStatus(String status) {
		for (TestStatus s : TestStatus.values()) {
			if (s.getString().equals(status)) {
				return s;
			}
		}
		return null;
	}
}
