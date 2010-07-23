package org.zeroturnaround.jrebel.mybatis;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collection;
import java.util.HashSet;

public class ResourceContext
{
  private static final ThreadLocal CONTEXT = new ThreadLocal();

  public static void enter()
  {
    CONTEXT.set(new HashSet());
  }

  public static Collection exit()
  {
    Collection resources = (Collection)CONTEXT.get();
    CONTEXT.set(null);
    return resources;
  }

  public static void addUrl(URL url) {
    Collection resources = (Collection)CONTEXT.get();
    if (resources == null)
      return;
    resources.add(url);
  }
  
  public static void addUrl(String urlString) throws MalformedURLException {		
	    Collection resources = (Collection)CONTEXT.get();
	    if (resources == null)
	      return;
	    URL url = new URL(urlString);
	    resources.add(url);
	}
}