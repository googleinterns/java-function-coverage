load("@rules_java//java:defs.bzl", "java_binary")

java_binary(
    name = "handler",
    srcs = ["Handler.java"],
    visibility = ["//visibility:public"],
    deps = ["//src/main/java/example/handler/collect:Collect"],
)

java_binary(
    name = "simpleHandler",
    srcs = ["SimpleHandler.java"],
    visibility = ["//visibility:public"],
)
