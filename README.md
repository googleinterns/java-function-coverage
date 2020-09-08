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

* Build [funccover agent](#funccover-agent)

```bash
$ bazel build //src/main/java/com/funccover:funccover_deploy.jar
```

* Build the example [handler](#handler) in the repository

```bash
$ bazel build //src/main/java/example/handler:Handler
```

* Build the example program binary with coverage

```bash
$ bazel build --jvmopt="-javaagent:agent_path='jar:handler_path example.handler.Handler' "  //src/main/java/example/program:HelloWorld 

Here agent_path is bazel-bin/src/main/java/com/funccover/funccover_deploy.jar, 
handler_path is bazel-bin/src/main/java/example/handler/libHandler.jar
```

This will generate an executable inside ```bazel-bin/src/main/java/example/program/HelloWorld```.
When you run it, it will ask you to enter numbers in range [1-9] in a line, then it will call ```f$number``` function for each number you entered.
Coverage data will be saved to ```coverage.out```.

### With Customization

* Build [funccover agent](#funccover-agent)

```bash
$ bazel build //src/main/java/com/funccover:funccover_deploy.jar
```


* Implement and build your [handler](#handler)
   * Handler program implements an entry class.
   * Entry class must implement a consturctor that uses CoverageMetrics parameters.
   * Entry class must implement a function start(), this function is the entry point.
   * Agent loads entry class into the memory, creates an instance of it with CoverageMetrics variables and invokes the start() method. 
   * Please take a look at the examples, [Handler](../blob/master/src/main/java/example/handler/Handler.java), [Simple Handler](..blob/master/src/main/java/example/handler/SimpleHandler.java)
   
* Build and run your program

```bash
$ bazel build --jvmopt="-javaagent:path/to/agent.jar='type:path/to/handler packageName.ClassName' "  //YourProgram
```

You can also attach funccover agent to programs that are already built. To attach the agent you should give the agent as an argument to jvm.

```bash
$ java -javaagent:path/to/agent.jar='type:path/to/handler packageName.ClassName' -jar YourProgram.jar
```

### Concepts

Java Function Coverage tool consists of 2 parts, funccover agent and a handler. To collect production coverage data you need to have both.

#### funccover Agent

In general a java agent is just a specially crafted .jar file that utilizes the [Instrumentation API](https://docs.oracle.com/javase/7/docs/api/java/lang/instrument/Instrumentation.html "Instrumentation API"). Using Instrumentation API, funccover agent instruments bytecodes before JVM loads them into memory. Using instrumentation, we record execution of methods inside a data structure called CoverageMetrics. Agent itself does not upload/write the coverage data. Handler program does.

funccover agent gets 2 arguments. 

```bash
"type:path/to/handler packageName.ClassName"
```

* type can be "url", "jar" or "dir". 
    * If its url, path/to/handler must be an url
    * If its jar path/to/handler must be a path to a jar file  
    * If its dir path/to/handler must be a path to a directory that contains .class files (folders must be structured same as jar files)
    
* packageName.ClassName argument must be the fully qualified name of the Handler class

#### Handler

Handler is a program that implements certain functionality. Our agent gets the handler program and its entry class. It loads the program and creates an instance of given class with CoverageMetrics variables. Handler must implment an entry function. funccover agent calls that entry function. 
