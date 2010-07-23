package org.zeroturnaround.jrebel.mybatis;

import org.zeroturnaround.javarebel.Resource;

public class MonitoredFile
{
  private Resource resource;
  private long lastModified;

  public MonitoredFile(Resource resource)
  {
    this.resource = resource;
    this.lastModified = resource.lastModified();
  }

  public boolean hasChanged() {
    if (this.resource.lastModified() > this.lastModified) {
      this.lastModified = this.resource.lastModified();
      return true;
    }
    return false;
  }

  public String toString() {
    return "MonitoredFile resource=" + this.resource;
  }
}