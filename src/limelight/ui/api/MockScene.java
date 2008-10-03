//- Copyright 2008 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui.api;

import limelight.ResourceLoader;

import java.util.Map;
import java.util.HashMap;

public class MockScene extends MockProp implements Scene
{
  public Map styles = new HashMap();

  public ResourceLoader getLoader()
  {
    throw new RuntimeException("MockScene.getLoader() not implemented");
  }

  public Map getStyles()
  {
    return styles;
  }
}
