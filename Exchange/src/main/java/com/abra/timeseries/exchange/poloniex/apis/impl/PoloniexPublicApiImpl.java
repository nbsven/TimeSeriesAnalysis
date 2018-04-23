package com.abra.timeseries.exchange.poloniex.apis.impl;

import com.abra.timeseries.exchange.poloniex.Converters.Converter;
import com.abra.timeseries.exchange.poloniex.Pairs.Exchange;
import com.abra.timeseries.exchange.poloniex.apis.PublicApi;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PoloniexPublicApiImpl implements PublicApi {

  public static final class Period {

    public static final int PERIOD300 = 300;
    public static final int PERIOD900 = 900;
    public static final int PERIOD1800 = 1800;
    public static final int PERIOD7200 = 7200;
    public static final int PERIOD14400 = 14400;
    public static final int PERIOD86400 = 86400;
  }

  private static class Execution implements Callable<String>{
    private static final String PREFIX = "https://poloniex.com/public?command=";

    private String command;

    public Execution(String command) {
      this.command = command;
    }

    @Override
    public String call() throws Exception {
      String responseString = "";
      try {
        URL url = new URL(PREFIX + command);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.connect();
        try (BufferedReader reader = new BufferedReader(
            new InputStreamReader(connection.getInputStream()))) {
          responseString = reader.readLine();
        } finally {
          connection.disconnect();
        }
      } catch (MalformedURLException e) {
        e.printStackTrace();
      } catch (IOException e) {
        e.printStackTrace();
      }
      return responseString;
    }
  }



  private static final PoloniexPublicApiImpl singleton = new PoloniexPublicApiImpl();
  private ExecutorService service = Executors.newSingleThreadExecutor();
  private Runnable pause = () -> {
    try {
      Thread.sleep(1000 / 5);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  };


  public static PoloniexPublicApiImpl instance() {
    return singleton;
  }

  public void shutdown() {
    service.shutdown();
  }

  private String doRequest(String command) {
    String result = "";

    try {
      result = service.submit(new Execution(command)).get();
    } catch (InterruptedException e) {
      e.printStackTrace();
    } catch (ExecutionException e) {
      e.printStackTrace();
    }
    service.submit(pause);

    return result;
  }

  public String return24hVolume() {
    String command = "return24hVolume";
    return doRequest(command);
  }

  public String returnChartData(String currencyPair, long start, long end, int period){
    String command = String.format("returnChartData&currencyPair=%s&start=%d&end=%d&period=%d",currencyPair,start,end,period);
    return doRequest(command);
  }

  @Override
  public String getMarkets() {
    return new Converter(Exchange.POLONIEX).markets(return24hVolume());
  }
}
