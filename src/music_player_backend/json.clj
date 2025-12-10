(ns music-player-backend.json
  (:require [clojure.data.json :as json]))

(defn get-value
  "Get value from a json string"
  [value str]
  (value (json/read-str str :key-fn keyword)))

(defn value-from-request
  "Retrieve json from a request and get a value out of it"
  [value req]
  (get-value value (slurp (:body req))))
