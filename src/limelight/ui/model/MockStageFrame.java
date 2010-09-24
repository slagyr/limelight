//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui.model;

import limelight.ui.api.MockStageProxy;

public class MockStageFrame extends Stage
{
  public boolean wasRefreshed;
  public boolean closed;
  public boolean shouldAllowClose;
  public boolean visible;
  public boolean wasClosed;
  public boolean iconified;
  public boolean activated;

  public MockStageFrame()
  {
    setStage(new MockStageProxy());
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

  public boolean isVisible()
  {
    return visible;
  }
}
