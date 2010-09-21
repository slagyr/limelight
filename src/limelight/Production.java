//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight;

import limelight.io.FileUtil;
import limelight.ui.api.ProductionProxy;
import limelight.util.ResourceLoader;

public abstract class Production
{
  private String name;
  private boolean allowClose = true;
  private ResourceLoader resourceLoader;
  private ProductionProxy proxy;

  public Production(String path)
  {
    name = FileUtil.baseName(path);
    resourceLoader = ResourceLoader.forRoot(path);
  }
//  String getName();
//  void setName(String name);
//  boolean allowClose();
//  void close();
//  Object callMethod(String name, Object... args);
//  void publish_on_drb(int port);

  public abstract void close();

  public String getName()
  {
    return name;
  }

  public void setName(String name)
  {
    this.name = name;
  }

  public boolean allowClose()
  {
    return allowClose;
  }

  public void setAllowClose(boolean value)
  {
    allowClose = value;
  }

  public ResourceLoader getResourceLoader()
  {
    return resourceLoader;
  }

  public ProductionProxy getProxy()
  {
    return proxy;
  }

  public void setProxy(ProductionProxy proxy)
  {
    this.proxy = proxy;
  }
}
