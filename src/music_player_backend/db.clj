(ns music-player-backend.db
  (:require [clojure.java.jdbc :as jdbc]
            [clojure.java.jdbc :as jbdc])
  (:gen-class))

(def db
  {:classname   "org.sqlite.JDBC"
   :subprotocol "sqlite"
   :subname     "db/database.db"})

(defn user-table []
  (println "Start user-table")
  (map #(apply jdbc/create-table-ddl
               (conj % {:conditional? true}))
       [[:users
         [[:id :integer :primary :key :autoincrement]
          [:username :text :unique]
          [:password :text]]]

        [:playlists
         [[:id :integer :primary :key :autoincrement]
          [:name :text :unique]
          [:userId :int]]]

        [:songs
         [[:id :integer :primary :key :autoincrement]
          [:name :text]
          [:url :text]]]

        [:connections
         [[:id :integer :primary :key :autoincrement]
          [:songId :integer]
          [:playlistId :integer]
          [:trackNumber :integer]]]]))

(defn crud-delete
  [table id]
  (case table
    :songs (jdbc/delete! db :connections ["songId = ?" id])
    :playlists (jdbc/delete! db :connections ["playlistId = ?" id]))
  (jdbc/delete! db table ["id = ?" id]))

(defn crud
  ([table action data]
   (case action
     :read (jbdc/query db (format "SELECT * FROM %s WHERE %s = %d"
                                  (name table)
                                  (name (first (:match data)))
                                  (second (:match data))))
     :insert (jdbc/insert! db table data)
     :update (jdbc/update! db table (:new data) ["id = ?" (:id data)])
     :delete (crud-delete table (:id data))))
  ([table action]
   (println action)
   (case action
     :read (jbdc/query db (str "SELECT * FROM " (name table))))))

(defn update-playlist
  [data]
  (let [id (:id data)
        new-songs (:content data)]))

;; create playlist: needs user id

(defn create-db []
  (jdbc/execute! db ["PRAGMA foreign_keys = ON;"])
  (doall (map (partial jdbc/db-do-commands db)
              (user-table))))

(defn set-db []
  (println "Setting DB...")
  (create-db))
