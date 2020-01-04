package com.gohubert.xbrlfetcher;

import java.util.Map;

public class Env {
  public static void show() {
    Map<String, String> env = System.getenv();
    for (String envName : env.keySet()) {
        System.out.format("%s=%s%n",
                          envName,
                          env.get(envName));
    }
  }

  // Returns the ENV variable.
  // Either a string
  public static String get(final String env) {
    Map<String, String> envs = System.getenv();
    return envs.get(env);
  }
}