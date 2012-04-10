;- Copyright © 2008-2011 8th Light, Inc. All Rights Reserved.
;- Limelight and all included source files are distributed under terms of the MIT License.

(ns limelight.clojure.casting
  (:use
    [limelight.clojure.core]
    [limelight.clojure.player]
    [limelight.clojure.util :only (read-src)]))

(defn- cast-player [player-ns prop-panel]
  (let [event-actions @(ns-resolve player-ns '*event-actions*)
        event-handler (.getEventHandler prop-panel)]
    (if-let [on-cast-actions (:on-cast @event-actions)]
      (doseq [action on-cast-actions]
        (.invoke action (limelight.ui.events.panel.CastEvent. prop-panel))))
    (doseq [[event-class actions] @event-actions]
      (when (not (= :on-cast event-class))
        (doseq [action actions]
          (.add event-handler event-class action))))))

(deftype Player [_name _path _ns]
  limelight.model.api.Player
  (cast [this prop-panel]
    (cast-player _ns prop-panel))
  (getPath [this] _path)
  (getName [this] _name)
  (applyOptions [this prop-panel options] options)

  clojure.lang.Named

  limelight.clojure.core.Pathed
  (path [this] _path)
  )

(defn new-player [name path player-ns]
  (Player. name path player-ns))

(defn- player-path [player-name players-path]
  (str players-path "/" (limelight.util.StringUtil/snakeCase player-name) ".clj"))

; TODO MDM Player namespaces should be cleaned up... memory leak otherwise
(defn- load-player-from [recruiter player-path player-name]
  (let [player-content (.readTextFile (limelight.Context/fs) player-path)
        player-ns (create-ns (gensym (str "limelight.dynamic-player." player-name "-")))
        player (new-player player-name player-path player-ns)
        event-actions (intern player-ns '*event-actions* (atom {}))
        scene (._scene recruiter)
        production (production scene)]
    (binding [*ns* player-ns]
      (refer 'clojure.core)
      (refer 'limelight.clojure.player)
      (refer 'limelight.clojure.core)
      (when production (alias 'production (.getName @(._ns production))))
      (doseq [player (clj-players scene)] (alias (symbol (._name player)) (.getName (._ns player))))
      (binding [limelight.clojure.player/*action-cache* @event-actions]
        (read-src production player-path player-content)))
    (swap! (._cast recruiter) #(assoc % player-name player))
    player))

(deftype PlayerRecruiter [_scene _cast]
  limelight.model.api.PlayerRecruiter
  (canRecruit [this player-name players-path]
    (if (@_cast player-name)
      true
      (.exists (limelight.Context/fs) (player-path player-name players-path))))
  (recruitPlayer [this player-name players-path]
    (let [player-path (player-path player-name players-path)]
      (or (@_cast player-name) (load-player-from this player-path player-name)))))

(defn new-player-recruiter [scene]
  (PlayerRecruiter. scene (atom {})))

