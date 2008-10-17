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
    root = new RootPanel(new MockFrame());
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

  public void testGetScalleTransformWhenDimensionsAreNotAuto() throws Exception
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
    assertEquals(141.0, tranform.getTranslateX(), 0.001);
    assertEquals(0.0, tranform.getTranslateY(), 0.001);
  }

  public void testScaled() throws Exception
  {
    assertEquals(true, panel.isScaled());

    panel.setScaled(false);

    assertEquals(false, panel.isScaled());
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
}
