load("@rules_java//java:defs.bzl", "java_binary", "java_library")

java_binary(
    name = "funccover",
    srcs = [
        "CoverageAgent.java",
        "CoverageMetrics.java",
        "CoverageTransformer.java",
        "Filter.java",
        "HandlerLoader.java",
    ],
    deploy_manifest_lines = ["Premain-Class: com.funccover.CoverageAgent"],
    visibility = ["//visibility:public"],
    deps = [
        "@maven//:org_ow2_asm_asm",
        "@maven//:org_ow2_asm_asm_commons",
    ],
)

java_library(
    name = "CoverageMetrics",
    srcs = ["CoverageMetrics.java"],
    visibility = ["//visibility:public"],
)
