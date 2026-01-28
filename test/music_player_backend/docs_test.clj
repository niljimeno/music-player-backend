(ns music-player-backend.docs-test
  (:require [clojure.test :refer :all]
            [music-player-backend.docs :as docs]))

(deftest docstest
  (docs/generate))
