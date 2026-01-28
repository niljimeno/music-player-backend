(ns music-player-backend.playlist
  (:require [music-player-backend.server :as server]
            [music-player-backend.db :as db]
            [clojure.java.jdbc :as jdbc]))

(defn add-playlist [data]
  (try
    (let [result (db/crud :playlists
                          :insert data)]

      (println "Inserted playlist:" result)
      (server/respond result :status 201))

    (catch Exception e
      (println "An error ocurred:" (.getMessage e))
      (server/respond "Error inserting playlist" :status 400))))

(defn update-playlist [data]
  (try
    (let [playlist (jdbc/query db/db ["SELECT name FROM playlists
                                       WHERE userid = ? AND id = ?" (:userid data) (:id data)])]

      (if (empty? playlist)
        (server/respond "Playlist does not exist" :status 404)

        (let [result (db/crud :playlists
                              :update data)]
          (println "Updated playlist" playlist ": " result)
          (server/respond result :status 200))))

    (catch Exception e
      (println "An error ocurred:" (.getMessage e))
      (server/respond "Error updating playlist" :status 400))))

(defn delete-playlist [data]
  (try
    (let [playlist (jdbc/query db/db ["SELECT name FROM playlists
                                       WHERE userid = ? AND id = ?" (:userid data) (:id data)])]
      (if (empty? playlist)
        (server/respond "Playlist does not exists" :status 404)

        (let [result (db/crud :playlists
                              :delete (:id data))]
          (println "Removed playlist" playlist ": " result)
          (server/respond result :status 200))))

    (catch Exception e
      (println "An error ocurred:" (.getMessage e))
      (server/respond "Error deleting playlist" :status 400))))

(defn add-track [data]
  (try
    (let [song (jdbc/query db/db ["SELECT name FROM songs
                                   WHERE userid = ? AND url = ?" (:userid data) (:url data)])]

      (if (empty? song)
        (let [result (db/crud :songs
                              :insert data)]
          (println "Inserted song:" result)
          (server/respond result :status 200))

        (server/respond "Song already exists" :status 409)))

    (catch Exception e
      (println "An error ocurred:" (.getMessage e))
      (server/respond "Error Inserting song" :status 400))))

(defn update-track [data]
  (try
    (let [song (jdbc/query db/db ["SELECT name FROM songs
                                   WHERE userid = ? AND id = ?" (:userid data) (:id data)])]

      (if (empty? song)
        (server/respond "Song does not exist" :status 404)

        (let [result (db/crud :songs
                              :update data)]
          (println "Updated song" song ":" result)
          (server/respond result :status 200))))

    (catch Exception e
      (println "An error ocurred:" (.getMessage e))
      (server/respond "Error updating song" :status 400))))

(defn delete-track [data]
  (try
    (let [song (jdbc/query db/db ["SELECT name FROM songs
                                   WHERE userid = ? AND id = ?" (:userid data) (:id data)])]

      (if (empty? song)
        (server/respond "Song does not exist" :status 404)

        (let [result (db/crud :songs
                              :delete data)]
          (println "Deleted song:" result)
          (server/respond result :status 200))))

    (catch Exception e
      (println "An error ocurred:" (.getMessage e))
      (server/respond "Error deleting song" :status 400))))

(defn update-playlist-list [data]
  (try
    (let [playlist (jdbc/query db/db ["SELECT name FROM playlists
                                       WHERE userid = ? AND id = ?" (:userid data) (:id data)])]
  
      (if (empty? playlist)
        (server/respond "Playlist does not exist" :status 404)
  
        (let [result (db/update-playlist data)]
          (println "Updated playlist" playlist ":" result)
          (server/respond result :status 200))))
  
    (catch Exception e
      (println "An error ocurred:" (.getMessage e))
      (server/respond "Error updating playlist" :status 400))))