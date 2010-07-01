package limelight.ui.model.inputs.keyProcessors;


import org.junit.Before;
import org.junit.Test;

import java.awt.event.KeyEvent;

public class SelectionOnShiftCmdKeyProcessorTest extends AbstractKeyProcessorTest
{
  @Before
  public void setUp()
  {
    selectionBoxSetUp();
    processor = SelectionOnShiftCmdKeyProcessor.instance;
    modifier = 5;
  }

  @Test
  public void canProcessRightArrowAndContinueSelectionToTheRightEdge()
  {
    mockEvent = new MockKeyEvent(modifier, KeyEvent.VK_RIGHT);

    processor.processKey(mockEvent, model);

    assertSelection(model.getText().length(), SELECTION_START_INDEX, true);
  }

  @Test
  public void canProcessLeftArrowAndContinueSelectionToTheLeftEdge()
  {
    mockEvent = new MockKeyEvent(modifier, KeyEvent.VK_LEFT);
    model.setCaretIndex(2);

    processor.processKey(mockEvent, model);

    assertSelection(0, SELECTION_START_INDEX, true);
  }

  @Test
  public void canProcessUpArrowAndJumpToTheStart()
  {
    mockEvent = new MockKeyEvent(modifier, KeyEvent.VK_UP);

    processor.processKey(mockEvent, model);

    assertSelection(0, SELECTION_START_INDEX, true);
  }

  @Test
  public void canProcessDownArrowAndJumpToTheEnd()
  {
    mockEvent = new MockKeyEvent(modifier, KeyEvent.VK_DOWN);

    processor.processKey(mockEvent, model);

    assertSelection(model.getText().length(), SELECTION_START_INDEX, true);
  }

}
