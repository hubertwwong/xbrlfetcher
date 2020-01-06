package com.gohubert.xbrlfetcher;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.MalformedURLException;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Calendar;

public class Fetch {
  private String TMP_DIR;
  private String XBRL_ACTUAL_DIR;
  private int XBRL_DOWNLOAD_DELAY;
  private String XBRL_OUTPUT_FILENAME;
  private String XBRL_FULL_INDEX_URL_PREFIX;
  private String XBRL_INDEX_DIR;
  private int XBRL_START_YEAR;
  private String XBRL_URL_PREFIX;
  private String XBRL_ZIP_FILENAME;
  
  public Fetch() {
    this.TMP_DIR = Env.get("TMP_DIR");
    this.XBRL_ACTUAL_DIR = Env.get("XBRL_ACTUAL_DIR");
    this.XBRL_DOWNLOAD_DELAY = Integer.parseInt(Env.get("XBRL_DOWNLOAD_DELAY"));
    this.XBRL_FULL_INDEX_URL_PREFIX = Env.get("XBRL_FULL_INDEX_URL_PREFIX");
    this.XBRL_INDEX_DIR = Env.get("XBRL_INDEX_DIR");
    this.XBRL_OUTPUT_FILENAME = Env.get("XBRL_OUTPUT_FILENAME");
    this.XBRL_START_YEAR = Integer.parseInt(Env.get("XBRL_START_YEAR"));
    this.XBRL_URL_PREFIX = Env.get("XBRL_URL_PREFIX");
    this.XBRL_ZIP_FILENAME = Env.get("XBRL_ZIP_FILENAME");
  }

  // grabs a gz file.
  // I think technically you ca use this to get any file.
  // https://www.mkyong.com/java/how-to-decompress-file-from-gzip-file/
  // url is the url to fetch the file
  // path is the output path.
  // document this.
  public boolean get(final String url, final String curDir, final String curFile) {
    try {
      // making the path if it does not exist.
      File file = new File(curDir);
      boolean res = file.mkdirs();

      // Download the actual file.
      Files.copy(new URL(url).openStream(), Paths.get(curDir + "/" + curFile));
    } catch (final MalformedURLException e) {
      e.printStackTrace();
    } catch (final IOException e) {
      e.printStackTrace();
    }

    return false;
  }

  public boolean fetchXBRLAll() {
    int yearCurrent = Calendar.getInstance().get(Calendar.YEAR);
    
    for (int year = this.XBRL_START_YEAR ; year <= yearCurrent ; year++) {
      for (int qtr = 1 ; qtr <= 4 ; qtr++) {
        String urlDate = "/" + year + "/QTR" + qtr;

        String in = this.XBRL_INDEX_DIR + urlDate + "/" + this.XBRL_ZIP_FILENAME;
        String outDir = this.TMP_DIR;

        // TODO: probably add some validations here.
        // Unzips the index file.
        Zip.gunzip(in, outDir, this.XBRL_OUTPUT_FILENAME);
        
        // Prases the index files.
        ArrayList<IndexRow> res = Parse.parseIndex(outDir + "/" + this.XBRL_OUTPUT_FILENAME);
        
        // Download the individual xbrl files using the index file.
        for (IndexRow row: res) {
          String xbrlURL = this.XBRL_URL_PREFIX + "/" + row.getPath();
          String xbrlOutputDir = this.XBRL_ACTUAL_DIR + "/" + row.getPathDir();
          File xbrlOutputFile = new File("" + xbrlOutputDir + "/" + row.getPathFile());
          
          // TODO:
          // Probably should have some configuration on this to allow a full download.
          // There is also an issue of the file not being complete.
          if (!xbrlOutputFile.exists()) {
            // Download the xbrl file.
            this.get(xbrlURL, xbrlOutputDir, row.getPathFile());
            System.out.println("> Downloaded " + row);

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

  // probably need a way to check for existing files.
  // TODO:
  // 1. add a check before downloading it.
  public boolean fetchXBRLIndexAll() {
    int yearCurrent = Calendar.getInstance().get(Calendar.YEAR);
    for (int year = this.XBRL_START_YEAR ; year <= yearCurrent ; year++) {
      for (int qtr = 1 ; qtr <= 4 ; qtr++) {
        String urlDate = "/" + year + "/QTR" + qtr;

        // create dir
        String curDir = this.XBRL_INDEX_DIR + urlDate;
        File file = new File(curDir);
        boolean res = file.mkdirs();

        // download file.
        File outputFile = new File(curDir + "/" + this.XBRL_ZIP_FILENAME);
        String url = this.XBRL_FULL_INDEX_URL_PREFIX + urlDate + "/" + this.XBRL_ZIP_FILENAME;
        
        // check if file exist before download.
        if (!outputFile.exists()) {
          // Grab the file.
          this.get(url, curDir, this.XBRL_ZIP_FILENAME);

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
    // this.fetchXBRLIndexAll();
    this.fetchXBRLAll();
    return false;
  }
}

// final String url = "https://www.sec.gov/Archives/edgar/full-index/2019/QTR1/xbrl.gz";