package com.gohubert.xbrlfetcher;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;

public class Zip {
  public static boolean gunzip(final String fileIn, final String fileOut) {
    byte[] buffer = new byte[1024];
    try {
    	GZIPInputStream gzis = 
    	  new GZIPInputStream(new FileInputStream(fileIn));
 
    	FileOutputStream out = 
        new FileOutputStream(fileOut);
 
      int len;
      while ((len = gzis.read(buffer)) > 0) {
        out.write(buffer, 0, len);
      }
 
      gzis.close();
    	out.close();
 
      System.out.println("Extracting " + fileIn + " complete.");
      return true;
    } catch(IOException ex){
       ex.printStackTrace();   
    }
    
    return true;
  }
}

// https://www.mkyong.com/java/how-to-decompress-file-from-gzip-file/