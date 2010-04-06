//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui.model;

import junit.framework.TestCase;
import limelight.MockResourceLoader;
import limelight.io.FileUtil;
import limelight.util.TestUtil;

import java.awt.*;

public class ImageCacheTest extends TestCase
{
  private ImageCache cache;

  public void setUp() throws Exception
  {
    cache = new ImageCache(new MockResourceLoader());
  }

  public void testLoadingAnImage() throws Exception
  {
    Image image = cache.getImage(FileUtil.buildPath(TestUtil.DATA_DIR, "star.gif"));
    assertNotNull(image);
  }

  public void testLoadingAnImageTwiceGivesTheSameImage() throws Exception
  {
    String imagePath = FileUtil.buildPath(TestUtil.DATA_DIR, "star.gif");
    Image image = cache.getImage(imagePath);
    Image image2 = cache.getImage(imagePath);

    assertSame(image, image2);
  }



}
