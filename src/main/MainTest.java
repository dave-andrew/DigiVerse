package main;

import database.builder.NeoQueryBuilder;
import database.builder.Results;
import helper.Closer;

import java.sql.ResultSet;
import java.sql.SQLException;

public class MainTest {

    public MainTest() {
        this.query();
    }

    public static void main(String[] args) {
        new MainTest();
    }

    private void query() {
        try (Closer closer = new Closer()) {
            Results results = closer.add(new NeoQueryBuilder(NeoQueryBuilder.QueryType.SELECT)
                    .table("msuser")
                    .condition("UserEmail", "=", "dep@gmail.com")
                    .condition("UserPassword", "=", "hello1234")
                    .getResults());

            ResultSet set = results.getResultSet();
            while (set.next()) {
                System.out.println(set.getString("UserID"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
