//- Copyright © 2008-2011 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the MIT License.

package limelight.ui.model;

import junit.framework.TestCase;
import limelight.Context;
import limelight.io.FakeFileSystem;
import limelight.util.FakeResourceLoader;
import limelight.util.TestUtil;

import java.awt.*;

public class ImageCacheTest extends TestCase
{
  private ImageCache cache;

  public void setUp() throws Exception
  {
    cache = new ImageCache(TestUtil.DATA_DIR);
  }

  public void testLoadingAnImage() throws Exception
  {
    Image image = cache.getImage("star.gif");
    assertNotNull(image);
  }

  public void testLoadingAnImageTwiceGivesTheSameImage() throws Exception
  {
    Image image = cache.getImage("star.gif");
    Image image2 = cache.getImage("star.gif");

    assertSame(image, image2);
  }



}
