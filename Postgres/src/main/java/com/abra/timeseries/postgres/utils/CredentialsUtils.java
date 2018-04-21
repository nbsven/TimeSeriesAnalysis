package com.abra.timeseries.postgres.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class CredentialsUtils {

  public static final String DB_CREDENTIALS_ENV = "./db_credentials.env";

  public static Properties getPropFromEnvFile() {
    Properties properties = new Properties();
    try {
      properties.load(new FileInputStream(DB_CREDENTIALS_ENV));
      Properties result = new Properties();
      result.setProperty("user", properties.getProperty("POSTGRES_USER"));
      result.setProperty("password", properties.getProperty("POSTGRES_PASSWORD"));
      return result;
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

}
