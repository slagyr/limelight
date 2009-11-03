//- Copyright © 2008-2009 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui.model;

import junit.framework.TestCase;
import limelight.MockResourceLoader;
import limelight.io.StreamReader;
import limelight.util.TestUtil;

import java.io.FileInputStream;

public class ImagePanelTest extends TestCase
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

  public void testSettingImageData() throws Exception
  {
    checkSettingImageDataWith("star.gif");
    checkSettingImageDataWith("star.jpg");
    checkSettingImageDataWith("star.jpg");
//    checkSettingImageDataWith("star.tif", "tif");
//    checkSettingImageDataWith("star.tif", "tiff");
    checkSettingImageDataWith("star.png");
    checkSettingImageDataWith("star.bmp");
    checkSettingImageDataWith("star.wbm");
    checkSettingImageDataWith("star.wbm");
  }

  private void checkSettingImageDataWith(String imageFile) throws Exception
  {
    StreamReader reader = new StreamReader(new FileInputStream(TestUtil.dataDirPath(imageFile)));
    byte[] bytes = reader.readBytes(100000);

    panel.setImageData(bytes);

    assertEquals(200, panel.getImage().getHeight(null));
    assertEquals(200, panel.getImage().getWidth(null));
  }

  public void testSettingImageDataUpdatesInfo() throws Exception
  {
    panel.setImageFile(TestUtil.dataDirPath("small_star.gif"));
    panel.getImage();
    panel.resetLayout();
    parent.resetLayout();

    checkSettingImageDataWith("star.gif");

    assertEquals(200, panel.getImageWidth(), 0.1);
    assertEquals(200, panel.getImageHeight(), 0.1);
    assertEquals("<data>", panel.getImageFile());
    assertEquals(true, panel.needsLayout());
    assertEquals(true, parent.needsLayout());
  }

  public void testDoesntCrashWhenNoImageFileProvided() throws Exception
  {
    panel.setImageFile(null);
    assertEquals(null, panel.getImage());

    panel.setImageFile("");
    assertEquals(null, panel.getImage());
  }
}
