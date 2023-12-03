package net.slc.dv.interfaces;

import net.slc.dv.enums.QuestionType;

public interface CreateQuestionBox {

	public QuestionType getQuestionType();
	public String getQuestionText();
	public String getQuestionAnswer();
	public String getQuestionKey();
}
