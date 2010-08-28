package limelight.ui.model.inputs.keyProcessors;


import limelight.ui.events.KeyEvent;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SelectionOnCmdKeyProcessorTest extends AbstractKeyProcessorTest
{
  @Before
  public void setUp()
  {
    selectionBoxSetUp();
    processor = SelectionOnCmdKeyProcessor.instance;
    modifiers = 4;
  }

  @Test
  public void canSelectAll()
  {
    processor.processKey(press(KeyEvent.KEY_A), model);

    assertSelection(model.getText().length(), 0, true);
  }

  @Test
  public void canPasteAtCursor()
  {
    model.copyText("oot");

    processor.processKey(press(KeyEvent.KEY_V), model);

    assertTextState(SELECTION_START_INDEX, 1, "Hoot are four words");
    assertSelection(SELECTION_START_INDEX, 1, false);
  }

  @Test
  public void canCopySelectedText()
  {
    processor.processKey(press(KeyEvent.KEY_C), model);

    assertTextState(1, 0, "Here are four words");
    assertSelection(1, SELECTION_START_INDEX, true);
    assertEquals("ere", model.getClipboardContents());
  }

  @Test
  public void canCutSelectedText()
  {
    processor.processKey(press(KeyEvent.KEY_X), model);

    assertTextState(1, 1, "H are four words");
    assertSelection(1, 1, false);
    assertEquals("ere", model.getClipboardContents());
  }

  @Test
  public void canProcessRightArrowAndJumpToRightEdgeAndEndSelection()
  {
    processor.processKey(press(KeyEvent.KEY_RIGHT), model);

    assertSelection(model.getText().length(), SELECTION_START_INDEX, false);
  }

  @Test
  public void canProcessLeftArrowAndJumpToLeftEdgeAndEndSelection()
  {
    processor.processKey(press(KeyEvent.KEY_LEFT), model);

    assertSelection(0, SELECTION_START_INDEX, false);
  }

   @Test
  public void canProcessUpArrowAndJumpToTheStart()
  {
    processor.processKey(press(KeyEvent.KEY_UP), model);

    assertSelection(0, SELECTION_START_INDEX, false);
  }

  @Test
  public void canProcessDownArrowAndJumpToTheEnd()
  {
    processor.processKey(press(KeyEvent.KEY_DOWN), model);

    assertSelection(model.getText().length(), SELECTION_START_INDEX, false);
  }

}
