package limelight;

public class Logger
{
  private static boolean on = true;

  public static void on()
  {
    on = true;
  }

  public static void off()
  {
    on = false;
  }

  public static void info(String log)
  {
    if(on)
      System.err.println(log);
  }
}
