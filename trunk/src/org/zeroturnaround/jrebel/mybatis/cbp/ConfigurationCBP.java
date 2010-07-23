package org.zeroturnaround.jrebel.mybatis.cbp;

import org.zeroturnaround.bundled.javassist.ClassPool;
import org.zeroturnaround.bundled.javassist.CtClass;
import org.zeroturnaround.bundled.javassist.CtNewMethod;
import org.zeroturnaround.javarebel.integration.support.JavassistClassBytecodeProcessor;
import org.zeroturnaround.jrebel.mybatis.JrConfiguration;

public class ConfigurationCBP extends JavassistClassBytecodeProcessor
{
  public void process(ClassPool cp, ClassLoader cl, CtClass ctClass)
    throws Exception
  {
    cp.importPackage("java.util");

    ctClass.addInterface(cp.get(JrConfiguration.class.getName()));

    ctClass.addMethod(CtNewMethod.make("public void reinit() {" +
    		"mappedStatements.clear();" +
    		"caches.clear();" +
    		"resultMaps.clear();" +
    		"parameterMaps.clear();" +
    		"keyGenerators.clear();" +
    		"loadedResources.clear();}", 
    		ctClass));
  }
}