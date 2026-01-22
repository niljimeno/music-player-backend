(ns music-player-backend.middleware
  (:require [buddy.sign.jwt :as jwt]
            [environ.core :refer [env]]
            [music-player-backend.json :as json]
            [music-player-backend.server :as server]))

(defn check-master-key [route req]
  (let [data (json/json-from-request req)
        master-key (env :master-key)
        user-key (:key data)]

    (println "Master key env:" master-key "| User key:" user-key)
    (if (= master-key user-key)
      (route data)
      (server/respond "Invalid key" :status 401))))


(defn check-token [route req]
  (let [data (json/json-from-request req)
        secret-key (env :secret-key)
        user-token (:token data)]

    (try
      ;; (jwt/unsign user-token secret-key)
      (route data)
      (catch Exception e
        (println "An error ocurred: " (.getMessage e))
        (server/respond "Invalid token" :status 401)))))
