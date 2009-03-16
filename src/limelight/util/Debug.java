//- Copyright © 2008-2009 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.util;

import limelight.ui.Panel;
import limelight.ui.api.Prop;
import limelight.ui.model.PropPanel;

import java.text.DecimalFormat;

public class Debug
{
  private static final DecimalFormat secondsFormat = new DecimalFormat("0.0000");

  public static Debug debug1 = new Debug();
  public static Debug debug2 = new Debug();

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

  public void log(String message)
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

  private String secString(long nanos)
  {
    return secondsFormat.format((double) nanos / 1000000000.0);
  }

  public void mark()
  {
    interval.markTime();
  }
}
