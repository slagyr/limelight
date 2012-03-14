//- Copyright © 2008-2011 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the MIT License.

package limelight.ui.model;

import limelight.io.FileSystem;
import limelight.model.FakeProduction;
import limelight.model.api.FakePropProxy;
import limelight.util.TestUtil;
import limelight.util.Box;
import org.junit.Before;
import org.junit.Test;

import java.awt.geom.AffineTransform;

import static org.junit.Assert.assertEquals;

public class ImagePanelLayoutTest
{
  private ImagePanel panel;
  private MockProp parent;

  @Before
  public void setUp() throws Exception
  {
    FileSystem.installed();
    Scene root = new FakeScene();
    parent = new MockProp();
    root.add(parent);
    panel = new ImagePanel();
    parent.add(panel);
  }

  @Test
  public void override() throws Exception
  {
    assertEquals(true, ImagePanelLayout.instance.overides(null));
  }

  @Test
  public void getDimensionsWhenAuto() throws Exception
  {
    final String filePath = TestUtil.DATA_DIR + "/star.gif";
    panel.setFilename(filePath);
    panel.doLayout();

    assertEquals(200, panel.getHeight());
    assertEquals(200, panel.getWidth());
  }

  @Test
  public void getDimensionsWhenNotAuto() throws Exception
  {
    panel.setFilename(TestUtil.DATA_DIR + "/star.gif");
    parent.style.setWidth("100");
    parent.style.setHeight("150");
    parent.childConsumableBounds = new Box(0, 0, 100, 150);
    parent.doLayout();

    assertEquals(100, panel.getWidth());
    assertEquals(150, panel.getHeight());
  }

  @Test
  public void getScaleTransformWhenDimensionsAreAuto() throws Exception
  {
    panel.setFilename(TestUtil.DATA_DIR + "/star.gif");
    panel.doLayout();

    AffineTransform tranform = panel.getTransform();
    assertEquals(1.0, tranform.getScaleX(), 0.001);
    assertEquals(1.0, tranform.getScaleY(), 0.001);
  }

  @Test
  public void getScaleTransformWhenDimensionsAreNotAuto() throws Exception
  {
    panel.setFilename(TestUtil.DATA_DIR + "/star.gif");
    parent.style.setWidth("100");
    parent.style.setHeight("150");
    parent.childConsumableBounds = new Box(0, 0, 100, 150);
    parent.doLayout();

    AffineTransform tranform = panel.getTransform();
    assertEquals(0.5, tranform.getScaleX(), 0.001);
    assertEquals(0.75, tranform.getScaleY(), 0.001);
  }

  @Test
  public void getRotationTransformWhenDimensionsAreAuto() throws Exception
  {
    panel.setFilename(TestUtil.DATA_DIR + "/star.gif");
    panel.setRotation(45);
    panel.doLayout();

    AffineTransform tranform = panel.getTransform();

    // No good way to test rotation....
    assertEquals(141.0, tranform.getTranslateX(), 0.5);
    assertEquals(0.0, tranform.getTranslateY(), 0.5);
  }

  @Test
  public void getScalleTransformWhenDimensionsAreNotAutoAndScalingIsOff() throws Exception
  {
    panel.setFilename(TestUtil.DATA_DIR + "/star.gif");
    panel.setScaled(false);
    parent.style.setWidth("100");
    parent.style.setHeight("150");
    parent.childConsumableBounds = new Box(0, 0, 100, 150);
    parent.doLayout();

    AffineTransform tranform = panel.getTransform();
    assertEquals(1.0, tranform.getScaleX(), 0.001);
    assertEquals(1.0, tranform.getScaleY(), 0.001);
  }

  @Test
  public void hasConstrainedProportionsWhenWidthIsNotAuto() throws Exception
  {
    panel.setFilename(TestUtil.DATA_DIR + "/star.gif");
    parent.style.setWidth("100");
    parent.childConsumableBounds = new Box(0, 0, 100, 200);
    parent.doLayout();

    AffineTransform tranform = panel.getTransform();
    assertEquals(0.5, tranform.getScaleX(), 0.001);
    assertEquals(0.5, tranform.getScaleY(), 0.001);
  }

  @Test
  public void hasConstrainedProportionsWhenHeightIsNotAuto() throws Exception
  {
    panel.setFilename(TestUtil.DATA_DIR + "/star.gif");
    parent.style.setHeight("100");
    parent.childConsumableBounds = new Box(0, 0, 200, 100);
    parent.doLayout();

    AffineTransform tranform = panel.getTransform();
    assertEquals(0.5, tranform.getScaleX(), 0.001);
    assertEquals(0.5, tranform.getScaleY(), 0.001);
  }

  @Test
  public void sizeRemainsWhenStaticAndNotScaled() throws Exception
  {
    panel.setFilename(TestUtil.DATA_DIR + "/star.gif");
    panel.setScaled(false);
    parent.style.setHeight("400");
    parent.style.setHeight("400");
    parent.childConsumableBounds = new Box(0, 0, 400, 400);
    parent.doLayout();

    AffineTransform tranform = panel.getTransform();
    assertEquals(1.0, tranform.getScaleX(), 0.001);
    assertEquals(1.0, tranform.getScaleY(), 0.001);
    assertEquals(200, panel.getWidth());
    assertEquals(200, panel.getHeight());
  }
}
