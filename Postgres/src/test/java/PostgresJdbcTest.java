import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import org.junit.Test;

public class PostgresJdbcTest {

  private String URL = "jdbc:postgresql://localhost:5433/postgres";

  @Test
  public void testJdbcConnection() throws ClassNotFoundException {

    Class.forName("org.postgresql.Driver");
    try (Connection connection = DriverManager.getConnection(URL, "timeseries", "custom");
        ) {
//      String sqlCreateTable = "INSERT INTO test.schema.students(name, id_group) VALUES(?,?)";
      Statement statement = connection.createStatement();
      statement.executeUpdate("CREATE SCHEMA IF NOT EXISTS test");
      statement=connection.createStatement();
      statement.executeUpdate("CREATE TABLE IF NOT EXISTS test.students(\n"
          + "  \"name\" VARCHAR(255),\n"
          + "  \"id_group\" BIGINT\n"
          + ")");

      String sql = "INSERT INTO test.students(name, id_group) VALUES(?,?)";
      PreparedStatement ps = connection.prepareStatement(sql);
      ps.setString(1,"Кукушкин");
      ps.setInt(2, 851001);
      ps.executeUpdate();

      statement.executeUpdate("DROP SCHEMA test CASCADE ");
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }
}
