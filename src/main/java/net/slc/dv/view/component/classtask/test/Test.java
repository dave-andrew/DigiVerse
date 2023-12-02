package net.slc.dv.view.component.classtask.test;

import lombok.Getter;
import net.slc.dv.model.Task;

@Getter
public class Test {
	private Task task;
	public Test(Task task) {
		this.task = task;
	}

	private void fetchQuestions() {

	}
}
