package limelight.model.api;

import limelight.model.Stage;
import limelight.ui.model.MockStage;

import java.util.Map;

public class MockTheaterProxy implements TheaterProxy
{
  public Stage buildStage(String name, Map<String, Object> options)
  {
    return new MockStage(name);
  }
}
