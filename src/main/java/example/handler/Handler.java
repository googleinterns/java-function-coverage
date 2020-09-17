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

package example.handler;

import example.handler.collect.Collect;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

public class Handler implements Runnable {

  ScheduledExecutorService scheduler;

  // Function run() will be invoked at the beginning.
  // It is the entry point of Runnable.
  // It creates a scheduler that will call runnable "collect" every 2000ms.//
  // Creates a shutdown hook with "collect"
  public void run() {

    scheduler =
        Executors.newScheduledThreadPool(
            1,
            new ThreadFactory() {
              @Override
              public Thread newThread(Runnable runnable) {
                Thread thread = Executors.defaultThreadFactory().newThread(runnable);
                thread.setDaemon(true);
                return thread;
              }
            });

    Collect collect = new Collect();

    scheduler.scheduleWithFixedDelay(collect, 2000, 2000, TimeUnit.MILLISECONDS);

    Runtime.getRuntime()
        .addShutdownHook(
            new Thread() {
              public void run() {
                scheduler.shutdownNow();
                collect.run();
              }
            });
  }
}
