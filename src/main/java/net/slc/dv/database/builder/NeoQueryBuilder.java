package net.slc.dv.database.builder;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.slc.dv.database.builder.enums.ConditionCompareType;
import net.slc.dv.database.builder.enums.ConditionJoinType;
import net.slc.dv.database.builder.enums.OrderByType;
import net.slc.dv.database.builder.enums.QueryType;
import net.slc.dv.database.connection.Connect;

import java.io.FileInputStream;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class NeoQueryBuilder {

    private final QueryType queryType;
    private final AtomicInteger index = new AtomicInteger(1);

    private String table;
    private List<String> columns;
    private List<Join> joins;


    private List<AbstractCondition> conditionList;
    private ConditionJoinType conditionJoinType;


    private Map<String, Object> valueMap;

    private OrderBy orderBy;
    private String limit;
    private String offset;


    public NeoQueryBuilder(QueryType queryType) {
        this.queryType = queryType;
    }

    /**
     * Set the table name
     *
     * @param table Table name
     * @return NeoQueryBuilder
     */
    public NeoQueryBuilder table(String table) {
        this.table = table;
        return this;
    }

    /**
     * Set the columns to be selected
     *
     * @param columns Columns to be selected
     * @return NeoQueryBuilder
     */
    public NeoQueryBuilder columns(String... columns) {
        this.columns = List.of(columns);
        return this;
    }

    /**
     * Add or set join to the query
     *
     * @param sourceTable  The source table
     * @param sourceColumn The source column
     * @param targetTable  The target table
     * @param targetColumn The target column
     * @return NeoQueryBuilder
     */
    public NeoQueryBuilder join(String sourceTable, String sourceColumn, String targetTable, String targetColumn) {
        if (this.joins == null) {
            this.joins = new ArrayList<>();
            this.joins.add(new Join(sourceTable, targetTable, sourceColumn, targetColumn));
        } else {
            this.joins.add(new Join(sourceTable, targetTable, sourceColumn, targetColumn));
        }

        return this;
    }

    /**
     * Add or set a WHERE condition to the query
     *
     * @param column      The column to be compared
     * @param compareType The {@link ConditionCompareType} to be used
     * @param value       The value to be compared
     * @return NeoQueryBuilder
     */
    public NeoQueryBuilder condition(String column, String compareType, Object value) {
        ConditionCompareType conditionCompareType = ConditionCompareType.fromString(compareType);
        return this.condition(column, conditionCompareType, value);
    }


    /**
     * Add or set a WHERE condition to the query
     *
     * @param column      The column to be compared
     * @param compareType The {@link ConditionCompareType} to be used
     * @param value       The value to be compared
     * @return NeoQueryBuilder
     */
    public NeoQueryBuilder condition(String column, ConditionCompareType compareType, Object value) {
        AbstractCondition condition;
        if (compareType.equals(ConditionCompareType.IN) || compareType.equals(ConditionCompareType.NOT_IN)) {
            condition = new SubQueryCondition(column, (NeoQueryBuilder) value);
        } else {
            condition = new Condition(column, compareType, value);
        }

        if (this.conditionList == null) {
            this.conditionList = new ArrayList<>();
            this.conditionList.add(condition);
        } else {
            this.conditionList.add(condition);
        }

        return this;
    }

    /**
     * Set the condition join type
     *
     * @param conditionJoinType The {@link ConditionJoinType} to be used
     * @return NeoQueryBuilder
     */
    public NeoQueryBuilder conditionJoinType(ConditionJoinType conditionJoinType) {
        this.conditionJoinType = conditionJoinType;
        return this;
    }

    /**
     * Add or set a value to be inserted or updated
     *
     * @param column The column name
     * @param value  The value
     * @return NeoQueryBuilder
     */
    public NeoQueryBuilder values(String column, Object value) {
        if (this.valueMap == null) {
            this.valueMap = new HashMap<>();
            this.valueMap.put(column, value);
        } else {
            this.valueMap.put(column, value);
        }

        return this;
    }

    /**
     * Set the order by column and order
     *
     * @param orderBy The column to be ordered
     * @param order   The {@link OrderByType} to be used
     * @return NeoQueryBuilder
     */
    public NeoQueryBuilder orderBy(String orderBy, String order) {
        return this.orderBy(orderBy, OrderByType.fromString(order));
    }

    /**
     * Set the order by column and order
     *
     * @param orderBy The column to be ordered
     * @param order   The {@link OrderByType} to be used
     * @return NeoQueryBuilder
     */
    public NeoQueryBuilder orderBy(String orderBy, OrderByType order) {
        this.orderBy = new OrderBy(orderBy, order);
        return this;
    }

    /**
     * Set the selection limit
     *
     * @param limit The limit
     * @return NeoQueryBuilder
     */
    public NeoQueryBuilder limit(int limit) {
        return this.limit(String.valueOf(limit));
    }

    /**
     * Set the selection limit
     *
     * @param limit The limit
     * @return NeoQueryBuilder
     */
    public NeoQueryBuilder limit(String limit) {
        this.limit = limit;
        return this;
    }

    /**
     * Set the selection offset
     *
     * @param offset The offset
     * @return NeoQueryBuilder
     */
    public NeoQueryBuilder offset(int offset) {
        return this.offset(String.valueOf(offset));
    }

    /**
     * Set the selection offset
     *
     * @param offset The offset
     * @return NeoQueryBuilder
     */
    public NeoQueryBuilder offset(String offset) {
        this.offset = offset;
        return this;
    }

    /**
     * Execute the query
     *
     * @return {@link Results}
     * @throws SQLException If an error occurs
     */
    public Results getResults() throws SQLException {
        PreparedStatement statement = this.buildPreparedStatement();
        assert statement != null;

        return executeQuery(statement);
    }

    /**
     * Build the {@link PreparedStatement} without executing it
     *
     * @return {@link PreparedStatement}
     * @throws SQLException If an error occurs
     */
    public PreparedStatement buildPreparedStatement() throws SQLException {
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

        return statement;
    }

    private PreparedStatement buildSelectStatement(Connect connect) throws SQLException {
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

    private PreparedStatement buildDeleteStatement(Connect connect) throws SQLException {
        String sql = buildDeleteQuery();
        PreparedStatement statement = connect.prepareStatement(sql);
        applyConditions(statement);
        return statement;
    }

    private PreparedStatement buildUpdateStatement(Connect connect) throws SQLException {
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
            query.append("*").append(" ");
        } else {
            query.append(this.buildColumns()).append(" ");
        }

        query.append("FROM ").append(table).append(" ");
        if (joins != null) {
            query.append("JOIN ").append(this.buildJoin()).append(" ");
        }

        if (conditionList != null) {
            query.append("WHERE ").append(this.buildConditions()).append(" ");
        }

        if (orderBy != null) {
            query.append("ORDER BY ").append(this.buildOrderBy()).append(" ");
        }

        if (limit != null) {
            query.append("LIMIT ").append(limit).append(" ");
        }

        if (offset != null) {
            query.append("OFFSET ").append(offset).append(" ");
        }

        return query.toString().trim();
    }

    private String buildInsertQuery() {
        return "INSERT INTO " + table + " " +
                "(" + this.buildInsertColumns() + ") " +
                "VALUES (" + this.buildInsertValues() + ")";
    }

    private String buildDeleteQuery() {
        StringBuilder query = new StringBuilder();
        query.append("DELETE FROM ").append(table).append(" ");
        if (joins != null) {
            query.append("JOIN ").append(this.buildJoin()).append(" ");
        }

        if (conditionList != null) {
            query.append("WHERE ").append(this.buildConditions());
        }

        return query.toString();
    }

    private String buildUpdateQuery() {
        StringBuilder query = new StringBuilder();
        query.append("UPDATE ").append(table).append(" ");
        if (joins != null) {
            query.append("JOIN ").append(this.buildJoin()).append(" ");
        }

        query.append("SET ").append(this.buildUpdateValues()).append(" ");
        if (conditionList != null) {
            query.append("WHERE ").append(this.buildConditions());
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

    private String buildJoin() {
        StringJoiner stringJoiner = new StringJoiner(" JOIN ");
        for (Join join : this.joins) {
            stringJoiner.add(join.getTargetTable() + " ON " + join.buildCondition());
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

        if (this.conditionList != null) {
            for (AbstractCondition condition : this.conditionList) {
                if (condition instanceof Condition) {
                    stringJoiner.add(condition.getColumn() + " " + condition.getCompareType().getSymbol() + " ?");
                    continue;
                }

                SubQueryCondition subQueryCondition = (SubQueryCondition) condition;
                stringJoiner.add(subQueryCondition.getColumn() + " " + condition.getCompareType().getSymbol() + " (" + subQueryCondition.getQuery() + ") ");
            }
        }

        return stringJoiner.toString();
    }

    private String buildOrderBy() {
        return this.orderBy.getColumn() + " " + this.orderBy.getOrder().getType();
    }

    private void applyConditions(PreparedStatement preparedStatement) throws SQLException {
        this.applyConditions(preparedStatement, this.conditionList);
    }

    private void applyConditions(PreparedStatement preparedStatement, List<AbstractCondition> conditions) throws SQLException {
        for (AbstractCondition conditionElement : conditions) {
            if (conditionElement instanceof Condition) {
                Condition condition = (Condition) conditionElement;
                this.apply(preparedStatement, condition.getValue());
            } else if (conditionElement instanceof SubQueryCondition) {
                SubQueryCondition subQueryCondition = (SubQueryCondition) conditionElement;
                this.applyConditions(preparedStatement, subQueryCondition.getConditionList());
            }
        }
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
                this.apply(preparedStatement, val);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private void apply(PreparedStatement preparedStatement, Object val) throws SQLException {
        if (val == null) {
            preparedStatement.setNull(index.getAndIncrement(), Types.NULL);
            return;
        }

        if (val instanceof Integer) {
            preparedStatement.setInt(index.getAndIncrement(), (Integer) val);
        } else if (val instanceof String) {
            preparedStatement.setString(index.getAndIncrement(), (String) val);
        } else if (val instanceof Boolean) {
            preparedStatement.setBoolean(index.getAndIncrement(), (Boolean) val);
        } else if (val instanceof Double) {
            preparedStatement.setDouble(index.getAndIncrement(), (Double) val);
        } else if (val instanceof Float) {
            preparedStatement.setFloat(index.getAndIncrement(), (Float) val);
        } else if (val instanceof Long) {
            preparedStatement.setLong(index.getAndIncrement(), (Long) val);
        } else if (val instanceof Short) {
            preparedStatement.setShort(index.getAndIncrement(), (Short) val);
        } else if (val instanceof Byte) {
            preparedStatement.setByte(index.getAndIncrement(), (Byte) val);
        } else if (val instanceof byte[]) {
            preparedStatement.setBytes(index.getAndIncrement(), (byte[]) val);
        } else if (val instanceof java.sql.Date) {
            preparedStatement.setDate(index.getAndIncrement(), (java.sql.Date) val);
        } else if (val instanceof java.util.Date) {
            preparedStatement.setDate(index.getAndIncrement(), new java.sql.Date(((java.util.Date) val).getTime()));
        } else if (val instanceof UUID) {
            preparedStatement.setString(index.getAndIncrement(), val.toString());
        } else if (val instanceof FileInputStream) {
            preparedStatement.setBlob(index.getAndIncrement(), (FileInputStream) val);
        } else {
            throw new RuntimeException("Unsupported type: " + val.getClass().getSimpleName());
        }
    }

    @Getter
    private abstract static class AbstractCondition {
        private final String column;
        private final ConditionCompareType compareType;

        public AbstractCondition(String column, ConditionCompareType compareType) {
            this.column = column;
            this.compareType = compareType;
        }
    }

    @Getter
    private static class Condition extends AbstractCondition {
        private final Object value;

        public Condition(String column, ConditionCompareType compareType, Object value) {
            super(column, compareType);
            this.value = value;
        }
    }

    @Getter
    private static class SubQueryCondition extends AbstractCondition {
        private final String query;
        private final List<AbstractCondition> conditionList;

        public SubQueryCondition(String column, NeoQueryBuilder queryBuilder) {
            super(column, ConditionCompareType.IN);
            this.query = queryBuilder.buildSelectQuery();
            this.conditionList = queryBuilder.conditionList;
        }

        public String getQuery() {
            return query.trim();
        }
    }

    @AllArgsConstructor
    @Getter
    private static class OrderBy {
        private final String column;
        private final OrderByType order;
    }

    @AllArgsConstructor
    @Getter
    private static class Join {
        private final String sourceTable;
        private final String targetTable;

        private final String sourceColumn;
        private final String targetColumn;

        public String buildCondition() {
            return sourceTable + "." + sourceColumn + " = " + targetTable + "." + targetColumn;
        }
    }
}
