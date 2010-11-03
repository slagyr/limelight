(ns limelight.player)

(declare *action-cache*)

(defn on-mouse-clicked [action]
  (let [event-type limelight.ui.events.panel.MouseClickedEvent
        actions (or (@*action-cache* event-type) [])]
    (swap! *action-cache* assoc event-type (conj actions action))
    (println "actions: " @*action-cache*)
    ))



;(defn on-mouse-clicked [action]
;  (.add (.getEventHandler @(.peer *prop*))
;    limelight.ui.events.panel.MouseClickedEvent
;    (reify limelight.events.EventAction
;      (invoke [this e] (action e)))))