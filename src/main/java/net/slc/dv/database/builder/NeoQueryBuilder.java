package net.slc.dv.database.builder;

import net.slc.dv.database.builder.enums.ConditionCompareType;
import net.slc.dv.database.builder.enums.ConditionJoinType;
import net.slc.dv.database.builder.enums.QueryType;
import net.slc.dv.database.connection.Connect;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class NeoQueryBuilder {

    private final QueryType queryType;
    private final AtomicInteger index = new AtomicInteger(1);

    private String table;
    private List<String> columns;


    private List<Condition> conditionList;
    private ConditionJoinType conditionJoinType;


    private Map<String, String> valueMap;

    private String orderBy;
    private String limit;
    private String offset;


    public NeoQueryBuilder(QueryType queryType) {
        this.queryType = queryType;
    }

    public NeoQueryBuilder table(String table) {
        this.table = table;
        return this;
    }

    public NeoQueryBuilder columns(String... columns) {
        this.columns = List.of(columns);
        return this;
    }

    public NeoQueryBuilder condition(String column, String compareType, String value) {
        ConditionCompareType conditionCompareType = ConditionCompareType.fromString(compareType);
        return this.condition(column, conditionCompareType, value);
    }

    public NeoQueryBuilder condition(String column, ConditionCompareType compareType, String value) {
        if (this.conditionList == null) {
            this.conditionList = new ArrayList<>();
            this.conditionList.add(new Condition(column, compareType, value));
        } else {
            this.conditionList.add(new Condition(column, compareType, value));
        }

        return this;
    }

    public NeoQueryBuilder conditionJoinType(ConditionJoinType conditionJoinType) {
        this.conditionJoinType = conditionJoinType;
        return this;
    }

    public NeoQueryBuilder values(String key, String value) {
        if (this.valueMap == null) {
            this.valueMap = new HashMap<>();
            this.valueMap.put(key, value);
        } else {
            this.valueMap.put(key, value);
        }

        return this;
    }

    public NeoQueryBuilder orderBy(String orderBy) {
        this.orderBy = orderBy;
        return this;
    }

    public NeoQueryBuilder limit(int limit) {
        return this.limit(String.valueOf(limit));
    }

    public NeoQueryBuilder limit(String limit) {
        this.limit = limit;
        return this;
    }

    public NeoQueryBuilder offset(String offset) {
        this.offset = offset;
        return this;
    }

    public Results getResults() throws SQLException {
        Connect connect = Connect.getConnection();
        PreparedStatement statement;

        switch (queryType) {
            case SELECT:
                statement = buildSelectStatement(connect);
                break;
            case INSERT:
                statement = buildInsertStatement(connect);
                break;
            case DELETE:
                statement = buildDeleteStatement(connect);
                break;
            case UPDATE:
                statement = buildUpdateStatement(connect);
                break;
            default:
                throw new RuntimeException("Invalid queryType: " + queryType);
        }

        assert statement != null;
        return executeQuery(statement);
    }

    private PreparedStatement buildSelectStatement(Connect connect) {
        String sql = buildSelectQuery();
        PreparedStatement statement = connect.prepareStatement(sql);
        applyConditions(statement);
        return statement;
    }

    private PreparedStatement buildInsertStatement(Connect connect) {
        String sql = buildInsertQuery();
        PreparedStatement statement = connect.prepareStatement(sql);
        applyValues(statement);
        return statement;
    }

    private PreparedStatement buildDeleteStatement(Connect connect) {
        String sql = buildDeleteQuery();
        PreparedStatement statement = connect.prepareStatement(sql);
        applyConditions(statement);
        return statement;
    }

    private PreparedStatement buildUpdateStatement(Connect connect) {
        String sql = buildUpdateQuery();
        PreparedStatement statement = connect.prepareStatement(sql);
        applyValues(statement);
        applyConditions(statement);
        return statement;
    }

    private Results executeQuery(PreparedStatement statement) throws SQLException {
        System.out.println(statement);
        if (queryType == QueryType.SELECT) {
            return new Results(statement, statement.executeQuery());
        } else {
            statement.execute();
            return new Results(statement, null);
        }
    }

    private String buildSelectQuery() {
        StringBuilder query = new StringBuilder();
        query.append("SELECT ");

        if (this.columns == null) {
            query.append("* ");
        } else {
            query.append(this.buildColumns()).append(" ");
        }

        query.append("FROM ").append(table).append(" ");
        if (conditionList != null) {
            query.append("WHERE ").append(this.buildConditions()).append(" ");
        }

        if (orderBy != null) {
            query.append("ORDER BY ").append(orderBy);
        }

        if (limit != null) {
            query.append("LIMIT ").append(limit);
        }

        if (offset != null) {
            query.append("OFFSET ").append(offset);
        }

        return query.toString();
    }

    private String buildInsertQuery() {
        return "INSERT INTO " + table + " " +
                "(" + this.buildInsertColumns() + ") " +
                "VALUES (" + this.buildInsertValues() + ")";
    }

    private String buildDeleteQuery() {
        StringBuilder query = new StringBuilder();
        query.append("DELETE FROM ").append(table).append(" ");
        if (conditionList != null) {
            query.append("WHERE ").append(this.buildConditions());
        }

        return query.toString();
    }

    private String buildUpdateQuery() {
        StringBuilder query = new StringBuilder();
        query.append("UPDATE ").append(table).append(" SET ").append(this.buildUpdateValues());
        if (conditionList != null) {
            query.append(" WHERE ").append(this.buildConditions());
        }

        return query.toString();
    }

    private String buildColumns() {
        StringJoiner stringJoiner = new StringJoiner(", ");
        for (String column : this.columns) {
            stringJoiner.add(column);
        }

        return stringJoiner.toString();
    }

    private String buildInsertColumns() {
        StringJoiner stringJoiner = new StringJoiner(" , ");
        for (String column : this.valueMap.keySet()) {
            stringJoiner.add(column);
        }

        return stringJoiner.toString();
    }

    private String buildConditions() {
        StringJoiner stringJoiner;
        if (conditionJoinType == null || conditionJoinType == ConditionJoinType.AND) {
            stringJoiner = new StringJoiner(" AND ");
        } else {
            stringJoiner = new StringJoiner(" OR ");
        }

        for (Condition condition : this.conditionList) {
            stringJoiner.add(condition.getColumn() + " " + condition.getCompareType().getSymbol() + " ?");
        }

        return stringJoiner.toString();
    }

    private void applyConditions(PreparedStatement preparedStatement) {
        this.conditionList.forEach(condition -> {
            try {
                preparedStatement.setString(index.getAndIncrement(), condition.getValue());
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private String buildInsertValues() {
        StringJoiner stringJoiner = new StringJoiner(", ");
        for (int i = 0; i < this.valueMap.size(); i++) {
            stringJoiner.add("?");
        }

        return stringJoiner.toString();
    }

    private String buildUpdateValues() {
        StringJoiner stringJoiner = new StringJoiner(", ");
        for (String column : this.valueMap.keySet()) {
            stringJoiner.add(column + " = ?");
        }

        return stringJoiner.toString();
    }

    private void applyValues(PreparedStatement preparedStatement) {
        this.valueMap.values().forEach(val -> {
            try {
                preparedStatement.setString(index.getAndIncrement(), val);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }


    private static class Condition {
        private final String column;
        private final ConditionCompareType compareType;
        private final String value;

        public Condition(String column, ConditionCompareType compareType, String value) {
            this.column = column;
            this.compareType = compareType;
            this.value = value;
        }

        public String getColumn() {
            return column;
        }

        public ConditionCompareType getCompareType() {
            return compareType;
        }

        public String getValue() {
            return value;
        }
    }
}
