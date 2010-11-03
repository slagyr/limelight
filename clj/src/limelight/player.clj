(ns limelight.player)

(declare *action-cache*)

(defn on-mouse-clicked [action]
  (let [event-type limelight.ui.events.panel.MouseClickedEvent
        event-action (reify limelight.events.EventAction (invoke [this e] (action e)))
        actions (or (@*action-cache* event-type) [])]
    (swap! *action-cache* assoc event-type (conj actions event-action))))
