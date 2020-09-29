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

import java.util.regex.Pattern;

class Filter {

  private static Pattern includesPattern;
  private static Pattern excludesPattern;

  // Converts given includes expression with wildcards to regex and saves it.
  protected static void setIncludes(String includes) {
    includesPattern = Pattern.compile(toRegexAll(includes));
  }

  // Converts given excludes expression with wildcards to regex and saves it.
  protected static void setExcludes(String excludes) {
    excludesPattern = Pattern.compile(toRegexAll(excludes));
  }

  // Filters classes according to includes and exlcludes patterns.
  // Filters agent classes and classes in loaders that can not load CoverageMetrics such as
  // bootstrap classloader.
  protected static boolean check(ClassLoader loader, String className) {
    if (loader == null || className == null) {
      return false;
    }
    className = className.replace('/', '.');
    if (className.startsWith("com.funccover")) {
      return false;
    }
    while (loader != null && loader != CoverageMetrics.class.getClassLoader()) {
      loader = loader.getParent();
    }
    if (loader == CoverageMetrics.class.getClassLoader() && filterRegex(className)) {
      return true;
    }
    return false;
  }

  // Returns true only if x matches with includesPattern and doesn't match with excludesPattern.
  private static boolean filterRegex(String s) {
    return includesPattern.matcher(s).matches() == true
        && excludesPattern.matcher(s).matches() == false;
  }

  // Converts given string with wildcards to a regex expression.
  // Valid wildcards are '?' and '*'.
  // Expressions are seperated with  ':' in between.
  private static String toRegexAll(String accept) {
    final String[] sliced = accept.split("\\:");
    final StringBuilder regex = new StringBuilder();
    boolean before = false;
    for (final String expression : sliced) {
      if (before) {
        regex.append('|');
      }
      regex.append('(').append(toRegex(expression)).append(')');
      before = true;
    }
    return regex.toString();
  }

  // Converts given string with wildcards to regex.
  // Valid wildcards are '?' and '*'.
  // Expression doesn't contain any seperator (':').
  private static CharSequence toRegex(final String expression) {
    final StringBuilder regex = new StringBuilder();
    for (final char c : expression.toCharArray()) {
      switch (c) {
        case '*':
          regex.append(".*");
          break;
        case '?':
          regex.append(".");
          break;
        default:
          regex.append(Pattern.quote(String.valueOf(c)));
          break;
      }
    }
    return regex;
  }
}
