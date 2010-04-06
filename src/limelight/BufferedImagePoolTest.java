//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight;

import junit.framework.TestCase;

import java.awt.image.BufferedImage;
import java.awt.*;

public class BufferedImagePoolTest extends TestCase
{
  private BufferedImagePool pool;
  private Dimension dimension;

  public void setUp() throws Exception
  {
    pool = new BufferedImagePool(1);
    dimension = new Dimension(100, 200);
  }

  public void testEntryDuration() throws Exception
  {
    assertEquals(1, pool.getTimeoutSeconds(), 1);
  }
  
  public void testAcquireWhenNoneExists() throws Exception
  {
    BufferedImage image = pool.acquire(dimension);

    assertEquals(100, image.getWidth());
    assertEquals(200, image.getHeight());
    assertEquals(BufferedImage.TYPE_INT_ARGB, image.getType());
  }

  public void testAcquireWhenAnEntryIsPooled() throws Exception
  {
    BufferedImage image = pool.acquire(dimension);

    pool.recycle(image);
    BufferedImage acquired = pool.acquire(dimension);

    assertSame(acquired, image);
  }

  public void testClean() throws Exception
  {
    pool = new BufferedImagePool(-1);
    BufferedImage image = pool.acquire(dimension);
    pool.recycle(image);

    pool.clean();

    assertNotSame(image, pool.acquire(dimension));
  }

  public void testCanRecycleNullWithoutHarm() throws Exception
  {
    try
    {
      pool.recycle(null);
    }
    catch(Exception e)
    {
      fail("shouldn't raise exception");
    }
  }

}
