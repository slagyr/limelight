package limelight.ui.model.inputs.keyProcessors;


import org.junit.Before;
import org.junit.Test;

import java.awt.event.KeyEvent;

public class ExpandedShiftKeyProcessorTest extends AbstractKeyProcessorTest
{
  @Before
  public void setUp()
  {
    textAreaSetUp();
    processor = ExpandedShiftKeyProcessor.instance;
    modifier = 1;
  }

  @Test
  public void canProcessCharacters()
  {
    mockEvent = new MockKeyEvent(modifier, KeyEvent.VK_A, 'A');

    processor.processKey(mockEvent, modelInfo);

    asserter.assertTextState(2, "HAere are four words");
  }

  @Test
  public void canProcessRightArrowAndBeingSelection()
  {
    mockEvent = new MockKeyEvent(modifier, KeyEvent.VK_RIGHT);

    processor.processKey(mockEvent, modelInfo);

    asserter.assertSelection(2, 1, true);
  }

  @Test
  public void canProcessLeftArrowAndBeginSelection()
  {
    mockEvent = new MockKeyEvent(modifier, KeyEvent.VK_LEFT);

    processor.processKey(mockEvent, modelInfo);

    asserter.assertSelection(0, 1, true);
  }

  @Test
  public void canProcessUpArrowAndMoveUpALineWithSelection()
  {
    mockEvent = new MockKeyEvent(modifier, KeyEvent.VK_UP);
    modelInfo.setText("This is\nMulti lined.");
    modelInfo.setCaretIndex(11);

    processor.processKey(mockEvent, modelInfo);

    asserter.assertSelection(3, 11, true);
  }

  @Test
  public void canProcessDownArrowAndMoveDownALineWithSelection()
  {
    mockEvent = new MockKeyEvent(modifier, KeyEvent.VK_DOWN);
    modelInfo.setText("This is\nMulti lined.");
    modelInfo.setCaretIndex(3);

    processor.processKey(mockEvent, modelInfo);

    asserter.assertSelection(10, 3, true);
  }
}
