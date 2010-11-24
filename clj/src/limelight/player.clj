(ns limelight.player)

(declare *action-cache*)

(defn add-action [event-type action]
  (let [event-action (reify limelight.events.EventAction (invoke [this e] (action e)))
        actions (or (@*action-cache* event-type) [])]
    (swap! *action-cache* assoc event-type (conj actions event-action))))

(defmacro on-mouse-clicked [bindings & forms] `(add-action ~limelight.ui.events.panel.MouseClickedEvent (fn ~bindings ~@forms)))
(defmacro on-mouse-pressed [bindings & forms] `(add-action ~limelight.ui.events.panel.MousePressedEvent (fn ~bindings ~@forms)))

;(defn on-mouse-clicked [action] (add-action limelight.ui.events.panel.MouseClickedEvent action))
;(defn on-mouse-pressed [action] (add-action limelight.ui.events.panel.MousePressedEvent action))
;(defn on-mouse-released [action] (add-action limelight.ui.events.panel.MouseReleasedEvent action))
;(defn on-mouse-moved [action] (add-action limelight.ui.events.panel.MouseMovedEvent action))
;(defn on-mouse-dragged [action] (add-action limelight.ui.events.panel.MouseDraggedEvent action))
;(defn on-mouse-entered [action] (add-action limelight.ui.events.panel.MouseEnteredEvent action))
;(defn on-mouse-exited [action] (add-action limelight.ui.events.panel.MouseExitedEvent action))
