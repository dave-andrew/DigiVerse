package net.slc.dv.database;

import net.slc.dv.database.builder.NeoQueryBuilder;
import net.slc.dv.database.builder.Results;
import net.slc.dv.database.builder.enums.QueryType;
import net.slc.dv.helper.Closer;
import net.slc.dv.helper.toast.ToastBuilder;
import net.slc.dv.model.Classroom;
import net.slc.dv.model.Forum;
import net.slc.dv.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ForumQuery {

    public ArrayList<Forum> getClassroomForum(String classid) {
        ArrayList<Forum> forumList = new ArrayList<>();

        try (Closer closer = new Closer()) {
            NeoQueryBuilder queryBuilder = new NeoQueryBuilder(QueryType.SELECT)
                    .table("class_forum")
                    .columns("class_forum.ForumID", "ForumText", "class_forum.UserID", "UserName", "UserEmail", "UserDOB", "UserProfile", "class_forum.ClassID", "ClassName", "ClassDesc", "ClassCode", "ClassSubject", "ClassImage", "CreatedAt")
                    .join("class_forum", "ForumID", "msclass", "ClassID")
                    .join("class_forum", "UserID", "msuser", "UserID")
                    .join("class_forum", "ForumID", "msforum", "ForumID")
                    .condition("class_forum.ClassID", "=", classid)
                    .orderBy("CreatedAt", "DESC");

            Results results = closer.add(queryBuilder.getResults());
            ResultSet set = closer.add(results.getResultSet());
            while (set.next()) {
                User user = new User(set);
                Classroom classroom = new Classroom(set);

                forumList.add(new Forum(set.getString("ForumID"), set.getString("ForumText"), set.getString("UserID"), user, set.getString("ClassID"), classroom, set.getString("CreatedAt")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return forumList;
    }

    public Forum createForum(Forum forum) {
        try (Closer closer = new Closer()) {
            NeoQueryBuilder queryBuilder = new NeoQueryBuilder(QueryType.INSERT)
                    .table("msforum")
                    .values("ForumID", forum.getId())
                    .values("ForumText", forum.getText())
                    .values("CreatedAt", forum.getCreatedAt());

            closer.add(queryBuilder.getResults());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try (Closer closer = new Closer()) {
            NeoQueryBuilder queryBuilder = new NeoQueryBuilder(QueryType.INSERT)
                    .table("class_forum")
                    .values("ClassID", forum.getClassid())
                    .values("ForumID", forum.getId())
                    .values("UserID", forum.getUserid());

            closer.add(queryBuilder.getResults());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        ToastBuilder.buildNormal().setText("Forum Posted!").build();
        return forum;
    }

}
