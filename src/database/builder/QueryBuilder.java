package database.builder;

import database.connection.Connect;

import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.Iterator;

public class QueryBuilder {

    private Connect con;

    private String select;
    private String from;
    private ArrayList<String> where;
    private String orderBy;
    private String limit;
    private ArrayList<String> join;
    private String insert;
    private String offset;

    public QueryBuilder() {
//        TODO: update this QueryBuilder to be more flexible

        this.con = Connect.getConnection();

        this.join = new ArrayList<>();
        this.select = "";
        this.from = "";
        this.where = new ArrayList<>();
        this.orderBy = "";
        this.limit = "";
        this.offset = "";
    }

    public QueryBuilder into(String into) {
        this.insert += "INTO " + into + "\n";
        return this;
    }

    public QueryBuilder values(String values) {
        this.insert += "VALUES " + values + "\n";
        return this;
    }

    public QueryBuilder select(String select) {
        this.select = "SELECT " + select + "\n";
        return this;
    }

    public QueryBuilder from(String from) {
        this.from = "FROM " + from + "\n";
        return this;
    }

    public QueryBuilder where(String where) {
        this.where.add("WHERE " + where + "\n");
        return this;
    }

    public QueryBuilder orderBy(String orderBy) {
        this.orderBy = "ORDER BY " + orderBy + "\n";
        return this;
    }

    public QueryBuilder limit(int limit) {
        this.limit = "LIMIT " + limit + "\n";
        return this;
    }

    public QueryBuilder offset(int offset) {
        this.offset = "OFFSET " + offset + "\n";
        return this;
    }

    public QueryBuilder join(String join) {
        this.join.add("JOIN " + join);
        return this;
    }

    public QueryBuilder on(String on) {
        this.join.add(" ON " + on + "\n");
        return this;
    }

    public PreparedStatement select() {
        StringBuilder query = new StringBuilder(select + from);

        if(!join.isEmpty()) {
            for(String j : join) {
                query.append(j);
            }
        }

        if (!where.isEmpty()) {
            Iterator<String> iterator = where.iterator();
            query.append(iterator.next());

            while (iterator.hasNext()) {
                query.append(" AND ").append(iterator.next());
            }
        }

        if(!orderBy.isEmpty()) {
            query.append(orderBy);
        }

        if(!limit.isEmpty()) {
            query.append(limit);
        }

        if(!offset.isEmpty()) {
            query.append(offset);
        }

        return con.prepareStatement(query.toString());
    }

    public PreparedStatement insert() {
        return con.prepareStatement("INSERT INTO " + insert);
    }

    public PreparedStatement update() {
        return con.prepareStatement("");
    }
}
