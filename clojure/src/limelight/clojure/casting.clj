;- Copyright © 2008-2011 8th Light, Inc. All Rights Reserved.
;- Limelight and all included source files are distributed under terms of the MIT License.

(ns limelight.clojure.casting
  (:use
    [limelight.clojure.core]
    [limelight.clojure.player]
    [limelight.clojure.util :only (read-src)]))

; TODO MDM Player namespaces should be cleaned up... memory leak otherwise
(defn- load-player-from [recruiter player-path player-name]
  (let [player-content (.readTextFile (limelight.Context/fs) player-path)
        player-ns (create-ns (gensym (str "limelight.dynamic-player." player-name "-")))
        event-actions (intern player-ns '*event-actions* (atom {}))]
    (binding [*ns* player-ns]
      (refer 'clojure.core)
      (refer 'limelight.clojure.player)
      (refer 'limelight.clojure.core)
      (refer (.getName (._helper-ns (._scene recruiter))))
      (binding [limelight.clojure.player/*action-cache* @event-actions]
        (read-src player-path player-content)))
    (swap! (._cast recruiter) #(assoc % player-name player-ns))
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

(deftype Player [_name _path _player-ns]
  limelight.model.api.Player
  (cast [this prop-panel]
    (cast-player _player-ns prop-panel))
  (getPath [this] _path)
  (getName [this] _name)
  (applyOptions [this prop-panel options] options)

  clojure.lang.Named

  limelight.clojure.core.Pathed
  (path [this] _path)
  )

(defn new-player [name path player-ns]
  (Player. name path player-ns))

(deftype PlayerRecruiter [_scene _cast]
  limelight.model.api.PlayerRecruiter
  (canRecruit [this player-name players-path]
    (if (@_cast player-name)
      true
      (.exists (limelight.Context/fs) (player-path player-name players-path))))
  (recruitPlayer [this player-name players-path]
    (let [player-path (player-path player-name players-path)
          player-ns (or (@_cast player-name) (load-player-from this player-path player-name))]
      (when player-ns
        (new-player player-name player-path player-ns)))))

(defn new-player-recruiter [scene]
  (PlayerRecruiter. scene (atom {})))

