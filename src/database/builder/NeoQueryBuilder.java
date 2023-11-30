package database.builder;

import database.connection.Connect;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;
import java.util.concurrent.atomic.AtomicInteger;

public class NeoQueryBuilder {

    private final QueryType queryType;
    private final AtomicInteger index = new AtomicInteger(1);

    private String table;
    private List<String> columns;

    private ConditionType biConditionJoinType;
    private Map<ConditionType, Map<String, String>> conditionMap;
    private String conditionString;

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

    public NeoQueryBuilder condition(Map<String, String> condition, ConditionType conditionType) {
        this.conditionMap = new HashMap<>();
        this.conditionMap.put(conditionType, condition);

        return this;
    }

    public NeoQueryBuilder biConditionJoinType(ConditionType conditionType) {
        this.biConditionJoinType = conditionType;
        return this;
    }

    public NeoQueryBuilder conditionString(String conditionString) {
        this.conditionString = conditionString;
        return this;
    }

    public NeoQueryBuilder values(Map<String, String> values) {
        this.valueMap = values;
        return this;
    }

    public Results build() throws SQLException {
        Connect connect = Connect.getConnection();

        StringBuilder query = new StringBuilder();
        switch (queryType) {
            case SELECT: {
                query.append("SELECT ");

                if (this.columns != null) {
                    query.append(this.buildColumns()).append(" ");
                }

                query.append("FROM ").append(table);

                if (conditionMap != null) {
                    query.append("WHERE ").append(this.buildConditions());
                }

                PreparedStatement statement = connect.prepareStatement(query.toString());
                if (conditionMap != null) {
                    this.applyConditions(statement);
                }

                assert statement != null;
                return new Results(statement, statement.executeQuery());
            }
            case INSERT: {
                query.append("INSERT INTO ").append(table);
                if (this.columns != null) {
                    query.append(" (").append(this.buildColumns()).append(") ");
                }

                query.append("VALUES (").append(this.buildInsertValues()).append(")");

                PreparedStatement statement = connect.prepareStatement(query.toString());
                this.applyValues(statement);

                assert statement != null;
                statement.execute();

                return new Results(statement, null);
            }
            case DELETE: {
                query.append("DELETE FROM ").append(table);
                if (conditionMap != null) {
                    query.append(" WHERE ").append(this.buildConditions());
                }

                PreparedStatement statement = connect.prepareStatement(query.toString());
                if (conditionMap != null) {
                    this.applyConditions(statement);
                }

                assert statement != null;
                statement.execute();

                return new Results(statement, null);
            }
            case UPDATE: {
                query.append("UPDATE ").append(table).append(" SET ").append(this.buildUpdateValues());
                if (conditionMap != null) {
                    query.append(" WHERE ").append(this.buildConditions());
                }

                PreparedStatement statement = connect.prepareStatement(query.toString());
                this.applyValues(statement);
                if (conditionMap != null) {
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

    private String buildConditions() {
        if (this.conditionString != null) {
            return this.conditionString;
        }

        boolean containsBothType = this.conditionMap.containsKey(ConditionType.OR) && this.conditionMap.containsKey(ConditionType.AND);
        if (!containsBothType) {
            StringJoiner stringJoiner;
            if (this.conditionMap.containsKey(ConditionType.OR)) {
                stringJoiner = new StringJoiner(" OR ");
            } else {
                stringJoiner = new StringJoiner(" AND ");
            }

            conditionMap.values().forEach(it ->
                    it.keySet().forEach(col -> {
                        String condition = col + " = ?";
                        stringJoiner.add(condition);
                    })
            );
            return stringJoiner.toString();
        }

        StringBuilder stringBuilder = new StringBuilder();

        this.conditionMap.entrySet().stream()
                .filter(it -> it.getKey().equals(ConditionType.OR))
                .forEach(it -> {
                    StringJoiner stringJoiner = new StringJoiner(" OR ");
                    it.getValue().keySet().forEach(col -> {
                        String condition = col + " = ?";
                        stringJoiner.add(condition);
                    });

                    stringBuilder.append("(").append(stringJoiner).append(")");
                });

        if (this.biConditionJoinType == null || this.biConditionJoinType.equals(ConditionType.AND)) {
            stringBuilder.append(" AND ");
        } else {
            stringBuilder.append(" OR ");
        }

        this.conditionMap.entrySet().stream()
                .filter(it -> it.getKey().equals(ConditionType.AND))
                .forEach(it -> {
                    StringJoiner stringJoiner = new StringJoiner(" AND ");
                    it.getValue().keySet().forEach(col -> {
                        String condition = col + " = ?";
                        stringJoiner.add(condition);
                    });

                    stringBuilder.append("(").append(stringJoiner).append(")");
                });

        return stringBuilder.toString();
    }

    private void applyConditions(PreparedStatement preparedStatement) {
        if (this.conditionString != null) {
            return;
        }

        boolean containsBothType = this.conditionMap.containsKey(ConditionType.OR) && this.conditionMap.containsKey(ConditionType.AND);
        if (!containsBothType) {
            conditionMap.values().forEach(it ->
                    it.values().forEach(val -> {
                        try {
                            preparedStatement.setString(index.getAndIncrement(), val);
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }
                    })
            );
            return;
        }

        this.conditionMap.entrySet().stream()
                .filter(it -> it.getKey().equals(ConditionType.OR))
                .forEach(it ->
                        it.getValue().values().forEach(val -> {
                            try {
                                preparedStatement.setString(index.getAndIncrement(), val);
                            } catch (SQLException e) {
                                throw new RuntimeException(e);
                            }
                        })
                );
        this.conditionMap.entrySet().stream()
                .filter(it -> it.getKey().equals(ConditionType.AND))
                .forEach(it ->
                        it.getValue().values().forEach(val -> {
                            try {
                                preparedStatement.setString(index.getAndIncrement(), val);
                            } catch (SQLException e) {
                                throw new RuntimeException(e);
                            }
                        })
                );
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
        for (String column : this.columns) {
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

    public enum ConditionType {
        AND,
        OR
    }
}
