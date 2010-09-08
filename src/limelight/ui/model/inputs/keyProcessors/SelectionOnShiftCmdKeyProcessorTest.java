package limelight.ui.model.inputs.keyProcessors;


import limelight.ui.events.KeyEvent;
import limelight.ui.text.TextLocation;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SelectionOnShiftCmdKeyProcessorTest extends AbstractKeyProcessorTest
{
  @Before
  public void setUp()
  {
    setUpSingleLine();
    model.startSelection(TextLocation.at(0, 4));
    processor = SelectionOnShiftCmdKeyProcessor.instance;
    modifiers = 5;
  }

  @Test
  public void canProcessRightArrowAndContinueSelectionToTheRightEdge()
  {
    processor.processKey(press(KeyEvent.KEY_RIGHT), model);

    assertEquals(model.getText().length(), model.getCaretIndex());
    assertEquals(4, model.getSelectionIndex());
    assertEquals(true, model.isSelectionActivated());
  }

  @Test
  public void canProcessLeftArrowAndContinueSelectionToTheLeftEdge()
  {
    model.setCaretIndex(2);

    processor.processKey(press(KeyEvent.KEY_LEFT), model);

    assertEquals(0, model.getCaretIndex());
    assertEquals(4, model.getSelectionIndex());
    assertEquals(true, model.isSelectionActivated());
  }

  @Test
  public void canProcessUpArrowAndJumpToTheStart()
  {
    processor.processKey(press(KeyEvent.KEY_UP), model);

    assertEquals(0, model.getCaretIndex());
    assertEquals(4, model.getSelectionIndex());
    assertEquals(true, model.isSelectionActivated());
  }

  @Test
  public void canProcessDownArrowAndJumpToTheEnd()
  {
    processor.processKey(press(KeyEvent.KEY_DOWN), model);

    assertEquals(model.getText().length(), model.getCaretIndex());
    assertEquals(4, model.getSelectionIndex());
    assertEquals(true, model.isSelectionActivated());
  }

}
