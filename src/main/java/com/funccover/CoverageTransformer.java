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

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;
import org.objectweb.asm.*;
import org.objectweb.asm.commons.AdviceAdapter;

// CoverageTransformer implements ClassFileTransformer
// Its transform method will be invoked before JVM loads a class to the memory
public class CoverageTransformer implements ClassFileTransformer {

  // Keeps the number methods instrumented so far.
  // This variable is being used to index methods.
  private static int counter = 0;

  // Method transform instruments given bytecode and returns instrumented bytecode.
  // If it returns null, then given class will be loaded without instrumentation.
  @Override
  public byte[] transform(
      ClassLoader loader,
      String className,
      Class<?> classBeingRedefined,
      ProtectionDomain protectionDomain,
      byte[] classfileBuffer)
      throws IllegalClassFormatException {

    // Filtering classes we won't instrument
    if (classBeingRedefined != null || Filter.check(loader, className) == false) {
      return null;
    }

    // Instruments the methods of given class
    ClassReader reader = new ClassReader(classfileBuffer);
    ClassWriter writer = new ClassWriter(reader, ClassWriter.COMPUTE_MAXS);
    reader.accept(new CoverageClassVisitor(writer, className), ClassReader.EXPAND_FRAMES);

    return writer.toByteArray();
  }

  // CoverageClassVisitor instance will invoke visitMethod for each method.
  public static class CoverageClassVisitor extends ClassVisitor {

    private String className;

    public CoverageClassVisitor(ClassVisitor classVisitor, String className) {
      super(Opcodes.ASM8, classVisitor);
      this.className = className;
    }

    @Override
    public MethodVisitor visitMethod(
        int methodAccess,
        String methodName,
        String methodDesc,
        String signature,
        String[] exceptions) {
      MethodVisitor methodVisitor =
          cv.visitMethod(methodAccess, methodName, methodDesc, signature, exceptions);
      if (methodVisitor == null) {
        return null;
      }
      return new CoverageAdviceAdapter(
          Opcodes.ASM8, methodVisitor, methodAccess, methodName, methodDesc, className);
    }
  }

  // AdviceAdapter to instrument methods.
  public static class CoverageAdviceAdapter extends AdviceAdapter {

    private String methodName, methodDesc, className;

    protected CoverageAdviceAdapter(
        int api,
        MethodVisitor methodVisitor,
        int methodAccess,
        String methodName,
        String methodDesc,
        String className) {
      super(Opcodes.ASM8, methodVisitor, methodAccess, methodName, methodDesc);
      this.methodName = methodName;
      this.methodDesc = methodDesc;
      this.className = className;
    }

    @Override
    protected void onMethodEnter() {
      if ("<init>".equals(methodName) || "<clinit>".equals(methodName)) {
        return;
      }
      // Adds current method to CoverageMetrics
      CoverageMetrics.addMethod(className, methodName + ":" + methodDesc);

      // Adds necessary bytecode to beginning of the method
      // Source code representation is com.funccover.CoverageMetrics.setexecuted(counter)
      mv.visitLdcInsn(counter);
      mv.visitMethodInsn(
          INVOKESTATIC, "com/funccover/CoverageMetrics", "setExecuted", "(I)V", false);

      counter++;
    }
  }
}
