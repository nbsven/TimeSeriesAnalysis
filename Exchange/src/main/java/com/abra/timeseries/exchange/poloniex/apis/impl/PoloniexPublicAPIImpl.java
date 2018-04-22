package com.abra.timeseries.exchange.poloniex.apis.impl;

import com.abra.timeseries.exchange.poloniex.apis.PoloniexPublicAPI;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PoloniexPublicAPIImpl implements PoloniexPublicAPI {

  private static final PoloniexPublicAPIImpl singleton = new PoloniexPublicAPIImpl();

  private static final String PREFIX = "https://poloniex.com/public?command=";
  private ExecutorService service = Executors.newSingleThreadExecutor();
  private Runnable pause = () -> {
    try {
      Thread.sleep(1000 / 5);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  };

  @Override
  public String return24hVolume() {
    String result = "";
    try {
      result = service.submit(() -> {
        String responseString="";
        try {
          URL url = new URL(PREFIX + "return24hVolume");
          HttpURLConnection connection = (HttpURLConnection) url.openConnection();
          connection.connect();
          try (BufferedReader reader = new BufferedReader(
              new InputStreamReader(connection.getInputStream()))) {
            responseString = reader.readLine();
          }finally {
            connection.disconnect();
          }
        } catch (MalformedURLException e) {
          e.printStackTrace();
        } catch (IOException e) {
          e.printStackTrace();
        }
        return responseString;
      }).get();
      service.submit(pause);
    } catch (InterruptedException e) {
      e.printStackTrace();
    } catch (ExecutionException e) {
      e.printStackTrace();
    }
    return result;
  }

  public static PoloniexPublicAPIImpl instance() {
    return singleton;
  }

  public void shutdown(){
    service.shutdown();
  }
}
