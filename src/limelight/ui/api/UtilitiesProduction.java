//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui.api;

import limelight.Production;

public class UtilitiesProduction extends Production
{
  private Production production;

  public UtilitiesProduction(Production production)
  {
    super("utilities");
    this.production = production;
  }

  public String getName()
  {
    return production.getName();
  }

  public void setName(String name)
  {
    production.setName(name);
  }

  public boolean allowClose()
  {
    return production.allowClose();
  }

  public void close()
  {
    production.close();
  }

  public Object callMethod(String name, Object... args)
  {
//    return production.callMethod(name, args);
    return null;
  }

  public void publish_on_drb(int port)
  {
//    production.publish_on_drb(port);
  }

  public Object alert(Object message)
  {   
//    return production.callMethod("alert", message);
    return null;
  }

  public Object shouldProceedWithIncompatibleVersion(String name, String version)
  {
//    return production.callMethod("proceed_with_incompatible_version?", name, version);
    return null;
  }

  public Production getProduction()
  {
    return production;
  }
}
