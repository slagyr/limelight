package limelight.ui.model.inputs.keyProcessors;


import limelight.ui.events.KeyEvent;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

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

    assertEquals(5, model.getCaretIndex());
    assertEquals(1, model.getSelectionIndex());
    assertEquals(true, model.isSelectionActivated());
  }

  @Test
  public void canProcessLeftArrowAndSelectToThePreviousWord()
  {
    model.setCaretIndex(9);

    processor.processKey(press(KeyEvent.KEY_LEFT), model);

    assertEquals(5, model.getCaretIndex());
    assertEquals(9, model.getSelectionIndex());
    assertEquals(true, model.isSelectionActivated());
  }

}
