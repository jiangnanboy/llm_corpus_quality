
package com.sy.corpus_quality_process.deduplication.string_sim.interfaces;

/**
 * String distances that implement this interface are metrics.
 * This means:
 * - d(x, y) ≥ 0 (non-negativity, or separation axiom)
 * - d(x, y) = 0 if and only if x = y (identity, or coincidence axiom)
 * - d(x, y) = d(y, x) (symmetry)
 * - d(x, z) ≤ d(x, y) + d(y, z) (triangle inequality).
 *
 */
public interface MetricStringDistance extends StringDistance {

    /**
     * Compute and return the metric distance.
     * @param s1
     * @param s2
     * @return
     */
    @Override
    double distance(String s1, String s2);
}
