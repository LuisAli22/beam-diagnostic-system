(ns beam-diagnostic-system.rules.verificationRules
  (:use [beam-diagnostic-system.util.definitions]
        [beam-diagnostic-system.util.formulas])
  (:require [clara.rules :refer :all])
)

(defrecord Altura [verdadera minima])
(defrecord Seccion [verdadera minima limite])
(defrecord Corte [capacidad solicitacion])
(defrecord Flexion [capacidad solicitacion])
(defrecord Verification [name])
(defrule RVerificaAltura
  "Se debe verificar si la viga cumple con la altura mínima"
  [Altura (>= verdadera minima)]
  =>
  (insert! (->Verification
            OKHEIGHT))
)
(defrule RVerifLimiteSeccion
  [Seccion (>= minima limite)]
  =>
  (insert! (->Verification
            OKSECTIONLIMIT))
)
(defrule RVerifSeccion
  [Seccion (>= verdadera minima)]
  =>
  (insert! (->Verification
            OKSECTION))
)
(defrule RVerifCorte
  [Corte (>= (/ capacidad solicitacion) 1.0)]
  =>
  (insert! (->Verification
            OKSHEAR))
)
(defrule RVerifFlexion
  [Flexion (>= (/ capacidad solicitacion) 1.0)]
  =>
  (insert! (->Verification
            OKBINDING))
)
(defquery checkValidation
  "Busco los resultados de las verificaciones realizadas"
  []
  [?verificationItem <- Verification]
)
(defn printValidation!
  "Imprime el resultado de las verificaciones realllizadas en esta sesión"
  [session]
  (doseq [result (query session checkValidation)]
    (println "Verification item: "
            (get-in result [:?verificationItem :name])))
)
(defn checkDesignRequirements [beamDataMap]
  (def alturaUtil
    (calcularAlturaUtil (beamDataMap height)
                (beamDataMap longitudinalBarDiameter)
                (beamDataMap stirrupBarDiameter)
                (beamDataMap concreteCover))
  )
  (def alturaMinima
    (calcularAlturaMinima (beamDataMap supportPointAmount)
                          (beamDataMap beamLength))
  )
  (def limiteSeccion
    (calcularLimiteSecciónMinima  alturaUtil
                                  (beamDataMap steelQuality)
                                  (beamDataMap width))
  )
  (def seccionMinima
    (calcularSeccionMinima  alturaUtil
                            (beamDataMap steelQuality)
                            (beamDataMap concreteQuality)
                            (beamDataMap width))
  )
  (def capacidadCorte
    (calcularCapacidadCorte (beamDataMap nominalShearTension))
  )
  (def capacidadFlexion
    (calcularCapacidadFlexion (beamDataMap nominalBendingMoment))
  )
  (-> (mk-session 'beam-diagnostic-system.rules.verificationRules)
      (insert (->Altura (beamDataMap height)
                        alturaMinima)
              (->Seccion  (beamDataMap steelSection)
                          seccionMinima
                          limiteSeccion)
              (->Corte  capacidadCorte
                        (beamDataMap lastShearTension))
              (->Flexion  capacidadFlexion
                          (beamDataMap lastBendingMoment))
    )
    (fire-rules)
    (printValidation!)
  )
)
