(ns music-player-backend.router
  (:require [music-player-backend.docs :as docs]
            [music-player-backend.server :as server]
            [music-player-backend.middleware :as middleware]
            [music-player-backend.routes.song :as route-song]
            [music-player-backend.routes.login :as route-login]
            [music-player-backend.routes.track :as route-track]
            [music-player-backend.routes.search :as route-search]
            [music-player-backend.routes.register :as route-register]
            [music-player-backend.routes.playlist :as route-playlist]
            [music-player-backend.routes.backup :as route-backup]
            [music-player-backend.routes.not-found :as route-not-found]))

(defn handler
  "Handle http requests and send them to the correct route function"
  [req]
  (if (= (:request-method req) :options)
    (server/respond "" :status 204)
    (let [uri (:uri req)]
      (println uri)
      (case uri
        "/register" (middleware/check-master-key route-register/route req)
        "/login" (route-login/route req)
        "/song" (middleware/check-token route-song/route req)
        "/search" (middleware/check-token route-search/route req)
        "/track" (middleware/check-token route-track/route req)
        "/playlist" (middleware/check-token route-playlist/route req)
        "/backup" (middleware/check-token route-backup/route req)
        "/playlist/tracks" (middleware/check-token route-playlist/route req)
        "/docs/swagger.json" (docs/route)
        (if (.startsWith uri "/docs")
          (server/serve-static uri)
          (route-not-found/route))))))

