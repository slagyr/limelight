//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.model.api;

import limelight.model.Production;

public class UtilitiesProduction
{
  private Production production;

  public UtilitiesProduction(Production production)
  {
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
//
//  public void publish_on_drb(int port)
//  {
//    production.getProxy().publish_on_drb(port);
//  }

  public boolean alert(Object message)
  {
    final Object response = production.send("alert", message);
    return Boolean.parseBoolean(response.toString());
  }

  public boolean shouldProceedWithIncompatibleVersion(String name, String version)
  {
    final Object response = production.send("canProceedWithIncompatibleVersion", name, version);
    return Boolean.parseBoolean(response.toString());
  }

  public Production getProduction()
  {
    return production;
  }
}
