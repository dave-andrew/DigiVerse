package net.slc.dv.database.builder;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Results implements AutoCloseable {

    private final PreparedStatement statement;
    private final ResultSet resultSet;

    public Results(PreparedStatement statement, ResultSet resultSet) {
        this.statement = statement;
        this.resultSet = resultSet;
    }

    public PreparedStatement getStatement() {
        return statement;
    }

    public ResultSet getResultSet() {
        return resultSet;
    }

    public void close() {
        try {
            statement.close();
        } catch (Exception ignored) {
        }
        try {
            resultSet.close();
        } catch (Exception ignored) {
        }
    }

}
