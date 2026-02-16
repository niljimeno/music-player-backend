(ns music-player-backend.db-mg
  (:require [monger.core :as mg]
            [monger.collection :as mc]
            [environ.core :refer [env]])
  (:import org.bson.types.ObjectId))

(let [logger (java.util.logging.Logger/getLogger "org.mongodb.driver")]
  (.setLevel logger java.util.logging.Level/WARNING))

(def conn-map (mg/connect-via-uri (env :monger-uri)))
(def db   (:db conn-map))

(defn get-user [username]
  (mc/find-one-as-map db "users" {:username username}))

(defn get-playlist [user-id playlist-id]
  (mc/find-one-as-map db "playlists" {:user_id user-id
                                      :_id (ObjectId. playlist-id)}))

(defn get-track [user-id track-id]
  (mc/find-one-as-map db "tracks" {:user_id user-id
                                   :_id (ObjectId. track-id)}))

(defn get-track-by-url [user-id url]
  (mc/find-one-as-map db "tracks" {:user_id user-id
                                   :url url}))

(defn insert-data [table data]
  (mc/insert-and-return db table data))

(defn update-data [table id data]
  (mc/update-by-id db table (ObjectId. id) {:$set data}))

(defn remove-data [table id]
  (mc/remove-by-id db table (ObjectId. id)))