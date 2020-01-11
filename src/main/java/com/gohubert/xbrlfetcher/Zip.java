package com.gohubert.xbrlfetcher;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import java.nio.file.NoSuchFileException;

public class Zip {

  /**
   * Unzips a file to a target directory and output file.
   * 
   * Grabbed the code from here.
   * https://www.mkyong.com/java/how-to-decompress-file-from-gzip-file/
   *
   * TODO:
   * 1. Double check this. It seems off. Like the signature is weird. Why do you need to specify the output file.
   * 
   * 
   * @param pIn - Path object that represents the input file.
   * @param pOut - Path object that represents the output file.
   * @return boolean if the zip was sucessful.
   */
  public static boolean gunzip(final Path pIn, final Path pOut) {
    // final String fileIn, final String dirOut, String fileOut
    byte[] buffer = new byte[1024];
    
    try {
      // create output directory
      Files.createDirectories(Paths.get(pOut.getParent().toString()));

      // Delete the file
      try {
        Files.delete(Paths.get(pOut.toString()));
      } catch(NoSuchFileException e) {
        // You want to swallow this error.
        // No point in printing it.
        //e.printStackTrace();
      }
      
    	GZIPInputStream gzis = 
    	  new GZIPInputStream(new FileInputStream(pIn.toString()));
 
    	FileOutputStream out = 
        new FileOutputStream(pOut.toString());

      int len;
      while ((len = gzis.read(buffer)) > 0) {
        out.write(buffer, 0, len);
      }

      gzis.close();
    	out.close();
 
      System.out.println("> Extracting " + pIn + " complete to " + pOut);
    } catch(IOException ex){
       ex.printStackTrace();
       return false;
    }

    return true;
  }

  /**
   * Zips a file to a target directory and output file.
   * 
   * Grabbed the code from here.
   * https://www.mkyong.com/java/how-to-compress-a-file-in-gzip-format/
   *
   * TODO:
   * 1. Double check this. It seems off. Like the signature is weird. Why do you need to specify the output file.
   * 
   * 
   * @param pIn - Path object that represents the input file.
   * @param pOut - Path object that represents the output file.
   * @return boolean if the zip was sucessful.
   */
  public static boolean gzip(final Path pIn, final Path pOut) {
    byte[] buffer = new byte[1024];

    try{
      // create output directory
      Files.createDirectories(Paths.get(pOut.getParent().toString()));
      
      // Delete the file
      try {
        Files.delete(Paths.get(pOut.toString()));
      } catch(NoSuchFileException e) {
        // You want to swallow this error.
        // No point in printing it.
        //e.printStackTrace();
      }

      GZIPOutputStream gzos = 
        new GZIPOutputStream(new FileOutputStream(pOut.toString()));

      FileInputStream in = 
          new FileInputStream(pIn.toString());

      // zipping.
      int len;
      while ((len = in.read(buffer)) > 0) {
        gzos.write(buffer, 0, len);
      }

      // Closing streams.
      in.close();
      gzos.finish();
      gzos.close();

      System.out.println("> Compressing " + pIn + " complete to " + pOut);
    } catch(IOException ex){
      ex.printStackTrace();
      return false;   
    }

    return true;
  }
}