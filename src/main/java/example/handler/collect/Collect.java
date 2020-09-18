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

package example.handler.collect;

import com.funccover.CoverageMetrics;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

// Collect implements a Runnable that will write the coverage data.
public class Collect implements Runnable {

  // Method run must be implemented since it is a Runnable
  public void run() {
    try {
      // Creates a file named "coverage.out"
      File coverOut = new File("coverage.out");
      coverOut.createNewFile();
      FileWriter coverWriter = new FileWriter("coverage.out");

      // Iterates thorough all the methods and writes them to the file one by one
      final int len = CoverageMetrics.methodCount;
      for (int i = 0; i < len; i++) {
        coverWriter.write(CoverageMetrics.classNames.get(i) + ":");
        coverWriter.write(CoverageMetrics.methodNames.get(i) + ":");
        if (CoverageMetrics.methodFlags[i]) coverWriter.write("1\n");
        else coverWriter.write("0\n");
      }
      coverWriter.close();
    } catch (IOException e) {
      System.out.println("An error occurred while writing the coverage data");
      e.printStackTrace();
    }
  }
}
