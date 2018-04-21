import com.abra.timeseries.exchange.poloniex.Pairs;
import com.abra.timeseries.exchange.poloniex.Pairs.Exchanges;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import org.junit.Test;


public class ExchangeTest {

  @Test
  public void testPairs() {
    System.out.println(Pairs.getAllPairs(Exchanges.POLONIEX));
    System.out.println(Pairs.getAllPairs(Exchanges.POLONIEX).keySet());
//    Arrays.stream(Pairs.getAllPairs(Exchanges.POLONIEX).get("BTC").split(",")).forEach(System.out::print);
    System.out.println(Pairs.getAllPairsByMarket(Exchanges.POLONIEX,"BTC"));
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
