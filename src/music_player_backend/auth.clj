(ns music-player-backend.auth
  (:require [clojure.java.jdbc :as jbdc]
            [buddy.sign.jwt :as jwt]
            [environ.core :refer [env]]
            [buddy.hashers :as hashers])
  (:gen-class))

(def db
  {:classname   "org.sqlite.JDBC"
   :subprotocol "sqlite"
   :subname     "db/database.db"})

(defn insert-user [data]
  (let [hashed-data (assoc data :password (hashers/derive (:password data) {:alg :argon2id}))]
    (jbdc/insert! db :users hashed-data)
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
          result (jbdc/query db ["SELECT username FROM users 
                                  WHERE username = ?" username])]
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
          result (jbdc/query db ["SELECT password FROM users 
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