package com.abra.timeseries.exchange.poloniex;

import com.abra.timeseries.exchange.poloniex.Converters.Converter;
import com.abra.timeseries.exchange.poloniex.apis.PublicApi;
import com.abra.timeseries.exchange.poloniex.apis.impl.PoloniexPublicApiImpl;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Pairs {


  public enum Exchange {
    POLONIEX("_");

    private PublicApi publicApi;

    public final String delimiter;
    private String markets;

    Exchange(String delimiter) {
      this.delimiter = delimiter;
      this.publicApi = PoloniexPublicApiImpl.instance();
    }

    public String getDelimiter() {
      return delimiter;
    }

    public String getMarkets() {
      if (markets == null) {
        synchronized (this) {
          if (markets == null) {
            markets = publicApi.getMarkets();
          }
        }
      }
      return markets;
    }
  }

  public static Map<String, String> getAllPairs(Exchange exchange) {
    return Arrays.stream(exchange.getMarkets().split(","))
        .collect(
            Collectors.toMap(
                str -> str.split(exchange.delimiter)[0],
                str -> str.split(exchange.delimiter)[1],
                (s, a) -> s + "," + a)
        );
  }

  public static List<String> getAllPairsByMarket(Exchange exchange, String market) {
    return Arrays.stream(exchange.getMarkets().split(","))
        .filter(str -> str.split(exchange.delimiter)[0].equals(market))
        .map(s -> s.split(exchange.delimiter)[1])
        .collect(Collectors.toList());
  }
}
