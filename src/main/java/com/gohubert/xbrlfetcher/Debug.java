package com.gohubert.xbrlfetcher;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;

/**
 * Just some debug static methods to dry the code It was stuff that I was using.
 */

public class Debug {
  // prints some info about the path that was specified.
  public static void fileInfo(String path) {
    File file = new File(path);
    System.out.println("> path " + path);
    System.out.println("> exist " + file.exists());
    System.out.println("> size " + file.getTotalSpace());
  }

  // prints the content of a text file.
  // TODO: test if this works.
  public static void printFile(String path) {
    BufferedReader br = null;
    try {
      br = new BufferedReader(new FileReader(path));
    } catch (FileNotFoundException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    String line;
    try {
      while ((line = br.readLine()) != null) {
        System.out.println(line);
      }
      br.close();
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  public static void ls(String dirName) {
    try {
      Files.list(new File(dirName).toPath()).limit(10).forEach(path -> {
        System.out.println(path);
      });
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }
}