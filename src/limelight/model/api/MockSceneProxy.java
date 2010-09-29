//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.model.api;

import limelight.ui.model.Scene;
import limelight.util.ResourceLoader;

public class MockSceneProxy extends MockPropProxy implements SceneProxy
{
  public ResourceLoader loader;

  public ResourceLoader getLoader()
  {
    return loader;
  }

  public Scene getPeer()
  {
    return null;
  }
}
