package controller;

import database.TaskQuery;
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

    public ArrayList<Task> fetchTaskByDate(String date) {
        return taskQuery.fetchTaskByDate(date);
    }

    public ArrayList<Task> fetchUserPendingTask(String userid) {
        return taskQuery.fetchUserPendingTask(userid);
    }

    public ArrayList<Task> fetchUserFinishedTask(String userid) {
        return taskQuery.fetchUserFinishedTask(userid);
    }
    public ArrayList<Task> fetchClassroomPendingTask(String classid) {
        return taskQuery.fetchClassroomPendingTask(classid);
    }
}
