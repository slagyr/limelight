package limelight.ui.model.inputs;

import java.awt.event.KeyEvent;
import java.awt.font.TextHitInfo;

public abstract class KeyProcessor
{
  protected TextModel modelInfo;

  public KeyProcessor(TextModel modelInfo)
  {
    this.modelInfo = modelInfo;
  }

  public abstract void processKey(KeyEvent event);

  protected boolean isACharacter(int keyCode)
  {
    return (keyCode > 40 && keyCode < 100 || keyCode == 222 || keyCode == 32);
  }
}

