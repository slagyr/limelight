//- Copyright © 2008-2011 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the MIT License.

package limelight.model.api;

import limelight.model.StylesSource;
import limelight.styles.RichStyle;

import java.util.HashMap;
import java.util.Map;

public class FakeProductionProxy implements ProductionProxy
{
  public TheaterProxy theater;

  public Object send(String name, Object... args)
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

  public Map<String,RichStyle> loadStyles(StylesSource source, Map<String, RichStyle> extendableStyles)
  {
    return new HashMap<String, RichStyle>();
  }
}
