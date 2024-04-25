package student.explore;

import game.NodeStatus;

import java.util.*;

/**
 * Class holding a graph made up of ExploreNodes. Used in pathfinding for the
 * BasicFindBestNodeExplore strategy.
 */
public class ExploreGraph {
    /**
     * All known ExploreNodes so far. Held in a Map so that, given an id, an ExploreNode can be
     * identified.
     */
    final private Map<Long, ExploreNode> knownNodes;

    /**
     * Set of ExploreNodes which have been seen, but not yet visited
     */
    final private Set<ExploreNode> unvisitedNodes;

    /**
     * The id of the player's starting point
     */
    private long firstNode;

    /**
     * Basic constructor, initialising the knownNodes HashMap and unvisitedNodes HashSet.
     */
    public ExploreGraph() {
        this.knownNodes = new HashMap<>();
        this.unvisitedNodes = new HashSet<>();
    }

    /**
     * Sets the id of firstNode (i.e. where the player starts on the grid).
     * @param id the id of the player's starting Node
     */
    public void setFirstNode(long id) {
        firstNode = id;
    }

    /**
     * Adds an ExploreNode with the given id and distanceToOrb to the graph (if it is not already
     * known). Returns the ExploreNode with the given id.
     * @param id the id of the ExploreNode to add to the graph
     * @param distanceToOrb the distance of the ExploreNode to the orb
     * @return the ExploreNode with the given id
     */
    public ExploreNode addNodeToGraph(long id, int distanceToOrb) {
        // If ExploreNode is already known, simply return it:
        if (knownNodes.containsKey(id))
            return knownNodes.get(id);

        // Otherwise, create a new ExploreNode, add it to the graph, and return it:
        ExploreNode newNode = new ExploreNode(id, distanceToOrb);
        knownNodes.put(id, newNode);
        if (id != firstNode) unvisitedNodes.add(newNode);
        return newNode;
    }

    /**
     * Add a Collection of neighbours to an ExploreNode
     * @param baseNode the ExploreNode to which the neighbours should be added
     * @param neighbours the neighbours to add to baseNode
     */
    public void addNeighbours(ExploreNode baseNode, Collection<NodeStatus> neighbours) {
        for (NodeStatus neighbour : neighbours) {
            ExploreNode neighbourNode = addNodeToGraph(neighbour.nodeID(),
                    neighbour.distanceToTarget());
            baseNode.addEdge(neighbourNode);
        }
    }

    /**
     * Method for adding a Node id to the set of visitedNodes. Removes the related ExploreNode from
     * unvisitedNodes.
     * @param id the id of the Node to visit
     */
    public void visitNode(long id) {
        ExploreNode visitedNode = getNodeFromID(id);
        unvisitedNodes.removeIf(node -> node.equals(visitedNode));
    }

    /**
     * Get the Node with the corresponding ID
     * @param id the ID of the desired Node
     * @return the Node which has the provided ID
     */
    public ExploreNode getNodeFromID(long id) {
        return knownNodes.get(id);
    }

    /**
     * Gets the Set of ExploreNodes which have been seen, but not yet visited.
     * @return the Set of ExploreNodes which have been seen, but not yet visited
     */
    public Set<ExploreNode> unvisitedNodes() {
        return unvisitedNodes;
    }
}
