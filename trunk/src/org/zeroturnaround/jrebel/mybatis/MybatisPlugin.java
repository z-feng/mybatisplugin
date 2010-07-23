package org.zeroturnaround.jrebel.mybatis;

import org.zeroturnaround.javarebel.ClassResourceSource;
import org.zeroturnaround.javarebel.IntegrationFactory;
import org.zeroturnaround.javarebel.Plugin;
import org.zeroturnaround.jrebel.mybatis.cbp.BaseBuilderCBP;
import org.zeroturnaround.jrebel.mybatis.cbp.ConfigurationCBP;
import org.zeroturnaround.jrebel.mybatis.cbp.DefaultSqlSessionFactoryCBP;
import org.zeroturnaround.jrebel.mybatis.cbp.ReflectorCBP;
import org.zeroturnaround.jrebel.mybatis.cbp.ResourcesCBP;
import org.zeroturnaround.jrebel.mybatis.cbp.SqlSessionFactoryBuilderCBP;
import org.zeroturnaround.jrebel.mybatis.cbp.XMLConfigBuilderCBP;

public class MybatisPlugin
  implements Plugin
{
  public void preinit()
  {
    ClassLoader cl = MybatisPlugin.class.getClassLoader();

    IntegrationFactory.getInstance().addIntegrationProcessor(cl, "org.apache.ibatis.reflection.Reflector", new ReflectorCBP());

    IntegrationFactory.getInstance().addIntegrationProcessor(cl,"org.apache.ibatis.io.Resources", new ResourcesCBP());

    IntegrationFactory.getInstance().addIntegrationProcessor(cl, "org.apache.ibatis.builder.xml.XMLConfigBuilder", new XMLConfigBuilderCBP());

    IntegrationFactory.getInstance().addIntegrationProcessor(cl, "org.apache.ibatis.builder.BaseBuilder",new BaseBuilderCBP());
    
    IntegrationFactory.getInstance().addIntegrationProcessor(cl, "org.apache.ibatis.session.SqlSessionFactoryBuilder",new SqlSessionFactoryBuilderCBP());

    IntegrationFactory.getInstance().addIntegrationProcessor("org.apache.ibatis.session.defaults.DefaultSqlSessionFactory", new DefaultSqlSessionFactoryCBP());
    
    IntegrationFactory.getInstance().addIntegrationProcessor(cl,"org.apache.ibatis.session.Configuration",new ConfigurationCBP());
 
  }

  public boolean checkDependencies(ClassLoader cl, ClassResourceSource crs)
  {
    return (crs.getClassResource("org.apache.ibatis.io.Resources") != null);
  }

  public String getAuthor() {
    return "mochasoft";
  }

  public String getDescription() {
    return "Reloads modified sql maps.";
  }

  public String getId() {
    return "mybatis-plugin";
  }

  public String getName() {
    return "Mybatis Plugin";
  }

  public String getWebsite() {
    return "http://www.mochasoft.com.cn";
  }
}