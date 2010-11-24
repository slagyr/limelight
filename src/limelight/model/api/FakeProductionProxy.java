//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.model.api;

import java.util.Map;

public class FakeProductionProxy implements ProductionProxy
{
  public TheaterProxy theater;

  public Object callMethod(String name, Object... args)
  {
    return null;
  }

  public TheaterProxy getTheater()
  {
    return theater;
  }

  public void illuminate()
  {
  }

  public void loadLibraries()
  {
  }

  public void loadStages()
  {
  }

  public SceneProxy loadScene(String scenePath, Map<String, Object> options)
  {
    return null;
  }

  public void loadStyles(SceneProxy scene)
  {
  }
}
