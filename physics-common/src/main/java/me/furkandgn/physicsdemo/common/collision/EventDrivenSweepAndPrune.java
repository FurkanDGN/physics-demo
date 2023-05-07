package me.furkandgn.physicsdemo.common.collision;

import me.furkandgn.physicsdemo.common.Body;
import me.furkandgn.physicsdemo.common.CollisionDetector;
import me.furkandgn.physicsdemo.common.CollisionEvent;

import java.util.*;

public class EventDrivenSweepAndPrune implements CollisionDetector {

  @Override
  public List<CollisionEvent> findCollisions(List<Body> bodies) {
    List<CollisionEvent> events = new ArrayList<>();
    List<StartEnd> xCoords = new ArrayList<>();
    for (Body body : bodies) {
      xCoords.add(new StartEnd(body.x(), true, body));
      xCoords.add(new StartEnd(body.x() + body.width(), false, body));
    }
    Collections.sort(xCoords);

    List<StartEnd> yCoords = new ArrayList<>();
    for (Body body : bodies) {
      yCoords.add(new StartEnd(body.y(), true, body));
      yCoords.add(new StartEnd(body.y() + body.height(), false, body));
    }
    Collections.sort(yCoords);

    Set<Body> activeBodies = new HashSet<>();
    for (StartEnd se : xCoords) {
      if (se.isStart) {
        activeBodies.add(se.body);
        for (Body other : activeBodies) {
          if (se.body != other && (se.body.canCollide(other) || other.canCollide(se.body))) {
            CollisionEvent event = new CollisionEvent(se.body, other);
            events.add(event);
          }
        }
      } else {
        activeBodies.remove(se.body);
      }
    }

    activeBodies.clear();
    for (StartEnd se : yCoords) {
      if (se.isStart) {
        activeBodies.add(se.body);
        for (Body other : activeBodies) {
          if (se.body != other && (se.body.canCollide(other) || other.canCollide(se.body))) {
            CollisionEvent event = new CollisionEvent(se.body, other);
            events.add(event);
          }
        }
      } else {
        activeBodies.remove(se.body);
      }
    }

    return events;
  }

  private record StartEnd(double value, boolean isStart, Body body) implements Comparable<StartEnd> {

    @Override
    public int compareTo(StartEnd o) {
      if (this.value != o.value) {
        return Double.compare(this.value, o.value);
      }
      if (this.isStart && !o.isStart) {
        return -1;
      }
      if (!this.isStart && o.isStart) {
        return 1;
      }
      return 0;
    }
  }
}