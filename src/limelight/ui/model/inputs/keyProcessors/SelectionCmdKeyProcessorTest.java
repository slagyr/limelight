package limelight.ui.model.inputs.keyProcessors;


import org.junit.Before;
import org.junit.Test;

import java.awt.event.KeyEvent;

import static org.junit.Assert.assertEquals;

public class SelectionCmdKeyProcessorTest extends AbstractKeyProcessorTest
{
  @Before
  public void setUp()
  {
    selectionBoxSetUp();
    processor = SelectionOnCmdKeyProcessor.instance;
    modifier = 4;
  }

  @Test
  public void canSelectAll()
  {
    mockEvent = new MockKeyEvent(modifier, KeyEvent.VK_A);

    processor.processKey(mockEvent, modelInfo);

    asserter.assertSelection(modelInfo.getText().length(), 0, true);
  }

  @Test
  public void canPasteAtCursor()
  {
    mockEvent = new MockKeyEvent(modifier, KeyEvent.VK_V);
    modelInfo.copyText("oot");

    processor.processKey(mockEvent, modelInfo);

    asserter.assertTextState(SELECTION_START_INDEX, "Hoot are four words");
    asserter.assertSelection(SELECTION_START_INDEX, 0, false);
  }

  @Test
  public void canCopySelectedText()
  {
    mockEvent = new MockKeyEvent(modifier, KeyEvent.VK_C);

    processor.processKey(mockEvent, modelInfo);

    asserter.assertTextState(1, "Here are four words");
    asserter.assertSelection(1, SELECTION_START_INDEX, true);
    assertEquals("ere", modelInfo.getClipboardContents());
  }

  @Test
  public void canCutSelectedText()
  {
    mockEvent = new MockKeyEvent(modifier, KeyEvent.VK_X);

    processor.processKey(mockEvent, modelInfo);

    asserter.assertTextState(1, "H are four words");
    asserter.assertSelection(1, 0, false);
    assertEquals("ere", modelInfo.getClipboardContents());
  }

  @Test
  public void canProcessRightArrowAndJumpToRightEdgeAndEndSelection()
  {
    mockEvent = new MockKeyEvent(modifier, KeyEvent.VK_RIGHT);

    processor.processKey(mockEvent, modelInfo);

    asserter.assertSelection(modelInfo.getText().length(), SELECTION_START_INDEX, false);
  }

  @Test
  public void canProcessLeftArrowAndJumpToLeftEdgeAndEndSelection()
  {
    mockEvent = new MockKeyEvent(modifier, KeyEvent.VK_LEFT);

    processor.processKey(mockEvent, modelInfo);

    asserter.assertSelection(0, SELECTION_START_INDEX, false);
  }

   @Test
  public void canProcessUpArrowAndJumpToTheStart()
  {
    mockEvent = new MockKeyEvent(modifier, KeyEvent.VK_UP);

    processor.processKey(mockEvent, modelInfo);

    asserter.assertSelection(0, SELECTION_START_INDEX, false);
  }

  @Test
  public void canProcessDownArrowAndJumpToTheEnd()
  {
    mockEvent = new MockKeyEvent(modifier, KeyEvent.VK_DOWN);

    processor.processKey(mockEvent, modelInfo);

    asserter.assertSelection(modelInfo.getText().length(), SELECTION_START_INDEX, false);
  }

}
