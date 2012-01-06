(ns limelight.clojure.specs.spec-helper
  (:use
    [speclj.core]
    [limelight.clojure.core :as ll :exclude (production stage scene)]
    [limelight.clojure.util :only (->options)]
    [limelight.clojure.prop-building :only (build-props)]
    [limelight.clojure.scene :only (new-scene)]))

(def default-options
  {:start-background-threads false
   :environment "test"
   :production "."})

(defn load-production [options]
  (limelight.Boot/reset)
  (limelight.Boot/boot (limelight.util.Opts. options))
  (set! (. limelight.ui.model.StageFrame hiddenMode) true)
  (let [production-path (:production options)
        peer-prod (limelight.clojure.ClojureProduction. production-path)]
    (.open peer-prod {"open-default-scenes" false})
    (.getProxy peer-prod)))

(defn load-stage [production options]
  (let [stage-name (or (:stage options) (name (default-stage (theater production))))
        result (get-stage (theater production) stage-name)]
    (if result
      result
      (throw (limelight.LimelightException. (str "No stage found named: " stage-name))))))

(defn- load-regular-scene [production stage options]
  (let [scene-options (or (:scene-options options) {})
        scene-options (update-in scene-options [:prop-params] merge (:prop-params options))]
    (open-scene production (:scene options) (name stage) scene-options)))

(defn- load-ghost-scene [production stage options]
  (let [scene-name (or (:scene-name options) (.filename (limelight.Context/fs) (:scene-path options)))
        scene-options (or (:scene-options options) {})
        scene-options (assoc scene-options :production production :path (:scene-path options) :name scene-name)
        scene (new-scene scene-options)]
    (.styleAndStageScene (peer production) (peer scene) (peer stage))
    scene))

(defn- build-scene [production stage options]
  (cond
    (:scene options) (load-regular-scene production stage options)
    (:scene-path options) (load-ghost-scene production stage options)
    :else (throw (limelight.LimelightException. "You must provide either :scene or :scene-path"))))

(defn load-scene [production stage options]
  (let [scene (build-scene production stage options)]
    (when (:props options)
      (let [prop-params (or (:prop-params options) {})
            prop-params (assoc prop-params :root-path (path production))]
        (build-props scene (:props options) (path scene) prop-params)))
    scene))

(defmacro with-limelight [& args]
  `(list
     (with-all ~'production (load-production (merge default-options (->options [~@args]))))
     (with ~'stage (load-stage @~'production (merge default-options (->options [~@args]))))
     (with ~'scene (load-scene @~'production @~'stage (merge default-options (->options [~@args]))))
     (after (.setScene (peer @~'stage) nil))
     (after-all (.shutdown (limelight.Context/instance)))))

