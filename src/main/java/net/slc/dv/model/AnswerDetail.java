package net.slc.dv.model;

import lombok.Data;

import java.sql.ResultSet;
import java.util.UUID;

@Data
public class AnswerDetail {
	private String answerId;
	private String questionId;
	private String answerText;

	public AnswerDetail(String questionId, String answerText) {
		this.answerId = "";
		this.questionId = questionId;
		this.answerText = answerText;
	}

	public AnswerDetail(ResultSet set) {
		try {
			this.answerId = set.getString("AnswerID");
			this.questionId = set.getString("QuestionID");
			this.answerText = set.getString("AnswerText");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
