package org.zeroturnaround.jrebel.mybatis.cbp;

import org.zeroturnaround.bundled.javassist.ClassPool;
import org.zeroturnaround.bundled.javassist.CtClass;
import org.zeroturnaround.bundled.javassist.CtConstructor;
import org.zeroturnaround.javarebel.integration.support.JavassistClassBytecodeProcessor;

public class BaseBuilderCBP extends JavassistClassBytecodeProcessor
{
  public void process(ClassPool cp, ClassLoader cl, CtClass ctClass)
    throws Exception
  {

    cp.importPackage("org.zeroturnaround.jrebel.myibatis");
    cp.importPackage("org.apache.ibatis.type");
    
    
    CtConstructor constructor = ctClass.getDeclaredConstructor(cp.get(new String[]{"org.apache.ibatis.session.Configuration"}));
   
   //myibatis3.0
    constructor.setBody("{" +
  		"SqlMapReloader reloader = SqlMapReloader.getInstance();"+
  		"if(reloader!=null&&reloader.getDefaultSqlSessionFactory()!=null){this.configuration = reloader.getDefaultSqlSessionFactory().getConfiguration();}else{this.configuration = $1;}"+
  		"$0.typeAliasRegistry = $0.configuration.getTypeAliasRegistry();"+
  		"$0.typeHandlerRegistry = $0.configuration.getTypeHandlerRegistry();}"
  		);
  }
}