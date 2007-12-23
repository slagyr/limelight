package limelight;

import java.util.Hashtable;
import java.util.Map;

public class FlatStyle extends Style
{
	private Hashtable<String, String> styles;

	public FlatStyle()
	{
		styles = new Hashtable<String, String>();
	}

  protected String get(String key)
	{
		return styles.get(key);
	}

	protected void put(String key, String value)
	{
		styles.put(key, value);
	}

  protected boolean has(Object key)
  {
    return styles.containsKey(key);
  }

  public Hashtable<String, String> getStyles()
  {
    return styles;
  }

  public int checksum()
  {
    int checksum = 0;
    for (Map.Entry entry : styles.entrySet())
    {
      if(entry.getKey() != "transparency")
        checksum += (entry.getKey().hashCode() & entry.getValue().hashCode());
    }
    return checksum;
  }
}
