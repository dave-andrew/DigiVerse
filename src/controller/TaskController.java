package controller;

import database.TaskQuery;
import helper.StageManager;
import helper.Toast;
import model.LoggedUser;
import model.Task;

import java.util.ArrayList;

public class TaskController {

    private TaskQuery taskQuery;

    public TaskController() {
        this.taskQuery = new TaskQuery();
    }

    public Task createTask(String title, String description, String deadlineAt, boolean scored, String classid) {

        if(title.isEmpty() || description.isEmpty() || deadlineAt.isEmpty()) {
            Toast.makeError(StageManager.getInstance(), "Please fill all the fields!", 2000, 500, 500);
            return null;
        }

        Task newTask = new Task(LoggedUser.getInstance().getId(), LoggedUser.getInstance(), title, description, deadlineAt, scored);

        return this.taskQuery.createTask(newTask, classid);
    }

    public ArrayList<Task> getClassroomTask(String classid) {
        return taskQuery.getClassroomTask(classid);
    }

    public ArrayList<Task> getScoredClassroomTask(String classid) {
        return taskQuery.getScoredClassroomTask(classid);
    }
}
