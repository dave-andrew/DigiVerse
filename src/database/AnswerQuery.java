package database;

import java.io.*;
import java.sql.Blob;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class AnswerQuery {

    private Connect con;

    public AnswerQuery() {
        this.con = Connect.getConnection();
    }

    public ArrayList<File> getMemberAnswer(String taskid, String userid) {

        ArrayList<File> fileList = new ArrayList<>();

        String query = "SELECT\n" +
                "\tmsfile.FileID, FileName, FileBlob, FileType\n" +
                "FROM task_answer\n" +
                "JOIN msfile\n" +
                "ON msfile.FileID = task_answer.FileID\n" +
                "WHERE TaskID = ? AND UserID = ?";

        try (PreparedStatement ps = con.prepareStatement(query)) {

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

}
