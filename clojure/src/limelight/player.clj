(ns limelight.player)

(declare *action-cache*)

(defn add-action [event-type action]
  (let [event-action (reify limelight.events.EventAction (invoke [this e] (action e)))
        actions (or (@*action-cache* event-type) [])]
    (swap! *action-cache* assoc event-type (conj actions event-action))))


(defmacro add-action2 [event-type & forms]
  (if (and (> (count forms) 1) (vector? (first forms)))
    `(add-action ~event-type (fn ~(first forms) ~@(rest forms)))
    `(add-action ~event-type (fn [~'%] ~@forms))))

(defmacro on-mouse-clicked [bindings & forms] `(add-action ~limelight.ui.events.panel.MouseClickedEvent (fn ~bindings ~@forms)))
(defmacro on-mouse-pressed [bindings & forms] `(add-action ~limelight.ui.events.panel.MousePressedEvent (fn ~bindings ~@forms)))
(defmacro on-mouse-released [bindings & forms] `(add-action ~limelight.ui.events.panel.MouseReleasedEvent (fn ~bindings ~@forms)))
(defmacro on-mouse-entered [bindings & forms] `(add-action ~limelight.ui.events.panel.MouseEnteredEvent (fn ~bindings ~@forms)))
(defmacro on-mouse-exited [bindings & forms] `(add-action ~limelight.ui.events.panel.MouseExitedEvent (fn ~bindings ~@forms)))
(defmacro on-mouse-moved [bindings & forms] `(add-action ~limelight.ui.events.panel.MouseMovedEvent (fn ~bindings ~@forms)))
(defmacro on-mouse-dragged [bindings & forms] `(add-action ~limelight.ui.events.panel.MouseDraggedEvent (fn ~bindings ~@forms)))
(defmacro on-mouse-wheel [bindings & forms] `(add-action ~limelight.ui.events.panel.MouseWheelEvent (fn ~bindings ~@forms)))
(defmacro on-key-pressed [bindings & forms] `(add-action ~limelight.ui.events.panel.KeyPressedEvent (fn ~bindings ~@forms)))
(defmacro on-key-released [bindings & forms] `(add-action ~limelight.ui.events.panel.KeyReleasedEvent (fn ~bindings ~@forms)))
(defmacro on-char-typed [bindings & forms] `(add-action ~limelight.ui.events.panel.CharTypedEvent (fn ~bindings ~@forms)))
(defmacro on-focus-gained [bindings & forms] `(add-action ~limelight.ui.events.panel.FocusGainedEvent (fn ~bindings ~@forms)))
(defmacro on-focus-lost [bindings & forms] `(add-action ~limelight.ui.events.panel.FocusLostEvent (fn ~bindings ~@forms)))
(defmacro on-button-pushed [bindings & forms] `(add-action ~limelight.ui.events.panel.ButtonPushedEvent (fn ~bindings ~@forms)))
(defmacro on-value-changed [bindings & forms] `(add-action ~limelight.ui.events.panel.ValueChangedEvent (fn ~bindings ~@forms)))
(defmacro on-scene-opened [bindings & forms] `(add-action ~limelight.ui.events.panel.SceneOpenedEvent (fn ~bindings ~@forms)))
(defmacro on-cast [& forms] `(add-action2 :on-cast ~@forms))