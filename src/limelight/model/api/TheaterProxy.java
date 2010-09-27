package limelight.model.api;

import limelight.model.Stage;

import java.util.Map;

public interface TheaterProxy
{
  Stage buildStage(String name, Map<String, Object> options);
}
