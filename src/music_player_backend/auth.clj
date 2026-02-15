(ns music-player-backend.auth
  (:require [music-player-backend.db-mg :as db]
            [environ.core :refer [env]]
            [buddy.hashers :as hashers]
            [buddy.sign.jwt :as jwt])
  (:gen-class))

(defn insert-user [data]
  (let [hashed-data (assoc data :password (hashers/derive (:password data) {:alg :argon2id}))
        new-user (db/insert-data "users" hashed-data)]
    
    (println "Inserted user:" (:username hashed-data))
    (str (:_id new-user))))

(defn check-password [user-password db-password]
  (hashers/check user-password db-password))

(defn get-token [data]
  (jwt/sign data (env :secret-key)))

(defn register-user [data]
  (try
    (let [username (:username data)
          password (:password data)
          user-data (db/get-user username)]

      (if (nil? user-data)
        (insert-user {:username username
                      :password password})
        :already-exist))

    (catch Exception e
      (println "An error ocurred: " (.getMessage e))
      :user-error)))

(defn login-user [data]
  (try
    (let [username (:username data)
          password (:password data)
          user-data (db/get-user username)]

      (if (nil? user-data)
        :not-found
        (let [db-password (:password user-data)]

          (if (check-password password db-password)
            (get-token {:_id (str (:_id user-data))
                        :username username})
            :incorrect-password))))

    (catch Exception e
      (println "An error ocurred: " (.getMessage e))
      :user-error)))