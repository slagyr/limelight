//- Copyright © 2008-2011 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the MIT License.

package limelight.model.api;

import limelight.Context;
import limelight.model.Production;
import limelight.model.Studio;

public class MockStudio extends Studio
{
  public String openedProduction;
  public boolean allowShutdown;
  public boolean isShutdown;
  public boolean shouldProceedWithIncompatibleVersion;

  public static MockStudio installed()
  {
    MockStudio instance = new MockStudio();
    Context.instance().studio = instance;
    return instance;
  }

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

  @Override
  public boolean canProceedWithIncompatibleVersion(String name, String minimumLimelightVersion)
  {
    return shouldProceedWithIncompatibleVersion;
  }


}
