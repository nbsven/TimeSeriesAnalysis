import com.abra.timeseries.utils.CredentialsUtils;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import org.junit.Assert;
import org.junit.Test;

public class PostgresJdbcTest {

  public static final String DB_CREDENTIALS_ENV = "./db_credentials.env";
  private String URL = "jdbc:postgresql://localhost:5433/test";

  @Test
  public void testPath() {
    File file = new File(".");
    System.out.println(file.getAbsolutePath());
  }

  @Test
  public void testProperty() throws IOException {

    System.out.println(CredentialsUtils.getPropFromEnvFile().keySet());
  }

  @Test
  public void testJdbcConnection() throws ClassNotFoundException, IOException {

    Class.forName("org.postgresql.Driver");

    try (Connection connection = DriverManager
        .getConnection(URL, CredentialsUtils.getPropFromEnvFile())) {
//      String sqlCreateTable = "INSERT INTO test.schema.students(name, id_group) VALUES(?,?)";
      Statement statement = connection.createStatement();
      statement.executeUpdate("CREATE SCHEMA IF NOT EXISTS test");
      statement = connection.createStatement();
      statement.executeUpdate("CREATE TABLE IF NOT EXISTS test.students(\n"
          + "  \"name\" VARCHAR(255),\n"
          + "  \"id_group\" BIGINT\n"
          + ")");

      String sql = "INSERT INTO test.students(name, id_group) VALUES(?,?)";
      PreparedStatement ps = connection.prepareStatement(sql);
      ps.setString(1, "Кукушкин");
      ps.setInt(2, 851001);
      ps.executeUpdate();

      statement.executeUpdate("DROP SCHEMA test CASCADE ");
    } catch (SQLException e) {
      Assert.fail();
      e.printStackTrace();
    }
  }
}
