package limelight;

import java.util.logging.*;

public class Log
{
  public static Level defaultLevel = Level.WARNING;
  public static Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).getParent();

  static {
    for(Handler h : logger.getHandlers())
      logger.removeHandler(h);

    logger.setLevel(defaultLevel);
    ConsoleHandler handler = new ConsoleHandler();
    handler.setLevel(defaultLevel);
    handler.setFormatter(new LimelightFormatter());
    logger.addHandler(handler);
  }

  private static class LimelightFormatter extends Formatter
  {
    @Override
    public String format(LogRecord logRecord)
    {
      return String.format("%1$7s %2$tH:%2$tm:%2$tS:%2$tL %3$s: %4$s\n",
        logRecord.getLevel(),
        logRecord.getMillis(),
        logRecord.getLoggerName(),
        logRecord.getMessage());
    }
  }

  public static void setLevel(Level defaultLevel)
  {
    logger.setLevel(defaultLevel);
  }

  public static void off()
  {
    setLevel(Level.OFF);
  }

  public static void infoOn()
  {
    setLevel(Level.INFO);
  }

  public static void warnOn()
  {
    setLevel(Level.WARNING);
  }

  public static void severe(String message)
  {
    logger.severe(message);
  }

  public static void warn(String message)
  {
    logger.warning(message);
  }

  public static void info(String message)
  {
    logger.info(message);
  }

  public static void config(String message)
  {
    logger.config(message);
  }

  public static void fine(String message)
  {
    logger.fine(message);
  }

  public static void finer(String message)
  {
    logger.finer(message);
  }

  public static void finest(String message)
  {
    logger.finest(message);
  }
}
