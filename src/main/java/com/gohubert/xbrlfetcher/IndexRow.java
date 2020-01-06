package com.gohubert.xbrlfetcher;

public class IndexRow {
  private String CIK;
  private String companyName;
  private String formType;
  private String dateFiled;
  private String path;
  private String pathDir;
  private String pathFile;

  public IndexRow(final String CIK, final String companyName, final String formType, final String dateFiled,
      final String filename, final String pathDir, final String pathFile) {
    this.CIK = CIK;
    this.companyName = companyName;
    this.formType = formType;
    this.dateFiled = dateFiled;
    this.path = filename;
    this.pathDir = pathDir;
    this.pathFile = pathFile;
  }

  public String getCIK() {
    return this.CIK;
  }

  public String getCompanyName() {
    return this.companyName;
  }

  public String getFormType() {
    return this.formType;
  }

  public String getDateFiled() {
    return this.dateFiled;
  }

  public String getPath() {
    return this.path;
  }

  public String getPathDir() {
    return this.pathDir;
  }

  public String getPathFile() {
    return this.pathFile;
  }

  public String toString() {
    return this.CIK + " | " + this.companyName + " | " + this.formType + " | " + this.dateFiled + " | " + this.path + " | " + this.pathDir + " | " + this.pathFile;
  }

  // Factory that returns a IndexRow.
  public static IndexRow create(String row) {
    // pipe char need to be escaped
    String[] cols = row.split("\\|");
    IndexRow curRow = null;
    
    // Simple Sanity check.
    // TODO: Add a more comprehensive check
    if (cols.length == 5 && cols[0].length() > 3) {
      // split the path into the directory and file.
      // probably add some checking.
      // probably move this to a separate function.
      String[] paths = cols[4].split("/");
      String pathDir = "";
      for (String curPath: paths) {
        if (pathDir.length() != 0) {
          pathDir += "/";
        }
        if (!curPath.equals(paths[paths.length-1])) {
          pathDir += curPath;
        }
      }
      
      curRow = new IndexRow(cols[0], cols[1], cols[2], cols[3], cols[4], pathDir, paths[paths.length-1]);
    }

    return curRow;
  }
}

// Row looks like
// 1000045|NICHOLAS FINANCIAL INC|10-Q|2019-02-14|edgar/data/1000045/0001193125-19-039489.txt