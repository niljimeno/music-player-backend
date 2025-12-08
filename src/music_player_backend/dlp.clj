(ns music-player-backend.dlp
  (:require [clojure.java.shell :refer [sh]]))

(defn run-command []
  (:out (sh "ls" "-la")))
