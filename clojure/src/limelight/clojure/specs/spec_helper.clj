(ns limelight.clojure.specs.spec-helper
  (:use
    [speclj.core]
    [limelight.clojure.core]))

(defn load-production [options]
  (limelight.Boot/reset)
  (limelight.Boot/boot {"start-background-threads" (or (:start-back-ground-threads options) false)})
  (set! (. limelight.ui.model.StageFrame hiddenMode) true)
  (let [production-path (or (:production-path options) ".")
        peer-prod (limelight.clojure.ClojureProduction. production-path)]
    (.open peer-prod {"open-default-scenes" false})
    (.getProxy peer-prod)))

(defn load-stage [production options]
  )

(defn load-scene [options]
  )

;(defn get-stage [production stage]
;  (.get (.getTheater production) stage))
;
;(defn get-scene [production stage scene-name]
;  (let [prod (if (= limelight.clojure.ClojureProduction (class production)) (.getProxy production) production)
;        scene (open-scene prod scene-name (.getName stage))]
;    (.getProxy scene)))

(defn with-limelight [& args]
  (let [options (apply hash-map args)]
    (list
      (with-all production (load-production options))
      (with stage (load-stage @production options))
      (with scene (load-scene @production @stage options)))))