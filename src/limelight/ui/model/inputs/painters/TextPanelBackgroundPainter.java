package limelight.ui.model.inputs.painters;

import com.android.ninepatch.NinePatch;
import limelight.ui.model.inputs.TextModel;
import limelight.ui.model.inputs.TextPanelPainter;
import limelight.util.Colors;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class TextPanelBackgroundPainter extends TextPanelPainter
{
  private NinePatch normal9Patch;
  private NinePatch focus9Patch;

  public TextPanelBackgroundPainter(TextModel boxInfo)
  {
    super(boxInfo);
    try
    {
      normal9Patch = NinePatch.load(ImageIO.read(getClass().getClassLoader().getResource("limelight/ui/images/text_box.9.png")), true, true);
      focus9Patch = NinePatch.load(ImageIO.read(getClass().getClassLoader().getResource("limelight/ui/images/text_box_focus.9.png")), true, true);
    }
    catch(IOException e)
    {
      throw new RuntimeException(e);
    }
  }

  public void paint(Graphics2D graphics)
  {
    Dimension dimension = new Dimension(boxInfo.getPanelWidth(), boxInfo.getPanelHeight());
    normal9Patch.draw(graphics, 0, 0, dimension.width, dimension.height);
    if(boxInfo.isFocused())
      focus9Patch.draw(graphics, 0, 0, dimension.width, dimension.height);
  }
}
