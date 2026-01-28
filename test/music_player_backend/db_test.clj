(ns music-player-backend.db-test
  (:require [clojure.test :refer :all]
            [music-player-backend.db :as db]
            [clojure.java.shell :as sh]))

(deftest datatest
  (sh/sh "rm" "-f" "./db/database.db")
  (println "deleted database")
  (db/set-db)
  (println "set'd database")

  (db/crud :playlists
           :insert {:name "Geronima"
                    :userId 0})
  (db/crud :playlists
           :insert {:name "Alvaro"
                    :userId 0})
  (db/crud :playlists
           :update {:id 1
                    :name "Temitas"})
  (db/crud :songs
           :insert {:name "El rap de fernanfloo"})

  (db/crud :songs
           :insert {:name "El rubiuh - Minero"})

  (db/crud :songs
           :insert {:name "Cuerno de babu"})

  (db/crud :songs
           :insert {:name "Caballo homosexual de las montanas"})

  (db/crud :songs
           :insert {:name "Ratas congeladas"})

  (println (db/crud :playlists
                    :read))
  (println (db/crud :songs
                    :read))

  (db/update-playlist {:id 1
                       :new [{:songid 1
                              :tracknumber 1}
                             {:songid 2
                              :tracknumber 2}]})
  (db/update-playlist {:id 1
                       :new [{:songid 2
                              :tracknumber 2}]})

  (println "Connections:" (db/crud :connections :read)))
