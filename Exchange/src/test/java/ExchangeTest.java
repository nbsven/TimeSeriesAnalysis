import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.junit.Test;


public class ExchangeTest {

  @Test
  public void name() throws IOException {
    URL url = new URL("https://poloniex.com/public?command=returnChartData&currencyPair=BTC_ETH&start=1521400000&end9999999999&period=300");
    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
    System.out.println(urlConnection.getResponseMessage());
    try(BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()))){
      System.out.println(reader.readLine());
    }
    urlConnection.disconnect();
  }
}
