(ns beam-diagnostic-system.util.formulas
  (:require [clojure.math.numeric-tower :as math]))
(def SINGLESUPPORT 2.0)
(def CANTILEVER 1.0)
(def ONECONTINUOUSEXTREME 3.0)
(def TWOCONTINUOUSEXTREME 4.0)
(def SINGLESUPPORTCOEFFICIENT 16)
(def CANTILEVERCOEFFICIENT 8)
(def ONECONTINUOUSEXTREMECOEFFICIENT 18.5)
(def TWOCONTINUOUSEXTREMECOEFFICIENT 21)
(def RESISTENCESHEARFACTOR 0.75)
(def RESISTENCEBINDINGFACTOR 0.9)
(defn calcularAlturaMinima [cantidadApoyo longitud]
  (cond
    (= cantidadApoyo CANTILEVER) (/ longitud CANTILEVERCOEFFICIENT)
    (= cantidadApoyo SINGLESUPPORT) (/ longitud SINGLESUPPORTCOEFFICIENT)
    (= cantidadApoyo ONECONTINUOUSEXTREME) (/ longitud ONECONTINUOUSEXTREMECOEFFICIENT)
    (>= cantidadApoyo TWOCONTINUOUSEXTREME) (/ longitud TWOCONTINUOUSEXTREMECOEFFICIENT)
  )
)
(defn calcularAlturaUtil [altura diamLongitudinal diamEstribos recubrimiento]
  (- altura (- (/ diamLongitudinal 2) (- diamEstribos recubrimiento)))
)
(defn calcularLimiteSecciónMinima [alturaUtil calidadAcero ancho]
  (* (/ 1.4 calidadAcero) (* ancho alturaUtil))
)
(defn calcularSeccionMinima [alturaUtil calidadAcero calidadHormigon ancho]
  (* (* (/ (math/sqrt calidadHormigon) (* calidadAcero 4.0)) ancho) alturaUtil)
)
(defn calcularCapacidadCorte [corteNominal]
  (* corteNominal RESISTENCESHEARFACTOR)
)
(defn calcularCapacidadFlexion [momentoNominal]
  (* momentoNominal RESISTENCEBINDINGFACTOR)
)
