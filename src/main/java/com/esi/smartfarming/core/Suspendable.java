package com.esi.smartfarming.core;

/**
 * Interface fonctionnelle permettant à une entité d'être suspendue ou réactivée.
 * <p>
 * Implémentée par {@link com.esi.smartfarming.zone.Zone} et
 * {@link com.esi.smartfarming.capteur.Capteur}.
 */
public interface Suspendable {
    void suspendre();
    void activer();
}
