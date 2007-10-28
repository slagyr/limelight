package limelight;

import java.awt.*;
import java.lang.reflect.Field;
import java.util.regex.Pattern;

public class Colors
{
	private static Pattern fullHex = Pattern.compile("#[0-9aAbBcCdDeEfF]{6}");
	private static Pattern shortHex = Pattern.compile("#[0-9aAbBcCdDeEfF]{3}");
	private static Pattern fullHexWithAlpha = Pattern.compile("#[0-9aAbBcCdDeEfF]{8}");
	private static Pattern shortHexWithAlpha = Pattern.compile("#[0-9aAbBcCdDeEfF]{4}");

	public static Color resolve(String value)
	{
		Color result = Color.black;
		if(fullHexWithAlpha.matcher(value).find())
		{
            int r = hexToInt(value.substring(1, 3));
            int g = hexToInt(value.substring(3, 5));
            int b = hexToInt(value.substring(5, 7));
            int a = hexToInt(value.substring(7, 9));
            result = new Color(r, g, b, a);
        }
		else if(fullHex.matcher(value).find())
		{
			result = Color.decode(value);
		}
		else if(shortHexWithAlpha.matcher(value).find())
		{
            int r = hexToInt("" + value.charAt(1) + value.charAt(1));
            int g = hexToInt("" + value.charAt(2) + value.charAt(2));
            int b = hexToInt("" + value.charAt(3) + value.charAt(3));
            int a = hexToInt("" + value.charAt(4) + value.charAt(4));
            result = new Color(r, g, b, a);
        }
		else if(shortHex.matcher(value).find())
		{
			String fullHexValue = "#" +
                value.charAt(1) + value.charAt(1) +
                value.charAt(2) + value.charAt(2) +
                value.charAt(3) + value.charAt(3);

			result = Color.decode(fullHexValue);
		}
		else
		{
			try
			{

				Field field = Color.class.getField(value.toLowerCase());
				Object obj = field.get(Color.class);
				result = (Color)obj;
			}
			catch(Exception e)
			{
			}
		}

		return result;
	}

    private static int hexToInt(String hex)
    {
        return Integer.parseInt(hex, 16);
    }
}
