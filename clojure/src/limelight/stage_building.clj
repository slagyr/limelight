(ns limelight.stage-building
  (:use
    [limelight.util :only (read-src)]))

(declare *theater*)

(defn build-stages [theater src path]
  (binding [*theater* theater
            *ns* (the-ns 'limelight.stage-building)]
    (read-src path src))
  theater)

(defn stage [name & options]
  (let [options (if (and (= 1 (count options)) (map? (first options))) (first options) (apply hash-map options))
        stage (.buildStage *theater* name options)]
    (-> *theater*
        (.peer)
        (.add stage))))
