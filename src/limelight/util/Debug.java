//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.util;

import limelight.ui.Panel;
import limelight.ui.api.Prop;
import limelight.ui.model.PropPanel;

import javax.imageio.ImageIO;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.io.*;
import java.awt.image.RenderedImage;
import java.util.Date;

public class Debug
{
  private static final DecimalFormat secondsFormat = new DecimalFormat("0.0000");

  public static Debug debug1 = new Debug();
  public static Debug debug2 = new Debug();
  private static SimpleDateFormat timeFormat = new SimpleDateFormat("kk:mm:ss:SSS");
  private static DecimalFormat numberFormat = new DecimalFormat("#,##0.000"); 
  private static long startTime = System.currentTimeMillis();

  private final NanoTimer interval;
  private long life = 0;
  private String name = "";

  public Debug()
  {
    interval = new NanoTimer();
  }

  public Debug(String name)
  {
    this.name = name;
    interval = new NanoTimer();
  }

  public void log2(String message)
  {
    long idleNanos = interval.getIdleNanos();
    life += idleNanos;
    if("event".equals(name))
      System.err.println(name + " " + secString(life) + " " + secString(idleNanos) + ": " + message);
    interval.markTime();
  }

  public void log(Panel panel, String message)
  {
    if(panel instanceof PropPanel)
    {
      PropPanel propPanel = (PropPanel) panel;
      Prop prop = propPanel.getProp();
      if(prop != null && "sandbox".equals(prop.getName()))
        System.err.println(message);
    }
  }

  public static void log(String message)
  {
    System.err.println(runtime() + ": " + message);
//    try
//    {
//      OutputStream output = new FileOutputStream("/tmp/limelight.log", true);
//      String entry = runtime() + ": " + message + "\n";
//      output.write(entry.getBytes());
//      output.close();
//    }
//    catch(Exception e)
//    {
//      e.printStackTrace();
//    }
  }

  private static String runtime()
  {
    long diff = System.currentTimeMillis() - startTime;
    return numberFormat.format(diff / 1000.0);
  }

  private static String timestamp()
  {
    return timeFormat.format(new Date());
  }

  private String secString(long nanos)
  {
    return secondsFormat.format((double) nanos / 1000000000.0);
  }

  public void mark()
  {
    interval.markTime();
  }

  public static void saveImage(RenderedImage image, String prefix)
  {
    if(image == null)
      return;
    
    File file = new File("/tmp/" + prefix + System.nanoTime() + ".png");
    try
    {
      ImageIO.write(image, "PNG", file);
    }
    catch(IOException e)
    {
      e.printStackTrace();
    }
  }
}
