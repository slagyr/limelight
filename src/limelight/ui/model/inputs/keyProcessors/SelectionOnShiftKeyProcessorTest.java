package limelight.ui.model.inputs.keyProcessors;


import limelight.ui.events.KeyEvent;
import org.junit.Before;
import org.junit.Test;

public class SelectionOnShiftKeyProcessorTest extends AbstractKeyProcessorTest
{
  @Before
  public void setUp()
  {
    selectionBoxSetUp();
    processor = SelectionOnShiftKeyProcessor.instance;
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

}
