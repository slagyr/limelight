//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight;

import limelight.caching.TimedCacheEntry;
import limelight.util.NanoTimer;
import limelight.util.Debug;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.LinkedList;
import java.util.Iterator;

public class BufferedImagePool
{
  private final double timeoutSeconds;
  private final LinkedList<TimedCacheEntry<BufferedImage>> entries;

  public BufferedImagePool(double timeoutSeconds)
  {
    this.timeoutSeconds = timeoutSeconds;
    entries = new LinkedList<TimedCacheEntry<BufferedImage>>();
  }

  public double getTimeoutSeconds()
  {
    return timeoutSeconds;
  }

  public synchronized BufferedImage acquire(Dimension dimension)
  {    
    TimedCacheEntry<BufferedImage> match = null;
    for(TimedCacheEntry<BufferedImage> entry : entries)
    {
      BufferedImage image = entry.value();
      if(image != null && image.getWidth() == dimension.width && image.getHeight() == dimension.height)
      {
        match = entry;
        break;
      }
    }
    if(match != null)
    {
      entries.remove(match);
      return match.value();
    }
    else
    {
      return allocateNewBufferedImage(dimension, 3);
    }
  }

  private BufferedImage allocateNewBufferedImage(Dimension dimension, int attemtps)
  {
    try
    {
      BufferedImage image = new BufferedImage(dimension.width, dimension.height, BufferedImage.TYPE_INT_ARGB);
      return image;
    }
    catch(OutOfMemoryError e)
    {
      if(attemtps > 1)
      {
        entries.clear();
        System.gc(); 
        return allocateNewBufferedImage(dimension, attemtps - 1);
      }
      throw e;
    }
  }

  public synchronized void recycle(BufferedImage image)
  {
    if(image != null)
      entries.add(new TimedCacheEntry<BufferedImage>(image, timeoutSeconds));
  }

  public synchronized void clean()
  {
    for(Iterator<TimedCacheEntry<BufferedImage>> iterator = entries.iterator(); iterator.hasNext();)
    {
      TimedCacheEntry<BufferedImage> entry = iterator.next();
      if(entry.isExpired())
        iterator.remove();
    }
  }
}
