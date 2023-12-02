package net.slc.dv.database;

import net.slc.dv.database.builder.NeoQueryBuilder;
import net.slc.dv.database.builder.Results;
import net.slc.dv.database.builder.enums.QueryType;
import net.slc.dv.helper.Closer;
import net.slc.dv.helper.DateManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.UUID;

public class AnswerQuery {

    public ArrayList<File> getMemberAnswer(String taskid, String userid) {
        ArrayList<File> fileList = new ArrayList<>();

        try (Closer closer = new Closer()) {
            NeoQueryBuilder queryBuilder = new NeoQueryBuilder(QueryType.SELECT)
                    .table("answer_header")
                    .columns("msfile.FileID", "FileName", "FileBlob", "FileType")
                    .join("answer_header", "AnswerID", "answer_detail", "AnswerID")
                    .join("answer_detail", "FileID", "msfile", "FileID")
                    .condition("TaskID", "=", taskid)
                    .condition("UserID", "=", userid);

            Results results = closer.add(queryBuilder.getResults());
            ResultSet set = closer.add(results.getResultSet());
            while (set.next()) {
                Blob blob = set.getBlob("FileBlob");

                File outputFile = convertBlobToFile(blob, set.getString("FileName"));
                fileList.add(outputFile);
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
        try (Closer closer = new Closer()) {
            NeoQueryBuilder queryBuilder = new NeoQueryBuilder(QueryType.SELECT)
                    .table("answer_header")
                    .columns("AnswerID")
                    .condition("TaskID", "=", taskid)
                    .condition("UserID", "=", userid);

            Results results = closer.add(queryBuilder.getResults());
            ResultSet set = closer.add(results.getResultSet());

            return set.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void markAsDone(String taskid, String userid) {
        try (Closer closer = new Closer()) {
            NeoQueryBuilder queryBuilder = new NeoQueryBuilder(QueryType.INSERT)
                    .table("answer_header")
                    .values("AnswerID", UUID.randomUUID().toString())
                    .values("TaskID", taskid)
                    .values("UserID", userid)
                    .values("Score", null)
                    .values("CreatedAt", DateManager.getNow());

            closer.add(queryBuilder.getResults());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void markUndone(String taskid, String userid) {
        try (Closer closer = new Closer()) {
            NeoQueryBuilder queryBuilder = new NeoQueryBuilder(QueryType.DELETE)
                    .table("answer_header")
                    .condition("TaskID", "=", taskid)
                    .condition("UserID", "=", userid);

            closer.add(queryBuilder.getResults());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Integer getAnswerScore(String taskid, String userid) {
        try (Closer closer = new Closer()) {
            NeoQueryBuilder queryBuilder = new NeoQueryBuilder(QueryType.SELECT)
                    .table("answer_header")
                    .columns("score")
                    .condition("TaskID", "=", taskid)
                    .condition("UserID", "=", userid);

            Results results = closer.add(queryBuilder.getResults());
            ResultSet set = closer.add(results.getResultSet());
            if (!set.next()) {
                return null;
            }

            return set.getInt("SCORE");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public boolean scoreAnswer(String taskid, String userid, int score) {
        try (Closer closer = new Closer()) {
            NeoQueryBuilder queryBuilder = new NeoQueryBuilder(QueryType.UPDATE)
                    .table("answer_header")
                    .join("answer_header", "TaskID", "mstask", "TaskID")
                    .values("answer_header.Score", score)
                    .condition("mstask.TaskTitle", "=", taskid)
                    .condition("answer_header.UserID", "=", userid);

            closer.add(queryBuilder.getResults());
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }
}
