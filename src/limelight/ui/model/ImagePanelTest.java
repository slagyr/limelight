//- Copyright © 2008-2009 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui.model;

import junit.framework.TestCase;
import limelight.MockResourceLoader;
import limelight.util.Box;
import limelight.util.TestUtil;

import java.awt.geom.AffineTransform;

public class ImagePanelTest extends TestCase
{
  private ImagePanel panel;
  private MockPropablePanel parent;
  private RootPanel root;
  private MockResourceLoader loader;

  public void setUp() throws Exception
  {
    root = new RootPanel(new MockStageFrame());
    parent = new MockPropablePanel();
    loader = new MockResourceLoader();
    parent.prop.loader = loader;
    root.setPanel(parent);

    panel = new ImagePanel();
    parent.add(panel);
  }

  public void testSetImageFile() throws Exception
  {
    panel.setImageFile("blah/blah.png");
    assertEquals("blah/blah.png", panel.getImageFile());
  }

  public void testRotationAngle() throws Exception
  {
    panel.setRotation(0);
    assertEquals(0, panel.getRotation(), 0.1);
  }

  public void testParentIsSterilized() throws Exception
  {
    assertEquals(true, parent.isSterilized());
  }

  public void testScaled() throws Exception
  {
    assertEquals(true, panel.isScaled());

    panel.setScaled(false);

    assertEquals(false, panel.isScaled());
  }
  
  public void testParentSizeChangesAlwaysRequiresLayout() throws Exception
  {
    panel.resetLayout();
    assertEquals(false, panel.needsLayout());

    panel.consumableAreaChanged();

    assertEquals(true, panel.needsLayout());
  }
}
