package limelight.ui.api;

public class MockStudio implements Studio
{
  public String openedProduction;
  public boolean allowShutdown;
  public boolean isShutdown;

  public void open(String production)
  {
    openedProduction = production;
  }

  public boolean should_allow_shutdown()
  {
    return allowShutdown;
  }

  public void shutdown()
  {
    isShutdown = true;
  }
}
