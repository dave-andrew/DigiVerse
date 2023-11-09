package controller;

import database.ForumQuery;
import model.Forum;
import model.LoggedUser;

import java.util.ArrayList;

public class ForumController {

    private ForumQuery forumQuery;
    private LoggedUser loggedUser;

    public ForumController() {
        forumQuery = new ForumQuery();
        loggedUser = LoggedUser.getInstance();
    }

    public Forum createForum(String text, String classid) {
        Forum newForum = new Forum(text, loggedUser.getId(), classid);

        return forumQuery.createForum(newForum);
    }

    public ArrayList<Forum> getClassroomForum(String classid) {
        return forumQuery.getClassroomForum(classid);
    }

}
