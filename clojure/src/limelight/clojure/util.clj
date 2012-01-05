;- Copyright © 2008-2011 8th Light, Inc. All Rights Reserved.
;- Limelight and all included source files are distributed under terms of the MIT License.

(ns limelight.clojure.util
  (:import
    [limelight.clojure ProxyMap]))

(defn read-src [src-path src-content]
  (let [rdr (java.io.StringReader. src-content)
        parent-path (.parentPath (limelight.Context/fs) src-path)
        src-filename (.filename (limelight.Context/fs) src-path)]
    (try
      (clojure.lang.Compiler/load rdr parent-path src-filename)
      (catch java.lang.Exception e
        (if-let [cause (.getCause e)]
          (throw (limelight.LimelightException. (str "Failed to read src: " src-path "\n\t<- " (.getMessage e)) cause))
          (throw (limelight.LimelightException. (str "Failed to read src: " src-path) e)))))))

(defn map-for-clojure [the-map]
  (cond
    (nil? the-map) {}
    (= ProxyMap (class the-map)) the-map
    (isa? (class the-map) java.util.HashMap) (ProxyMap. the-map)
    :else the-map))

(defn ->options [options]
  (if (map? (first options))
    (merge (first options) (apply hash-map (rest options)))
    (apply hash-map options)))

(defn ->java-map [hash]
  (let [jmap (.getMap (ProxyMap.))]
    (doseq [[key value] hash]
      (if (keyword? key)
        (.put jmap (name key) value)
        (.put jmap (str key) value)))
    jmap))

(defn map-for-java [the-map]
  (cond
    (nil? the-map) (.getMap (ProxyMap.))
    (= ProxyMap (class the-map)) (.getMap the-map)
    (isa? (class the-map) clojure.lang.IPersistentMap) (limelight.util.Opts. the-map)
    :else the-map))













