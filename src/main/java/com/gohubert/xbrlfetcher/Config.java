package com.gohubert.xbrlfetcher;

import java.io.FileInputStream;
import java.io.InputStream;
import java.io.IOException;
import java.util.Properties;
 
public class Config
{
  Properties configFile;

  public Config() {
    InputStream is = null;
    InputStream userIS = null;
    configFile = new java.util.Properties();
    
    try {
      is = new FileInputStream("./default.cfg");
      userIS = null;

      // try to load user config.
      try {
        userIS = new FileInputStream("./user.cfg");
        if (userIS != null) {
          is = userIS;
          System.out.println("User config found");
        }
      } catch(Exception eta) {
        eta.printStackTrace();
      }

      configFile.load(is);
    } catch(Exception eta) {
      eta.printStackTrace();
    }
    
    // Close connections.
    // TODO: This seems off
    if (is != null) {
      try {
        is.close();
      } catch (IOException ex) {
        // ignore ... any significant errors should already have been
        // reported via an IOException from the final flush.
      }
    }
    if (userIS != null) {
      try {
        userIS.close();
      } catch (IOException ex) {
        // ignore ... any significant errors should already have been
        // reported via an IOException from the final flush.
      }
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