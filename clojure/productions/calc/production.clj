(on-production-created [e]
  (println "created e: " e))

(on-production-loaded [e]
  (println "loaded e: " e))

(on-production-opened [e]
  (println "opened e: " e))

(on-production-closing[e]
  (println "closing e: " e))

(on-production-closed [e]
  (println "closed e: " e))