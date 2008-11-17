//- Copyright 2008 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui.api;

import limelight.ResourceLoader;
import limelight.MockResourceLoader;
import limelight.styles.Style;

import java.util.Map;
import java.util.HashMap;

public class MockScene extends MockProp implements Scene
{
  public Map<String, Style> styles = new HashMap<String, Style>();
  public ResourceLoader loader;

  public ResourceLoader getLoader()
  {
    return loader;
  }

  public Map getStyles()
  {
    return styles;
  }
}
