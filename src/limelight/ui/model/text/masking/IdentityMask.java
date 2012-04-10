package limelight.ui.model.text.masking;

public class IdentityMask implements TextMask
{
  public static final TextMask instance = new IdentityMask();

  public String mask(String text)
  {
    return text;
  }
}
