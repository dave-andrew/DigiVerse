package net.slc.dv.database;

import net.slc.dv.database.builder.NeoQueryBuilder;
import net.slc.dv.database.builder.Results;
import net.slc.dv.database.builder.enums.QueryType;
import net.slc.dv.database.connection.Connect;
import net.slc.dv.helper.Closer;
import net.slc.dv.helper.toast.ToastBuilder;
import net.slc.dv.model.AnswerFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class FileQuery {

    private final Connect connection;

    public FileQuery() {
        this.connection = Connect.getConnection();
    }

    public void uploadTaskAnswer(AnswerFile answer) {
        try {
            connection.getConnect().setAutoCommit(false);

            List<String> fileidList = new ArrayList<>();
            try (Closer closer = new Closer()) {
                NeoQueryBuilder queryBuilder = new NeoQueryBuilder(QueryType.SELECT)
                        .table("answer_header")
                        .columns("FileID")
                        .join("answer_header", "AnswerID", "answer_detail", "AnswerID")
                        .condition("TaskID", "=", answer.getTaskid())
                        .condition("UserID", "=", answer.getUserid());

                Results results = closer.add(queryBuilder.getResults());
                ResultSet set = closer.add(results.getResultSet());
                while (set.next()) {
                    fileidList.add(set.getString("FileID"));
                }
            }

            try (Closer closer = new Closer()) {
                for (String fileId : fileidList) {
                    NeoQueryBuilder queryBuilder = new NeoQueryBuilder(QueryType.DELETE)
                            .table("msfile")
                            .condition("FileID", "=", fileId);

                    closer.add(queryBuilder.getResults());
                }
            }

            try (Closer closer = new Closer()) {
                NeoQueryBuilder queryBuilder = new NeoQueryBuilder(QueryType.DELETE)
                        .table("answer_header")
                        .condition("TaskID", "=", answer.getTaskid())
                        .condition("UserID", "=", answer.getUserid());

                closer.add(queryBuilder.getResults());
            }

            try (Closer closer = new Closer()) {
                NeoQueryBuilder queryBuilder = new NeoQueryBuilder(QueryType.INSERT)
                        .table("answer_header")
                        .values("AnswerID", answer.getId())
                        .values("TaskID", answer.getTaskid())
                        .values("UserID", answer.getUserid())
                        .values("CreatedAt", answer.getCreatedAt());

                closer.add(queryBuilder.getResults());
            }

            try (Closer closer = new Closer()) {
                for (File file : answer.getFileList()) {
                    String fileType = getFileType(file);
                    String fileId = String.valueOf(UUID.randomUUID());

                    NeoQueryBuilder queryBuilder = new NeoQueryBuilder(QueryType.INSERT)
                            .table("msfile")
                            .values("FileID", fileId)
                            .values("FileName", file.getName())
                            .values("FileData", new FileInputStream(file))
                            .values("FileType", fileType);

                    closer.add(queryBuilder.getResults());

                    NeoQueryBuilder queryBuilder2 = new NeoQueryBuilder(QueryType.INSERT)
                            .table("answer_detail")
                            .values("AnswerID", answer.getId())
                            .values("FileID", fileId);

                    closer.add(queryBuilder2.getResults());
                }
            } catch (FileNotFoundException e) {
                connection.getConnect().rollback();
                throw new RuntimeException(e);
            }

            connection.getConnect().commit();
            ToastBuilder.buildNormal().setText("Answer uploaded successfully!").build();
        } catch (SQLException e) {
            try {
                if (connection.getConnect() != null) {
                    connection.getConnect().rollback();
                }
            } catch (SQLException rollbackException) {
                rollbackException.printStackTrace();
            }

            throw new RuntimeException(e);
        } finally {
            try {
                if (connection.getConnect() != null) {
                    connection.getConnect().setAutoCommit(true);
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
