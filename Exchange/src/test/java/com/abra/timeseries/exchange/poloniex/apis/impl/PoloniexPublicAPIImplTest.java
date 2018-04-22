package com.abra.timeseries.exchange.poloniex.apis.impl;

import org.junit.Test;

public class PoloniexPublicAPIImplTest {

  @Test
  public void return24hVolume() {
    PoloniexPublicAPIImpl instance = PoloniexPublicAPIImpl.instance();
    System.out.println(instance.return24hVolume());
    instance.shutdown();
  }
}