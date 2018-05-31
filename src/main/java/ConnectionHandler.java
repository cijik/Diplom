import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class ConnectionHandler {

    private Connection con;
    private Logger log = LoggerFactory.getLogger(ConnectionHandler.class);

    ConnectionHandler(Connection connection) {
        this.con = connection;
        try {
            con.setAutoCommit(false);
            init();
        } catch (SQLException e) {
            log.error("Cannot perform SQL operation");
        }
    }

    private void init() throws SQLException {
        String[] sql = {"TRUNCATE banking",
                "INSERT INTO banking VALUES (1234, 1000);",
                "INSERT INTO banking VALUES (5678, 0);"};
        PreparedStatement stmt;
        for (String s : sql) {
            stmt = con.prepareStatement(s);
            stmt.executeUpdate();
        }
        con.commit();
    }

    public void update(String sql, List<Integer> list) throws SQLException {
        PreparedStatement statement = con.prepareStatement(sql);
        statement.setObject(1, list.get(0));
        statement.setObject(2, list.get(0));
        statement.setObject(4, list.get(1));
        statement.setObject(5, list.get(1));
        statement.setObject(3, list.get(2));
        statement.setObject(6, list.get(2));
        statement.executeUpdate();
        con.commit();
        con.close();
    }
}