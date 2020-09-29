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

package example.handler;

public class SimpleHandler implements Runnable {

  // Entry point run() method must be implemented for Runnable.
  // It will be loaded and invoked by HandlerLoader.
  public void run() {
    // You can import CoverageMetrics and use it to upload coverage data.
    System.out.println("Simple Handler started executing");
    Runner runner = new Runner("google");
    runner.hello();
  }

  // Some random class definition.
  public class HelloWorld {

    private String str;

    public HelloWorld(String name) {
      str = name;
    }

    public void hello() {
      System.out.println("hello " + str);
    }
  }
}
