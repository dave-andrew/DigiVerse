package database;

import helper.StageManager;
import helper.Toast;
import model.Task;
import model.User;

import java.sql.PreparedStatement;
import java.util.ArrayList;

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
            ps.setString(5, task.getCreatedAt());
            ps.setString(6, task.getDeadlineAt());
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

    public ArrayList<Task> getClassroomTask(String classid) {
        ArrayList<Task> taskList = new ArrayList<>();
        String query = "SELECT\n" +
                "    class_task.TaskID, TaskTitle, TaskDesc, DeadlineAt, CreatedAt, Scored, msuser.UserID, UserName, UserEmail, UserAge, UserProfile\n" +
                "FROM class_task\n" +
                "JOIN mstask ON class_task.TaskID = mstask.TaskID\n" +
                "JOIN msuser ON mstask.UserID = msuser.UserID\n" +
                "WHERE class_task.ClassID = ?\n" +
                "ORDER BY DeadlineAt ASC";

        PreparedStatement ps = connect.prepareStatement(query);
        try {
            assert ps != null;
            ps.setString(1, classid);

            try(var rs = ps.executeQuery()) {
                while(rs.next()) {
                    User user = new User(rs.getString("UserID"), rs.getString("UserName"), rs.getString("UserEmail"), "", rs.getInt("UserAge"), rs.getBlob("UserProfile"));
                    Task task = new Task(rs.getString("TaskID"), rs.getString("UserID"), user, rs.getString("TaskTitle"), rs.getString("TaskDesc"), rs.getString("DeadlineAt"), rs.getString("CreatedAt"), rs.getBoolean("Scored"));
                    taskList.add(task);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return taskList;
    }
}
