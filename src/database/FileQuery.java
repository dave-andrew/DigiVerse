package database;

import helper.StageManager;
import helper.Toast;
import model.LoggedUser;

import java.io.File;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class FileQuery {

    private Connect connect;

    public FileQuery() {
        this.connect = Connect.getConnection();
    }

    public void uploadTaskAnswer(List<File> fileList, String taskid) {
        String query = "INSERT INTO msfile VALUES (?, ?, ?, ?)";
        String query2 = "INSERT INTO task_answer VALUES (?, ?, ?)";

        PreparedStatement ps = connect.prepareStatement(query);
        PreparedStatement ps2 = connect.prepareStatement(query2);

        try {
            for (File file : fileList) {
                String fileType = getFileType(file);

                String fileid = String.valueOf(UUID.randomUUID());

                assert ps != null;
                ps.setString(1, fileid);
                ps.setString(2, file.getName());
                ps.setBlob(3, new FileInputStream(file));
                ps.setString(4, fileType);
                ps.executeUpdate();

                assert ps2 != null;
                ps2.setString(1, taskid);
                ps2.setString(2, fileid);
                ps2.setString(3, LoggedUser.getInstance().getId());
                ps2.executeUpdate();
            }

            Toast.makeText(StageManager.getInstance(), "File uploaded successfully!", 2000, 500, 500);

        } catch (Exception e) {
            e.printStackTrace();
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
