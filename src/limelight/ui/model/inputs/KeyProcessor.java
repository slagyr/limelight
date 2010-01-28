package limelight.ui.model.inputs;

import java.awt.event.KeyEvent;

public abstract class KeyProcessor
{
   protected TextModel boxInfo;

  public KeyProcessor(TextModel boxInfo){
    this.boxInfo = boxInfo;
  }

  public abstract void processKey(int keyCode);

  protected boolean isACharacter(int keyCode)
  {
    return (keyCode > 40 && keyCode < 100 || keyCode == 222 || keyCode == 32);
  }

  private void insertCharIntoTextBox(char c)
  {
    boxInfo.text.insert(boxInfo.cursorIndex, c);
    boxInfo.cursorIndex ++;
  }


  protected void insertLowercaseCharIntoTextBox(int keyCode)
  {
    char c;
    if(keyCode == KeyEvent.VK_SPACE)
       c = ' ';
    else
      c = Character.toLowerCase(KeyEvent.getKeyText(keyCode).charAt(0));
    insertCharIntoTextBox(c);
  }
  protected void insertUppercaseCharIntoTextBox(int keyCode)
  {
    char c;
    if(keyCode == KeyEvent.VK_SPACE)
       c = ' ';
    else
      c = KeyEvent.getKeyText(keyCode).charAt(0);
    insertCharIntoTextBox(c);
  }

  protected boolean isMoveRightEvent(int keyCode)
  {
    return keyCode == KeyEvent.VK_RIGHT && boxInfo.cursorIndex < boxInfo.text.length();
  }

  protected boolean isMoveLeftEvent(int keyCode)
  {
    return keyCode == KeyEvent.VK_LEFT && boxInfo.cursorIndex > 0;
  }

  protected void initSelection()
  {
    boxInfo.selectionOn = true;
    boxInfo.selectionIndex = boxInfo.cursorIndex;
  }

  protected int findNearestWordToTheLeft()
  {
    return boxInfo.findWordsLeftEdge(boxInfo.cursorIndex);
  }

  protected int findNearestWordToTheRight()
  {
    return findNextWordSkippingSpaces(boxInfo.findWordsRightEdge(boxInfo.cursorIndex));
  }

  private int findNextWordSkippingSpaces(int startIndex)
  {
    for (int i = startIndex;i <= boxInfo.text.length() -1;i++)
    {
      if(boxInfo.text.charAt(i-1) == ' ' && boxInfo.text.charAt(i) != ' ')
        return i;
    }
    return boxInfo.text.length();
  }

  protected void selectAll()
  {
    boxInfo.selectionOn = true;
    boxInfo.cursorIndex = boxInfo.text.length();
    boxInfo.selectionIndex = 0;
  }
}

