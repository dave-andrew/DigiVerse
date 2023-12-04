package net.slc.dv.model;

import lombok.Data;
import lombok.Setter;
import net.slc.dv.enums.QuestionType;

import java.sql.ResultSet;
import java.util.UUID;

@Data
public class Question {
	private final String questionID;
	@Setter
	private String taskID;
	private final QuestionType questionType;
	private final String questionText;
	private final String questionChoice;
	private final String questionAnswer;

	public Question(QuestionType questionType, String questionText, String questionChoice, String questionAnswer) {
		this.questionID = UUID.randomUUID().toString();
		this.taskID = "";
		this.questionType = questionType;
		this.questionText = questionText;
		this.questionChoice = questionChoice;
		this.questionAnswer = questionAnswer;
	}

	public Question(ResultSet set){
		try {
			this.questionID = set.getString("QuestionID");
			this.taskID = set.getString("TaskID");
			this.questionType = QuestionType.valueOf(set.getString("QuestionType"));
			this.questionText = set.getString("QuestionText");
			this.questionChoice = set.getString("QuestionChoice");
			this.questionAnswer = set.getString("QuestionAnswer");
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

//	public Question(String questionTitle)
}
