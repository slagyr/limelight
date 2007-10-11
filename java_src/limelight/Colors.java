package limelight;

import java.awt.*;
import java.lang.reflect.Field;
import java.util.regex.Pattern;

public class Colors
{
	private static Pattern fullHex = Pattern.compile("#[0-9aAbBcCdDeEfF]{6}");
	private static Pattern shortHex = Pattern.compile("#[0-9aAbBcCdDeEfF]{3}");

	public static Color resolve(String value)
	{
		Color result = Color.black;
		if(fullHex.matcher(value).find())
		{
			result = Color.decode(value);
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
}
