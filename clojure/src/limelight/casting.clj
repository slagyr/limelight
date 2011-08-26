;- Copyright © 2008-2011 8th Light, Inc. All Rights Reserved.
;- Limelight and all included source files are distributed under terms of the MIT License.

(ns limelight.casting
  (:use
    [limelight.common]
    [limelight.player]
    [limelight.util :only (read-src)]))

(defn- load-player-from [casting-director player-path player-name]
  (let [player-content (.readTextFile (limelight.Context/fs) player-path)
        player-ns (create-ns (gensym (str "limelight.dynamic-player." player-name "-")))
        event-actions (intern player-ns '*event-actions* (atom {}))]
    (binding [*ns* player-ns]
      (use 'clojure.core)
      (use 'limelight.player)
      (use 'limelight.common)
      (binding [limelight.player/*action-cache* @event-actions]
        (read-src player-path player-content)))
    (swap! (.cast casting-director) #(assoc % player-name player-ns))
    player-ns))

(defn- player-path [resource-root relative-player-path]
  (resource-path resource-root relative-player-path))

(defn- cast-player [player-ns prop]
  (let [event-actions @(ns-resolve player-ns '*event-actions*)
        event-handler (.getEventHandler @(.peer prop))]
    (if-let [on-cast-actions (:on-cast @event-actions)]
      (doseq [action on-cast-actions]
        (.invoke action nil)))
    (doseq [[event-class actions] @event-actions]
      (when (not (= :on-cast event-class))
        (doseq [action actions]
          (.add event-handler event-class action))))))

(defn- player-path [player-name players-path]
  (str players-path "/" (limelight.util.StringUtil/underscore player-name) ".clj"))

(deftype CastingDirector [scene cast]
  limelight.model.api.CastingDirector
  (hasPlayer [this player-name players-path]
    (if (@cast player-name)
      true
      (.exists (limelight.Context/fs) (player-path player-name players-path))))
  (castPlayer [this prop player-name players-path]
    (if-let [player (or (@cast player-name) (load-player-from this (player-path player-name players-path) player-name))]
        (cast-player player prop))))

(defn new-casting-director [scene]
  (CastingDirector. scene (atom {})))

