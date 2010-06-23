package limelight.ui.model.inputs.keyProcessors;


import org.junit.Before;
import org.junit.Test;

import java.awt.event.KeyEvent;

public class ExpandedSelectionKeyProcessorTest extends AbstractKeyProcessorTest
{
  @Before
  public void setUp()
  {
    selectionAreaSetUp();
    processor = ExpandedSelectionOnKeyProcessor.instance;
    modifier = 0;
  }

  @Test
  public void canProcessCharactersAndReplaceSelection()
  {
    mockEvent = new MockKeyEvent(modifier, KeyEvent.VK_A, 'a');

    processor.processKey(mockEvent, modelInfo);

    asserter.assertTextState(2, "Ha are four words");
  }

  @Test
  public void canProcessBackSpaceAndDeleteSelection()
  {
    mockEvent = new MockKeyEvent(modifier, KeyEvent.VK_BACK_SPACE);

    processor.processKey(mockEvent, modelInfo);

    asserter.assertTextState(1, "H are four words");
  }

  @Test
  public void canProcessRightArrowAndEndSelection()
  {
    mockEvent = new MockKeyEvent(modifier, KeyEvent.VK_RIGHT);

    processor.processKey(mockEvent, modelInfo);

    asserter.assertSelection(2, SELECTION_START_INDEX, false);
  }

  @Test
  public void canProcessLeftArrowAndEndSelection()
  {
    mockEvent = new MockKeyEvent(modifier, KeyEvent.VK_LEFT);

    processor.processKey(mockEvent, modelInfo);

    asserter.assertSelection(0, SELECTION_START_INDEX, false);
  }

  @Test
  public void canProcessUpArrowAndEndSelection()
  {
    mockEvent = new MockKeyEvent(modifier, KeyEvent.VK_UP);
    modelInfo.setText("This is\nMulti lined.");
    modelInfo.setCaretIndex(11);

    processor.processKey(mockEvent, modelInfo);

    asserter.assertSelection(3, SELECTION_START_INDEX, false);
  }

  @Test
  public void canProcessDownArrowAndEndSelection()
  {
    mockEvent = new MockKeyEvent(modifier, KeyEvent.VK_DOWN);
    modelInfo.setText("This is\nMulti lined.");
    modelInfo.setCaretIndex(3);

    processor.processKey(mockEvent, modelInfo);

    asserter.assertSelection(10, SELECTION_START_INDEX, false);
  }

  @Test
  public void canProcessTheReturnKeyAndRemoveAllSelectedTextWhenStartingNewLine()
  {
    mockEvent = new MockKeyEvent(modifier, KeyEvent.VK_ENTER, '\r');

    processor.processKey(mockEvent, modelInfo);

    asserter.assertTextState(2, "H\r are four words");
  }
}
