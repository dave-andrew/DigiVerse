package net.slc.dv.database;

import net.slc.dv.database.connection.Connect;
import net.slc.dv.helper.toast.ToastBuilder;
import net.slc.dv.model.Answer;

import java.io.File;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class FileQuery {

    private final Connect connect;
    private final Connection connection;

    public FileQuery() {
        this.connect = Connect.getConnection();
        this.connection = connect.getConnect();
    }

    public void uploadTaskAnswer(Answer answer) {
        String query = "SELECT FileID FROM answer_header JOIN answer_detail ON answer_header.AnswerID = answer_detail.AnswerID WHERE TaskID = ? AND UserID = ? ";
        String deleteFile = "DELETE FROM msfile WHERE FileID = ?";
        String deleteAnswerQuery = "DELETE FROM answer_header WHERE TaskID = ? AND UserID = ?";
        String query2 = "INSERT INTO answer_header VALUES (?, ?, ?, NULL, ?)";
        String query3 = "INSERT INTO answer_detail VALUES (?, ?)";
        String insertFileQuery = "INSERT INTO msfile VALUES (?, ?, ?, ?)";

        try {
            connection.setAutoCommit(false); // Disable auto-commit

            // Get previous answer file id
            List<String> fileidList = new ArrayList<>();
            try (PreparedStatement ps = connect.prepareStatement(query)) {
                assert ps != null;
                ps.setString(1, answer.getTaskid());
                ps.setString(2, answer.getUserid());

                try (var rs = ps.executeQuery()) {
                    while (rs.next()) {
                        fileidList.add(rs.getString("FileID"));
                    }
                }
            }

            try (PreparedStatement deleteFileStatement = connect.prepareStatement(deleteFile)) {
                assert deleteFileStatement != null;
                for (String fileid : fileidList) {
                    deleteFileStatement.setString(1, fileid);
                    deleteFileStatement.executeUpdate();
                }
            }

            // Delete previous answer if it exists
            try (PreparedStatement deleteStatement = connect.prepareStatement(deleteAnswerQuery)) {
                assert deleteStatement != null;
                deleteStatement.setString(1, answer.getTaskid());
                deleteStatement.setString(2, answer.getUserid());
                deleteStatement.executeUpdate();
            }

            // Insert new answer header
            try (PreparedStatement insertHeaderStatement = connect.prepareStatement(query2)) {
                assert insertHeaderStatement != null;
                insertHeaderStatement.setString(1, answer.getId());
                insertHeaderStatement.setString(2, answer.getTaskid());
                insertHeaderStatement.setString(3, answer.getUserid());
                insertHeaderStatement.setString(4, answer.getCreatedAt());
                insertHeaderStatement.executeUpdate();
            }

            // Insert new answer details and files
            try (PreparedStatement insertDetailStatement = connect.prepareStatement(query3);
                 PreparedStatement insertFileStatement = connect.prepareStatement(insertFileQuery)) {

                for (File file : answer.getFileList()) {
                    String fileType = getFileType(file);
                    String fileid = String.valueOf(UUID.randomUUID());

                    // Insert file
                    assert insertFileStatement != null;
                    insertFileStatement.setString(1, fileid);
                    insertFileStatement.setString(2, file.getName());
                    insertFileStatement.setBlob(3, new FileInputStream(file));
                    insertFileStatement.setString(4, fileType);
                    insertFileStatement.executeUpdate();

                    // Insert answer detail
                    assert insertDetailStatement != null;
                    insertDetailStatement.setString(1, answer.getId());
                    insertDetailStatement.setString(2, fileid);
                    insertDetailStatement.executeUpdate();
                }
            }

            connection.commit();
            ToastBuilder.buildNormal().setText("Answer uploaded successfully!").build();
        } catch (Exception e) {
            try {
                if (connection != null) {
                    connection.rollback(); // Rollback the transaction in case of an exception
                }
            } catch (SQLException rollbackException) {
                rollbackException.printStackTrace();
            }

            e.printStackTrace();
        } finally {
            try {
                if (connection != null) {
                    connection.setAutoCommit(true);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private String getFileType(File file) {
        String fileName = file.getName();
        int dotIndex = fileName.lastIndexOf('.');

        if (dotIndex > 0 && dotIndex < fileName.length() - 1) {
            return fileName.substring(dotIndex + 1).toLowerCase();
        }

        return "";
    }

}
