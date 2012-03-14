//- Copyright © 2008-2011 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the MIT License.

package limelight.ui.model;

import junit.framework.TestCase;

import java.awt.*;
import java.util.ArrayList;

import limelight.model.api.FakePropProxy;

public class FloaterLayoutTest extends TestCase
{
  private PropPanel panel;
  private FakeScene root;

  public void setUp() throws Exception
  {
    root = new FakeScene();
    root.setStage(new MockStage());
    panel = new PropPanel(new FakePropProxy());
    root.add(panel);
  }

  public void testOverrides() throws Exception
  {
    assertEquals(true, FloaterLayout.instance.overides(null));
    assertEquals(false, FloaterLayout.instance.overides(PropPanelLayout.instance));
  }

  public void testDoFloatLayoutNonFloater() throws Exception
  {
    panel.getStyle().setX(100);
    panel.getStyle().setY(200);
    panel.getStyle().setFloat(false);
    root.getAndClearDirtyRegions(new ArrayList<Rectangle>());

    FloaterLayout.instance.doLayout(panel);

    assertEquals(0, panel.getX());
    assertEquals(0, panel.getY());
    assertEquals(false, root.dirtyRegions.contains(panel.getAbsoluteBounds()));
  }

  public void testDoFloatLayoutAsFloater() throws Exception
  {
    panel.getStyle().setX(100);
    panel.getStyle().setY(200);
    panel.getStyle().setFloat(true);
    root.getAndClearDirtyRegions(new ArrayList<Rectangle>());
    Rectangle before = panel.getBounds();

    FloaterLayout.instance.doLayout(panel);

    assertEquals(100, panel.getX());
    assertEquals(200, panel.getY());
    assertEquals(true, root.dirtyRegions.contains(before));
    assertEquals(true, root.dirtyRegions.contains(panel.getAbsoluteBounds()));
  }
}
