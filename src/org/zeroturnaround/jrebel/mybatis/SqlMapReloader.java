package org.zeroturnaround.jrebel.mybatis;

import java.io.CharArrayReader;
import java.io.CharArrayWriter;
import java.io.IOException;
import java.io.Reader;
import java.net.URL;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.ibatis.builder.xml.XMLConfigBuilder;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.defaults.DefaultSqlSessionFactory;
import org.zeroturnaround.javarebel.LoggerFactory;
import org.zeroturnaround.javarebel.support.URLResource;

public class SqlMapReloader
{
  private Map monitoredFiles = Collections.synchronizedMap(new LinkedHashMap());
  
  private char[] config;

  private String environment;
  private Properties properties;

  private DefaultSqlSessionFactory sqlFactory;
  
  private volatile long lastCheck;

  private static SqlMapReloader loader;
  
  public void  setDefaultSqlSessionFactory(DefaultSqlSessionFactory sqlFactory) {
    this.sqlFactory = sqlFactory;
  }
  
  private SqlMapReloader() {
	
  }

  public static SqlMapReloader getInstance(){
	  if(loader==null){
		  loader = new SqlMapReloader();
	  }
	  return loader;
  }
  
  public void reload() {
    synchronized (this) {
      if (this.lastCheck + 1000L < System.currentTimeMillis()) {
        if (hasChanged())
          reconfigure();

        this.lastCheck = System.currentTimeMillis();
      }
    }
  }

  private void reconfigure() {
    LoggerFactory.getInstance().echo("JRebel-MyIbatis: reloading sql maps.");

    Configuration conf = this.sqlFactory.getConfiguration();
    if (conf instanceof JrConfiguration)
      ((JrConfiguration)conf).reinit();
    

    if(config!=null){
    	JrXMLConfigBuilder parser = (JrXMLConfigBuilder)new XMLConfigBuilder(new CharArrayReader(config), environment, properties);

    	parser.reparse();
    }
  }

  private boolean hasChanged()
  {
    boolean hasChanged = false;
    Collection files = Arrays.asList(this.monitoredFiles.values().toArray());

    Iterator it = files.iterator();
    while (it.hasNext()) {
      MonitoredFile file = (MonitoredFile)it.next();
      hasChanged = (hasChanged) || (file.hasChanged());
    }

    return hasChanged;
  }

  public DefaultSqlSessionFactory getDefaultSqlSessionFactory(){
	  return this.sqlFactory;
  }

  public void setConfig(char[] config){
	  this.config = config;
  }
  public void setEnv(String env){
	  this.environment = env;
  }
  
 
  public void setProp(Properties prop){
	  this.properties = prop;
  }
  
  public void enterConf()
  {
    ResourceContext.enter();
  }

  public void exitConf()
  {
	  Collection resources = ResourceContext.exit();
    
     for (Iterator i = resources.iterator(); i.hasNext(); ) {
      URL url = (URL)i.next();
      LoggerFactory.getInstance().log("JRebel-Ibatis: monitoring " + url);
      if (!(this.monitoredFiles.containsKey(url))) {
        MonitoredFile file = new MonitoredFile(new URLResource(url));
        this.monitoredFiles.put(url, file);
      }
    }
  }

  public static char[] capture(Reader reader)
  {
    int bufSize = 128;
    char[] buf = new char[bufSize];
    CharArrayWriter writer = new CharArrayWriter();
    try
    {int len;
      while ((len = reader.read(buf, 0, bufSize)) != -1) {
        
        writer.write(buf, 0, len);
      }
      return writer.toCharArray();
    }
    catch (IOException e) {
      throw new RuntimeException();
    }
  }
}