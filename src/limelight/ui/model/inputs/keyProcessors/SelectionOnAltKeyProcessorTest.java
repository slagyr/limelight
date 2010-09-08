package limelight.ui.model.inputs.keyProcessors;


import limelight.ui.events.KeyEvent;
import limelight.ui.text.TextLocation;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SelectionOnAltKeyProcessorTest extends AbstractKeyProcessorTest
{
  @Before
  public void setUp()
  {
    setUpSingleLine();
    model.startSelection(TextLocation.at(0, 4));
    processor = SelectionOnAltKeyProcessor.instance;
    modifiers = 8;
  }

  @Test
  public void canProcessRightArrowAndMoveToStartOfNextWord()
  {
    processor.processKey(press(KeyEvent.KEY_RIGHT), model);

    assertEquals(5, model.getCaretIndex());
    assertEquals(4, model.getSelectionIndex());
    assertEquals(false, model.isSelectionActivated());
  }

  @Test
  public void canProcessLeftArrowAndMoveToEndOfPreviousWord()
  {
    model.setCaretIndex(9);

    processor.processKey(press(KeyEvent.KEY_LEFT), model);

    assertEquals(5, model.getCaretIndex());
    assertEquals(4, model.getSelectionIndex());
    assertEquals(false, model.isSelectionActivated());
  }

}
