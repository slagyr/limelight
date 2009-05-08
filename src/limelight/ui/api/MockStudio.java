package limelight.ui.api;

public class MockStudio implements Studio
{
  public String openedProduction;

  public void open(String production)
  {
    openedProduction = production;
  }
}
