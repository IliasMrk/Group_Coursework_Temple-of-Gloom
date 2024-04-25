package student.explore;

import game.ExplorationState;
import game.NodeStatus;
import student.PathFinder;

import java.util.*;

/**
 * Explore strategy based on always looking for the best Node to visit next, and taking the shortest
 * path to get there.
 * "Best" Node is currently selected by choosing the Node with the lowest combined estimated
 * distance from the orb + actual distance from current location.
 * <br />
 * 500 headless runs on 26/3/24:
 * Average bonus: 1.235
 * % of bonuses at minimum: 3.80%
 */
public class BasicFindBestNodeExplore implements ExploreStrategy {
    /**
     * ExploreGraph, to keep track of the ExploreNodes discovered so far
     */
    private ExploreGraph exploreGraph;

    /**
     * Method to explore the cavern and grab the orb
     * @param state what is in the current state
     */
    @Override
    public void explore(ExplorationState state) {
        // Set up the ExploreGraph
        exploreGraph = new ExploreGraph();
        exploreGraph.setFirstNode(state.getCurrentLocation());

        // Keep iterating until the orb has been reached
        while (state.getDistanceToTarget() > 0) {
            // Update the ExploreGraph's knowledge of the grid, with the current node and its
            // neighbours:
            Collection<NodeStatus> neighbours = state.getNeighbours();
            ExploreNode currNode = exploreGraph.addNodeToGraph(
                    state.getCurrentLocation(), state.getDistanceToTarget()
            );
            exploreGraph.addNeighbours(currNode, neighbours);

            // Get the path to the best unvisited node:
            List<ExploreNode> bestPath = getPathToNextBestNode(currNode);

            // Follow the path to the next best unvisited node.
            // Start from the second node in the path to avoid revisiting the current node.
            bestPath.subList(1, bestPath.size()).forEach(
                    node -> makeExploreMove(state, node.id())
            );
        }
    }

    /**
     * Gets the path to the next best as-yet unvisited node. Uses the heuristic in
     * <code>getHeuristicForBestNextNode()</code> to determine which node is "best".
     * @param currNode where the player currently is
     * @return a List of ExploreNodes, representing the shortest path to the best node to visit next
     */
    private List<ExploreNode> getPathToNextBestNode(ExploreNode currNode) {
        // Find the paths to each of the unvisited Nodes, and sort according to the heuristic:
        List<List<ExploreNode>> pathsToUnvisitedNodes = exploreGraph.unvisitedNodes().stream()
                .map(unvisited -> PathFinder.findShortestPath(currNode, unvisited))
                .sorted(this::getHeuristicForBestNextNode)
                .toList();
        // Return the best path as per the heruistic:
        return pathsToUnvisitedNodes.get(0);
    }

    /**
     * Comparator using <code>getHeuristic()</code>, to compare the value of two different paths to
     * each other.
     * @param pathToNode1 the first path to compare
     * @param pathToNode2 the second path to compare
     * @return a negative value if pathToNode1 is better than pathToNode2; a positive value if
     * pathToNode1 is worse than pathToNode2; zero if they are equal.
     */
    private int getHeuristicForBestNextNode(List<ExploreNode> pathToNode1,
                                            List<ExploreNode> pathToNode2) {

        return Double.compare(getHeuristic(pathToNode1), getHeuristic(pathToNode2));
    }

    /**
     * Gets the heuristic for how good a path is to take next.
     * The heuristic is defined as:
     * (shortest path from current location to that node) + (node's expected distance to orb)
     * <br />
     * "expected distance to orb" is estimated by multiplying the Node's Manhattan distance to the
     * orb, by a weight (which accounts for the uncertainty in the as-yet-unseen path). Experiments
     * suggest the best weight to use is somewhere between 1.0 and 1.4. This method uses 1.1.
     * @param path the path to get the heuristic for
     * @return the result of the heuristic (lower is better; higher is worse)
     */
    private double getHeuristic(List<ExploreNode> path) {
        final double weight = 1.1;
        ExploreNode node = path.get(path.size()-1);
        return path.size() + node.distanceToOrb() * weight;
    }

    /**
     * Make a move during the explore phase. SHOULD BE USED INSTEAD OF DIRECTLY CALLING
     * <code>state.moveTo()</code>.
     * Using this helper method ensures that, any time the explorer moves, the visited and
     * unvisited nodes are updated accordingly.
     * @param state the current ExplorationState
     * @param moveToId the ID of the Tile to move to
     */
    private void makeExploreMove(ExplorationState state, long moveToId) {
        exploreGraph.visitNode(moveToId);
        state.moveTo(moveToId);
    }
}
