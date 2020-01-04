package com.gohubert.xbrlfetcher;

import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class Parse {
  // parse the sec index files in idx format.
  public static ArrayList<IndexRow> parseIndex(String path) {
    ArrayList<IndexRow> res = new ArrayList<>();

    try {
      BufferedReader reader = new BufferedReader(new FileReader(path));
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