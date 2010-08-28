package limelight.ui.model.inputs.keyProcessors;


import limelight.ui.events.KeyEvent;
import org.junit.Before;
import org.junit.Test;

public class ExpandedSelectionShiftKeyProcessorTest extends AbstractKeyProcessorTest
{
  @Before
  public void setUp()
  {
    selectionAreaSetUp();
    processor = ExpandedSelectionOnShiftKeyProcessor.instance;
    modifiers = 1;
  }

  @Test
  public void canProcessRightArrowAndContinueSelectionToTheRight()
  {
    processor.processKey(press(KeyEvent.KEY_RIGHT), model);

    assertSelection(2, SELECTION_START_INDEX, true);
  }

  @Test
  public void canProcessLeftArrowAndContinueSelectionToTheLeft()
  {
    processor.processKey(press(KeyEvent.KEY_LEFT), model);

    assertSelection(0, SELECTION_START_INDEX, true);
  }

//  @Test
//  public void canProcessCharacterAndPlaceUppercaseCharAtCursorIndex()
//  {
//    mockEvent = new MockKeyEvent(modifiers, KeyEvent.KEY_A, 'A');
//
//    processor.processKey(mockEvent, model);
//
//    assertTextState(2, 0, "HA are four words");
//  }

  @Test
  public void canProcessUpArrowAndMoveUpALineWithSelection()
  {
    model.setText("This is\nMulti lined.");
    model.setCaretIndex(11);

    processor.processKey(press(KeyEvent.KEY_UP), model);

    assertSelection(3, SELECTION_START_INDEX, true);
  }

  @Test
  public void canProcessDownArrowAndMoveDownALineWithSelection()
  {
    model.setText("This is\nMulti lined.");
    model.setCaretIndex(3);

    processor.processKey(press(KeyEvent.KEY_DOWN), model);

    assertSelection(11, SELECTION_START_INDEX, true);
  }
}
