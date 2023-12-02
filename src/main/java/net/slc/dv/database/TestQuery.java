package net.slc.dv.database;

import net.slc.dv.database.builder.NeoQueryBuilder;
import net.slc.dv.database.builder.Results;
import net.slc.dv.database.builder.enums.ConditionCompareType;
import net.slc.dv.database.builder.enums.QueryType;
import net.slc.dv.database.connection.Connect;
import net.slc.dv.helper.Closer;
import net.slc.dv.model.Question;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TestQuery {
	private final Connect connect;

	public TestQuery() {
		this.connect = Connect.getConnection();
	}

	public List<Question> fetchQuestions(String taskID) {
		List<Question> questionList = new ArrayList<>();

		try(Closer closer = new Closer()) {
			NeoQueryBuilder queryBuilder = new NeoQueryBuilder(QueryType.SELECT)
					.columns("*")
					.table("msquestion")
					.condition("taskid", ConditionCompareType.EQUAL, taskID);

			Results results = closer.add(queryBuilder.getResults());
			ResultSet set = closer.add(results.getResultSet());

			while (set.next()) {
				Question question = new Question(set);
				questionList.add(question);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return questionList;
	}
}
