load("@rules_java//java:defs.bzl", "java_library")

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
    deps = ["@maven//:org_javassist_javassist"],
)
