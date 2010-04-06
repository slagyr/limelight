//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui.api;

import limelight.ResourceLoader;
import limelight.styles.Style;
import java.util.Map;
import java.util.HashMap;

public class MockScene extends MockProp implements Scene
{
  public ResourceLoader loader;

  public ResourceLoader getLoader()
  {
    return loader;
  }
}
