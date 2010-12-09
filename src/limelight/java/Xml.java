package limelight.java;

import limelight.Context;
import limelight.LimelightException;
import limelight.model.Theater;
import limelight.styles.RichStyle;
import limelight.util.Options;
import limelight.util.OptionsMap;
import org.w3c.dom.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Map;

public class Xml
{
  private static DocumentBuilder builder;

  static
  {
    try
    {
      DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
      builder = factory.newDocumentBuilder();
    }
    catch(ParserConfigurationException e)
    {
      throw new LimelightException(e);
    }
  }

  public static Document loadDocumentFrom(String xmlPath)
  {
    try
    {
      final InputStream xmlInput = Context.fs().inputStream(xmlPath);
      return builder.parse(xmlInput);
    }
    catch(Exception e)
    {
      throw new LimelightException("Error parsing XML file: " + xmlPath, e);
    }
  }

  public static ArrayList<Element> loadRootElements(String xmlPath)
  {
    if(Context.fs().exists(xmlPath))
    {
      final Document document = loadDocumentFrom(xmlPath);
      return loadChildElements(document.getDocumentElement());
    }
    return new ArrayList<Element>();
  }

  public static ArrayList<Element> loadChildElements(Element parent)
  {
    ArrayList<Element> elements = new ArrayList<Element>();
    final NodeList stageElements = parent.getChildNodes();
    for(int i = 0; i < stageElements.getLength(); i++)
    {
      final Node node = stageElements.item(i);
      if(node instanceof Element)
        elements.add((Element) node);
    }
    return elements;
  }

  public static OptionsMap loadOptions(Element stageElement)
  {
    OptionsMap options = new OptionsMap();
    final NamedNodeMap attributes = stageElement.getAttributes();
    for(int j = 0; j < attributes.getLength(); j++)
    {
      final Node node = attributes.item(j);
      final String key = node.getNodeName();
      final String value = node.getNodeValue();
      options.put(key, value);
    }

    return options;
  }

  public static JavaProp toProp(Element propElement)
  {
    String name = propElement.getNodeName();
    final OptionsMap options = loadOptions(propElement);
    options.put("name", name);
    final JavaProp prop = new JavaProp(options);

    for(Element childElement : loadChildElements(propElement))
      prop.add(toProp(childElement));

    return prop;
  }

  public static void toStyle(Element styleElement, Map<String, RichStyle> map)
  {
    String name = styleElement.getNodeName();
    final OptionsMap options = loadOptions(styleElement);
    RichStyle style = new RichStyle();
    Options.apply(style, options);
    map.put(name, style);
  }

  public static void toStage(Theater theater, Element stageElement)
  {
    final String name = stageElement.getNodeName();
    OptionsMap options = loadOptions(stageElement);
    final JavaStage stage = new JavaStage(name, options);
    theater.add(stage.getPeer());
  }
}
