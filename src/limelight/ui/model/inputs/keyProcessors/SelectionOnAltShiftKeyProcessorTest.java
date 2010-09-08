package limelight.ui.model.inputs.keyProcessors;


import limelight.ui.events.KeyEvent;
import limelight.ui.text.TextLocation;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SelectionOnAltShiftKeyProcessorTest extends AbstractKeyProcessorTest
{
  @Before
  public void setUp()
  {
    setUpSingleLine();
    model.startSelection(TextLocation.at(0, 4));
    processor = SelectionOnAltShiftKeyProcessor.instance;
    modifiers = 9;
  }

  @Test
  public void canProcessRightArrowAndSelectToStartOfNextWord()
  {
    processor.processKey(press(KeyEvent.KEY_RIGHT), model);

    assertEquals(5, model.getCaretIndex());
    assertEquals(4, model.getSelectionIndex());
    assertEquals(true, model.isSelectionActivated());
  }

  @Test
  public void canProcessLeftArrowAndSelectToEndOfPreviousWord()
  {
    model.setCaretIndex(9);

    processor.processKey(press(KeyEvent.KEY_LEFT), model);

    assertEquals(5, model.getCaretIndex());
    assertEquals(4, model.getSelectionIndex());
    assertEquals(true, model.isSelectionActivated());
  }

}
