package limelight.ui.model.inputs.keyProcessors;


import limelight.ui.events.KeyEvent;
import org.junit.Before;
import org.junit.Test;

public class SelectionOnShiftCmdKeyProcessorTest extends AbstractKeyProcessorTest
{
  @Before
  public void setUp()
  {
    selectionBoxSetUp();
    processor = SelectionOnShiftCmdKeyProcessor.instance;
    modifiers = 5;
  }

  @Test
  public void canProcessRightArrowAndContinueSelectionToTheRightEdge()
  {
    processor.processKey(press(KeyEvent.KEY_RIGHT), model);

    assertSelection(model.getText().length(), SELECTION_START_INDEX, true);
  }

  @Test
  public void canProcessLeftArrowAndContinueSelectionToTheLeftEdge()
  {
    model.setCaretIndex(2);

    processor.processKey(press(KeyEvent.KEY_LEFT), model);

    assertSelection(0, SELECTION_START_INDEX, true);
  }

  @Test
  public void canProcessUpArrowAndJumpToTheStart()
  {
    processor.processKey(press(KeyEvent.KEY_UP), model);

    assertSelection(0, SELECTION_START_INDEX, true);
  }

  @Test
  public void canProcessDownArrowAndJumpToTheEnd()
  {
    processor.processKey(press(KeyEvent.KEY_DOWN), model);

    assertSelection(model.getText().length(), SELECTION_START_INDEX, true);
  }

}
