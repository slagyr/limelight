package limelight.util;

import java.text.DecimalFormat;

public class Debug
{
  private static DecimalFormat secondsFormat = new DecimalFormat("0.0000");

  public static Debug debug1 = new Debug();
  public static Debug debug2 = new Debug();
  public static Debug alloc = new Debug("alloc");
  public static Debug paint = new Debug("paint");
  public static Debug copy = new Debug("copy");
  public static Debug event = new Debug("event");

  private NanoTimer interval;
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

  private String secString(long nanos)
  {
    return secondsFormat.format((double) nanos / 1000000000.0);
  }

  public void mark()
  {
    interval.markTime();
  }
}
