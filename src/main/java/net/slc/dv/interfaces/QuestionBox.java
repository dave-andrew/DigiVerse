package net.slc.dv.interfaces;

import net.slc.dv.enums.QuestionType;

public interface QuestionBox {
	public QuestionType getQuestionType();
	public String getQuestionAnswer();
	public String getQuestionKey();
}
