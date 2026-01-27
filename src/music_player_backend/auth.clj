(ns music-player-backend.auth
  (:require [clojure.java.jdbc :as jdbc]
            [music-player-backend.db :as db]
            [buddy.sign.jwt :as jwt]
            [environ.core :refer [env]]
            [buddy.hashers :as hashers])
  (:gen-class))

(defn insert-user [data]
  (let [hashed-data (assoc data :password (hashers/derive (:password data) {:alg :argon2id}))]
    (jdbc/insert! db/db :users hashed-data)
    (println "Inserted user:" (:username hashed-data) "| hashed-pass:" (:password hashed-data))
    :user-created))

(defn check-password [user-password db-password]
  (hashers/check user-password db-password))

(defn get-token [data]
  (jwt/sign data (env :secret-key)))

(defn register-user [data]
  (try
    (let [username (:username data)
          password (:password data)
          result (db/get-userid username)]
      
      (if (empty? result)
        (insert-user {:username username
                      :password password})
        :already-exist))

    (catch Exception e
      (println "An error ocurred: " (.getMessage e))
      :user-error)))

(defn login-user [data]
  (try
    (let [username (:username data)
          user-password (:password data)
          result (jdbc/query db/db ["SELECT password FROM users
                                     WHERE username = ?" username])]

      (if (empty? result)
        :not-found
        (let [db-password (:password (first result))]

          (if (check-password user-password db-password)
            (get-token {:username username})
            :incorrect-password))))

    (catch Exception e
      (println "An error ocurred: " (.getMessage e))
      :user-error)))