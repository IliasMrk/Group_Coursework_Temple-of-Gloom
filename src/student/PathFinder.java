package student;

import java.util.*;

/**
 * Helper class to find shortest path between two nodes.
 * Works on both <code>ExploreNode</code>s and <code>EscapeNode</code>s.
 */
public class PathFinder {
    /**
     * Method to find the shortest path between two nodes using a modified version of Dijkstra's
     * algorithm
     * @param start the start node
     * @param goal  the goal node
     * @return the shortest path between start and goal
     * @param <T> any subclass of StudentNode<T> (should be either EscapeNode or ExploreNode)
     */
    public static <T extends StudentNode<T>> List<T> findShortestPath(T start, T goal) {
        // Keep a frontier of all Nodes to which a path has been found, prioritised by how far from
        // the start that Node is
        Map<T, Integer> distanceFromStart = new HashMap<>();
        PriorityQueue<T> frontier = new PriorityQueue<>(
                Comparator.comparingInt(distanceFromStart::get)
        );

        // Keep track of the best Nodes from which to reach each Node using a HashMap
        // This is used in reconstructing the path once the path to the target Node has been found
        Map<T, T> bestPreviousNodes = new HashMap<>();

        distanceFromStart.put(start, 0);

        T current = start;

        // Until the goal has been found;
        while (!current.equals(goal)) {
            // Check each neighbour of the current first Node in the frontier:
            for (T neighbour : current.neighbours()) {
                // If the neighbour can be reached in a better time than has previously been found,
                // update distanceFromStart, bestPreviousNodes, and frontier accordingly
                int newDist = distanceFromStart.get(current) + current.lengthTo(neighbour);
                if (!distanceFromStart.containsKey(neighbour) ||
                        newDist < distanceFromStart.get(neighbour))
                {
                    distanceFromStart.put(neighbour, newDist);
                    bestPreviousNodes.put(neighbour, current);
                    frontier.add(neighbour);
                }
            }
            current = frontier.poll();
        }

        return rebuildPathTo(goal, bestPreviousNodes);
    }

    /**
     * Helper function that rebuilds path from the start to the goal node.
     *
     * @param <T>           any subclass of StudentNode<T> (should be either EscapeNode or ExploreNode)
     * @param goal          the goal node
     * @param previousNodes the map of previous nodes - each node should be mapped to the best node
     *                      to visit immediately prior to it, to help get the shortest path to the
     *                      goal
     * @return a list of nodes - the path from the start node to the goal node
     */
    private static <T extends StudentNode<T>> List<T> rebuildPathTo(T goal,
                                                                    Map<T, T> previousNodes) {

        LinkedList<T> path = new LinkedList<>();
        // Starting with the goal, iteratively add the best Node to visit before the most recently
        // added Node
        for (T at = goal; at != null; at = previousNodes.get(at)) {
            path.addFirst(at);
        }
        return path;
    }
}