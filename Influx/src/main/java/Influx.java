import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.BatchPoints;
import org.influxdb.dto.Point;
import org.influxdb.dto.Query;
import org.influxdb.dto.QueryResult;


import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

public class Influx {

  public static void main(String[] args) throws IOException {
    InfluxDB influxDB = InfluxDBFactory.connect("http://127.0.0.1:8086", "root", "root");

    Query query = new Query("SELECT usage_idle FROM cpu LIMIT 10", "telegraf");

    URL url = new URL(
        "http://127.0.0.1:8086/query?db=telegraf&q=SELECT%20usage_idle%20FROM%20cpu%20LIMIT%2020&pretty=true");
    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
    urlConnection.setRequestMethod("GET");
    urlConnection
        .setRequestProperty("Accept", "application/csv"); // -H Accept:application/csv //format=csv

    try (BufferedReader reader = new BufferedReader(
        new InputStreamReader(urlConnection.getInputStream()))) {
      StringBuilder stringBuilder = new StringBuilder();
      String line;
      while ((line = reader.readLine()) != null) {
        stringBuilder.append(line).append("\n");
      }
      System.out.println(stringBuilder);
      urlConnection.disconnect();
    }

    QueryResult result = influxDB.query(query);
    System.out.println(query.getCommandWithUrlEncoded());
    System.out.println(result.toString());
//        influxDB.deleteDatabase(dbName);

  }
}
