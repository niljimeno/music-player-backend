(ns music-player-backend.playlist
  (:require [music-player-backend.server :as server]
            [music-player-backend.db :as db]
            [clojure.java.jdbc :as jdbc]))

(defn add-playlist [data]
  (try
    (let [result (db/crud :playlists
                          :insert data)
          id (-> result first first second str)]

      (println "Playlist added - id:" id)
      (server/respond id :status 201))

    (catch Exception e
      (println "Error adding playlist:" (.getMessage e))
      (server/respond "Error adding playlist" :status 400))))

(defn update-playlist [data]
  (try
    (let [playlist (jdbc/query db/db ["SELECT name FROM playlists
                                       WHERE userid = ? AND id = ?" (:userid data) (:id data)])]

      (if (empty? playlist)
        (server/respond "Playlist does not exist" :status 404)

        (let [result (db/crud :playlists
                              :update data)]
          (println "Updated playlist" playlist "->" (:name data) "-" result)
          (server/respond "Playlist updated" :status 200))))

    (catch Exception e
      (println "Error updating playlist:" (.getMessage e))
      (server/respond "Error updating playlist" :status 400))))

(defn delete-playlist [data]
  (try
    (let [playlist (jdbc/query db/db ["SELECT name FROM playlists
                                       WHERE userid = ? AND id = ?" (:userid data) (:id data)])]
      (if (empty? playlist)
        (server/respond "Playlist does not exists" :status 404)

        (let [result (db/crud :playlists
                              :delete (:id data))]
          (println "Removed playlist" playlist "-" result)
          (server/respond "Playlist removed" :status 200))))

    (catch Exception e
      (println "Error removing playlist:" (.getMessage e))
      (server/respond "Error removing playlist" :status 400))))

(defn add-track [data]
  (try
    (let [song (jdbc/query db/db ["SELECT name FROM songs
                                   WHERE userid = ? AND url = ?" (:userid data) (:url data)])]

      (if (empty? song)
        (let [result (db/crud :songs
                              :insert data)
              id (-> result first first second str)]
          (println "Track added - id:" id)
          (server/respond id :status 200))

        (server/respond "Track already exists" :status 409)))

    (catch Exception e
      (println "Error adding track:" (.getMessage e))
      (server/respond "Error adding track" :status 400))))

(defn update-track [data]
  (try
    (let [song (jdbc/query db/db ["SELECT name FROM songs
                                   WHERE userid = ? AND id = ?" (:userid data) (:id data)])]

      (if (empty? song)
        (server/respond "Track does not exist" :status 404)

        (let [result (db/crud :songs
                              :update data)]
          (println "Updated track" song "->" (:name data) "-" result)
          (server/respond "Track updated" :status 200))))

    (catch Exception e
      (println "Error updating track:" (.getMessage e))
      (server/respond "Error updating track" :status 400))))

(defn delete-track [data]
  (try
    (let [song (jdbc/query db/db ["SELECT name FROM songs
                                   WHERE userid = ? AND id = ?" (:userid data) (:id data)])]

      (if (empty? song)
        (server/respond "Track does not exist" :status 404)

        (let [result (db/crud :songs
                              :delete data)]
          (println "Removed track:" song "-" result)
          (server/respond "Track removed:" :status 200))))

    (catch Exception e
      (println "Error removed track:" (.getMessage e))
      (server/respond "Error removed track" :status 400))))

(defn set-playlist-map [index songid]
  {:songid songid
   :tracknumber (+ index 1)})

(defn update-playlist-list [data]
  (try
    (let [playlist (jdbc/query db/db ["SELECT name FROM playlists
                                       WHERE userid = ? AND id = ?" (:userid data) (:id data)])]

      (if (empty? playlist)
        (server/respond "Playlist does not exist" :status 404)

        (let [data (assoc data :new (map-indexed set-playlist-map (:new data)))
              result (db/update-playlist data)]

          (println "Updated playlist" playlist "-" result)
          (server/respond "Updated playlist tracks" :status 200))))

    (catch Exception e
      (println "Error updating playlist tracks:" (.getMessage e))
      (server/respond "Error updating playlist tracks" :status 400))))