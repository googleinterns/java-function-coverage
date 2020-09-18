load("@rules_java//java:defs.bzl", "java_binary")

java_binary(
    name = "ExampleProgram",
    srcs = ["ExampleProgram.java"],
    main_class = "example.program.ExampleProgram",
    visibility = ["//visibility:public"],
    deps = ["//src/main/java/example/program/functions:Functions"],
)
