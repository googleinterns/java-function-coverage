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

package example.program;

import example.program.functions.Functions;
import java.util.Scanner;

public class ExampleProgram {

  public static void main(String[] args) {
    System.out.println("This is an example program to test funccover coverage agent");
    System.out.println("Please print an integer in range [0-9].");
    System.out.println("Program will call f$number function in the functions package.");
    System.out.println("You can enter as many numbers you want.");
    System.out.println(
        "Example handler will save the data to coverage.out file every 500ms and when the program"
            + " exits.");
    System.out.println("Enter -1 to exit the program.");
    Scanner in = new Scanner(System.in);
    while (true) {

      int number = in.nextInt();

      switch (number) {
        case -1:
          exitProgram();
        case 0:
          Functions.f0();
          break;
        case 1:
          Functions.f1();
          break;
        case 2:
          Functions.f2();
          break;
        case 3:
          Functions.f3();
          break;
        case 4:
          Functions.f4();
          break;
        case 5:
          Functions.f5();
          break;
        case 6:
          Functions.f6();
          break;
        case 7:
          Functions.f7();
          break;
        case 8:
          Functions.f8();
          break;
        case 9:
          Functions.f9();
          break;
      }
    }
  }

  private static void exitProgram() {
    System.out.println("Exit!");
    System.exit(0);
  }
}
