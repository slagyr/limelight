//- Copyright © 2008-2009 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui.model;

import junit.framework.TestCase;
import limelight.util.TestUtil;
import limelight.util.Box;
import limelight.MockResourceLoader;

import java.awt.geom.AffineTransform;

public class ImagePanelLayoutTest extends TestCase
{
  private ImagePanel panel;
  private MockPropablePanel parent;
  private RootPanel root;
  private MockResourceLoader loader;

  public void setUp() throws Exception
  {
    root = new RootPanel(new MockPropFrame());
    parent = new MockPropablePanel();
    loader = new MockResourceLoader();
    parent.prop.loader = loader;
    root.setPanel(parent);

    panel = new ImagePanel();
    parent.add(panel);
  }

  public void testOverride() throws Exception
  {
    assertEquals(true, ImagePanelLayout.instance.overides(null));
  }

  public void testGetDimensionsWhenAuto() throws Exception
  {
    panel.setImageFile(TestUtil.DATA_DIR + "/star.gif");
    panel.doLayout();

    assertEquals(200, panel.getHeight());
    assertEquals(200, panel.getWidth());
  }

  public void testGetDimensionsWhenNotAuto() throws Exception
  {
    panel.setImageFile(TestUtil.DATA_DIR + "/star.gif");
    parent.style.setWidth("100");
    parent.style.setHeight("150");
    parent.childConsumableBox = new Box(0, 0, 100, 150);
    parent.doLayout();

    assertEquals(100, panel.getWidth());
    assertEquals(150, panel.getHeight());
  }

  public void testGetScaleTransformWhenDimensionsAreAuto() throws Exception
  {
    panel.setImageFile(TestUtil.DATA_DIR + "/star.gif");
    panel.doLayout();

    AffineTransform tranform = panel.getTransform();
    assertEquals(1.0, tranform.getScaleX(), 0.001);
    assertEquals(1.0, tranform.getScaleY(), 0.001);
  }

  public void testGetScaleTransformWhenDimensionsAreNotAuto() throws Exception
  {
    panel.setImageFile(TestUtil.DATA_DIR + "/star.gif");
    parent.style.setWidth("100");
    parent.style.setHeight("150");
    parent.childConsumableBox = new Box(0, 0, 100, 150);
    parent.doLayout();

    AffineTransform tranform = panel.getTransform();
    assertEquals(0.5, tranform.getScaleX(), 0.001);
    assertEquals(0.75, tranform.getScaleY(), 0.001);
  }

  public void testGetRotationTransformWhenDimensionsAreAuto() throws Exception
  {
    panel.setImageFile(TestUtil.DATA_DIR + "/star.gif");
    panel.setRotation(45);
    panel.doLayout();

    AffineTransform tranform = panel.getTransform();

    // No good way to test rotation....
    assertEquals(141.0, tranform.getTranslateX(), 0.5);
    assertEquals(0.0, tranform.getTranslateY(), 0.5);
  }

  public void testGetScalleTransformWhenDimensionsAreNotAutoAndScalingIsOff() throws Exception
  {
    panel.setImageFile(TestUtil.DATA_DIR + "/star.gif");
    panel.setScaled(false);
    parent.style.setWidth("100");
    parent.style.setHeight("150");
    parent.childConsumableBox = new Box(0, 0, 100, 150);
    parent.doLayout();

    AffineTransform tranform = panel.getTransform();
    assertEquals(1.0, tranform.getScaleX(), 0.001);
    assertEquals(1.0, tranform.getScaleY(), 0.001);
  }

  public void testHasConstrainedProportionsWhenWidthIsNotAuto() throws Exception
  {
    panel.setImageFile(TestUtil.DATA_DIR + "/star.gif");
    parent.style.setWidth("100");
    parent.childConsumableBox = new Box(0, 0, 100, 200);
    parent.doLayout();

    AffineTransform tranform = panel.getTransform();
    assertEquals(0.5, tranform.getScaleX(), 0.001);
    assertEquals(0.5, tranform.getScaleY(), 0.001);
  }

  public void testHasConstrainedProportionsWhenHeightIsNotAuto() throws Exception
  {
    panel.setImageFile(TestUtil.DATA_DIR + "/star.gif");
    parent.style.setHeight("100");
    parent.childConsumableBox = new Box(0, 0, 200, 100);
    parent.doLayout();

    AffineTransform tranform = panel.getTransform();
    assertEquals(0.5, tranform.getScaleX(), 0.001);
    assertEquals(0.5, tranform.getScaleY(), 0.001);
  }

  public void testSizeRemainsWhenStaticAndNotScaled() throws Exception
  {
    panel.setImageFile(TestUtil.DATA_DIR + "/star.gif");
    panel.setScaled(false);
    parent.style.setHeight("400");
    parent.style.setHeight("400");
    parent.childConsumableBox = new Box(0, 0, 400, 400);
    parent.doLayout();

    AffineTransform tranform = panel.getTransform();
    assertEquals(1.0, tranform.getScaleX(), 0.001);
    assertEquals(1.0, tranform.getScaleY(), 0.001);
    assertEquals(200, panel.getWidth());
    assertEquals(200, panel.getHeight());
  }
}
