//- Copyright © 2008-2009 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui.model;

import limelight.ui.api.MockStage;
import limelight.ui.MockGraphics;

import java.awt.*;

public class MockStageFrame extends StageFrame
{
  private final Container contentPanel;
  public boolean wasRefreshed;
  public boolean closed;
  public boolean shouldAllowClose;

  public MockStageFrame()
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

  public void close()
  {
    closed = true;
  }

  public boolean shouldAllowClose()
  {
    return shouldAllowClose;
  }
}
