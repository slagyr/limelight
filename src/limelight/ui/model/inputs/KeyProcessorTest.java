package limelight.ui.model.inputs;

import limelight.ui.model.inputs.keyProcessors.*;
import org.junit.Before;
import org.junit.Test;

import java.awt.event.KeyEvent;
import java.util.ArrayList;

import static org.junit.Assert.*;

public class KeyProcessorTest
{
  ArrayList<KeyProcessor> processors = new ArrayList<KeyProcessor>(16);
  TextModel boxInfo;
  TextInputPanel boxPanel;
  private KeyProcessor processor;


  @Before
  public void setUp()
  {
    boxPanel = new TextBox2Panel();
    boxInfo = new PlainTextModel(boxPanel);
    processors.add(0, new KPKey(boxInfo));
    processors.add(1, new KPCMD(boxInfo));
    processors.add(2, new KPShift(boxInfo));
    processors.add(3, new KPShiftCMD(boxInfo));
    processors.add(4, new KPAlt(boxInfo));
    processors.add(5, new KPAltCMD(boxInfo));
    processors.add(6, new KPAltShift(boxInfo));
    processors.add(7, new KPAltShiftCMD(boxInfo));
  }

  @Test
  public void canProcessStandardKey()
  {
    processor = processors.get(0);
    assertEquals(0, processor.boxInfo.cursorIndex);
    processor.processKey(KeyEvent.VK_A);
    assertTrue(boxInfo.getText().charAt(0) == 'a');
    assertEquals(1, boxInfo.cursorIndex);

    processor.processKey(KeyEvent.VK_BACK_SPACE);
    assertEquals(0, boxInfo.cursorIndex);
    assertEquals(0, boxInfo.text.length());

  }

  @Test
  public void canProcessArrowKeys()
  {
    processor = processors.get(0);
    boxInfo.setText("Text");
    boxInfo.cursorIndex = 3;
    processor.processKey(KeyEvent.VK_RIGHT);
    assertEquals(4, boxInfo.cursorIndex);
    processor.processKey(KeyEvent.VK_RIGHT);
    assertEquals(4, boxInfo.cursorIndex);

    processor.processKey(KeyEvent.VK_LEFT);
    assertEquals(3, boxInfo.cursorIndex);
    boxInfo.cursorIndex = 0;
    processor.processKey(KeyEvent.VK_LEFT);
    assertEquals(0, boxInfo.cursorIndex);
  }

  @Test
  public void canProcessKeysWithCMD()
  {
    processor = processors.get(1);
    boxInfo.setText("Bob");
    boxInfo.selectionOn = false;
    processor.processKey(KeyEvent.VK_A);
    assertTrue(boxInfo.selectionOn);
    assertEquals(0, boxInfo.selectionIndex);
    assertEquals(3, boxInfo.cursorIndex);

    boxInfo.copyText(" Dole");
    processor.processKey(KeyEvent.VK_V);
    assertEquals("Bob Dole", boxInfo.getText());
  }

  @Test
  public void canProcessKeysWithShift()
  {
    processor = processors.get(2);
    boxInfo.setText("ustin");
    boxInfo.cursorIndex = 0;
    processor.processKey(KeyEvent.VK_J);
    assertEquals("Justin", boxInfo.getText());
    assertEquals(1, boxInfo.cursorIndex);

    processor.processKey(KeyEvent.VK_RIGHT);
    assertTrue(boxInfo.selectionOn);
    assertEquals(1, boxInfo.selectionIndex);
    assertEquals(2, boxInfo.cursorIndex);

    boxInfo.selectionOn = false;
    processor.processKey(KeyEvent.VK_LEFT);
    processor.processKey(KeyEvent.VK_LEFT);
    assertTrue(boxInfo.selectionOn);
    assertEquals(1, boxInfo.selectionIndex);
    assertEquals(0, boxInfo.cursorIndex);
  }

  @Test
  public void canProcessShiftCMD()
  {
    processor = processors.get(3);
    boxInfo.setText("Rerroo");
    boxInfo.cursorIndex = 0;
    boxInfo.selectionOn = false;
    processor.processKey(KeyEvent.VK_RIGHT);
    assertTrue(boxInfo.selectionOn);
    assertEquals(0, boxInfo.selectionIndex);
    assertEquals(boxInfo.text.length(), boxInfo.cursorIndex);

    boxInfo.selectionOn = false;
    processor.processKey(KeyEvent.VK_LEFT);
    assertTrue(boxInfo.selectionOn);
    assertEquals(0, boxInfo.cursorIndex);
    assertEquals(boxInfo.text.length(), boxInfo.selectionIndex);
  }

  @Test
  public void canProcessAlt()
  {
    processor = processors.get(4);
    boxInfo.setText("Here are four words");
    boxInfo.cursorIndex = 0;
    processor.processKey(KeyEvent.VK_RIGHT);
    assertEquals(5, boxInfo.cursorIndex);

    processor.processKey(KeyEvent.VK_RIGHT);
    assertEquals(9, boxInfo.cursorIndex);

    processor.processKey(KeyEvent.VK_LEFT);
    assertEquals(5, boxInfo.cursorIndex);
  }

  @Test
  public void canProcessAltCMDAndDoNothing()
  {
    processor = processors.get(5);
    boxInfo.cursorIndex = 0;
    processor.processKey(KeyEvent.VK_RIGHT);
    assertEquals(0, boxInfo.cursorIndex);
  }

  @Test
  public void canProcessAltShift()
  {
    processor = processors.get(6);
    boxInfo.setText("Here are four words");
    boxInfo.cursorIndex = 0;
    boxInfo.selectionOn = false;
    processor.processKey(KeyEvent.VK_RIGHT);
    assertEquals(5, boxInfo.cursorIndex);
    assertTrue(boxInfo.selectionOn);
    assertEquals(0, boxInfo.selectionIndex);

    boxInfo.selectionOn = false;
    processor.processKey(KeyEvent.VK_LEFT);
    assertEquals(5, boxInfo.selectionIndex);
    assertTrue(boxInfo.selectionOn);
    assertEquals(0, boxInfo.cursorIndex);
  }

  @Test
  public void canProcessAltShiftCMDAndDoNothing()
  {
    processor = processors.get(7);
    boxInfo.cursorIndex = 0;
    processor.processKey(KeyEvent.VK_RIGHT);
    assertEquals(0, boxInfo.cursorIndex);
  }


}
