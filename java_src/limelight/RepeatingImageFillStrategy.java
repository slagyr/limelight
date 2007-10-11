package limelight;

import java.awt.*;

public class RepeatingImageFillStrategy implements ImageFillStrategy
{
	public void fill(Graphics2D graphics, Image image)
	{
		int imageWidth = image.getWidth(null);
		int imageHeight = image.getHeight(null);
		Rectangle area = new Rectangle(graphics.getClipBounds());

		for(int y = 0; y < area.height; y += imageHeight)
		{
			for(int x = 0; x < area.width; x += imageWidth)
			{
				graphics.drawImage(image, x, y, null);				
			}
		}
	}
}
