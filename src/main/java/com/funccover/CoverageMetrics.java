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

import java.util.ArrayList;

// CoverageMetrics class saves the coverage data.
// Every index corresponds to one method, transformer calls addMethod with method descriptions.
// Then it inserts a call to setExecuted with corresponding index to each method in bytecode.
public class CoverageMetrics {

  public static ArrayList<String> classNames = new ArrayList<String>();
  public static ArrayList<String> methodNames = new ArrayList<String>();

  // Instead of an ArrayList primitive boolean array is used here to avoid unnecessary overhead.
  // Variables, classNames and methodNames can be implemented in such way too.
  // Since a method is only added once and can be executed any number of times methodFlags being
  // primitive is vital.
  public static boolean[] methodFlags = new boolean[1024];

  private static int ARRAY_SIZE = 1024;
  public static int METHOD_COUNT = 0;

  // Inserts a new flag with the method descriptions.
  public static synchronized void addMethod(final String packageName, final String methodName) {
    // If size of methodFlags is smaller than number of methods, doubles its size.
    // Since we increase the size exponentially, in average time complexity of this method is O(1)
    // Implemented carefully to avoid race conditions since other threads can call setExecuted
    if (METHOD_COUNT == ARRAY_SIZE) {
      boolean[] temp = methodFlags;
      methodFlags = new boolean[ARRAY_SIZE * 2];
      for (int i = 0; i < ARRAY_SIZE; i++) {
        methodFlags[i] |= temp[i];
      }
      ARRAY_SIZE <<= 1;
    }

    // Adds method descriptions to the variables
    classNames.add(packageName);
    methodNames.add(methodName);

    METHOD_COUNT++;
  }

  // Sets the given methods flag value to true.
  public static void setExecuted(final int index) {
    methodFlags[index] = true;
  }
}
