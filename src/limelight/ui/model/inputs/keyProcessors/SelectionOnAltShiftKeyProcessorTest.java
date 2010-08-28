package limelight.ui.model.inputs.keyProcessors;


import limelight.ui.events.KeyEvent;
import org.junit.Before;
import org.junit.Test;

public class SelectionOnAltShiftKeyProcessorTest extends AbstractKeyProcessorTest
{
  @Before
  public void setUp()
  {
    selectionBoxSetUp();
    processor = SelectionOnAltShiftKeyProcessor.instance;
    modifiers = 9;
  }

//  @Test
//  public void canProcessSpecialKeys()
//  {
//    mockEvent = new MockKeyEvent(modifiers, KeyEvent.KEY_A, 'A');
//
//    processor.processKey(mockEvent, model);
//
//    assertTextState(2, 0, "HA are four words");
//  }

  @Test
  public void canProcessRightArrowAndSelectToStartOfNextWord()
  {
    processor.processKey(press(KeyEvent.KEY_RIGHT), model);

    assertSelection(5, SELECTION_START_INDEX, true);
  }

  @Test
  public void canProcessLeftArrowAndSelectToEndOfPreviousWord()
  {
    model.setCaretIndex(9);

    processor.processKey(press(KeyEvent.KEY_LEFT), model);

    assertSelection(5, 4, true);
  }

}
