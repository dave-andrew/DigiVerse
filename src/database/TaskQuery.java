package database;

import helper.StageManager;
import helper.Toast;
import model.Task;

public class TaskQuery {

    private Connect connect;
    public TaskQuery() {
        this.connect = Connect.getConnection();
    }

    public Task createTask(Task task, String classid) {

        String query = "INSERT INTO mstask VALUES (?, ?, ?, ?, ?, ?, ?)";
        String query2 = "INSERT INTO class_task VALUES (?, ?)";

        try {
            var ps = connect.prepareStatement(query);
            var ps2 = connect.prepareStatement(query2);

            assert ps != null;
            ps.setString(1, task.getId());
            ps.setString(2, task.getUserid());
            ps.setString(3, task.getTitle());
            ps.setString(4, task.getDescription());
            ps.setString(5, task.getDeadlineAt());
            ps.setString(6, task.getCreatedAt());
            ps.setBoolean(7, task.isScored());

            ps.executeUpdate();

            assert ps2 != null;
            ps2.setString(1, classid);
            ps2.setString(2, task.getId());

            ps2.executeUpdate();

            Toast.makeText(StageManager.getInstance(), "Task Created!", 2000, 500, 500);

            return task;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
