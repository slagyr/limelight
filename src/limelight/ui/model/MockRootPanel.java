//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui.model;

import limelight.styles.RichStyle;
import limelight.ui.Panel;
import java.awt.*;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Map;

public class MockRootPanel extends MockPropablePanel implements RootPanel
{
  public LinkedList<Rectangle> dirtyRegions = new LinkedList<Rectangle>();
  private RootKeyListener keyListener;
  public Map<String, RichStyle> styleStore;

  public MockRootPanel()
  {
    keyListener = new RootKeyListener(this);
  }

  @Override
  public RootPanel getRoot()
  {
    return this;
  }

  public void setFrame(PropFrame frame)
  {
  }

  public boolean hasPanelsNeedingLayout()
  {
    return false;
  }

  public boolean hasDirtyRegions()
  {
    return false;
  }

  public void addPanelNeedingLayout(Panel panel)
  {
  }

  public PropFrame getFrame()
  {
    return null;
  }

  public void setCursor(Cursor cursor)
  {
  }

  public Cursor getCursor()
  {
    return Cursor.getDefaultCursor();
  }

  public ImageCache getImageCache()
  {
    return null;
  }

  public RootKeyListener getKeyListener()
  {
    return keyListener;
  }

  public void getAndClearPanelsNeedingLayout(Collection<Panel> panelBuffer)
  {
  }

  public void getAndClearDirtyRegions(Collection<Rectangle> regionBuffer)
  {
  }

  public void addDirtyRegion(Rectangle bounds)
  {
    dirtyRegions.add(bounds);
  }

  public Map<String, RichStyle> getStylesStore()
  {
    return styleStore;
  }
}
