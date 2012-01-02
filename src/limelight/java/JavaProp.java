//- Copyright © 2008-2011 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the MIT License.

package limelight.java;

import limelight.model.api.PropProxy;
import limelight.styles.Style;
import limelight.ui.Panel;
import limelight.ui.model.PropPanel;
import limelight.util.Box;

import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class JavaProp implements PropProxy
{
  protected PropPanel peer;
  private LinkedList<Object> players = new LinkedList<Object>();

  public JavaProp(Map<String, Object> options)
  {
    peer = createPeer(options);
  }

  protected PropPanel createPeer(Map<String, Object> options)
  {
    return new PropPanel(this, options);
  }

  public PropPanel getPeer()
  {
    return peer;
  }

  public Map<String, Object> applyOptions(Map<String, Object> options)
  {
    return options;
  }

  public void add(JavaProp prop)
  {
    peer.add(prop.getPeer());
  }

  public String getId()
  {
    return peer.getId();
  }

  public String getName()
  {
    return peer.getName();
  }

  public List getPlayers()
  {
    return players;
  }

  public void addPlayer(Object player)
  {
    players.add(player);
  }

  public JavaScene getScene()
  {
    return (JavaScene)peer.getRoot().getProxy();
  }

  public String getText()
  {
    return peer.getText();
  }

  public void setText(String text)
  {
    peer.setText(text);
  }

  public List<JavaProp> getChildren()
  {
    ArrayList<JavaProp> children = new ArrayList<JavaProp>();
    for(PropPanel propPanel : peer.getChildPropPanels())
      children.add((JavaProp)propPanel.getProxy());
    return children;
  }

  @Override
  public String toString()
  {
    String value = "JavaProp[" + getName() + "]";
    if(getId() != null)
      value += " id=" + getId();
    if(getText() != null && getText().length() > 0)
      value += " text='" + getText() + "'";
    return value;
  }

  public Box getBounds()
  {
    return peer.getBounds();
  }

  public Style getStyle()
  {
    return peer.getStyle();
  }
}
