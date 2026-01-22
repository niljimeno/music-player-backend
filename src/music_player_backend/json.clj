(ns music-player-backend.json
  (:require [clojure.data.json :as json]))

(defn json-from-request
  "Retrieve json from a request"
  [req]
  (-> (:body req)
      slurp
      (json/read-str :key-fn keyword)))
