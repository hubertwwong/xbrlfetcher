package com.gohubert.xbrlfetcher;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;
 
public class Config
{
  Properties configFile;

  public Config() {
    configFile = new java.util.Properties();
    try {
      InputStream is = new FileInputStream("./default.cfg");

      // try to load user config.
      try {
        InputStream userIS = new FileInputStream("./user.cfg");
        if (userIS != null) {
          is = userIS;
          System.out.println("User config found");
        }
      } catch(Exception eta) {
        eta.printStackTrace();
      }

      configFile.load(is);
      is.close();
    } catch(Exception eta) {
      eta.printStackTrace();
    }
  }

  public String getProperty(String key) {
    String value = this.configFile.getProperty(key);
    return value;
  }

  public String getString(String key) {
    String value = this.configFile.getProperty(key);
    return value;
  }

  public int getInt(String key) {
    int value = Integer.parseInt(this.configFile.getProperty(key));
    return value;
  }
}
// https://www.opencodez.com/java/read-config-file-in-java.htm