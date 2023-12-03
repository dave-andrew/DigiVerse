package net.slc.dv.database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import net.slc.dv.database.builder.NeoQueryBuilder;
import net.slc.dv.database.builder.Results;
import net.slc.dv.database.builder.enums.ConditionCompareType;
import net.slc.dv.database.builder.enums.QueryType;
import net.slc.dv.database.connection.Connect;
import net.slc.dv.helper.Closer;
import net.slc.dv.helper.DateManager;
import net.slc.dv.helper.toast.ToastBuilder;
import net.slc.dv.model.*;

public class TaskQuery {

    private final Connect connect;

    public TaskQuery() {
        this.connect = Connect.getConnection();
    }

    public void createFileTask(Task task, String classid) {

        String query = "INSERT INTO mstask VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        String query2 = "INSERT INTO class_task VALUES (?, ?)";

        try (var ps = connect.prepareStatement(query);
                var ps2 = connect.prepareStatement(query2)) {
            assert ps != null;
            ps.setString(1, task.getId());
            ps.setString(2, task.getUserid());
            ps.setString(3, task.getTitle());
            ps.setString(4, task.getDescription());
            ps.setString(5, task.getTaskType().toString());
            ps.setString(6, task.getCreatedAt());
            ps.setString(7, task.getDeadlineAt());
            ps.setBoolean(8, task.isScored());

            ps.executeUpdate();

            assert ps2 != null;
            ps2.setString(1, classid);
            ps2.setString(2, task.getId());

            ps2.executeUpdate();

            ToastBuilder.buildNormal().setText("Task Created!").build();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void createQuestionTask(Task task, List<Question> questionList, String classId) {
        // TODO DEADLINE
        String query =
                "INSERT INTO mstask (TaskID, UserID, TaskTitle, TaskDesc, TaskType, CreatedAt, DeadlineAt, Scored) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        String query2 = "INSERT INTO class_task VALUES (?, ?)";
        String query3 =
                "INSERT INTO msquestion (QuestionID, TaskID, QuestionType, QuestionText, QuestionChoice, QuestionAnswer) VALUES (?, ?, ?, ?, ?, ?)";

        try (var ps = connect.prepareStatement(query);
                var ps2 = connect.prepareStatement(query2);
                var ps3 = connect.prepareStatement(query3)) {
            assert ps != null;
            ps.setString(1, task.getId());
            ps.setString(2, task.getUserid());
            ps.setString(3, task.getTitle());
            ps.setString(4, task.getDescription());
            ps.setString(5, task.getTaskType().toString());
            ps.setString(6, task.getCreatedAt());
            ps.setString(7, task.getDeadlineAt());
            ps.setBoolean(8, task.isScored());

            ps.executeUpdate();

            assert ps2 != null;
            ps2.setString(1, classId);
            ps2.setString(2, task.getId());

            ps2.executeUpdate();

            for (Question question : questionList) {
                assert ps3 != null;
                ps3.setString(1, question.getQuestionID());
                ps3.setString(2, task.getId());
                ps3.setString(3, question.getQuestionType().toString());
                ps3.setString(4, question.getQuestionText());
                ps3.setString(5, question.getQuestionChoice());
                ps3.setString(6, question.getQuestionAnswer());

                ps3.addBatch();
            }

            assert ps3 != null;
            ps3.executeBatch();

            ToastBuilder.buildNormal().setText("Task Created!").build();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public ArrayList<Task> getClassroomTask(String classid) {
        ArrayList<Task> taskList = new ArrayList<>();

        String query = "SELECT\n"
                + "    class_task.TaskID, TaskTitle, TaskType, TaskDesc, DeadlineAt, CreatedAt, Scored, msuser.UserID, UserName, UserEmail, UserDOB, UserProfile\n"
                + "FROM class_task\n"
                + "JOIN mstask ON class_task.TaskID = mstask.TaskID\n"
                + "JOIN msuser ON mstask.UserID = msuser.UserID\n"
                + "WHERE class_task.ClassID = ?\n"
                + "ORDER BY DeadlineAt ASC";

        return getTasks(classid, taskList, query);
    }

    public ArrayList<Task> getScoredClassroomTask(String classid) {
        ArrayList<Task> taskList = new ArrayList<>();
        String query = "SELECT\n"
                + "    class_task.TaskID, TaskTitle, TaskType, TaskDesc, DeadlineAt, CreatedAt, Scored, msuser.UserID, UserName, UserEmail, UserDOB, UserProfile\n"
                + "FROM class_task\n"
                + "JOIN mstask ON class_task.TaskID = mstask.TaskID\n"
                + "JOIN msuser ON mstask.UserID = msuser.UserID\n"
                + "WHERE class_task.ClassID = ? AND Scored = 1\n"
                + "ORDER BY DeadlineAt ASC";

        return getTasks(classid, taskList, query);
    }

    private ArrayList<Task> getTasks(String classid, ArrayList<Task> taskList, String query) {
        try (PreparedStatement ps = connect.prepareStatement(query)) {
            assert ps != null;
            ps.setString(1, classid);

            try (var rs = ps.executeQuery()) {
                while (rs.next()) {
                    User user = new User(rs);
                    Task task = new Task(rs, user);
                    taskList.add(task);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return taskList;
    }

    public ArrayList<Task> fetchTaskByDate(String date) {
        String query = "SELECT * FROM class_task\n" + "JOIN mstask ON class_task.TaskID = mstask.TaskID\n"
                + "JOIN msuser ON mstask.UserID = msuser.UserID\n"
                + "JOIN msclass ON msclass.ClassID = class_task.ClassID\n"
                + "WHERE DATE(DeadlineAt) = ? AND "
                + "class_task.ClassID IN (SELECT ClassID FROM class_member WHERE UserID = ?)\n";

        ArrayList<Task> taskList = new ArrayList<>();
        try (PreparedStatement ps = connect.prepareStatement(query)) {
            assert ps != null;
            ps.setString(1, date);
            ps.setString(2, LoggedUser.getInstance().getId());

            try (var rs = ps.executeQuery()) {
                while (rs.next()) {

                    Classroom classroom = new Classroom(rs);
                    User user = new User(rs);
                    Task task = new Task(rs, user, classroom);
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

        String query = "SELECT * FROM class_task\n" + "JOIN mstask ON class_task.TaskID = mstask.TaskID\n"
                + "JOIN msuser ON mstask.UserID = msuser.UserID\n"
                + "JOIN msclass ON msclass.ClassID = class_task.ClassID\n"
                + "WHERE class_task.ClassID IN (SELECT ClassID FROM class_member WHERE UserID = ? AND Role = ?) AND \n"
                + "mstask.TaskID NOT IN (SELECT TaskID FROM answer_header WHERE UserID = ?)\n";

        try (PreparedStatement ps = connect.prepareStatement(query)) {
            assert ps != null;
            ps.setString(1, userid);
            ps.setString(2, "Student");
            ps.setString(3, userid);

            try (var rs = ps.executeQuery()) {
                while (rs.next()) {
                    Classroom classroom = new Classroom(rs);
                    User user = new User(rs);
                    Task task = new Task(rs, user, classroom);
                    taskList.add(task);
                }
            }
        } catch (Exception ignored) {
        }

        return taskList;
    }

    public ArrayList<Task> fetchUserFinishedTask(String userid) {

        ArrayList<Task> taskList = new ArrayList<>();

        String query = "SELECT * FROM class_task\n" + "JOIN mstask ON class_task.TaskID = mstask.TaskID\n"
                + "JOIN msuser ON mstask.UserID = msuser.UserID\n"
                + "JOIN msclass ON msclass.ClassID = class_task.ClassID\n"
                + "WHERE class_task.ClassID IN (SELECT ClassID FROM class_member WHERE UserID = ? AND Role = ?) AND \n"
                + "mstask.TaskID IN (SELECT TaskID FROM answer_header WHERE UserID = ?)\n";

        try (PreparedStatement ps = connect.prepareStatement(query)) {

            assert ps != null;
            ps.setString(1, userid);
            ps.setString(2, "Student");
            ps.setString(3, userid);

            try (var rs = ps.executeQuery()) {
                while (rs.next()) {

                    Classroom classroom = new Classroom(rs);
                    User user = new User(rs);
                    Task task = new Task(rs, user, classroom);
                    taskList.add(task);
                }
            }

        } catch (Exception ignored) {
        }

        return taskList;
    }

    public ArrayList<Task> fetchClassroomPendingTask(String classid) {

        ArrayList<Task> taskList = new ArrayList<>();

        String query = "SELECT * FROM class_task\n" + "JOIN mstask ON class_task.TaskID = mstask.TaskID\n"
                + "JOIN msuser ON mstask.UserID = msuser.UserID\n"
                + "JOIN msclass ON msclass.ClassID = class_task.ClassID\n"
                + "WHERE class_task.ClassID = ? AND \n"
                + "mstask.DeadlineAt > ?\n"
                + "ORDER BY mstask.DeadlineAt ASC\n"
                + "LIMIT 2";

        try (PreparedStatement ps = connect.prepareStatement(query)) {

            assert ps != null;
            ps.setString(1, classid);
            ps.setString(2, DateManager.getNow());

            try (var rs = ps.executeQuery()) {
                while (rs.next()) {

                    Classroom classroom = new Classroom(rs);
                    User user = new User(rs);
                    Task task = new Task(rs, user, classroom);
                    taskList.add(task);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return taskList;
    }

    public List<Question> fetchQuestions(String taskID) {
        List<Question> questionList = new ArrayList<>();

        try (Closer closer = new Closer()) {
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
