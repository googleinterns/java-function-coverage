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

import java.lang.instrument.Instrumentation;

public class CoverageAgent {

  // Method premain is the entry point of agent.
  // Java Virtual Machine invokes premain before main
  // Initializes the Handler and Transformer.
  public static void premain(String args, Instrumentation inst) {

    if (args == null || args == "") {
      System.out.println("no arguments");
      return;
    }

    String[] tokens = args.split(" ");
    if (tokens.length != 2) {
      System.out.println("arguments are invalid");
      return;
    }

    // Loads the handler, creates an instance of given runnable object
    // Invokes its entry point run().
    HandlerLoader.initializeCustomHandler(tokens[0], tokens[1]);

    // Adds CoverageTransformer class as Transformer
    // CoverageTransformer implements transform method which will be invoked before jvm loads a
    // class.
    inst.addTransformer(new CoverageTransformer());
  }
}
