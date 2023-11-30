package net.slc.dv.database;

import net.slc.dv.database.connection.Connect;
import net.slc.dv.enums.ToastType;
import net.slc.dv.helper.DateManager;
import net.slc.dv.helper.toast.ToastBuilder;
import net.slc.dv.model.Classroom;
import net.slc.dv.model.LoggedUser;
import net.slc.dv.model.Task;
import net.slc.dv.model.User;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

public class TaskQuery {

    private final Connect connect;

    public TaskQuery() {
        this.connect = Connect.getConnection();
    }

    public Task createTask(Task task, String classid) {

        String query = "INSERT INTO mstask VALUES (?, ?, ?, ?, ?, ?, ?)";
        String query2 = "INSERT INTO class_task VALUES (?, ?)";

        try (var ps = connect.prepareStatement(query);
             var ps2 = connect.prepareStatement(query2)) {
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

            ToastBuilder.buildNormal(ToastType.NORMAL).setText("Task Created!").build();

            return task;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public ArrayList<Task> getClassroomTask(String classid) {
        ArrayList<Task> taskList = new ArrayList<>();
        String query = "SELECT\n" +
                "    class_task.TaskID, TaskTitle, TaskDesc, DeadlineAt, CreatedAt, Scored, msuser.UserID, UserName, UserEmail, UserDOB, UserProfile\n" +
                "FROM class_task\n" +
                "JOIN mstask ON class_task.TaskID = mstask.TaskID\n" +
                "JOIN msuser ON mstask.UserID = msuser.UserID\n" +
                "WHERE class_task.ClassID = ?\n" +
                "ORDER BY DeadlineAt ASC";

        return getTasks(classid, taskList, query);
    }

    public ArrayList<Task> getScoredClassroomTask(String classid) {
        ArrayList<Task> taskList = new ArrayList<>();
        String query = "SELECT\n" +
                "    class_task.TaskID, TaskTitle, TaskDesc, DeadlineAt, CreatedAt, Scored, msuser.UserID, UserName, UserEmail, UserDOB, UserProfile\n" +
                "FROM class_task\n" +
                "JOIN mstask ON class_task.TaskID = mstask.TaskID\n" +
                "JOIN msuser ON mstask.UserID = msuser.UserID\n" +
                "WHERE class_task.ClassID = ? AND Scored = 1\n" +
                "ORDER BY DeadlineAt ASC";

        return getTasks(classid, taskList, query);
    }

    private ArrayList<Task> getTasks(String classid, ArrayList<Task> taskList, String query) {
        try (PreparedStatement ps = connect.prepareStatement(query)) {
            assert ps != null;
            ps.setString(1, classid);

            try (var rs = ps.executeQuery()) {
                while (rs.next()) {
                    User user = new User(rs.getString("UserID"), rs.getString("UserName"), rs.getString("UserEmail"), "", rs.getString("UserDOB"), rs.getBlob("UserProfile"));
                    Task task = new Task(rs.getString("TaskID"), rs.getString("UserID"), user, rs.getString("TaskTitle"), rs.getString("TaskDesc"), rs.getString("DeadlineAt"), rs.getString("CreatedAt"), rs.getBoolean("Scored"));
                    taskList.add(task);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return taskList;
    }

    public ArrayList<Task> fetchTaskByDate(String date) {
        String query = "SELECT * FROM class_task\n" +
                "JOIN mstask ON class_task.TaskID = mstask.TaskID\n" +
                "JOIN msuser ON mstask.UserID = msuser.UserID\n" +
                "JOIN msclass ON msclass.ClassID = class_task.ClassID\n" +
                "WHERE DATE(DeadlineAt) = ? AND " +
                "class_task.ClassID IN (SELECT ClassID FROM class_member WHERE UserID = ?)\n";

        ArrayList<Task> taskList = new ArrayList<>();
        try (PreparedStatement ps = connect.prepareStatement(query)) {
            assert ps != null;
            ps.setString(1, date);
            ps.setString(2, LoggedUser.getInstance().getId());

            try (var rs = ps.executeQuery()) {
                while (rs.next()) {

                    Classroom classroom = new Classroom(rs.getString("ClassID"), rs.getString("ClassName"), rs.getString("ClassDesc"), rs.getString("ClassCode"), rs.getString("ClassSubject"), rs.getBlob("ClassImage"));
                    User user = new User(rs.getString("UserID"), rs.getString("UserName"), rs.getString("UserEmail"), "", rs.getString("UserDOB"), rs.getBlob("UserProfile"));
                    Task task = new Task(rs.getString("TaskID"), rs.getString("UserID"), user, rs.getString("TaskTitle"), rs.getString("TaskDesc"), rs.getString("DeadlineAt"), rs.getString("CreatedAt"), rs.getBoolean("Scored"), classroom);
                    taskList.add(task);

                }
            }

            return taskList;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return taskList;
    }

    public ArrayList<Task> fetchUserPendingTask(String userid) {

        ArrayList<Task> taskList = new ArrayList<>();

        String query = "SELECT * FROM class_task\n" +
                "JOIN mstask ON class_task.TaskID = mstask.TaskID\n" +
                "JOIN msuser ON mstask.UserID = msuser.UserID\n" +
                "JOIN msclass ON msclass.ClassID = class_task.ClassID\n" +
                "WHERE class_task.ClassID IN (SELECT ClassID FROM class_member WHERE UserID = ? AND Role = ?) AND \n" +
                "mstask.TaskID NOT IN (SELECT TaskID FROM answer_header WHERE UserID = ?)\n";

        try (PreparedStatement ps = connect.prepareStatement(query)) {
            assert ps != null;
            ps.setString(1, userid);
            ps.setString(2, "Student");
            ps.setString(3, userid);

            try (var rs = ps.executeQuery()) {
                while (rs.next()) {
                    Classroom classroom = new Classroom(rs.getString("ClassID"), rs.getString("ClassName"), rs.getString("ClassDesc"), rs.getString("ClassCode"), rs.getString("ClassSubject"), rs.getBlob("ClassImage"));
                    User user = new User(rs.getString("UserID"), rs.getString("UserName"), rs.getString("UserEmail"), "", rs.getString("UserDOB"), rs.getBlob("UserProfile"));
                    Task task = new Task(rs.getString("TaskID"), rs.getString("UserID"), user, rs.getString("TaskTitle"), rs.getString("TaskDesc"), rs.getString("DeadlineAt"), rs.getString("CreatedAt"), rs.getBoolean("Scored"), classroom);
                    taskList.add(task);

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return taskList;
    }

    public ArrayList<Task> fetchUserFinishedTask(String userid) {

        ArrayList<Task> taskList = new ArrayList<>();

        String query = "SELECT * FROM class_task\n" +
                "JOIN mstask ON class_task.TaskID = mstask.TaskID\n" +
                "JOIN msuser ON mstask.UserID = msuser.UserID\n" +
                "JOIN msclass ON msclass.ClassID = class_task.ClassID\n" +
                "WHERE class_task.ClassID IN (SELECT ClassID FROM class_member WHERE UserID = ? AND Role = ?) AND \n" +
                "mstask.TaskID IN (SELECT TaskID FROM answer_header WHERE UserID = ?)\n";

        try (PreparedStatement ps = connect.prepareStatement(query)) {

            assert ps != null;
            ps.setString(1, userid);
            ps.setString(2, "Student");
            ps.setString(3, userid);

            try (var rs = ps.executeQuery()) {
                while (rs.next()) {

                    Classroom classroom = new Classroom(rs.getString("ClassID"), rs.getString("ClassName"), rs.getString("ClassDesc"), rs.getString("ClassCode"), rs.getString("ClassSubject"), rs.getBlob("ClassImage"));
                    User user = new User(rs.getString("UserID"), rs.getString("UserName"), rs.getString("UserEmail"), "", rs.getString("UserDOB"), rs.getBlob("UserProfile"));
                    Task task = new Task(rs.getString("TaskID"), rs.getString("UserID"), user, rs.getString("TaskTitle"), rs.getString("TaskDesc"), rs.getString("DeadlineAt"), rs.getString("CreatedAt"), rs.getBoolean("Scored"), classroom);
                    taskList.add(task);

                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return taskList;
    }

    public ArrayList<Task> fetchClassroomPendingTask(String classid) {

        ArrayList<Task> taskList = new ArrayList<>();

        String query = "SELECT * FROM class_task\n" +
                "JOIN mstask ON class_task.TaskID = mstask.TaskID\n" +
                "JOIN msuser ON mstask.UserID = msuser.UserID\n" +
                "JOIN msclass ON msclass.ClassID = class_task.ClassID\n" +
                "WHERE class_task.ClassID = ? AND \n" +
                "mstask.DeadlineAt > ?\n" +
                "ORDER BY mstask.DeadlineAt ASC\n" +
                "LIMIT 2";

        try (PreparedStatement ps = connect.prepareStatement(query)) {

            assert ps != null;
            ps.setString(1, classid);
            ps.setString(2, DateManager.getNow());

            try (var rs = ps.executeQuery()) {
                while(rs.next()) {

                    Classroom classroom = new Classroom(rs.getString("ClassID"), rs.getString("ClassName"), rs.getString("ClassDesc"), rs.getString("ClassCode"), rs.getString("ClassSubject"), rs.getBlob("ClassImage"));
                    User user = new User(rs.getString("UserID"), rs.getString("UserName"), rs.getString("UserEmail"), "", rs.getString("UserDOB"), rs.getBlob("UserProfile"));
                    Task task = new Task(rs.getString("TaskID"), rs.getString("UserID"), user, rs.getString("TaskTitle"), rs.getString("TaskDesc"), rs.getString("DeadlineAt"), rs.getString("CreatedAt"), rs.getBoolean("Scored"), classroom);
                    taskList.add(task);

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return taskList;
    }
}