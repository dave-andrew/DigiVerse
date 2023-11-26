package database;

import helper.DateManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.UUID;

public class AnswerQuery {

    private final Connect connect;

    public AnswerQuery() {
        this.connect = Connect.getConnection();
    }

    public ArrayList<File> getMemberAnswer(String taskid, String userid) {

        ArrayList<File> fileList = new ArrayList<>();

        String query = "SELECT\n" +
                "\tmsfile.FileID, FileName, FileBlob, FileType\n" +
                "FROM answer_header\n" +
                "JOIN answer_detail\n" +
                "ON answer_detail.AnswerID = answer_header.AnswerID\n" +
                "JOIN msfile\n" +
                "ON msfile.FileID = answer_detail.FileID\n" +
                "WHERE TaskID = ? AND UserID = ?";

        try (PreparedStatement ps = connect.prepareStatement(query)) {
            assert ps != null;
            ps.setString(1, taskid);
            ps.setString(2, userid);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Blob blob = rs.getBlob("FileBlob");

                    File outputFile = convertBlobToFile(blob, rs.getString("FileName"));

                    fileList.add(outputFile);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return fileList;
    }

    private File convertBlobToFile(Blob blob, String fileName) {
        File file = new File(fileName);

        try (InputStream inStream = blob.getBinaryStream();
             FileOutputStream fileOutputStream = new FileOutputStream(file)) {

            byte[] buffer = new byte[1024];
            int bytesRead;

            while ((bytesRead = inStream.read(buffer)) > 0) {
                fileOutputStream.write(buffer, 0, bytesRead);
            }

        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }

        return file;
    }

    public boolean checkAnswer(String taskid, String userid) {
        String query = "SELECT\n" +
                "\tAnswerID\n" +
                "FROM answer_header\n" +
                "WHERE TaskID = ? AND UserID = ?";

        try (PreparedStatement ps = connect.prepareStatement(query)) {

            assert ps != null;
            ps.setString(1, taskid);
            ps.setString(2, userid);

            try (ResultSet rs = ps.executeQuery()) {

                return rs.next();
            }

        } catch (SQLException e) {
            return false;
        }
    }

    public void markAsDone(String taskid, String userid) {
        String query = "INSERT INTO answer_header VALUES (?, ?, ?, NULL, ?)";

        try (PreparedStatement ps = connect.prepareStatement(query)) {
            assert ps != null;
            ps.setString(1, UUID.randomUUID().toString());
            ps.setString(2, taskid);
            ps.setString(3, userid);
            ps.setString(4, DateManager.getNow());

            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void markUndone(String taskid, String userid) {
        String query = "DELETE FROM answer_header WHERE TaskID = ? AND UserID = ?";

        try (PreparedStatement ps = connect.prepareStatement(query)) {
            assert ps != null;
            ps.setString(1, taskid);
            ps.setString(2, userid);

            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Integer getAnswerScore(String taskid, String userid) {
        String query = "SELECT SCORE FROM answer_header WHERE TaskID = ? AND UserID = ?";

        try (PreparedStatement ps = connect.prepareStatement(query)) {

            assert ps != null;
            ps.setString(1, taskid);
            ps.setString(2, userid);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Object scoreObj = rs.getObject("SCORE");

                    if (scoreObj != null) {
                        return (Integer) scoreObj;
                    } else {
                        return null;
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public boolean scoreAnswer(String taskid, String userid, int score) {
        String query = "UPDATE answer_header " +
                "JOIN mstask ON answer_header.TaskID = mstask.TaskID " +
                "SET answer_header.Score = ? " +
                "WHERE mstask.TaskTitle = ? AND answer_header.UserID = ?";

        try (PreparedStatement ps = connect.prepareStatement(query)) {
            assert ps != null;
            ps.setInt(1, score);
            ps.setString(2, taskid);
            ps.setString(3, userid);

            ps.executeUpdate();

            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
