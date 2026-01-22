(ns music-player-backend.middleware
  (:require [buddy.sign.jwt :as jwt]
            [clojure.string :as string]
            [environ.core :refer [env]]
            [music-player-backend.server :as server]))

(defn check-master-key [route req]
  (let [master-key (env :master-key)
        user-key (:authorization (:headers req))]

    (println "Master key env:" master-key "| User key:" user-key)
    (if (= master-key user-key)
      (route req)
      (server/respond "Invalid key" :status 401))))

(defn check-token [route req]
  (let [secret-key (env :secret-key)
        auth-token (:authorization (:headers req))]

    (if (empty? auth-token)
      (server/respond "Missing token" :status 401)
      (let [user-token (string/trim (string/replace auth-token #"Bearer " ""))
            valid-token (try
                          (jwt/unsign user-token secret-key)
                          true
                          (catch Exception e
                            (println "An error ocurred:" (.getMessage e))
                            false))]
        (if valid-token
          (route req)
          (server/respond "Invalid token" :status 401))))))