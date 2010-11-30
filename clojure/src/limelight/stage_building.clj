(ns limelight.stage-building)

(declare *theater*)
(defn build-stages [theater input]
  (binding [*theater* theater
            *ns* (the-ns 'limelight.stage-building)]
    (load-string input))
  theater)

(defn stage [name options]
  (let [stage (.buildStage *theater* name options)]
    (-> *theater*
        (.peer)
        (.add stage))))
