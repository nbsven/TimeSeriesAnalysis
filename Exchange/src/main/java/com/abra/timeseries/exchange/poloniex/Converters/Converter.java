package com.abra.timeseries.exchange.poloniex.Converters;

import static com.abra.timeseries.exchange.poloniex.Pairs.Exchange.POLONIEX;

import com.abra.timeseries.exchange.poloniex.Pairs.Exchange;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.StreamSupport;

public class Converter {

  private Exchange exchange;

  public Converter(Exchange exchange) {
    this.exchange = exchange;
  }

  public String markets(String json) {
    try {

      if (exchange == POLONIEX) {
        JsonNode node = new ObjectMapper().readTree(json);
        return StreamSupport
            .stream(Spliterators.spliteratorUnknownSize(node.fieldNames(), Spliterator.ORDERED),
                false)
            .filter(s -> !s.contains("total"))
            .reduce((s1, s2) -> s1 + "," + s2).get();
      }
    }catch (IOException e){
      throw new RuntimeException(e);
    }
    return "";
  }
}
