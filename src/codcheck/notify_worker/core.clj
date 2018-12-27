(ns codcheck.notify-worker.core
  (:require
   [langohr.consumers]
   [codcheck.notify-worker handler]
   [codcheck logging rmq]
   [taoensso.timbre :as timbre])
  (:gen-class))

(defn- setup []
  (codcheck.logging/setup-logging "codcheck-notify-worker")

  (timbre/info "Connecting to RabbitMQ Cluster...")
  (codcheck.rmq/connect!)
  (codcheck.rmq/open-chan!)
  (timbre/info "Connection established" @codcheck.rmq/conn "with chan" @codcheck.rmq/chan))

(defn -main
  [& args]
  (setup)
  (langohr.consumers/subscribe @codcheck.rmq/chan
                               (:pr-code-checked codcheck.rmq/queues)
                               codcheck.notify-worker.handler/on-checked-pr
                               {:auto-ack true}))
