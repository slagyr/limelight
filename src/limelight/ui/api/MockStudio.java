//- Copyright © 2008-2009 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui.api;

import limelight.Studio;

public class MockStudio extends Studio
{
  public String openedProduction;
  public boolean allowShutdown;
  public boolean isShutdown;

  public Production open(String production)
  {
    openedProduction = production;
    return null;
  }

  public boolean shouldAllowShutdown()
  {
    return allowShutdown;
  }

  public void shutdown()
  {
    isShutdown = true;
  }

  public Object utilities_production()
  {
    return null;
  }

  public void production_closed(Object production)
  {
  }
}
