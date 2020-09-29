// Copyright 2020 Google LLC

// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//    https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.funccover;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;

// HandlerLoader implements the functionality of loading and starting the Handler.
class HandlerLoader {

  // Loads the given Runnable class to the memory, creates an instance, invokes its run() method.
  protected static void initializeCustomHandler(String path, String className) {
    if (path == null || className == null) {
      return;
    }

    // Converts given jar path to URL.
    URL url = getURL(path);

    // Variable cl is the URLClassloader that will load the entry point of Handler.
    // Any external dependencies of the Handler must be in the jar.
    URLClassLoader cl = null;

    // Variable runnable will keep the instance of given class.
    // Variable entry will keep the run() method of the Runnable.
    Object runnable = null;
    Method entry = null;

    try {
      // Loads the class from given URL.
      cl = new URLClassLoader(new URL[] {url});
      Class<?> cls = cl.loadClass(className);

      // Gets the constructor of given Runnable class an creates an instance.
      Constructor<?> runnableConstructor = cls.getConstructor();
      runnable = runnableConstructor.newInstance();

      // Gets the run() method inside the Runnable.
      entry = cls.getMethod("run");
    } catch (Exception e) {
      e.printStackTrace();
      System.out.println("Could not load the custom handler " + className);
      return;
    }

    if (runnable == null || entry == null || cl == null) {
      return;
    }

    try {
      // Invokes the run() method of the given Runnable class.
      entry.invoke(runnable);
    } catch (Exception e) {
      e.printStackTrace();
      System.out.println("Could not invoke the start() method in " + className);
    }
  }

  // Parses the given path and returns corresponding URL.
  private static URL getURL(String path) {
    File file = new File(path);
    try {
      return new URL("jar", "", "file:" + file.getAbsolutePath() + "!/");
    } catch (java.net.MalformedURLException e) {
      System.out.println("malformed jar path " + path);
      return null;
    }
  }
}
