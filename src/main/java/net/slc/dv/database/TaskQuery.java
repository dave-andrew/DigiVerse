package net.slc.dv.database;

import net.slc.dv.database.builder.BatchQueryBuilder;
import net.slc.dv.database.builder.NeoQueryBuilder;
import net.slc.dv.database.builder.Results;
import net.slc.dv.database.builder.enums.ConditionCompareType;
import net.slc.dv.database.builder.enums.QueryType;
import net.slc.dv.database.connection.Connect;
import net.slc.dv.helper.Closer;
import net.slc.dv.helper.DateManager;
import net.slc.dv.helper.toast.ToastBuilder;
import net.slc.dv.model.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TaskQuery {

    private final Connect connect;

    public TaskQuery() {
        this.connect = Connect.getConnection();
    }

    public void createFileTask(Task task, String classid) {
        try (Closer closer = new Closer()) {
            NeoQueryBuilder queryBuilder = new NeoQueryBuilder(QueryType.INSERT)
                    .table("mstask")
                    .values("TaskID", task.getId())
                    .values("UserID", task.getUserid())
                    .values("TaskTitle", task.getTitle())
                    .values("TaskDesc", task.getDescription())
                    .values("TaskType", task.getTaskType().toString())
                    .values("CreatedAt", task.getCreatedAt())
                    .values("DeadlineAt", task.getDeadlineAt())
                    .values("Scored", task.isScored());

            closer.add(queryBuilder.getResults());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        try (Closer closer = new Closer()) {
            NeoQueryBuilder queryBuilder = new NeoQueryBuilder(QueryType.INSERT)
                    .table("class_task")
                    .values("ClassID", classid)
                    .values("TaskID", task.getId());

            closer.add(queryBuilder.getResults());

            ToastBuilder.buildNormal().setText("Task Created!").build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public void createQuestionTask(Task task, List<Question> questionList, String classId) {
        this.createFileTask(task, classId);

        try {
            BatchQueryBuilder batchQueryBuilder = new BatchQueryBuilder();
            for (Question question : questionList) {
                NeoQueryBuilder queryBuilder = new NeoQueryBuilder(QueryType.INSERT)
                        .table("msquestion")
                        .values("QuestionID", question.getQuestionID())
                        .values("TaskID", task.getId())
                        .values("QuestionType", question.getQuestionType().toString())
                        .values("QuestionText", question.getQuestionText())
                        .values("QuestionChoice", question.getQuestionChoice())
                        .values("QuestionAnswer", question.getQuestionKey());

                batchQueryBuilder.add(queryBuilder);
            }

            batchQueryBuilder.executeBatch();
            ToastBuilder.buildNormal().setText("Task Created!").build();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public ArrayList<Task> getClassroomTask(String classid) {
        return this.getTasks(classid, false);
    }

    public ArrayList<Task> getScoredClassroomTask(String classid) {
        return this.getTasks(classid, true);
    }

    private ArrayList<Task> getTasks(String classId, boolean scored) {
        ArrayList<Task> taskList = new ArrayList<>();
        try (Closer closer = new Closer()) {
            NeoQueryBuilder queryBuilder = new NeoQueryBuilder(QueryType.SELECT)
                    .table("class_task")
                    .join("class_task", "TaskID", "mstask", "TaskID")
                    .join("mstask", "UserID", "msuser", "UserID")
                    .condition("class_task.ClassID", "=", classId)
                    .orderBy("DeadlineAt", "ASC");

            if (scored) {
                queryBuilder.condition("Scored", "=", "1");
            }

            Results results = closer.add(queryBuilder.getResults());
            ResultSet set = closer.add(results.getResultSet());
            while (set.next()) {
                User user = new User(set);
                Task task = new Task(set, user);

                taskList.add(task);
            }
        } catch (Exception e) {
            e.printStackTrace();
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

        try (Closer closer = new Closer()) {
            NeoQueryBuilder queryBuilder = new NeoQueryBuilder(QueryType.SELECT)
                    .table("class_task")
                    .join("class_task", "TaskID", "mstask", "TaskID")
                    .join("mstask", "UserID", "msuser", "UserID")
                    .join("msclass", "ClassID", "class_task", "ClassID")
                    .condition("class_task.ClassID", "=", classid)
                    .condition("mstask.DeadlineAt", ">", DateManager.getNow())
                    .orderBy("mstask.DeadlineAt", "ASC")
                    .limit(2);

            Results results = closer.add(queryBuilder.getResults());
            ResultSet set = closer.add(results.getResultSet());
            while (set.next()) {
                Classroom classroom = new Classroom(set);
                User user = new User(set);
                Task task = new Task(set, user, classroom);

                taskList.add(task);
            }
        } catch (SQLException e) {
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
