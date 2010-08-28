package limelight.ui.model.inputs.keyProcessors;


import limelight.ui.events.KeyEvent;
import org.junit.Before;
import org.junit.Test;

public class AltShiftKeyProcessorTest extends AbstractKeyProcessorTest
{
  @Before
  public void setUp()
  {
    setUpSingleLine();
    processor = AltShiftKeyProcessor.instance;
    modifiers = 9;
  }

//  @Test
//  public void canProcessSpecialKeys()
//  {
//    mockEvent = new MockKeyEvent(modifiers, KeyEvent.KEY_A, 'A');
//
//    processor.processKey(mockEvent, model);
//
//    assertTextState(2, 0, "HAere are four words");
//  }

  @Test
  public void canProcessRightArrowAndSelectToTheNextWord()
  {
    processor.processKey(press(KeyEvent.KEY_RIGHT), model);

    assertSelection(5, 1, true);
  }

  @Test
  public void canProcessLeftArrowAndSelectToThePreviousWord()
  {
    model.setCaretIndex(9);

    processor.processKey(press(KeyEvent.KEY_LEFT), model);

    assertSelection(5, 9, true);
  }

}
