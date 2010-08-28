package limelight.ui.model.inputs.keyProcessors;


import limelight.ui.events.KeyEvent;
import org.junit.Before;
import org.junit.Test;

public class SelectionOnAltKeyProcessorTest extends AbstractKeyProcessorTest
{
  @Before
  public void setUp()
  {
    selectionBoxSetUp();
    processor = SelectionOnAltKeyProcessor.instance;
    modifiers = 8;
  }

//  @Test
//  public void canProcessSpecialKeys()
//  {
//    mockEvent = new MockKeyEvent(modifiers, KeyEvent.KEY_A, 'a');
//
//    processor.processKey(mockEvent, model);
//
//    assertTextState(2, 0, "Ha are four words");
//  }

  @Test
  public void canProcessRightArrowAndMoveToStartOfNextWord()
  {
    processor.processKey(press(KeyEvent.KEY_RIGHT), model);

    assertSelection(5, SELECTION_START_INDEX, false);
  }

  @Test
  public void canProcessLeftArrowAndMoveToEndOfPreviousWord()
  {
    model.setCaretIndex(9);

    processor.processKey(press(KeyEvent.KEY_LEFT), model);

    assertSelection(5, 4, false);
  }

}
