(ns music-player-backend.json
  (:require [clojure.data.json :as json]))


(defn get-value [value str]
  (value (json/read-str str :key-fn keyword)))

(defn value-from-request [value req]
  (get-value value (slurp (:body req))))
