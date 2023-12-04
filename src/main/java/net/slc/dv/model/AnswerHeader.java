package net.slc.dv.model;

import lombok.Data;
import net.slc.dv.helper.DateManager;
import org.jetbrains.annotations.Nullable;

import java.sql.ResultSet;
import java.util.UUID;

@Data
public class AnswerHeader {
	private String id;
	private String taskid;
	private String userid;
	private boolean finished;
	@Nullable
	private Double score;
	private String createdAt;
	private String finishedAt;

	public AnswerHeader(String taskid, String userid, boolean finished, @Nullable Double score, String finishedAt) {
		this.id = UUID.randomUUID().toString();
		this.taskid = taskid;
		this.userid = userid;
		this.finished = finished;
		this.score = score;
		this.createdAt = DateManager.getNow();
		this.finishedAt = finishedAt;
	}

	public AnswerHeader(ResultSet set) {
		try {
			this.id = set.getString("AnswerID");
			this.taskid = set.getString("TaskID");
			this.userid = set.getString("UserID");
			this.finished = set.getBoolean("Finished");
			this.score = set.getDouble("Score");
			if(set.wasNull()) this.score = null;
			this.createdAt = set.getString("CreatedAt");
			this.finishedAt = set.getString("FinishedAt");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
