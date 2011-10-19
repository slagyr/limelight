//- Copyright © 2008-2011 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the MIT License.

package limelight.ui.model;

import limelight.util.TestUtil;
import java.awt.*;

import org.junit.Before;
import org.junit.Test;
import static junit.framework.Assert.assertSame;
import static org.junit.Assert.assertNotNull;

public class ImageCacheTest
{
  private ImageCache cache;

  @Before
  public void setUp() throws Exception
  {
    cache = new ImageCache(TestUtil.DATA_DIR);
  }

  @Test
  public void loadingAnImage() throws Exception
  {
    Image image = cache.getImage("star.gif");
    assertNotNull(image);
  }

  @Test
  public void loadingAnImageTwiceGivesTheSameImage() throws Exception
  {
    Image image = cache.getImage("star.gif");
    Image image2 = cache.getImage("star.gif");

    assertSame(image, image2);
  }



}
