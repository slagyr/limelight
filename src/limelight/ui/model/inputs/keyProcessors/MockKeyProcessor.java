package limelight.ui.model.inputs.keyProcessors;

import limelight.ui.events.KeyEvent;
import limelight.ui.model.inputs.KeyProcessor;
import limelight.ui.model.inputs.TextModel;

public class MockKeyProcessor extends KeyProcessor
{
  public KeyEvent processedEvent;

  public MockKeyProcessor()
  {
    super(null);
  }

  @Override
  public void processKey(KeyEvent event, TextModel model)
  {
    processedEvent = event;
  }
}
