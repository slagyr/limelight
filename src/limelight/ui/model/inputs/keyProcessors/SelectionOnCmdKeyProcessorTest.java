package limelight.ui.model.inputs.keyProcessors;


import org.junit.Before;
import org.junit.Test;

import java.awt.event.KeyEvent;

import static org.junit.Assert.assertEquals;

public class SelectionOnCmdKeyProcessorTest extends AbstractKeyProcessorTest
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

    processor.processKey(mockEvent, model);

    assertSelection(model.getText().length(), 0, true);
  }

  @Test
  public void canPasteAtCursor()
  {
    mockEvent = new MockKeyEvent(modifier, KeyEvent.VK_V);
    model.copyText("oot");

    processor.processKey(mockEvent, model);

    assertTextState(SELECTION_START_INDEX, 1, "Hoot are four words");
    assertSelection(SELECTION_START_INDEX, 1, false);
  }

  @Test
  public void canCopySelectedText()
  {
    mockEvent = new MockKeyEvent(modifier, KeyEvent.VK_C);

    processor.processKey(mockEvent, model);

    assertTextState(1, 0, "Here are four words");
    assertSelection(1, SELECTION_START_INDEX, true);
    assertEquals("ere", model.getClipboardContents());
  }

  @Test
  public void canCutSelectedText()
  {
    mockEvent = new MockKeyEvent(modifier, KeyEvent.VK_X);

    processor.processKey(mockEvent, model);

    assertTextState(1, 1, "H are four words");
    assertSelection(1, 1, false);
    assertEquals("ere", model.getClipboardContents());
  }

  @Test
  public void canProcessRightArrowAndJumpToRightEdgeAndEndSelection()
  {
    mockEvent = new MockKeyEvent(modifier, KeyEvent.VK_RIGHT);

    processor.processKey(mockEvent, model);

    assertSelection(model.getText().length(), SELECTION_START_INDEX, false);
  }

  @Test
  public void canProcessLeftArrowAndJumpToLeftEdgeAndEndSelection()
  {
    mockEvent = new MockKeyEvent(modifier, KeyEvent.VK_LEFT);

    processor.processKey(mockEvent, model);

    assertSelection(0, SELECTION_START_INDEX, false);
  }

   @Test
  public void canProcessUpArrowAndJumpToTheStart()
  {
    mockEvent = new MockKeyEvent(modifier, KeyEvent.VK_UP);

    processor.processKey(mockEvent, model);

    assertSelection(0, SELECTION_START_INDEX, false);
  }

  @Test
  public void canProcessDownArrowAndJumpToTheEnd()
  {
    mockEvent = new MockKeyEvent(modifier, KeyEvent.VK_DOWN);

    processor.processKey(mockEvent, model);

    assertSelection(model.getText().length(), SELECTION_START_INDEX, false);
  }

}
