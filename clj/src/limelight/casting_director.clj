(ns limelight.casting-director
  (:use
    [limelight.common]
    [limelight.player]))

(defn- load-player-from [casting-director player-path player-name]
  (if-not (.exists (limelight.Context/fs) player-path)
    nil
    (let [player-content (.readTextFile (limelight.Context/fs) player-path)
          player-ns (create-ns (gensym (str "limelight.dynamic-player." player-name "-")))
          event-actions (intern player-ns '*event-actions* (atom {}))]
      (binding [*ns* player-ns]
        (use 'clojure.core)
        (use 'limelight.player)
        (binding [limelight.player/*action-cache* @event-actions]
          (load-string player-content)))
      (swap! (.cast casting-director) #(assoc % player-name player-ns))
      player-ns)))

(defn- player-path [resource-root relative-player-path]
  (resource-path resource-root relative-player-path))

(defn- load-player [casting-director player-name]
  (if-let [player (@(.cast casting-director) player-name)]
    player
    (let [relative-player-path (str "players/" (limelight.util.StringUtil/underscore player-name) ".clj")]
      (if-let [player (load-player-from casting-director (player-path (.scene casting-director) relative-player-path) player-name)]
        player
        (load-player-from casting-director (player-path (production (.scene casting-director)) relative-player-path) player-name)))))

(defn- cast-player [player-ns prop]
  (let [event-actions @(ns-resolve player-ns '*event-actions*)
        event-handler (.getEventHandler @(.peer prop))]
    (doseq [[event-class actions] @event-actions]
      (doseq [action actions]
        (.add event-handler event-class action)))))

(deftype CastingDirector [scene cast]
  limelight.model.api.CastingDirector
  (castPlayer [this prop player-name]
    (if-let [player (load-player this player-name)]
      (cast-player player prop))))

(defn new-casting-director [scene]
  (CastingDirector. scene (atom {})))
