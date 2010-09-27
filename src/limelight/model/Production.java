//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.model;

import limelight.io.FileUtil;
import limelight.model.api.CastingDirector;
import limelight.model.api.ProductionProxy;
import limelight.util.ResourceLoader;

public abstract class Production
{
  private String name;
  private boolean allowClose = true;
  private ResourceLoader resourceLoader;
  private ProductionProxy proxy;
  private CastingDirector castingDirector;
  private Theater theater;

  public Production(String path)
  {
    try
    {
      name = FileUtil.baseName(path);
    }
    catch(Exception e)
    {
    }
    resourceLoader = ResourceLoader.forRoot(path);
    theater = new Theater(this);
  }

  public abstract void open();
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
    castingDirector = proxy.getCastingDirector();    
    theater.setProxy(proxy.getTheater());
  }

  public CastingDirector getCastingDirector()
  {
    return castingDirector;
  }

  public void setCastingDirector(CastingDirector director)
  {
    castingDirector = director;
  }

  public void attemptClose()
  {
    if(allowClose())
      close();
  }

  public Theater getTheater()
  {
    return theater;
  }

  public void setTheater(Theater theater)
  {
    this.theater = theater;
  }
}
