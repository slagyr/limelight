;- Copyright © 2008-2011 8th Light, Inc. All Rights Reserved.
;- Limelight and all included source files are distributed under terms of the MIT License.

(ns limelight.clojure.core)

(def *log* (java.util.logging.Logger/getLogger  "clojure"))

(defn log [& messages]
  (.info *log* (apply str messages)))

(defprotocol ResourceRoot
  (resource-path [this resource]))

;(defprotocol ProductionSource
;  (production [this]))

(defprotocol TheaterSource
  (theater [this]))

(defn production [prop]
  (.getProxy (.getProduction (.getRoot @(._peer prop)))))

(defn- as-proxies [peer-props]
  (map (fn [child] (.getProxy child)) peer-props))

(defn child-props [parent]
  (as-proxies (.getChildPropPanels @(._peer parent))))

(defn add-props [parent & children]
  (let [peer-prop @(._peer parent)
        children (flatten children)]
    (doseq [child children]
      (if child
        (.add peer-prop @(._peer child))))
    children))

(defn parent-prop [prop]
  (.getProxy (.getParent @(._peer prop))))

(defn scene-prop [prop]
  (.getProxy (.getRoot @(._peer prop))))

(defn find-prop [prop id]
  (if-let [peer-result (.find @(._peer (scene-prop prop)) id)]
    (.getProxy peer-result)
    nil))

(defn find-props-named [root name]
  (as-proxies (.findByName @(._peer root) name)))

(defn prop-id [prop]
  (.getId @(._peer prop)))

(defn prop-name [prop]
  (.getName @(._peer prop)))

(defn prop-text [prop]
  (.getText @(._peer prop)))

(defn prop-text= [prop value]
  (.setText @(._peer prop) value))

(defn open-scene
  ([production scene-name] (.openScene (._peer production) scene-name {}))
  ([production scene-name stage-or-options]
    (if (string? stage-or-options)
      (.openScene (._peer production) scene-name stage-or-options {})
      (.openScene (._peer production) scene-name stage-or-options)))
  ([production scene-name stage-name options] (.openScene (._peer production) scene-name stage-name options)))

(defn add-stage [theater stage]
  (.add (._peer theater) @(._peer stage)))

(defn create-stage [theater name & options]
  (let [options (apply hash-map options)
        stage-peer (.buildStage theater name options)
        stage (.getProxy stage-peer)]
    (add-stage theater stage)
    stage))

(defn get-stage [theater name]
  (if-let [peer (.get (._peer theater) name)]
    (.getProxy peer)
    nil))