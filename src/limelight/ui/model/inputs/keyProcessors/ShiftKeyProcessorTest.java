package limelight.ui.model.inputs.keyProcessors;


import limelight.ui.events.KeyEvent;
import org.junit.Before;
import org.junit.Test;

public class ShiftKeyProcessorTest extends AbstractKeyProcessorTest
{
  @Before
  public void setUp()
  {
    setUpSingleLine();
    processor = ShiftKeyProcessor.instance;
    modifiers = 1;
  }

//  @Test
//  public void canProcessCharacters()
//  {
//    mockEvent = new MockKeyEvent(modifiers, KeyEvent.KEY_A, 'A');
//
//    processor.processKey(mockEvent, model);
//
//    assertTextState(2, 0, "HAere are four words");
//  }

  @Test
  public void canProcessRightArrowAndBeingSelection()
  {
    processor.processKey(press(KeyEvent.KEY_RIGHT), model);

    assertSelection(2, 1, true);
  }

  @Test
  public void canProcessLeftArrowAndBeginSelection()
  {
    processor.processKey(press(KeyEvent.KEY_LEFT), model);

    assertSelection(0, 1, true);
  }

}
