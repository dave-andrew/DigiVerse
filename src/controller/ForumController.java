package controller;

import database.ForumQuery;
import helper.StageManager;
import helper.toast.Toast;
import model.Forum;
import model.LoggedUser;

import java.util.ArrayList;

public class ForumController {

    private final ForumQuery forumQuery;
    private final LoggedUser loggedUser;

    public ForumController() {
        forumQuery = new ForumQuery();
        loggedUser = LoggedUser.getInstance();
    }

    public Forum createForum(String text, String classid) {

        if(text.isEmpty()) {
            Toast.makeError(StageManager.getInstance(), "Please fill all the fields!", 2000, 500, 500);
            return null;
        }

        Forum newForum = new Forum(text, loggedUser.getId(), classid);

        return forumQuery.createForum(newForum);
    }

    public ArrayList<Forum> getClassroomForum(String classid) {
        return forumQuery.getClassroomForum(classid);
    }

}
