package com.gohubert.xbrlfetcher;

import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
// import java.nio.file.Paths;

public class Parse {
  /**
   * Parses an index file and return the rows an array of IndexRow objects.
   * See IndexRow to see what the rows look like.
   * 
   * @param path - Path of the idx file.
   * @return - Array of IndexRow items.
   */
  public static ArrayList<IndexRow> parseIndex(Path path) {
    ArrayList<IndexRow> res = new ArrayList<>();

    try {
      BufferedReader reader = new BufferedReader(new FileReader(path.toString()));
      String row = "";
      while ((row = reader.readLine()) != null) {
        IndexRow curRow = IndexRow.create(row);
        if (curRow != null) {
          res.add(curRow);
        }
      }
      reader.close();
    } catch(FileNotFoundException e) {
      e.printStackTrace();
    } catch(IOException e) {
      e.printStackTrace();
    }

    return res;
  }
}