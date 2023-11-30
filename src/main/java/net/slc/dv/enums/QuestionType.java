package net.slc.dv.enums;

public enum QuestionType {
	MULTIPLE_CHOICE,
	TRUE_FALSE,
	ESSAY;

	public static QuestionType fromString(String type) {
		switch (type) {
			case "Multiple Choice":
				return MULTIPLE_CHOICE;
			case "True/False":
				return TRUE_FALSE;
			case "Essay":
				return ESSAY;
			default:
				return null;
		}
	}

	public static String toString(QuestionType type) {
		switch (type) {
			case MULTIPLE_CHOICE:
				return "Multiple Choice";
			case TRUE_FALSE:
				return "True/False";
			case ESSAY:
				return "Essay";
			default:
				return null;
		}
	}
}
