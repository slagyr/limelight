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

    processor.processKey(mockEvent, modelInfo);

    asserter.assertSelection(2, SELECTION_START_INDEX, true);
  }

  @Test
  public void canProcessLeftArrowAndContinueSelectionToTheLeft()
  {
    mockEvent = new MockKeyEvent(modifier, KeyEvent.VK_LEFT);

    processor.processKey(mockEvent, modelInfo);

    asserter.assertSelection(0, SELECTION_START_INDEX, true);
  }

  @Test
  public void canProcessCharacterAndPlaceUppercaseCharAtCursorIndex()
  {
    mockEvent = new MockKeyEvent(modifier, KeyEvent.VK_A, 'A');

    processor.processKey(mockEvent, modelInfo);

    asserter.assertTextState(2, "HA are four words");
  }

  @Test
  public void canProcessUpArrowAndMoveUpALineWithSelection()
  {
    mockEvent = new MockKeyEvent(modifier, KeyEvent.VK_UP);
    modelInfo.setText("This is\nMulti lined.");
    modelInfo.setCaretIndex(11);

    processor.processKey(mockEvent, modelInfo);

    asserter.assertSelection(3, SELECTION_START_INDEX, true);
  }

  @Test
  public void canProcessDownArrowAndMoveDownALineWithSelection()
  {
    mockEvent = new MockKeyEvent(modifier, KeyEvent.VK_DOWN);
    modelInfo.setText("This is\nMulti lined.");
    modelInfo.setCaretIndex(3);

    processor.processKey(mockEvent, modelInfo);

    asserter.assertSelection(10, SELECTION_START_INDEX, true);
  }
}
