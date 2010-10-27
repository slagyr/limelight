(ns limelight.stage-building-test
  (:use
    [limelight.stage-building]
    [limelight.production :only (new-production)]
    [lazytest.describe :only (describe testing it with given before using)]
    [lazytest.context :only (fn-context)]
    [lazytest.context.stateful :only (stateful-fn-context)]))

(defn new-theater []
  (let [peer-production (limelight.model.FakeProduction. "Mock Production")
        production (new-production peer-production)
        theater @(.theater production)]
    theater))

(describe "Stage building"
  (given [theater (new-theater)
          result (build-stages theater "(stage \"One\" {})")]
    (it "make one stage" (= 1 (count (.getStages (.peer theater))))))
  (given [theater (new-theater)
          result (build-stages theater "(stage \"One\" {:title \"Super Stage\"})")]
    (it "applies options" (= "Super Stage" (-> theater
                                               (.peer)
                                               (.get "One")
                                               (.getTitle)))))
  (given [theater (new-theater)
          result (build-stages theater "(stage \"One\" {:title \"Super Stage\"})(stage \"Two\" {})")]
    (it "makes two stages" (= 2 (count (.getStages (.peer theater)))))))