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
  private static final String XBRL_FILENAME= "xbrl.gz";
  private static final int XBRL_START_YEAR = 1993;
  private static final String XBRL_FULL_INDEX_URL_PREFIX = "https://www.sec.gov/Archives/edgar/full-index";
  private static final String XBRL_URL_PREFIX = "https://www.sec.gov/Archives";

  private String rootPath;
  private int delay;
  
  public Fetch(String path, String delay) {
    this.rootPath = path;
    this.delay = 30;
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

  // TODO:
  // The hard bit it to actually fetch the real urls..
  // clean up variable names.
  public boolean fetchXBRLAll() {
    int yearCurrent = Calendar.getInstance().get(Calendar.YEAR);
    for (int year = this.XBRL_START_YEAR ; year <= yearCurrent ; year++) {
      for (int qtr = 1 ; qtr <= 4 ; qtr++) {
        String urlDate = "/" + year + "/QTR" + qtr;

        String in = "/tmp/xbrl/index" + urlDate + "/" + this.XBRL_FILENAME;
        String outDir = "/tmp";

        // TODO: probably add some validations here.
        // Unzips the index file.
        Zip.gunzip(in, outDir, this.XBRL_FILENAME);
        
        // Prases the index files.
        ArrayList<IndexRow> res = Parse.parseIndex(outDir + "/" + this.XBRL_FILENAME);
        
        // Download the individual xbrl files using the index file.
        // Assming the paths are valid
        for (IndexRow row: res) {
          System.out.println("> Downloading " + row);
          String xbrlURL = this.XBRL_URL_PREFIX + "/" + row.getPath();
          String xbrlOutputDir = "/tmp/xbrl/actual" + "/" + row.getPathDir();
          this.get(xbrlURL, xbrlOutputDir, row.getPathFile());

          // Sleep code.
          try {
            Thread.sleep(this.delay * 1000);
          } catch(InterruptedException e){
            System.out.println("main thread interrupted");
          }
        }
      }
    }

    //ArrayList<IndexRow> res = Parse.parseIndex("/tmp/xbrl.idx");
    
    return true;
  }

  // probably need a way to check for existing files.
  // TODO:
  // 1. add a check before downloading it.
  // 2. Fix hard code date.
  public boolean fetchXBRLIndexAll() {
    int yearCurrent = Calendar.getInstance().get(Calendar.YEAR);
    for (int year = this.XBRL_START_YEAR ; year <= yearCurrent ; year++) {
      for (int qtr = 1 ; qtr <= 4 ; qtr++) {
        String urlDate = "/" + year + "/QTR" + qtr;

        // create dir
        String curDir = this.rootPath + urlDate;
        File file = new File(curDir);
        boolean res = file.mkdirs();

        // download file.
        File outputFile = new File(curDir + "/" + this.XBRL_FILENAME);
        String url = this.XBRL_FULL_INDEX_URL_PREFIX + urlDate + "/" + this.XBRL_FILENAME;
        
        // check if file exist before download.
        if (!outputFile.exists()) {
          // Grab the file.
          this.get(url, curDir, this.XBRL_FILENAME);

          // sleep
          System.out.println("> File downloaded " + url);
          try {
            Thread.sleep(this.delay * 1000);
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
    //Creating a File object
    // File file = new File("/tmp/foo");
    // this.createDirs();
    // this.fetchXBRLIndexAll();
    this.fetchXBRLAll();

    return false;
  }
}

// final String url = "https://www.sec.gov/Archives/edgar/full-index/2019/QTR1/xbrl.gz";