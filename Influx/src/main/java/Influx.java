import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.BatchPoints;
import org.influxdb.dto.Point;
import org.influxdb.dto.Query;
import org.influxdb.dto.QueryResult;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

public class Influx {
    public static void main(String[] args) throws IOException {
        InfluxDB influxDB = InfluxDBFactory.connect("http://127.0.0.1:8086", "root", "root");
        String dbName = "aTimeSeries";
        influxDB.createDatabase(dbName);
        String rpName = "aRetentionPolicy";
        influxDB.createRetentionPolicy(rpName, dbName, "30d", "30m", 2, true);

        BatchPoints batchPoints = BatchPoints
                .database(dbName)
                .tag("async", "true")
                .retentionPolicy(rpName)
                .consistency(InfluxDB.ConsistencyLevel.ALL)
                .build();
        Point point1 = Point.measurement("cpu")
                .time(System.currentTimeMillis(), TimeUnit.MILLISECONDS)
                .addField("idle", 90L)
                .addField("user", 9L)
                .addField("system", 1L)
                .build();
        Point point2 = Point.measurement("disk")
                .time(System.currentTimeMillis(), TimeUnit.MILLISECONDS)
                .addField("used", 80L)
                .addField("free", 1L)
                .build();
        batchPoints.point(point1);
        batchPoints.point(point2);
        influxDB.write(batchPoints);
        Query query = new Query("SELECT usage_idle FROM cpu LIMIT 10", "telegraf");

        URL url=new URL("http://127.0.0.1:8086/query?db=telegraf&q=SELECT%20usage_idle%20FROM%20cpu%20LIMIT%2010&pretty=true");
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setRequestMethod("GET");
        urlConnection.setRequestProperty("Accept","application/csv");

        try(BufferedReader reader=new BufferedReader(new InputStreamReader(urlConnection.getInputStream()))) {
            StringBuilder stringBuilder=new StringBuilder();
            String line;
            while ((line=reader.readLine())!=null){
                stringBuilder.append(line);
                System.out.println(line);
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
