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

  protected void insertCharIntoTextBox(char c)
  {
    boxInfo.text.insert(boxInfo.cursorIndex, c);
    boxInfo.cursorIndex ++;
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
    for(int i = boxInfo.cursorIndex -1; i > 1; i-- ){
      if(boxInfo.text.charAt(i - 1) == ' ')
        return i;
    }
    return 0;
  }

  protected int findNearestWordToTheRight()
  {
    for(int i = boxInfo.cursorIndex; i < boxInfo.text.length() -1; i++ ){
      if(boxInfo.text.charAt(i) == ' ')
        return i + 1;
    }
    return boxInfo.text.length();
  }
}

