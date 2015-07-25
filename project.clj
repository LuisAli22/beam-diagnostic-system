(defproject beam-diagnostic-system "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.6.0"] 
		[dk.ative/docjure "1.8.0"]
		[org.clojure/tools.cli "0.3.1"]
		[org.toomuchcode/clara-rules "0.8.8"]
		[org.clojure/math.numeric-tower "0.0.4"]]
  :main ^:skip-aot beam-diagnostic-system.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
