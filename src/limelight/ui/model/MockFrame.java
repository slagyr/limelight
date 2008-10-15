//- Copyright 2008 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui.model;

import limelight.ui.api.MockStage;
import limelight.ui.MockGraphics;

import java.awt.*;

public class MockFrame extends Frame
{
  private final Container contentPanel;
  public boolean wasRefreshed;

  public MockFrame()
  {
    setStage(new MockStage());
    contentPanel = new Container(){
      public Graphics getGraphics()
      {
        return new MockGraphics();
      }
    };
  }

  public Container getContentPane()
  {
    return contentPanel;
  }

  public void setRoot(RootPanel root)
  {
    this.root = root;
  }

  public void refresh()
  {
    wasRefreshed = true;
  }
}
