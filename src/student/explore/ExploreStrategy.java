package student.explore;

import game.ExplorationState;

/**
 * Interface for approaches to the explore() phase.
 * Any approach for explore() should be done within a class which implements this interface. The
 * Explorer class will have a particular strategy selected, and will call it when needed.
 * This approach keeps algorithms developed separate from one another, and reduces the likelihood
 * of accidentally overwriting key pieces of other team-mates' work when resolving merge conflicts.
 */
public interface ExploreStrategy {
    /**
     * Explore the cavern, trying to find the orb in as few steps as possible.
     * Once you find the orb, you must return from the function in order to pick
     * it up. If you continue to move after finding the orb rather
     * than returning, it will not count.
     * @param state the information available at the current state
     */
    void explore(ExplorationState state);
}
