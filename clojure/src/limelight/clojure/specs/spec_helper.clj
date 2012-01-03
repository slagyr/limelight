(ns limelight.clojure.specs.spec-helper
  (:use
    [speclj.core]
    [limelight.clojure.core :as ll :exclude (production stage scene)]
    [limelight.clojure.util :only (->options)]))

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

(defn load-scene [production stage options]
  (let [scene-path (:scene options)]
    (open-scene production scene-path (name stage))))

(defn with-limelight [& args]
  (let [options (merge default-options (->options args))]
    (list
      (with-all production (load-production options))
      (with stage (load-stage @production options))
      (with scene (load-scene @production @stage options))
      (after-all (doseq [window (java.awt.Window/getWindows)] (.dispose window))))))