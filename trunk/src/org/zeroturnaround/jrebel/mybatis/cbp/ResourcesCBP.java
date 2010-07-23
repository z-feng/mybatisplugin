package org.zeroturnaround.jrebel.mybatis.cbp;

import org.zeroturnaround.bundled.javassist.ClassPool;
import org.zeroturnaround.bundled.javassist.CtClass;
import org.zeroturnaround.javarebel.integration.support.JavassistClassBytecodeProcessor;
import org.zeroturnaround.jrebel.mybatis.ResourceContext;

public class ResourcesCBP extends JavassistClassBytecodeProcessor
{
  public void process(ClassPool cp, ClassLoader cl, CtClass ctClass)
    throws Exception
  {
	
	//getResourceURL
    ctClass.getDeclaredMethod("getResourceURL", cp.get(new String[] { ClassLoader.class.getName(), String.class.getName() })).insertAfter("if ($_!= null) {  " + ResourceContext.class.getName() + ".addUrl($_);" + "}");

    //getResourceAsStream
    ctClass.getDeclaredMethod("getResourceAsStream", cp.get(new String[] { ClassLoader.class.getName(), String.class.getName() })).insertAfter("if ($_!= null) {  getResourceURL($1, $2);}");

    //getUrlAsStream
    ctClass.getDeclaredMethod("getUrlAsStream", cp.get(new String[] {String.class.getName() })).insertAfter("if ($_!= null) {  " + ResourceContext.class.getName() + ".addUrl($1);" + "}");
    
//    //getUrlAsReader
//    ctClass.getDeclaredMethod("getUrlAsReader", cp.get(new String[] {String.class.getName() })).insertAfter("System.out.println(\"call getUrlAsReader()\");if ($1!= null) {  " + ResourceContext.class.getName() + ".addUrl($1);" + "}");
//
//    //getResourceAsReader
//    ctClass.getDeclaredMethod("getResourceAsReader", cp.get(new String[] {String.class.getName() })).insertAfter("System.out.println(\"call getResourceAsReader()\");if ($1!= null) {  " + ResourceContext.class.getName() + ".addUrl($1);" + "}");
    
  }
}