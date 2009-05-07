package limelight.os;

public abstract class OS
{
  private boolean inKioskMode;

  protected abstract void turnOnKioskMode();
  protected abstract void turnOffKioskMode();

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
