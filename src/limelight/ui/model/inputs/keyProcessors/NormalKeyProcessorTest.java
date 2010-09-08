package limelight.ui.model.inputs.keyProcessors;


import limelight.ui.events.KeyEvent;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class NormalKeyProcessorTest extends AbstractKeyProcessorTest
{
  @Before
  public void setUp()
  {
    setUpSingleLine();
    processor = NormalKeyProcessor.instance;
    modifiers = 0;
  }

//  @Test
//  public void canProcessCharacters()
//  {
//    mockEvent = new MockKeyEvent(modifiers, KeyEvent.KEY_A, 'a');
//
//    processor.processKey(mockEvent, model);
//
//    assertTextState(2, 0, "Haere are four words");
//  }

//  @Test
//  public void willNotInsertAnUndefinedCharacter()
//  {
//    mockEvent = new MockKeyEvent(modifiers, KeyEvent.KEY_A, KeyEvent.CHAR_UNDEFINED);
//
//    processor.processKey(mockEvent, model);
//
//    assertTextState(1, 0, "Here are four words");
//  }

  @Test
  public void canProcessBackSpace()
  {
    processor.processKey(press(KeyEvent.KEY_BACK_SPACE), model);

    assertEquals(0, model.getCaretIndex());
    assertEquals("ere are four words", model.getText());
    if(!model.isSelectionActivated())
      assertEquals(0, model.getSelectionIndex());
  }

  @Test
  public void canProcessRightArrow()
  {
    processor.processKey(press(KeyEvent.KEY_RIGHT), model);

    assertEquals(2, model.getCaretIndex());
    assertEquals(0, model.getSelectionIndex());
    assertEquals(false, model.isSelectionActivated());
  }

  @Test
  public void canProcessLeftArrow()
  {
    processor.processKey(press(KeyEvent.KEY_LEFT), model);

    assertEquals(0, model.getCaretIndex());
    assertEquals(0, model.getSelectionIndex());
    assertEquals(false, model.isSelectionActivated());
  }

}
