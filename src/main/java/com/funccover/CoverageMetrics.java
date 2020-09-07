//Copyright 2020 Google LLC

//Licensed under the Apache License, Version 2.0 (the "License");
//you may not use this file except in compliance with the License.
//You may obtain a copy of the License at
//
//    https://www.apache.org/licenses/LICENSE-2.0
//
//Unless required by applicable law or agreed to in writing, software
//distributed under the License is distributed on an "AS IS" BASIS,
//WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
//See the License for the specific language governing permissions and
//limitations under the License.

package com.funccover;

import java.util.ArrayList;

// Metrics class saves the coverage data
// Every index corresponds to one method, transformer calls addMethod with method descriptions
// Then it inserts a call to setExecuted method with corresponding index to each method
public class CoverageMetrics {
        
    public static ArrayList<String> classNames = new ArrayList<String>(); 
    public static ArrayList<String> methodNames = new ArrayList<String>(); 
    public static ArrayList<Boolean> methodFlags = new ArrayList<Boolean>(); 

    // Inserts a new flag with the method description
    public static synchronized void addMethod(final String packageName, final String methodName) {
        classNames.add(packageName);
        methodNames.add(methodName);
        methodFlags.add(false);
       
    }

    // Sets the given methods flag value to true
    public static void setExecuted(final int index) {
        methodFlags.set(index, true);
    }

}
