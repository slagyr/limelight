package limelight.ui.model.inputs.keyProcessors;


import limelight.ui.events.KeyEvent;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ShiftCmdKeyProcessorTest extends AbstractKeyProcessorTest
{
  @Before
  public void setUp()
  {
    setUpSingleLine();
    processor = ShiftCmdKeyProcessor.instance;
    modifiers = 5;
  }

  @Test
  public void canProcessRightArrowAndSelectToTheRightEdge()
  {
    processor.processKey(press(KeyEvent.KEY_RIGHT), model);

    assertEquals(model.getText().length(), model.getCaretIndex());
    assertEquals(1, model.getSelectionIndex());
    assertEquals(true, model.isSelectionActivated());
  }

  @Test
  public void canProcessLeftArrowAndSelectToTheLeftEdge()
  {
    processor.processKey(press(KeyEvent.KEY_LEFT), model);

    assertEquals(0, model.getCaretIndex());
    assertEquals(1, model.getSelectionIndex());
    assertEquals(true, model.isSelectionActivated());
  }

  @Test
  public void canProcessUpArrow()
  {
    processor.processKey(press(KeyEvent.KEY_UP), model);

    assertEquals(0, model.getCaretIndex());
    assertEquals(1, model.getSelectionIndex());
    assertEquals(true, model.isSelectionActivated());
  }

  @Test
  public void canProcessDownArrow()
  {
    processor.processKey(press(KeyEvent.KEY_DOWN), model);

    assertEquals(model.getText().length(), model.getCaretIndex());
    assertEquals(1, model.getSelectionIndex());
    assertEquals(true, model.isSelectionActivated());
  }

}
