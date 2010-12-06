(ns limelight.production
  (:use
    [limelight.common]
    [limelight.theater]
    [limelight.stage-building :only (build-stages)]
    [limelight.prop-building :only (build-props)]
    [limelight.style-building :only (build-styles)]
    [limelight.scene :only (new-scene)]
    [limelight.util :only (read-src)])
  (:require
    [limelight.production-player])
  (:import [limelight.theater Theater]))

(deftype Production [peer theater ns]

  limelight.model.api.ProductionProxy
  (callMethod [this name args] nil)
  (getTheater [this] @theater)

  (illuminate [this]
    (let [player-path (resource-path this "production.clj")
          fs (limelight.Context/fs)
          player-src (if (.exists fs player-path) (.readTextFile fs player-path) nil)]
      (when player-src
        (binding [*ns* ns
                  limelight.production-player/*production* this]
          (use 'clojure.core)
          (use 'limelight.production-player)
          (read-src player-path player-src)))))

  (loadLibraries [this])

  (loadStages [this]
    (let [stages-path (resource-path this "stages.clj")
          fs (limelight.Context/fs)
          stages-src (if (.exists fs stages-path) (.readTextFile fs stages-path) nil)]
      (when stages-src
        (build-stages @theater stages-src stages-path))))

  (loadScene [this scene-path options]
    (let [full-scene-path (.pathTo (.getResourceLoader peer) scene-path)
          scene-name (limelight.io.FileUtil/filename scene-path)
          _ (.put options "path" full-scene-path)
          _ (.put options "name" scene-name)
          scene (new-scene options)]
      (.setProduction @(.peer scene) peer)
      (build-props scene (.readText (.getResourceLoader @(.peer scene)) "props.clj"))
      scene))

  (loadStyles [this scene]
    (let [styles-path (resource-path scene "styles.clj")
          fs (limelight.Context/fs)
          styles-src (if (.exists fs styles-path) (.readTextFile fs styles-path) nil)
          new-styles (if styles-src (build-styles {} styles-src styles-path) {})
          scene-styles (.getStylesStore @(.peer scene))]
      (doall
        (map (fn [[name value]] (.put scene-styles name value))
          new-styles))))

  ResourceRoot
  (resource-path [this resource]
    (.pathTo (.getResourceLoader (.peer this)) resource)))

(defn new-production [peer]
  (let [ns (create-ns (gensym "limelight.dynamic-player.production-"))
        production (Production. peer (atom nil) ns)]
    (swap! (.theater production) (fn [old] (Theater. (.getTheater peer) production)))
    (.setProxy peer production)
    production))
