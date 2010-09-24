package limelight.ui;

import limelight.ui.api.PropProxy;
import limelight.util.ResourceLoader;

import java.util.Map;

public class SimplePropProxy implements PropProxy
{
  public ResourceLoader getLoader()
  {
    return null;
  }

  public void applyOptions(Map<String, Object> options)
  {
  }
}
