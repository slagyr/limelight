package limelight.ui.model.inputs.keyProcessors;


import limelight.ui.events.KeyEvent;
import limelight.ui.text.TextLocation;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ShiftKeyProcessorTest extends AbstractKeyProcessorTest
{
  @Before
  public void setUp()
  {
    setUpSingleLine();
    processor = ShiftKeyProcessor.instance;
    modifiers = 1;
  }
  
  @Test
  public void canProcessRightArrowAndBeingSelection()
  {
    processor.processKey(press(KeyEvent.KEY_RIGHT), model);

    assertEquals(TextLocation.at(0, 2), model.getCaretLocation());
    assertEquals(TextLocation.at(0, 1), model.getSelectionLocation());
    assertEquals(true, model.isSelectionActivated());
  }

  @Test
  public void canProcessLeftArrowAndBeginSelection()
  {
    processor.processKey(press(KeyEvent.KEY_LEFT), model);

    assertEquals(TextLocation.at(0, 0), model.getCaretLocation());
    assertEquals(TextLocation.at(0, 1), model.getSelectionLocation());
    assertEquals(true, model.isSelectionActivated());
  }

}
