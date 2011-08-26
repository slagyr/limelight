//- Copyright © 2008-2011 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the MIT License.

package limelight.util;

import limelight.model.api.PropProxy;
import limelight.ui.Panel;
import limelight.ui.events.panel.*;
import limelight.ui.model.PropPanel;

import java.awt.*;


public class Mouse
{
  public static void press(PropProxy prop)
  {
    press(prop, 0, 0, 0, 1);
  }

  public static void press(PropProxy prop, int x, int y)
  {
    press(prop, x, y, 0, 1);
  }

  public static void press(PropProxy prop, int x, int y, int modifiers, int click_count)
  {
    Point location = pointFor(prop, x, y);
    Panel owner = ownerOf(location, prop);
    new MousePressedEvent(modifiers, location, click_count).dispatch(owner);
  }

  public static void release(PropProxy prop)
  {
    release(prop, 0, 0, 0, 1);
  }

  public static void release(PropProxy prop, int x, int y)
  {
    release(prop, x, y, 0, 1);
  }

  public static void release(PropProxy prop, int x, int y, int modifiers, int click_count)
  {
    Point location = pointFor(prop, x, y);
    Panel owner = ownerOf(location, prop);
    new MouseReleasedEvent(modifiers, location, click_count).dispatch(owner);
  }

  public static void click(PropProxy prop)
  {
    click(prop, 0, 0, 0, 1);
  }

  public static void click(PropProxy prop, int x, int y)
  {
    click(prop, x, y, 0, 1);
  }

  public static void click(PropProxy prop, int x, int y, int modifiers, int click_count)
  {
    Point location = pointFor(prop, x, y);
    Panel owner = ownerOf(location, prop);
    new MouseClickedEvent(modifiers, location, click_count).dispatch(owner);
  }

  public static void move(PropProxy prop)
  {
    move(prop, 0, 0, 0, 1);
  }

  public static void move(PropProxy prop, int x, int y)
  {
    move(prop, x, y, 0, 1);
  }

  public static void move(PropProxy prop, int x, int y, int modifiers, int click_count)
  {
    Point location = pointFor(prop, x, y);
    Panel owner = ownerOf(location, prop);
    new MouseMovedEvent(modifiers, location, click_count).dispatch(owner);
  }

  public static void drag(PropProxy prop)
  {
    drag(prop, 0, 0, 0, 1);
  }

  public static void drag(PropProxy prop, int x, int y)
  {
    drag(prop, x, y, 0, 1);
  }

  public static void drag(PropProxy prop, int x, int y, int modifiers, int click_count)
  {
    Point location = pointFor(prop, x, y);
    Panel owner = ownerOf(location, prop);
    new MouseDraggedEvent(modifiers, location, click_count).dispatch(owner);
  }

  public static void enter(PropProxy prop)
  {
    enter(prop, 0, 0, 0, 1);
  }

  public static void enter(PropProxy prop, int x, int y)
  {
    enter(prop, x, y, 0, 1);
  }

  public static void enter(PropProxy prop, int x, int y, int modifiers, int click_count)
  {
    Point location = pointFor(prop, x, y);
    Panel owner = ownerOf(location, prop);
    new MouseEnteredEvent(modifiers, location, click_count).dispatch(owner);
  }

  public static void exit(PropProxy prop)
  {
    exit(prop, 0, 0, 0, 1);
  }

  public static void exit(PropProxy prop, int x, int y)
  {
    exit(prop, x, y, 0, 1);
  }

  public static void exit(PropProxy prop, int x, int y, int modifiers, int click_count)
  {
    Point location = pointFor(prop, x, y);
    Panel owner = ownerOf(location, prop);
    new MouseExitedEvent(modifiers, location, click_count).dispatch(owner);
  }

  public static void wheel(PropProxy prop)
  {
    wheel(prop, 1, 0, 0, 0, 0, 0, 1);
  }

  public static void wheel(PropProxy prop, int scrollAmount)
  {
    wheel(prop, scrollAmount, 0, 0, 0, 0, 0, 1);
  }

  public static void wheel(PropProxy prop, int scrollAmount, int x, int y)
  {
    wheel(prop, scrollAmount, x, y, 0, 0, 0, 1);
  }

  public static void wheel(PropProxy prop, int scrollAmount, int x, int y, int modifiers, int clickCount, int scrollType, int wheelRotation)
  {
    Point location = pointFor(prop, x, y);
    Panel owner = ownerOf(location, prop);
    new MouseWheelEvent(modifiers, location, clickCount, scrollType, scrollAmount, wheelRotation).dispatch(owner);
  }

  private static Point pointFor(PropProxy prop, int x, int y)
  {
    final PropPanel propPanel = (PropPanel)prop.getPeer();
    Point absoluteLocation = propPanel.getAbsoluteLocation();
    int localX = absoluteLocation.x + x;
    int localY = absoluteLocation.y + y;
    return new Point(localX, localY);
  }

  private static Panel ownerOf(Point location, PropProxy prop)
  {
    return ((PropPanel)prop.getPeer()).getOwnerOfPoint(location);
  }

}
