package limelight.java;

import limelight.model.api.PropProxy;
import limelight.ui.model.PropPanel;

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

  public void applyOptions(Map<String, Object> options)
  {
  }

  public void add(JavaProp prop)
  {
    peer.add(prop.getPeer());
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
}
