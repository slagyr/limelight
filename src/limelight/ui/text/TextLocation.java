package limelight.ui.text;

import limelight.LimelightException;
import limelight.util.StringUtil;

import java.util.ArrayList;
import java.util.List;

public class TextLocation
{
  public static final TextLocation origin = TextLocation.at(0, 0);

  // MDM - Static constructor so that we can optimize the creation of these commonly used objects.
  public static TextLocation at(int lineNumber, int index)
  {
    return new TextLocation(lineNumber, index);
  }

  public static TextLocation fromIndex(List<TypedLayout> lines, int index)
  {
    int lineNumber;
    for(lineNumber = 0; lineNumber < lines.size(); lineNumber++)
    {
      TypedLayout line = lines.get(lineNumber);
      int lineLength = line.getText().length();
      if(lineLength > index)
        return TextLocation.at(lineNumber, index);
      else if(lineLength == index)
      {
        boolean isNotLastLine = (lineNumber + 1) < lines.size();
        if(isNotLastLine && StringUtil.endsWithNewline(line.getText()))
          return TextLocation.at(lineNumber + 1, 0);
        else
          return TextLocation.at(lineNumber, index);
      }
      else
      {
        index -= lineLength;
      }
    }
    throw new LimelightException("Can't find text location for index");
  }


  public int line;
  public int index;

  private TextLocation(int line, int index)
  {
    this.line = line;
    this.index = index;
  }

  @Override
  public String toString()
  {
    return "TextLocation: " + "line: " + line + ", index: " + index;
  }

  @Override
  public boolean equals(Object obj)
  {
    if(obj instanceof TextLocation)
    {
      TextLocation other = (TextLocation) obj;
      return this.line == other.line && this.index == other.index;
    }
    else
      return false;
  }

  public int toIndex(List<TypedLayout> lines)
  {
    int index = 0;
    for(int i = 0; i < line; i++)
      index += lines.get(i).getText().length();
    return index + this.index;
  }

  public boolean before(TextLocation other)
  {
    if(line < other.line)
      return true;
    else if(line == other.line)
      return index < other.index;
    return false;
  }

  public boolean isAfter(TextLocation other)
  {
    return other.before(this);
  }

  public boolean atStart()
  {
    return line == 0 && index == 0;
  }

  public boolean atEnd(ArrayList<TypedLayout> lines)
  {
    return line == lines.size() - 1 && index == lines.get(line).getText().length();
  }

  public TextLocation moved(ArrayList<TypedLayout> lines, int places)
  {
    int newLine = line;
    int newIndex = index + places;
    int lineLength = lines.get(newLine).visibleLength();

    while(newIndex > lineLength)
    {
      newLine++;
      newIndex -= lineLength + 1;
      if(newLine >= lines.size())
      {
        newLine = lines.size() - 1;
        newIndex = lineLength;
      }
      else
        lineLength = lines.get(newLine).visibleLength();
    }

    while(newIndex < 0)
    {
      newLine--;
      if(newLine < 0)
      {
        newLine = 0;
        newIndex = 0;
      }
      else
      {
        lineLength = lines.get(newLine).visibleLength();
        newIndex += lineLength + 1;
      }
    }

    return TextLocation.at(newLine, newIndex);
  }
}
