package limelight.os;

public abstract class OS
{
  private boolean inKioskMode;

  protected native void turnOnKioskMode();
  protected native void turnOffKioskMode();

  public void enterKioskMode()
  {
    turnOnKioskMode();
    inKioskMode = true;
  }

  public void exitKioskMode()
  {
    inKioskMode = false;
    turnOffKioskMode();
  }

  public boolean isInKioskMode()
  {
    return inKioskMode;
  }
}
