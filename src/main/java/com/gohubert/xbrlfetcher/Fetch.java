package com.gohubert.xbrlfetcher;

import java.nio.file.Paths;
import java.nio.file.Files;
import java.net.URL;
import java.net.MalformedURLException;
import java.io.File;
import java.io.IOException;

public class Fetch {
  private static final String XBRL_FILENAME= "xbrl.gz";
  private static final int XBRL_START_YEAR = 1993;
  private static final String XBRL_FULL_INDEX_URL_PREFIX = "https://www.sec.gov/Archives/edgar/full-index";

  private String rootPath;
  private int delay;
  
  public Fetch(String path, String delay) {
    this.rootPath = path;
    this.delay = 30;
  }

  // grabs a gz file.
  // I think technically you ca use this to get any file.
  public boolean get(final String url, final String path) {
    try {
        Files.copy(new URL(url).openStream(), Paths.get(path));
    } catch (final MalformedURLException e) {
        e.printStackTrace();
    } catch (final IOException e) {
        e.printStackTrace();
    }

    return false;
  }

  public boolean fetchXBRLAll() {
    String in = "/tmp/xbrl/index/2009/QTR1/xbrl.gz";
    String out = "/tmp/foo/blah.txt";
    Zip.gunzip(in, out);
    return true;
  }

  // probably need a way to check for existing files.
  public boolean fetchAll() {
    // fix the hard coding for 2020.
    for (int year = this.XBRL_START_YEAR ; year <= 2020 ; year++) {
      for (int qtr = 1 ; qtr <= 4 ; qtr++) {
        String urlDate = "/" + year + "/QTR" + qtr;

        // create dir
        String curDir = this.rootPath + urlDate;
        File file = new File(curDir);
        boolean res = file.mkdirs();

        // download file.
        String url = this.XBRL_FULL_INDEX_URL_PREFIX + urlDate + "/" + this.XBRL_FILENAME;
        this.get(url, curDir + "/" + this.XBRL_FILENAME);

        // sleep
        System.out.println(url);
        try {
          Thread.sleep(this.delay * 1000);
        } catch(InterruptedException e){
            System.out.println("main thread interrupted");
        }
      }
    }

    return true;
  }

  // create the directories to storel the index files.
  // add some error handing later too.
  // public boolean createDirs() {
  //   // fix the hard coding for 2020.
  //   for (int year = this.XBRL_START_YEAR ; year <= 2020 ; year++) {
  //     for (int qtr = 1 ; qtr <= 4 ; qtr++) {
  //       File file = new File(this.rootPath + "/" + year + "/QTR" + qtr);
  //       boolean res = file.mkdirs();
  //     }
  //   }

  //   return true;
  // }

  public boolean run() {
    // final String url = "https://www.sec.gov/Archives/edgar/full-index/2019/QTR1/xbrl.gz";
		// final String path = "/tmp/xbrl.gz";
    // Fetch.getGZ(url, path);
  
    //Creating a File object
    // File file = new File("/tmp/foo");
    // this.createDirs();
    // this.fetchAll();
    this.fetchXBRLAll();

    return false;
  }
}

// final String url = "https://www.sec.gov/Archives/edgar/full-index/2019/QTR1/xbrl.gz";