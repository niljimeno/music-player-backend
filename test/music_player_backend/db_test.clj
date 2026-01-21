(ns music-player-backend.db-test
  (:require [clojure.test :refer :all]
            [music-player-backend.db :as db]))

(deftest datatest
  (db/set-db))
