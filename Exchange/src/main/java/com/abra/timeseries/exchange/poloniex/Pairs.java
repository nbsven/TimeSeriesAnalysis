package com.abra.timeseries.exchange.poloniex;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Pairs {


  public enum Exchanges {
    POLONIEX ("_");

    private final String delimiter;

    Exchanges(String delimiter) {
      this.delimiter = delimiter;
    }

    public String getDelimiter() {
      return delimiter;
    }

    public String getMarketFilePath() {
      return "./src/main/resources/" + this.name().toLowerCase().concat("_pairs");
    }
  }

  private static final Map<Exchanges, String> markets;

  static {
    markets = Arrays.stream(Exchanges.values())
        .collect(Collectors.toMap(ex -> ex, Exchanges::getMarketFilePath));
  }

  public static Map<String, String> getAllPairs(Exchanges exchange) {
    try (BufferedReader br = new BufferedReader(new FileReader(markets.get(exchange)))) {
      return Arrays.stream(br.readLine().split(","))
          .collect(
              Collectors.toMap(
                  str -> str.split(exchange.delimiter)[0],
                  str -> str.split(exchange.delimiter)[1],
                  (s, a) -> s + "," + a)
          );
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public static List<String> getAllPairsByMarket(Exchanges exchange, String market) {
    try (BufferedReader br = new BufferedReader(new FileReader(markets.get(exchange)))) {
      return Arrays.stream(br.readLine().split(","))
          .filter(str -> str.split(exchange.delimiter)[0].equals(market))
          .map(s -> s.split(exchange.delimiter)[1])
          .collect(Collectors.toList());
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
