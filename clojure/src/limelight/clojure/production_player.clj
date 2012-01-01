;- Copyright © 2008-2011 8th Light, Inc. All Rights Reserved.
;- Limelight and all included source files are distributed under terms of the MIT License.

(ns limelight.clojure.production-player)

(declare *production*)

(defn add-action-with-bindings [event-type action]
  (let [event-action (reify limelight.events.EventAction (invoke [this e] (action e)))]
    (.add (.getEventHandler (.peer *production*)) event-type event-action)))

(defmacro add-action [event-type & forms]
  (if (vector? (first forms))
    `(add-action-with-bindings ~event-type (fn ~(first forms) ~@(rest forms)))
    `(add-action-with-bindings ~event-type (fn [~'%] ~@forms))))

(defmacro on-production-created [& forms] `(add-action ~limelight.model.events.ProductionCreatedEvent ~@forms))
(defmacro on-production-loaded [& forms] `(add-action ~limelight.model.events.ProductionLoadedEvent ~@forms))
(defmacro on-production-opened [& forms] `(add-action ~limelight.model.events.ProductionOpenedEvent ~@forms))
(defmacro on-production-closing [& forms] `(add-action ~limelight.model.events.ProductionClosingEvent ~@forms))
(defmacro on-production-closed [& forms] `(add-action ~limelight.model.events.ProductionClosedEvent ~@forms))


