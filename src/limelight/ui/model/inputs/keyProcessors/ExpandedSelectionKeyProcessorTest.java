package limelight.ui.model.inputs.keyProcessors;


import limelight.ui.events.KeyEvent;
import org.junit.Before;
import org.junit.Test;

public class ExpandedSelectionKeyProcessorTest extends AbstractKeyProcessorTest
{
  @Before
  public void setUp()
  {
    selectionAreaSetUp();
    processor = ExpandedSelectionOnKeyProcessor.instance;
    modifiers = 0;
  }

//  @Test
//  public void canProcessCharactersAndReplaceSelection()
//  {
//    mockEvent = new MockKeyEvent(modifiers, KeyEvent.KEY_A, 'a');
//
//    processor.processKey(mockEvent, model);
//
//    assertTextState(2, 1, "Ha are four words");
//  }

  @Test
  public void canProcessBackSpaceAndDeleteSelection()
  {
    processor.processKey(press(KeyEvent.KEY_BACK_SPACE), model);

    assertTextState(1, 1, "H are four words");
  }

  @Test
  public void canProcessRightArrowAndEndSelection()
  {
    processor.processKey(press(KeyEvent.KEY_RIGHT), model);

    assertSelection(2, SELECTION_START_INDEX, false);
  }

  @Test
  public void canProcessLeftArrowAndEndSelection()
  {
    processor.processKey(press(KeyEvent.KEY_LEFT), model);

    assertSelection(0, SELECTION_START_INDEX, false);
  }

  @Test
  public void canProcessUpArrowAndEndSelection()
  {
    model.setText("This is\nMulti lined.");
    model.setCaretIndex(11);

    processor.processKey(press(KeyEvent.KEY_UP), model);

    assertSelection(3, SELECTION_START_INDEX, false);
  }

  @Test
  public void canProcessDownArrowAndEndSelection()
  {
    model.setText("This is\nMulti lined.");
    model.setCaretIndex(3);

    processor.processKey(press(KeyEvent.KEY_DOWN), model);

    assertSelection(11, SELECTION_START_INDEX, false);
  }

//  @Test
//  public void canProcessTheReturnKeyAndRemoveAllSelectedTextWhenStartingNewLine()
//  {
//    mockEvent = new MockKeyEvent(modifiers, KeyEvent.KEY_ENTER, '\r');
//
//    processor.processKey(mockEvent, model);
//
//    assertTextState(2, 1, "H\r are four words");
//  }
}
