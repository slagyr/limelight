package limelight.ui;

public class MockTheater implements Theater
{
  public Stage activatedStage;

  public void stage_activated(Stage stage)
  {
    activatedStage = stage;
  }
}
