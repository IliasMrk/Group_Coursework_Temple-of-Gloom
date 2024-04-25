package student.explore;

import student.StudentNode;

import java.util.*;

/**
 * Lightweight class to replicate Nodes in the explore phase.
 * The ExplorationState doesn't expose the Nodes publicly - it only provides NodeStatus, which
 * gives limited information regarding each Node. By replicating key functionality in this class,
 * it is possible to use algorithms such as Dijkstra / A* on the ExploreNodes, thus allowing for
 * efficient shortest-pathfinding.
 */
public class ExploreNode extends StudentNode<ExploreNode> {
    /**
     * The node's distance to the orb (needed to prioritise which node to visit next)
     */
    final private int distanceToOrb;

    /**
     * The neighbours of the node
     * (Note - if the node has not yet been visited, this may be incomplete)
     */
    final private Set<ExploreNode> neighbourNodes;

    /**
     * Constructs an ExploreNode using information which can be gleaned from NodeStatus
     * @param id the node's id (mirrors the id used in NodeStatus)
     * @param distanceToOrb the Manhattan distance from the node to the orb
     */
    public ExploreNode(long id, int distanceToOrb) {
        super(id);
        this.distanceToOrb = distanceToOrb;
        neighbourNodes = new HashSet<>();
    }

    /**
     * Add an edge between this ExploreNode and another
     * @param neighbour the neighbour of this ExploreNode
     */
    public void addEdge(ExploreNode neighbour) {
        this.neighbourNodes.add(neighbour);
        neighbour.neighbourNodes.add(this);
    }

    /**
     * Returns the Manhattan distance from the ExploreNode to the orb
     * @return the Manhattan distance to the orb
     */
    public int distanceToOrb() {
        return distanceToOrb;
    }

    /**
     * Returns the set of nodes which are known to neighbour this Node.
     * Note: If the ExploreNode has not yet been visited, this Set may be incomplete.
     * @return the known neighbours of this Node
     */
    @Override
    public Set<ExploreNode> neighbours() {
        return neighbourNodes;
    }

    /**
     * Gets the length from this ExploreNode to a given neighbour. Always returns 1, because there
     * are no edge weights in the explore phase.
     * @param neighbour the given neighbour
     * @return the length from this ExploreNode to a given neighbour (which is always 1)
     */
    @Override
    public int lengthTo(ExploreNode neighbour) {
        return 1;
    }
}
