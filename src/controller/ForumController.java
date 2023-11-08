package controller;

import database.ForumQuery;
import model.Forum;
import model.LoggedUser;

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

}
