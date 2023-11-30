import net.slc.dv.database.builder.NeoQueryBuilder;
import net.slc.dv.database.builder.Results;
import net.slc.dv.helper.Closer;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class MainTest {

    public MainTest() {
        this.select();
        this.insert();
        this.update();
        this.delete();
    }

    public static void main(String[] args) {
        new MainTest();
    }

    private void select() {
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

    private void insert() {
        try (Closer closer = new Closer()) {
            closer.add(new NeoQueryBuilder(NeoQueryBuilder.QueryType.INSERT)
                    .table("msuser")
                    .values("UserID", UUID.randomUUID().toString())
                    .values("UserName", "Tester123")
                    .values("UserEmail", "email@123.com")
                    .values("UserPassword", "hello1234")
                    .values("UserDOB", "1990-01-01")
                    .getResults());

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void update() {
        try (Closer closer = new Closer()) {
            closer.add(new NeoQueryBuilder(NeoQueryBuilder.QueryType.UPDATE)
                    .table("msuser")
                    .values("UserName", "Tester456")
                    .condition("UserName", "=", "Tester123")
                    .getResults());

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void delete() {
        try (Closer closer = new Closer()) {
            closer.add(new NeoQueryBuilder(NeoQueryBuilder.QueryType.DELETE)
                    .table("msuser")
                    .condition("UserName", "=", "Tester456")
                    .getResults());

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
