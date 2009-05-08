package limelight.os;

public class MockOS extends OS
{
  protected void turnOnKioskMode()
  {
  }

  protected void turnOffKioskMode()
  {
  }

  public void configureSystemProperties()
  {
    System.setProperty("jruby.shell", "silly shell");
    System.setProperty("jruby.script", "sticky script");
  }
}
