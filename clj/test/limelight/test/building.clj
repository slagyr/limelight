(ns limelight.test.building
  (:use
    [limelight.building]
    [limelight.production :only (new-production)]
    [lazytest.describe :only (describe testing it with given before using)]
    [lazytest.context :only (fn-context)]
    [lazytest.context.stateful :only (stateful-fn-context)]))

;(def theater-context
;  (stateful-fn-context
;    (fn [] (let [peer-production (limelight.model.MockProduction. "Mock Production")
;                 production (new-production peer-production)
;                 theater @(.theater production)]
;      theater))
;    (fn [theater])))

(defn new-theater []
  (let [peer-production (limelight.model.MockProduction. "Mock Production")
        production (new-production peer-production)
        theater @(.theater production)]
    theater))

(describe "Stage building"
  (given [theater (new-theater)
          result (build-stages theater "(println *ns*)(stage \"One\" {})")]
    (it "make one stage" (= 1 (count (.getStages (.peer theater))))))
  (given [theater (new-theater)
          result (build-stages theater "(println *ns*)(stage \"One\" {:title \"Super Stage\"})")]
    (it "applies options" (= "Super Stage" (-> theater
                                               (.peer)
                                               (.get "One")
                                               (.getTitle)))))
  (given [theater (new-theater)
          result (build-stages theater "(println *ns*)(stage \"One\" {:title \"Super Stage\"})(stage \"Two\" {})")]
    (it "makes two stages" (= 2 (count (.getStages (.peer theater)))))))