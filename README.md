**This is not an officially supported Google product.**

# Java Function Coverage

The project aims to collect production Java function-level coverage with low overhead.

## Source Code Headers

Every file containing source code must include copyright and license
information. This includes any JS/CSS files that you might be serving out to
browsers. (This is to help well-intentioned people avoid accidental copying that
doesn't comply with the license.)

Apache header:

```
Copyright 2020 Google LLC

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    https://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```

## Quickstart


### With The Included Examples

* Build [funccover agent](#funccover-agent).

```bash
$ bazel build //src/main/java/com/funccover:funccover_deploy.jar
```

This should generate the file ```bazel-bin/src/main/java/com/funccover/funccover_deploy.jar``` under the project root.

* Build the example [handler](#handler) in the repository.

```bash
$ bazel build //src/main/java/example/handler:handler_deploy.jar
```

This should generate the file ```bazel-bin/src/main/java/example/handler/handler_deploy.jar``` under the project root.

* Build the example program binary.

```bash
$ bazel build //src/main/java/example/program:ExampleProgram 
```

This will generate an executable inside ```bazel-bin/src/main/java/example/program/ExampleProgram```. 

* Run the program with funccover.
```bash
$ ./bazel-bin/src/main/java/example/program/ExampleProgram --jvm_flag="-javaagent:bazel-bin/src/main/java/com/funccover/funccover_deploy.jar=handler-jar=bazel-bin/src/main/java/example/handler/handler_deploy.jar,handler-entry=example.handler.Handler"
```

When you run it, it will ask you to enter numbers in range [0-9], then it will call ```f$number``` function for each number you entered.
Coverage data will be saved to ```coverage.out```.

### With Customization

* Build [funccover agent](#funccover-agent).

```bash
$ bazel build //src/main/java/com/funccover:funccover_deploy.jar
```

* Implement and build your [handler](#handler).
   * Handler program must implement a Runnable class as it's entry pont.
   * Agent will create a new class loader with given path.
   * It will load given entry point, Runnable class into the memory, create an instance of it and invoke the run() method. 
   * Path must contain all the dependencies of handler program since they will be needed in runtime.
   * Please take a look at the examples, [Handler](../master/src/main/java/example/handler/Handler.java), [Simple Handler](../master/src/main/java/example/handler/SimpleHandler.java)
   
* Build your program binary.

```bash
$ bazel build //YourProgram
```

* Run your program with funccover.

```bash
$ ./YourProgram --jvm_flag="-javaagent:path/to/funccover.jar=[option1]=[value1],[option2]=[value2]"
```

You can also attach funccover agent to jar files.

```bash
$ java -javaagent:path/to/agent.jar=[option1]=[value1],[option2]=[value2] -jar YourProgram.jar
```

### Concepts

Java Function Coverage tool consists of 2 parts, funccover agent and a handler. To collect production coverage data you need to have both.

#### funccover Agent

In general a java agent is just a specially crafted .jar file that utilizes the [Instrumentation API](https://docs.oracle.com/javase/7/docs/api/java/lang/instrument/Instrumentation.html "Instrumentation API"). Using Instrumentation API, funccover agent instruments bytecodes before JVM loads them into memory. Using instrumentation, we record execution of methods inside a data structure called CoverageMetrics. Agent itself does not upload/write the coverage data. Handler program does.

funccover has 4 options. Options must be given in following format:
```bash
-javaagent:path/to/funccover.jar=[option1]=[value1],[option2]=[value2]...
```

| Option | Description |
| --- | ----------- |
| handler-jar | Path to handler's jar file. |
| handler-entry | Fully qualified name of the entry class that implements a Runnable (package.name.ClassName).|
| includes | A list of class names that should be included in instrumentation. The list entries are separated by a colon (:) and may use wildcard characters (* and ?). Default is *.
| excludes | A list of class names that should be excluded from instrumentation. The list entries are separated by a colon (:) and may use wildcard characters (* and ?). |

#### Handler

Handler is a program that implements Runnable class as entry point. Our agent gets the handler program and its entry class. It loads the program and creates an instance of given entry class.
