package limelight.ui.model.text.masking;

public class PasswordMask implements TextMask
{
  public static final TextMask instance = new PasswordMask();

  public String mask(String text)
  {
    StringBuilder builder = new StringBuilder();
    for(int i = 0; i < text.length(); i++)
      builder.append((char)0x2022);
    return builder.toString();
  }
}
