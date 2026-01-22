(ns music-player-backend.db
  (:require [clojure.java.jdbc :as jbdc])
  (:gen-class))

(def db
  {:classname   "org.sqlite.JDBC"
   :subprotocol "sqlite"
   :subname     "db/database.db"})

(defn user-table []
  (println "Start user-table")
  (map #(apply jbdc/create-table-ddl
               (conj % {:conditional? true}))
       [[:users
         [[:id :int :primary :key]
          [:username :text :unique]
          [:password :text]]]

        [:playlists
         [[:id :int :primary :key]
          [:name :text :unique]
          [:userId :int]]]

        [:songs
         [[:id :int :primary :key]
          [:name :text]
          [:url :text]]]

        [:connections
         [[:id :int :primary :key]
          [:songId :int]
          [:playlistId :int]
          [:trackNumber :int]]]]))

(defn create-playlist
  [data]
  (jbdc/insert! db :playlists data))

(defn update-playlist
  ([data]
   (let [new-data (jbdc/query db ["SELECT * FROM playlists
                                  WHERE id = ?" (:id data)])]
     (if (empty? new-data)
       (create-playlist data)
       (update-playlist new-data data))))
  ([new-data data]
   (println "Exists")))

(defn create-db []
  (jbdc/execute! db ["PRAGMA foreign_keys = ON;"])
  (doall (map (partial jbdc/db-do-commands db)
              (user-table))))

(defn set-db []
  (println "Setting DB...")
  (create-db))
