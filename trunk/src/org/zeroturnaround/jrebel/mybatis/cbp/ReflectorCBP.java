package org.zeroturnaround.jrebel.mybatis.cbp;

import org.zeroturnaround.bundled.javassist.ClassPool;
import org.zeroturnaround.bundled.javassist.CtClass;
import org.zeroturnaround.bundled.javassist.CtMethod;
import org.zeroturnaround.bundled.javassist.CtNewMethod;
import org.zeroturnaround.javarebel.ClassEventListener;
import org.zeroturnaround.javarebel.integration.support.JavassistClassBytecodeProcessor;

public class ReflectorCBP extends JavassistClassBytecodeProcessor
{
  public void process(ClassPool cp, ClassLoader cl, CtClass ctClass)
    throws Exception
  {
    cp.importPackage("java.lang.ref");
    cp.importPackage("java.util");
    cp.importPackage("org.zeroturnaround.javarebel");

    ctClass.addInterface(cp.get(ClassEventListener.class.getName()));

    ctClass.getDeclaredConstructor(cp.get(new String[] { Class.class.getName() })).insertAfter("ReloaderFactory.getInstance().addClassReloadListener(new WeakReference($0));");

    ctClass.getDeclaredMethod("forClass").insertBefore("ReloaderFactory.getInstance().checkAndReload($1);");

    ctClass.addMethod(CtNewMethod.make("public int priority() {  return ClassEventListener.PRIORITY_DEFAULT;}", ctClass));

    CtMethod onClassEventMethod = CtNewMethod.make("public void onClassEvent(int eventType, Class clazz) {  if (eventType != ClassEventListener.EVENT_RELOADED) return;   synchronized(REFLECTOR_MAP) {    for (Iterator i = new ArrayList(REFLECTOR_MAP.keySet()).iterator(); i.hasNext(); ) {      Class c = (Class) i.next();      if (clazz.isAssignableFrom(c)) {        LoggerFactory.getInstance().log(\"Clearing ibatis class info cache for: \" + c);        REFLECTOR_MAP.remove(c);      }    }  }}", ctClass);

    ctClass.addMethod(onClassEventMethod);
  }
}