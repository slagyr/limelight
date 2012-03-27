//- Copyright © 2008-2011 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the MIT License.

package limelight.ui.model;

import java.awt.*;
import java.util.ArrayList;

import limelight.model.api.FakePropProxy;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

public class FloaterLayoutTest
{
  private PropPanel panel;
  private FakeScene root;

  @Before
  public void setUp() throws Exception
  {
    root = new FakeScene();
    root.setStage(new MockStage());
    panel = new PropPanel(new FakePropProxy());
    root.add(panel);
  }

  @Test
  public void overrides() throws Exception
  {
    assertEquals(true, FloaterLayout.instance.overides(null));
    assertEquals(false, FloaterLayout.instance.overides(PropPanelLayout.instance));
  }

  @Test
  public void doFloatLayoutNonFloater() throws Exception
  {
    panel.getStyle().setX(100);
    panel.getStyle().setY(200);
    panel.getStyle().setFloat(false);
    root.dirtyRegions.clear();

    Layouts.on(panel, FloaterLayout.instance);

    assertEquals(0, panel.getX());
    assertEquals(0, panel.getY());
    assertEquals(false, root.dirtyRegions.contains(panel.getAbsoluteBounds()));
  }

  @Test
  public void doFloatLayoutAsFloater() throws Exception
  {
    panel.getStyle().setX(100);
    panel.getStyle().setY(200);
    panel.getStyle().setFloat(true);
    root.getAndClearDirtyRegions(new ArrayList<Rectangle>());
    Rectangle before = panel.getBounds();

    Layouts.on(panel, FloaterLayout.instance);

    assertEquals(100, panel.getX());
    assertEquals(200, panel.getY());
    assertEquals(true, root.dirtyRegions.contains(before));
    assertEquals(true, root.dirtyRegions.contains(panel.getAbsoluteBounds()));
  }
}
