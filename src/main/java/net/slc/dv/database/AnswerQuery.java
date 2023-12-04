package net.slc.dv.database;

import net.slc.dv.database.builder.NeoQueryBuilder;
import net.slc.dv.database.builder.Results;
import net.slc.dv.database.builder.enums.ConditionCompareType;
import net.slc.dv.database.builder.enums.QueryType;
import net.slc.dv.helper.Closer;
import net.slc.dv.helper.DateManager;
import net.slc.dv.model.AnswerDetail;
import net.slc.dv.model.AnswerHeader;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class AnswerQuery {

    public ArrayList<File> getMemberFileAnswer(String taskid, String userid) {
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

    public ArrayList<AnswerDetail> getMemberQuestionAnswer(String taskid, String userid) {
        ArrayList<AnswerDetail> answerDetails = new ArrayList<>();

        try (Closer closer = new Closer()) {
            NeoQueryBuilder queryHeaderBuilder = new NeoQueryBuilder(QueryType.SELECT)
                    .table("answer_header")
                    .condition("TaskID", ConditionCompareType.EQUAL, taskid)
                    .condition("UserID", ConditionCompareType.EQUAL, userid);

            Results resultsHeader = closer.add(queryHeaderBuilder.getResults());
            ResultSet setHeader = closer.add(resultsHeader.getResultSet());

            AnswerHeader answerHeader = null;
            while (setHeader.next()) {
                answerHeader = new AnswerHeader(setHeader);
            }

            NeoQueryBuilder queryDetailBuilder = new NeoQueryBuilder(QueryType.SELECT)
                    .columns("*")
                    .table("answer_detail")
                    .condition("AnswerID", ConditionCompareType.EQUAL, answerHeader.getId());

            Results resultsDetail = closer.add(queryDetailBuilder.getResults());
            ResultSet setDetail = closer.add(resultsDetail.getResultSet());

            while (setDetail.next()) {
                answerDetails.add(new AnswerDetail(setDetail));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return answerDetails;
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

    public AnswerHeader getAnswerHeader(String taskId, String userId) {
        try (Closer closer = new Closer()) {
            NeoQueryBuilder queryBuilder = new NeoQueryBuilder(QueryType.SELECT)
                    .columns("*")
                    .table("answer_header")
                    .condition("TaskID", ConditionCompareType.EQUAL, taskId)
                    .condition("UserID", ConditionCompareType.EQUAL, userId);

            Results results = closer.add(queryBuilder.getResults());
            ResultSet set = closer.add(results.getResultSet());

            while (set.next()) {
                System.out.println(set.getString("AnswerID"));
                return new AnswerHeader(set);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public List<AnswerDetail> getAnswerDetails(String questionId) {
        List<AnswerDetail> answerDetails = new ArrayList<>();

        try (Closer closer = new Closer()) {
            NeoQueryBuilder queryBuilder = new NeoQueryBuilder(QueryType.SELECT)
                    .columns("*")
                    .table("answer_detail")
                    .condition("AnswerID", ConditionCompareType.EQUAL, questionId);

            Results results = closer.add(queryBuilder.getResults());
            ResultSet set = closer.add(results.getResultSet());

            while (set.next()) {
                answerDetails.add(new AnswerDetail(set));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return answerDetails;
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

    public void createAnswerHeader(AnswerHeader answerHeader) {
        try (Closer closer = new Closer()) {
            NeoQueryBuilder deleteQuery = new NeoQueryBuilder(QueryType.DELETE)
                    .table("answer_header")
                    .condition("AnswerID", ConditionCompareType.EQUAL, answerHeader.getId())
                    .condition("TaskID", ConditionCompareType.EQUAL, answerHeader.getTaskid())
                    .condition("UserID", ConditionCompareType.EQUAL, answerHeader.getUserid());

            NeoQueryBuilder queryBuilder = new NeoQueryBuilder(QueryType.INSERT)
                    .table("answer_header")
                    .values("AnswerID", answerHeader.getId())
                    .values("TaskID", answerHeader.getTaskid())
                    .values("UserID", answerHeader.getUserid())
                    .values("Finished", answerHeader.isFinished())
                    .values("Score", answerHeader.getScore())
                    .values("CreatedAt", answerHeader.getCreatedAt())
                    .values("FinishedAt", answerHeader.getFinishedAt());

            closer.add(deleteQuery.getResults());
            closer.add(queryBuilder.getResults());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void createAnswerDetails(List<AnswerDetail> answerDetails) {
        try (Closer closer = new Closer()) {
            for (AnswerDetail answerDetail : answerDetails) {
                NeoQueryBuilder queryBuilder = new NeoQueryBuilder(QueryType.INSERT)
                        .table("answer_detail")
                        .values("AnswerID", answerDetail.getAnswerId())
                        .values("QuestionID", answerDetail.getQuestionId())
                        .values("AnswerText", answerDetail.getAnswerText())
                        .values("Score", answerDetail.getAnswerScore());

                closer.add(queryBuilder.getResults());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void submitTest(AnswerHeader answerHeader) {
        try (Closer closer = new Closer()) {
            NeoQueryBuilder queryBuilder = new NeoQueryBuilder(QueryType.UPDATE)
                    .table("answer_header")
                    .values("Finished", true)
                    .values("FinishedAt", DateManager.getNow())
                    .condition("AnswerID", ConditionCompareType.EQUAL, answerHeader.getId())
                    .condition("TaskID", ConditionCompareType.EQUAL, answerHeader.getTaskid())
                    .condition("UserID", ConditionCompareType.EQUAL, answerHeader.getUserid());

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

    public boolean checkTest(String taskid, String userid) {
        try (Closer closer = new Closer()) {
            NeoQueryBuilder queryBuilder = new NeoQueryBuilder(QueryType.SELECT)
                    .columns("*")
                    .table("answer_header")
                    .condition("TaskID", ConditionCompareType.EQUAL, taskid)
                    .condition("UserID", ConditionCompareType.EQUAL, userid)
                    .condition("Finished", ConditionCompareType.EQUAL, "1");

            Results results = closer.add(queryBuilder.getResults());
            ResultSet set = closer.add(results.getResultSet());

            if (set.next()) {
                return true;
            }

        } catch (SQLException e) {
            return false;
        }

        return false;
    }

    public void scoreQuestionAnswer(List<AnswerDetail> answerDetails) {
        try (Closer closer = new Closer()) {

            for (AnswerDetail answerDetail : answerDetails) {
                NeoQueryBuilder queryBuilder = new NeoQueryBuilder(QueryType.UPDATE)
                        .table("answer_detail")
                        .values("Score", answerDetail.getAnswerScore())
                        .condition("AnswerID", ConditionCompareType.EQUAL, answerDetail.getAnswerId())
                        .condition("QuestionID", ConditionCompareType.EQUAL, answerDetail.getQuestionId());

                closer.add(queryBuilder.getResults());
                System.out.println(answerDetail.getAnswerId());
            }
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

    public void finishScoring(String taskId, String userId, String answerid, Double score) {
        try (Closer closer = new Closer()) {
            NeoQueryBuilder queryHeaderBuilder = new NeoQueryBuilder(QueryType.UPDATE)
                    .table("answer_header")
                    .values("Score", score)
                    .condition("AnswerID", ConditionCompareType.EQUAL, answerid);

            closer.add(queryHeaderBuilder.getResults());

            NeoQueryBuilder queryTaskBuilder = new NeoQueryBuilder(QueryType.UPDATE)
                    .table("mstask")
                    .values("Scored", true)
                    .condition("UserID", ConditionCompareType.EQUAL, userId)
                    .condition("TaskID", ConditionCompareType.EQUAL, taskId);

            closer.add(queryTaskBuilder.getResults());
        } catch (SQLException e) {
            e.printStackTrace();
        }
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
