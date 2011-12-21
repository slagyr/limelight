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

(defn- cast-player [player-ns prop-panel]
  (let [event-actions @(ns-resolve player-ns '*event-actions*)
        event-handler (.getEventHandler prop-panel)]
    (if-let [on-cast-actions (:on-cast @event-actions)]
      (doseq [action on-cast-actions]
        (.invoke action nil)))
    (doseq [[event-class actions] @event-actions]
      (when (not (= :on-cast event-class))
        (doseq [action actions]
          (.add event-handler event-class action))))))

(defn- player-path [player-name players-path]
  (str players-path "/" (limelight.util.StringUtil/snakeCase player-name) ".clj"))

(deftype Player [name path player-ns]
  limelight.model.api.Player
  (cast [this prop-panel]
    (cast-player player-ns prop-panel))
  (getPath [this] path)
  (getName [this] name)
  (applyOptions [this prop-panel options] options))

(defn new-player [name path player-ns]
  (Player. name path player-ns))

(deftype PlayerRecruiter [scene cast]
  limelight.model.api.PlayerRecruiter
  (canRecruit [this player-name players-path]
    (if (@cast player-name)
      true
      (.exists (limelight.Context/fs) (player-path player-name players-path))))
  (recruitPlayer [this player-name players-path]
    (let [player-path (player-path player-name players-path)
          player-ns (or (@cast player-name) (load-player-from this player-path player-name))]
      (when player-ns
        (new-player player-name player-path player-ns)))))

(defn new-player-recruiter [scene]
  (PlayerRecruiter. scene (atom {})))

