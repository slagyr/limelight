package limelight.ui.model.inputs.keyProcessors;


import org.junit.Before;
import org.junit.Test;

import java.awt.event.KeyEvent;

public class ExpandedSelectionShiftKeyProcessorTest extends AbstractKeyProcessorTest
{
  @Before
  public void setUp()
  {
    selectionAreaSetUp();
    processor = ExpandedSelectionOnShiftKeyProcessor.instance;
    modifier = 1;
  }

  @Test
  public void canProcessRightArrowAndContinueSelectionToTheRight()
  {
    mockEvent = new MockKeyEvent(modifier, KeyEvent.VK_RIGHT);

    processor.processKey(mockEvent, model);

    assertSelection(2, SELECTION_START_INDEX, true);
  }

  @Test
  public void canProcessLeftArrowAndContinueSelectionToTheLeft()
  {
    mockEvent = new MockKeyEvent(modifier, KeyEvent.VK_LEFT);

    processor.processKey(mockEvent, model);

    assertSelection(0, SELECTION_START_INDEX, true);
  }

  @Test
  public void canProcessCharacterAndPlaceUppercaseCharAtCursorIndex()
  {
    mockEvent = new MockKeyEvent(modifier, KeyEvent.VK_A, 'A');

    processor.processKey(mockEvent, model);

    assertTextState(2, 0, "HA are four words");
  }

  @Test
  public void canProcessUpArrowAndMoveUpALineWithSelection()
  {
    mockEvent = new MockKeyEvent(modifier, KeyEvent.VK_UP);
    model.setText("This is\nMulti lined.");
    model.setCaretIndex(11);

    processor.processKey(mockEvent, model);

    assertSelection(3, SELECTION_START_INDEX, true);
  }

  @Test
  public void canProcessDownArrowAndMoveDownALineWithSelection()
  {
    mockEvent = new MockKeyEvent(modifier, KeyEvent.VK_DOWN);
    model.setText("This is\nMulti lined.");
    model.setCaretIndex(3);

    processor.processKey(mockEvent, model);

    assertSelection(11, SELECTION_START_INDEX, true);
  }
}
