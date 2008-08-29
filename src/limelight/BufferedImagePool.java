package limelight;

import limelight.caching.TimedCacheEntry;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.LinkedList;
import java.util.Iterator;

public class BufferedImagePool
{
  private double timeoutSeconds;
  private LinkedList<TimedCacheEntry<BufferedImage>> entries;

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
      return new BufferedImage(dimension.width, dimension.height, BufferedImage.TYPE_INT_ARGB);
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
