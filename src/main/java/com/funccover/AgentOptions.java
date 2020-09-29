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

import static java.lang.String.format;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

// Parses and keeps the agent options.
public final class AgentOptions {

  public static final String INCLUDES = "includes";
  public static final String EXCLUDES = "excludes";
  public static final String HANDLER_JAR = "handler-jar";
  public static final String HANDLER_ENTRY = "handler-entry";

  // Options are sperated with comma, following pattern is used to parse them.
  private static final Pattern OPTION_SPLIT = Pattern.compile(",(?=[a-zA-Z0-9_\\-]+=)");

  // Keeps the list of valid options.
  private static final Collection<String> VALID_OPTIONS =
      Arrays.asList(INCLUDES, EXCLUDES, HANDLER_JAR, HANDLER_ENTRY);

  // Maps options to their value.
  private final Map<String, String> options;

  public AgentOptions() {
    this.options = new HashMap<String, String>();
  }

  // Constructor that gets a string an parses it.
  public AgentOptions(final String agentOptions) {
    this();
    if (agentOptions != null && agentOptions != "") {
      for (final String option : OPTION_SPLIT.split(agentOptions)) {
        final int pos = option.indexOf('=');
        if (pos == -1) {
          throw new IllegalArgumentException(
              format("Invalid funccover option syntax \"%s\".", agentOptions));
        }
        final String key = option.substring(0, pos);
        if (!VALID_OPTIONS.contains(key)) {
          throw new IllegalArgumentException(format("Unknown funccover option \"%s\".", key));
        }

        final String value = option.substring(pos + 1);
        setOption(key, value);
      }
    }
  }

  public String getIncludes() {
    return getOption(INCLUDES, "*");
  }

  public String getExcludes() {
    return getOption(EXCLUDES, "");
  }

  public String getHandlerJar() {
    return getOption(HANDLER_JAR, null);
  }

  public String getHandlerEntry() {
    return getOption(HANDLER_ENTRY, null);
  }

  private void setOption(final String key, final String value) {
    options.put(key, value);
  }

  private String getOption(final String key, final String defaultValue) {
    final String value = options.get(key);
    return value == null ? defaultValue : value;
  }
}
