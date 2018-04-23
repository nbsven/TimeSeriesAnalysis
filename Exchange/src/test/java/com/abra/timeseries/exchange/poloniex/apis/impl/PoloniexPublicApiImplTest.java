package com.abra.timeseries.exchange.poloniex.apis.impl;

import org.junit.Test;

public class PoloniexPublicApiImplTest {

  @Test
  public void return24hVolume() {
    PoloniexPublicApiImpl instance = PoloniexPublicApiImpl.instance();
    System.out.println(instance.return24hVolume());
    instance.shutdown();
  }
}