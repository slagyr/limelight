package limelight;

import java.awt.*;

public class StaticImageFillStrategy implements ImageFillStrategy
{
	public void fill(Graphics2D graphics, Image image)
	{
		Rectangle area = new Rectangle(graphics.getClipBounds());
		Graphics innerGraphics = graphics.create(area.x, area.y, area.width, area.height);
		innerGraphics.drawImage(image, 0, 0, null);
	}
}
