package com.gohubert.xbrlfetcher;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.MalformedURLException;
import java.nio.file.Files;
// import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Properties;

public class Fetch {
  // TODO:
  // Don't really like loadiing the env variables like this.
  // Is there a better way to do this.
  private String TMP_DIR;
  private String XBRL_ACTUAL_DIR;
  private int XBRL_DOWNLOAD_DELAY;
  private String XBRL_OUTPUT_FILENAME;
  private String XBRL_FULL_INDEX_URL_PREFIX;
  private String XBRL_INDEX_DIR;
  private int XBRL_START_YEAR;
  private String XBRL_URL_PREFIX;
  private String XBRL_ZIP_FILENAME;
  // Properties configFile;
  Config cfg;
  
  public Fetch() {
    // Load the configuration.
    cfg = new Config();
    this.TMP_DIR = cfg.getString("TMP_DIR");
    this.XBRL_ACTUAL_DIR = cfg.getString("XBRL_ACTUAL_DIR");
    this.XBRL_DOWNLOAD_DELAY = cfg.getInt("XBRL_DOWNLOAD_DELAY");
    this.XBRL_FULL_INDEX_URL_PREFIX = cfg.getString("XBRL_FULL_INDEX_URL_PREFIX");
    this.XBRL_INDEX_DIR = cfg.getString("XBRL_INDEX_DIR");
    this.XBRL_OUTPUT_FILENAME = cfg.getString("XBRL_OUTPUT_FILENAME");
    this.XBRL_START_YEAR = cfg.getInt("XBRL_START_YEAR");
    this.XBRL_URL_PREFIX = cfg.getString("XBRL_URL_PREFIX");
    this.XBRL_ZIP_FILENAME = cfg.getString("XBRL_ZIP_FILENAME");
  }

  // grabs a gz file.
  // I think technically you ca use this to get any file.
  // url is the url to fetch the file
  // path is the output path.
  // document this.
  /**
   * Grabs a url to a and saves it to a location.
   * Slight modification to use a path.
   * 
   * https://www.mkyong.com/java/how-to-decompress-file-from-gzip-file/
   */
  public boolean get(final String url, final Path path) {
    try {
      // making the path if it does not exist.
      Files.createDirectories(Paths.get(path.getParent().toString()));
      
      // Download the actual file.
      Files.copy(new URL(url).openStream(), path);
    } catch (final MalformedURLException e) {
      e.printStackTrace();
    } catch (final IOException e) {
      e.printStackTrace();
    }

    return false;
  }

  /**
   * Fetches the actual XBRL files.
   * You need to run fetchXBRLIndexAll before running this.
   */
  public boolean fetchXBRLAll() {
    int yearCurrent = Calendar.getInstance().get(Calendar.YEAR);
    
    for (int year = this.XBRL_START_YEAR ; year <= yearCurrent ; year++) {
      for (int qtr = 1 ; qtr <= 4 ; qtr++) {
        String urlDate = "/" + year + "/QTR" + qtr;

        // PATH OBJECTS
        Path pInFile = Paths.get(this.XBRL_INDEX_DIR + urlDate + "/" + this.XBRL_ZIP_FILENAME);
        Path pTempFile = Paths.get(this.TMP_DIR + "/" + this.XBRL_OUTPUT_FILENAME);
        Path pOutFile;
        Path pOutGZFile; 
        
        // TODO: probably add some validations here.
        // Unzips the index file.
        Zip.gunzip(pInFile, pTempFile);
        
        // Prases the index files.
        // ArrayList<IndexRow> res = Parse.parseIndex(outDir + "/" + this.XBRL_OUTPUT_FILENAME);
        ArrayList<IndexRow> res = Parse.parseIndex(pTempFile);
        
        // Download the individual xbrl files using the index file.
        for (IndexRow row: res) {
          String xbrlURL = this.XBRL_URL_PREFIX + "/" + row.getPath();
          
          pOutFile = Paths.get(this.XBRL_ACTUAL_DIR + "/" + row.getPath());
          pOutGZFile = Paths.get(pOutFile.getParent() + "/" + new String(row.getPathFile()).split("\\.")[0] + ".gz");        
          
          // TODO:
          // Probably should have some configuration on this to allow a full download.
          // There is also an issue of the file not being complete.
          if (!Files.exists(pOutGZFile)) {
            // Download the xbrl file.
            this.get(xbrlURL, pOutFile);
            System.out.println("> Downloaded " + row);

            // Compress the file.
            // Its a text file so compression is like 10x space saving.
            // TODO: Add an option for this.
            Zip.gzip(pOutFile, pOutGZFile);

            // Delete the text file.
            try {
              Files.delete(Paths.get(pOutFile.toString()));
            } catch(IOException e) {
              // You want to swallow this error.
              // No point in printing it.
              //e.printStackTrace();
            }

            // Sleep to not spam the server.
            try {
              Thread.sleep(this.XBRL_DOWNLOAD_DELAY * 1000);
            } catch(InterruptedException e){
              System.out.println("main thread interrupted");
            }
          } else {
            System.out.println("> File exist. Skipping " + row);
          }
        }
      }
    }

    return true;
  }

  /**
   * Fetches the index files on the SEC Edgar website.
   * These index files are used to find the actual xbrl files.
   * The index files are gzipped.
   */
  public boolean fetchXBRLIndexAll() {
    int yearCurrent = Calendar.getInstance().get(Calendar.YEAR);
    for (int year = this.XBRL_START_YEAR ; year <= yearCurrent ; year++) {
      for (int qtr = 1 ; qtr <= 4 ; qtr++) {
        String urlDate = "/" + year + "/QTR" + qtr;
        
        // PATH OBJECTS
        Path pOutFile = Paths.get(this.XBRL_INDEX_DIR + urlDate + "/" + this.XBRL_ZIP_FILENAME);;
        
        // create dir
        try {
          Files.createDirectories(Paths.get(pOutFile.getParent().toString()));
        } catch(IOException e) {
          e.printStackTrace();
        }

        // download file.
        String url = this.XBRL_FULL_INDEX_URL_PREFIX + urlDate + "/" + this.XBRL_ZIP_FILENAME;
        
        // check if file exist before download.
        if (!Files.exists(pOutFile)) {
          // Grab the file.
          this.get(url, pOutFile);

          // sleep
          System.out.println("> File downloaded " + url);
          try {
            Thread.sleep(this.XBRL_DOWNLOAD_DELAY * 1000);
          } catch(InterruptedException e){
            System.out.println("main thread interrupted");
          }
        } else {
          System.out.println("> File exist. Skipping " + url);
        }
      }
    }

    return true;
  }

  public boolean run() {
    this.fetchXBRLIndexAll();
    this.fetchXBRLAll();
    return false;
  }
}