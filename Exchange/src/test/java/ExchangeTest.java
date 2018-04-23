import com.abra.timeseries.exchange.poloniex.Pairs;
import com.abra.timeseries.exchange.poloniex.Pairs.Exchange;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.StreamSupport;
import org.junit.Test;


public class ExchangeTest {

  @Test
  public void testJackson() throws IOException {
    try (BufferedReader reader = new BufferedReader(new FileReader("./src/main/resources/test"))) {
      String str = reader.readLine();
//      System.out.println(str);

      JsonNode node = new ObjectMapper().readTree(str);
      Iterator iterator = node.fieldNames();
      System.out.println(StreamSupport
          .stream(Spliterators.spliteratorUnknownSize(node.fieldNames(), Spliterator.ORDERED),
              false)
          .filter(s -> !s.contains("total"))
          .reduce((s1, s2) -> s1 + "," + s2).get());

    }
  }

  @Test
  public void testPairs() {
    System.out.println(Pairs.getAllPairs(Exchange.POLONIEX));
    System.out.println(Pairs.getAllPairs(Exchange.POLONIEX).keySet());
//    Arrays.stream(Pairs.getAllPairs(Exchange.POLONIEX).get("BTC").split(",")).forEach(System.out::print);
    System.out.println(Pairs.getAllPairsByMarket(Exchange.POLONIEX, "BTC"));
  }

  @Test
  public void name() throws IOException {
    URL url = new URL(
        "https://poloniex.com/public?command=returnChartData&currencyPair=BTC_ETH&start=1521400000&end9999999999&period=300");
    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
    urlConnection.connect();
    System.out.println(urlConnection.getResponseMessage());
    System.out.println();
    try (InputStream inputStream = urlConnection.getInputStream()) {
      ObjectMapper objectMapper = new ObjectMapper();
      JsonNode node = objectMapper.readTree(inputStream);
      System.out.println(node);
    }
    urlConnection.disconnect();
  }
}
