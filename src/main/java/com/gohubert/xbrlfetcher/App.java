package com.gohubert.xbrlfetcher;

public class App {
	public static void main(final String[] args) {
    // TODO: Fix an issue of ENV is not being passed in thru the docker container.
    // You have to export it
    // Env.show();
    String path = Env.get("XBRL_INDEX_PATH");
    String delay = Env.get("XBRL_DOWNLOAD_DELAY");
    System.out.println(path);
    if (path != null) {
		  Fetch f = new Fetch(path, delay);
      f.run();
    } else {
      System.out.println("Please specify the env XBRL_INDEX_PATH");
    }
	}
}
