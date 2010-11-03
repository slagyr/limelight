(ns limelight.common)

(defmulti path-to (fn [context & args] (class context)))

(defn production [scene]
  (.getProxy (.getProduction @(.peer scene))))
