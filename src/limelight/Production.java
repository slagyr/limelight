//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight;

import limelight.io.FileUtil;
import limelight.ui.api.CastingDirector;
import limelight.ui.api.ProductionProxy;
import limelight.util.ResourceLoader;

public abstract class Production
{
  private String name;
  private boolean allowClose = true;
  private ResourceLoader resourceLoader;
  private ProductionProxy proxy;
  private CastingDirector castingDirector;

  public Production(String path)
  {
    name = FileUtil.baseName(path);
    resourceLoader = ResourceLoader.forRoot(path);
  }

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


  public CastingDirector getCastingDirector()
  {
    return castingDirector;
  }

  public void setCastingDirector(CastingDirector director)
  {
    castingDirector = director;
  }
}
