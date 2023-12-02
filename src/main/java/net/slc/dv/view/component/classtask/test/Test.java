package net.slc.dv.view.component.classtask.test;

import javafx.stage.Stage;
import lombok.Getter;
import net.slc.dv.controller.TaskController;
import net.slc.dv.model.Question;
import net.slc.dv.model.Task;

import java.util.List;

@Getter
public class Test {
	private final Stage stage;
	private final Task task;
	private final TaskController taskController;
	private List<Question> questionList;

	public Test(Stage stage, Task task) {
		this.stage = stage;
		this.task = task;
		this.taskController = new TaskController();
	}

	private void fetchQuestions() {
		this.questionList = this.taskController.fetchQuestion(this.task.getId());
	}
}
