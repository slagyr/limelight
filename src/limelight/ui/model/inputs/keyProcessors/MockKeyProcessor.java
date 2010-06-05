package limelight.ui.model.inputs.keyProcessors;

import limelight.ui.model.inputs.KeyProcessor;
import limelight.ui.model.inputs.TextModel;

import java.awt.event.KeyEvent;

public class MockKeyProcessor extends KeyProcessor
{
  public KeyEvent processedEvent;

  public MockKeyProcessor()
  {
    super(null);
  }

  @Override
  public void processKey(KeyEvent event, TextModel boxInfo)
  {
    processedEvent = event;
  }
}
