package main;

import database.builder.NeoQueryBuilder;
import database.builder.Results;
import helper.Closer;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class MainTest {

    public MainTest() {
        this.query();
    }

    public static void main(String[] args) {
        new MainTest();
    }

    private void query() {
        try (Closer closer = new Closer()) {
            Map<String, String> condition = new HashMap<>();
            condition.put("UserEmail", "dep@gmail.com");
            condition.put("UserPassword", "hello1234");

            Results results = closer.add(new NeoQueryBuilder(NeoQueryBuilder.QueryType.SELECT)
                    .table("msuser")
                    .condition(condition, NeoQueryBuilder.ConditionType.AND)
                    .build());

            ResultSet set = results.getResultSet();
            while (set.next()) {
                System.out.println(set.getString("UserID"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
