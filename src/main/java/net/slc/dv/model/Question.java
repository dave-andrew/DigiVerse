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
	private final String questionKey;

	public Question(QuestionType questionType, String questionText, String questionChoice, String questionKey) {
		this.questionID = UUID.randomUUID().toString();
		this.taskID = "";
		this.questionType = questionType;
		this.questionText = questionText;
		this.questionChoice = questionChoice;
		this.questionKey = questionKey;
	}

	public Question(ResultSet set){
		try {
			this.questionID = set.getString("questionid");
			this.taskID = set.getString("taskid");
			this.questionType = QuestionType.valueOf(set.getString("questiontype"));
			this.questionText = set.getString("questiontext");
			this.questionChoice = set.getString("questionchoice");
			this.questionKey = set.getString("questionkey");
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

//	public Question(String questionTitle)
}
