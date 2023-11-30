package database.builder;

import database.connection.Connect;

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

    public Results getResults() throws SQLException {
        Connect connect = Connect.getConnection();

        StringBuilder query = new StringBuilder();
        switch (queryType) {
            case SELECT: {
                query.append("SELECT ");

                if (this.columns == null) {
                    query.append("* ");
                } else {
                    query.append(this.buildColumns()).append(" ");
                }

                query.append("FROM ").append(table).append(" ");
                if (conditionList != null) {
                    query.append("WHERE ").append(this.buildConditions());
                }

                PreparedStatement statement = connect.prepareStatement(query.toString());
                if (conditionList != null) {
                    this.applyConditions(statement);
                }

                assert statement != null;
                System.out.println(statement);
                return new Results(statement, statement.executeQuery());
            }
            case INSERT: {
                query.append("INSERT INTO ").append(table).append(" ");
                query.append("(").append(this.buildInsertColumns()).append(") ");
                query.append("VALUES (").append(this.buildInsertValues()).append(")");

                PreparedStatement statement = connect.prepareStatement(query.toString());
                this.applyValues(statement);

                assert statement != null;
                statement.execute();

                return new Results(statement, null);
            }
            case DELETE: {
                query.append("DELETE FROM ").append(table).append(" ");
                if (conditionList != null) {
                    query.append("WHERE ").append(this.buildConditions());
                }

                PreparedStatement statement = connect.prepareStatement(query.toString());
                if (conditionList != null) {
                    this.applyConditions(statement);
                }

                assert statement != null;
                statement.execute();

                return new Results(statement, null);
            }
            case UPDATE: {
                query.append("UPDATE ").append(table).append(" SET ").append(this.buildUpdateValues());
                if (conditionList != null) {
                    query.append(" WHERE ").append(this.buildConditions());
                }

                PreparedStatement statement = connect.prepareStatement(query.toString());
                this.applyValues(statement);
                if (conditionList != null) {
                    this.applyConditions(statement);
                }

                assert statement != null;
                statement.execute();

                return new Results(statement, null);
            }
            default:
                throw new RuntimeException();
        }
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

    public enum QueryType {
        SELECT,
        INSERT,
        DELETE,
        UPDATE
    }

    public enum ConditionJoinType {
        AND,
        OR
    }

    public enum ConditionCompareType {
        EQUAL("="),
        NOT_EQUAL("!="),
        GREATER_THAN(">"),
        LESS_THAN("<"),
        GREATER_THAN_EQUAL(">="),
        LESS_THAN_EQUAL("<=");

        private final String symbol;

        ConditionCompareType(String symbol) {
            this.symbol = symbol;
        }

        public static ConditionCompareType fromString(String symbol) {
            for (ConditionCompareType type : ConditionCompareType.values()) {
                if (type.symbol.equals(symbol)) {
                    return type;
                }
            }

            return null;
        }

        public String getSymbol() {
            return this.symbol;
        }
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
