(ns beam-diagnostic-system.util.definitions
  (:require [clojure.math.numeric-tower :as math]))

(def steelQuality "calidadAcero")
(def concreteQuality "calidadHormigon")
(def beamLength "longitud")
(def steelSection "areaAcero")
(def height "altura")
(def width "ancho")
(def supportPointAmount "cantidadPuntosApoyo")
(def lastBendingMoment "momentoFlectorUltimo")
(def nominalBendingMoment "momentoFlectorNominal")
(def lastShearTension "corteUltimo")
(def nominalShearTension "corteNominal")
(def longitudinalBarDiameter "diametroBarrasLongitudinales")
(def stirrupBarDiameter "diametroBarrasEstribos")
(def concreteCover "recubrimientoHormigon")
(def defaultBeamInputDataFile "src/beam_diagnostic_system/resources/beamDataFile.xlsx")
(def defaultSheet "Hoja1")
(def OKHEIGHT "verifica altura")
(def OKSECTIONLIMIT "verifica limite de sección")
(def OKSECTION "verifica sección")
(def OKSHEAR "verifica corte")
(def OKBINDING "verifica flexión")
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
(def cliOptions
  [
    ["-f" "--file" "REQUIRED: Beam 's data file name"
    :default defaultBeamInputDataFile]
    ["-h" "--help" "Show help"
    :flag true
    :default false]]
)
(defn inputBeamDataFile [arguments]
  (if (= (first arguments) nil) defaultBeamInputDataFile (first arguments))
)
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
