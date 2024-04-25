package student.escape;

import game.EscapeState;

/**
 * Interface for approaches to the escape() phase.
 * Any approach for escape() should be done within a class which implements this interface. The
 * Explorer class will have a particular strategy selected, and will call it when needed.
 * This approach keeps algorithms developed separate from one another, and reduces the likelihood
 * of accidentally overwriting key pieces of other team-mates' work when resolving merge conflicts.
 */
public interface EscapeStrategy {
    /**
     * Escape from the cavern before the ceiling collapses, trying to collect as much
     * gold as possible along the way. Your solution must ALWAYS escape before time runs
     * out, and this should be prioritized above collecting gold.
     * @param state the information available at the current state
     */
    void escape(EscapeState state);
}
