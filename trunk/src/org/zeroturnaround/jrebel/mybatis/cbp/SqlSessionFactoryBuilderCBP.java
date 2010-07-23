package org.zeroturnaround.jrebel.mybatis.cbp;

import java.io.Reader;
import java.util.Properties;

import org.zeroturnaround.bundled.javassist.ClassPool;
import org.zeroturnaround.bundled.javassist.CtClass;
import org.zeroturnaround.bundled.javassist.CtField;
import org.zeroturnaround.bundled.javassist.CtMethod;
import org.zeroturnaround.bundled.javassist.CtNewMethod;
import org.zeroturnaround.javarebel.integration.support.JavassistClassBytecodeProcessor;
import org.zeroturnaround.jrebel.mybatis.SqlMapReloader;

public class SqlSessionFactoryBuilderCBP extends JavassistClassBytecodeProcessor
{
  public void process(ClassPool cp, ClassLoader cl, CtClass ctClass)
    throws Exception
  {
    ctClass.addField(new CtField(cp.get(SqlMapReloader.class.getName()), "reloader", ctClass));

    cp.importPackage("java.io");
    cp.importPackage("org.apache.ibatis.session");
    cp.importPackage("org.zeroturnaround.jrebel.myibatis");
    
    CtMethod origMethod1 = ctClass.getDeclaredMethod("build", cp.get(new String[] { Reader.class.getName(),String.class.getName(),Properties.class.getName() }));
    CtMethod copyMethod1 = CtNewMethod.copy(origMethod1, "rebuild", ctClass, null);
    ctClass.addMethod(copyMethod1);
    origMethod1.setBody("{ char[] config = SqlMapReloader.capture($1); SqlSessionFactory factory = rebuild(new CharArrayReader(config),$2,$3); $0.reloader = " + SqlMapReloader.class.getName() + ".getInstance(); $0.reloader.setConfig(config);  $0.reloader.setEnv($2); $0.reloader.setProp($3); return factory;}");
  }
}