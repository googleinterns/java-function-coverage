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

  // Loads the given class to the memory and invokes its start() method with CoverageMetrics
  // variables.
  protected static void initializeCustomHandler(String path, String className) {

    URL url = getURL(path);
    File file = new File(path);

    // cl is the classloader to load the handler.
    URLClassLoader cl = null;

    // handler keeps an instance of given class.
    // starts keeps the start() method.
    Object handler = null;
    Method start = null;

    try {
      // load the class from given url
      cl = new URLClassLoader(new URL[] {url});
      Class cls = cl.loadClass(className);

      // Gets the constructor of given class an create an instance.
      Constructor handlerConstructor =
          cls.getConstructor(
              new Class[] {
                CoverageMetrics.classNames.getClass(),
                CoverageMetrics.methodNames.getClass(),
                CoverageMetrics.methodFlags.getClass()
              });

      // Creates an instance of the handler with CoverageMetrics variables.
      handler =
          handlerConstructor.newInstance(
              CoverageMetrics.classNames, CoverageMetrics.methodNames, CoverageMetrics.methodFlags);

      // Gets the start method in newly created instance.
      start = cls.getMethod("start");
    } catch (Exception e) {
      e.printStackTrace();
      System.out.println("Could not load the custom handler " + className);
      return;
    }

    if (handler == null || start == null || cl == null) {
      return;
    }

    try {
      start.invoke(handler);
    } catch (Exception e) {
      e.printStackTrace();
      System.out.println("Could not invoke the start() method in " + className);
    }
  }

  protected static URL getURL(String path) {

    String[] pathArgs = path.split(":", 2);

    if (pathArgs.length != 2) {
      System.out.println("invalid path " + path);
      return null;
    }

    // Converts dir in filepath to URL.
    if (pathArgs[0].equals("dir")) {
      File file = new File(pathArgs[1]);
      try {
        return file.toURI().toURL();
      } catch (java.net.MalformedURLException e) {
        System.out.println("malformed path " + pathArgs[1]);
        return null;
      }
    }
    // Converts jar in filepath to URL.
    else if (pathArgs[0].equals("jar")) {
      File file = new File(pathArgs[1]);
      try {
        return new URL("jar", "", "file:" + file.getAbsolutePath() + "!/");
      } catch (java.net.MalformedURLException e) {
        System.out.println("malformed path " + pathArgs[1]);
        return null;
      }
    } else if (pathArgs[0].equals("url")) {
      try {
        return new URL(pathArgs[1]);
      } catch (java.net.MalformedURLException e) {
        System.out.println("malformed url " + pathArgs[1]);
        return null;
      }
    }
    System.out.println("invalid path type " + pathArgs[0]);
    return null;
  }
}
