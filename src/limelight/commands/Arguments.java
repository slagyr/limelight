//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.commands;

import limelight.util.Util;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Arguments
{
  private String[] args;
  private int argIndex;
  private HashMap<String, String> results;
  private List<Parameter> parameters = new LinkedList<Parameter>();
  private List<Option> options = new LinkedList<Option>();
  private String message;
  private boolean success;


  public void addParameter(String name, String description)
  {
    parameters.add(new Parameter(name, description, true));
  }

  public void addOptionalParameter(String name, String description)
  {
    parameters.add(new Parameter(name, description, false));
  }

  public void addSwitchOption(String shortName, String fullName, String description)
  {
    addValueOption(shortName, fullName, null, description);
  }

  public void addValueOption(String shortName, String fullName, String valueDescription, String description)
  {
    if(shortName == null || fullName == null)
      throw new ArgumentException("Options require a shortName and fullName");
    options.add(new Option(shortName, fullName, valueDescription, description));
  }

  public Map<String, String> parse(String... args)
  {
    this.args = args;
    reset();
    try
    {
      parseParameters();
      parseArgs();
      success = true;
      message = "Arguments parsed successfully";
    }
    catch(ArgumentException e)
    {
      message = e.getMessage();
    }

    return results;
  }

  public String argString()
  {
    StringBuffer buffer = new StringBuffer();
    if(options.size() > 0)
      buffer.append("[options] ");

    for(int i = 0; i < parameters.size(); i++)
    {
      if(i > 0)
        buffer.append(" ");
      Parameter parameter = parameters.get(i);
      if(parameter.required)
        buffer.append("<").append(parameter.name).append(">");
      else
        buffer.append("[").append(parameter.name).append("]");
    }

    return buffer.toString();
  }


  public String parametersString()
  {
    String[] names = new String[parameters.size()];
    String[] descriptions = new String[parameters.size()];
    for(int i = 0; i < parameters.size(); i++)
    {
      final Parameter parameter = parameters.get(i);
      names[i] = parameter.name;
      descriptions[i] = parameter.description;
    }
    return tabularize(names, descriptions);
  }

  public String optionsString()
  {
    String[] heads = new String[options.size()];
    String[] descriptions = new String[options.size()];
    for(int i = 0; i < options.size(); i++)
    {
      final Option option = options.get(i);
      heads[i] = option.head();
      descriptions[i] = option.description;
    }

    return tabularize(heads, descriptions);
  }

  public boolean success()
  {
    return success;
  }

  public String getMessage()
  {
    return message;
  }

  public Option findOption(String name)
  {
    for(Option option : options)
    {
      if(option.shortName.equals(name) || option.fullName.equals(name))
        return option;
    }
    return null;
  }

  public int parameterCount()
  {
    return parameters.size();
  }

  public boolean hasParameters()
  {
    return parameters.size() > 0;
  }

  public boolean hasParameter(String name)
  {
    for(Parameter parameter : parameters)
    {
      if(name.equals(parameter.name))
        return true;
    }
    return false;
  }

  public String[] leftOverArgs()
  {
    int leftOverCount = args.length - argIndex;
    String[] leftOver = new String[leftOverCount];
    System.arraycopy(args, argIndex, leftOver, 0, leftOverCount);
    return leftOver;
  }

  public boolean hasOptions()
  {
    return options.size() > 0;
  }

  private void reset()
  {
    argIndex = 0;
    results = new HashMap<String, String>();
    success = false;
    message = null;
  }

  private void parseArgs()
  {
    while(hasMoreArgs())
    {
      String arg = peek(1);
      if(!isOption(arg))
        throw new ArgumentException("Unexpected parameter: " + arg);
      parseOption();
    }
  }

  private void parseParameters()
  {
    for(Parameter parameter : parameters)
    {
      boolean found = false;
      while(!found)
      {
        String arg = peek(1);
        if(arg == null)
        {
          if(parameter.required)
            throw new ArgumentException("Missing parameter: " + parameter.name);
          else
            found = true;
        }
        else
        {
          if(isOption(arg))
            parseOption();
          else
          {
            results.put(parameter.name, nextArg());
            found = true;
          }
        }
      }
    }
  }

  private void parseOption()
  {
    final String arg = peek(1);
    OptionParser parser = new OptionParser(arg);
    Option option = findOption(parser.argName);

    if(option == null)
      throw new ArgumentException("Unrecognized option: " + arg);

    if(option.requiresValue())
    {
      if(parser.usingEquals)
      {
        if(parser.argValue == null)
          throw new ArgumentException("Missing value for option: " + parser.argName);
        results.put(option.fullName, parser.argValue);
        nextArg();
      }
      else
      {
        final String nextArg = peek(2);
        if(nextArg == null || isOption(nextArg))
          throw new ArgumentException("Missing value for option: " + parser.argName);
        results.put(option.fullName, nextArg);
        nextArg();
        nextArg();
      }
    }
    else
    {
      results.put(option.fullName, "on");
      nextArg();
    }
  }

  private boolean isOption(String arg)
  {
    return arg.startsWith("-");
  }

  private String nextArg()
  {
    if(hasMoreArgs())
      return args[argIndex++];
    else
      return null;
  }

  private String peek(int delta)
  {
    final int peekIndex = argIndex + delta - 1;
    if(peekIndex < args.length)
      return args[peekIndex];
    else
      return null;
  }

  private boolean hasMoreArgs()
  {
    return argIndex < args.length;
  }

  public static String tabularize(String[] col1, String[] col2)
  {
    int maxLength = 0;
    for(String s : col1)
    {
      if(s.length() > maxLength)
        maxLength = s.length();
    }

    StringBuffer buffer = new StringBuffer();
    for(int i = 0; i < col1.length; i++)
    {
      buffer.append("  ").append(col1[i]);
      final int spaces = maxLength - col1[i].length();
      for(int j = 0; j < spaces; j++)
        buffer.append(" ");
      buffer.append("  ").append(col2[i]);
      buffer.append(Util.ENDL);
    }
    return buffer.toString();
  }

  public static class Parameter
  {
    private String name;
    private boolean required;
    private String description;

    public Parameter(String name, String description, boolean required)
    {
      this.name = name;
      this.description = description;
      this.required = required;
    }
  }

  public static class Option
  {
    private String shortName;
    private String fullName;
    private String valueDescription;
    private String description;
    private String head;

    public Option(String shortName, String fullName, String valueDescription, String description)
    {
      this.shortName = shortName;
      this.fullName = fullName;
      this.valueDescription = valueDescription;
      this.description = description;
    }

    public boolean requiresValue()
    {
      return valueDescription != null;
    }

    private String head()
    {
      if(head == null)
      {
        head = "-" + shortName + ", --" + fullName;
        if(requiresValue())
          head += "=<" + valueDescription + ">";
      }
      return head;
    }

    public String getShortName()
    {
      return shortName;
    }

    public String getFullName()
    {
      return fullName;
    }

    public String getValueDescription()
    {
      return valueDescription;
    }

    public String getDescription()
    {
      return description;
    }
  }

  private static class OptionParser
  {
    private String argName;
    private String argValue;
    private boolean usingFullName;
    private boolean usingEquals;

    public OptionParser(String arg)
    {
      stripDashes(arg);

      if(usingFullName)
      {
        int valueIndex = argName.indexOf("=");
        if(valueIndex > 0)
        {
          usingEquals = true;
          argValue = argName.substring(valueIndex + 1);
          argName = argName.substring(0, valueIndex);
        }
      }
    }

    private void stripDashes(String arg)
    {
      if(arg.startsWith("--"))
      {
        usingFullName = true;
        argName = arg.substring(2);
      }
      else
      {
        usingFullName = false;
        argName = arg.substring(1);
      }
    }

  }

  public static class ArgumentException extends RuntimeException
  {
    public ArgumentException(String message)
    {
      super(message);
    }
  }
}
