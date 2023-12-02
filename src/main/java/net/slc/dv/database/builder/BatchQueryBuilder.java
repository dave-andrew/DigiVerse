package net.slc.dv.database.builder;

import net.slc.dv.helper.Closer;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BatchQueryBuilder {

    private final List<NeoQueryBuilder> queryBuilderList = new ArrayList<>();

    public BatchQueryBuilder add(NeoQueryBuilder queryBuilder) {
        queryBuilderList.add(queryBuilder);
        return this;
    }

    public BatchQueryBuilder addAll(List<NeoQueryBuilder> queryBuilderList) {
        this.queryBuilderList.addAll(queryBuilderList);
        return this;
    }

    public void executeBatch() throws SQLException {
        try (Closer closer = new Closer()) {
            PreparedStatement masterStatement = closer.add(queryBuilderList.get(0).buildPreparedStatement());

            for (NeoQueryBuilder queryBuilder : queryBuilderList) {
                PreparedStatement preparedStatement = closer.add(queryBuilder.buildPreparedStatement());
                preparedStatement.addBatch();
            }

            masterStatement.executeBatch();
        }
    }
}
