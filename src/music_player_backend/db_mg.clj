(ns music-player-backend.db-mg
  (:require [monger.core :as mg]
            [monger.collection :as mc]
            [environ.core :refer [env]]))

(let [logger (java.util.logging.Logger/getLogger "org.mongodb.driver")]
  (.setLevel logger java.util.logging.Level/WARNING))

(def conn-map (mg/connect-via-uri (env :monger-uri)))
(def db   (:db conn-map))

(defn get-user [username]
  (mc/find-one-as-map db "users" {:username username}))

(defn insert-data [table data]
  (mc/insert-and-return db table data))