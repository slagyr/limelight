package limelight.ui.api;

import limelight.SceneLoader;

import java.util.Map;
import java.util.HashMap;

public class MockScene extends MockProp implements Scene
{
  public Map styles = new HashMap();

  public SceneLoader getLoader()
  {
    throw new RuntimeException("MockScene.getLoader() not implemented");
  }

  public Map getStyles()
  {
    return styles;
  }
}
