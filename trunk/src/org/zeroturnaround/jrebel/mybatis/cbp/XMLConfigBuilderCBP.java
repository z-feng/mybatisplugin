package org.zeroturnaround.jrebel.mybatis.cbp;

import java.io.Reader;
import java.util.Properties;

import org.zeroturnaround.bundled.javassist.ClassPool;
import org.zeroturnaround.bundled.javassist.CtClass;
import org.zeroturnaround.bundled.javassist.CtConstructor;
import org.zeroturnaround.bundled.javassist.CtField;
import org.zeroturnaround.bundled.javassist.CtMethod;
import org.zeroturnaround.bundled.javassist.CtNewMethod;
import org.zeroturnaround.javarebel.integration.support.JavassistClassBytecodeProcessor;
import org.zeroturnaround.jrebel.mybatis.JrXMLConfigBuilder;
import org.zeroturnaround.jrebel.mybatis.SqlMapReloader;

public class XMLConfigBuilderCBP extends JavassistClassBytecodeProcessor
{
  public void process(ClassPool cp, ClassLoader cl, CtClass ctClass)
    throws Exception
  {
    ctClass.addField(new CtField(cp.get(SqlMapReloader.class.getName()), "reloader", ctClass));
    ctClass.addInterface(cp.get(JrXMLConfigBuilder.class.getName()));
    
    CtConstructor constructor = ctClass.getDeclaredConstructor(cp.get(new String[]{Reader.class.getName(),String.class.getName(),Properties.class.getName()}));
    constructor.insertAfter("reloader = " + SqlMapReloader.class.getName() + ".getInstance();");
   
    //parse()
    CtMethod origMethod = ctClass.getDeclaredMethod("parse");
    CtMethod copyMethod = CtNewMethod.copy(origMethod, "reparse", ctClass, null);
    ctClass.addMethod(copyMethod);
    origMethod.setBody("{if ($0.reloader == null) {    return reparse();  } $0.reloader.enterConf();  try {    return reparse();  } finally {    $0.reloader.exitConf();  }}");
    
  }
}