package student.escape;

import game.Edge;
import game.Node;
import student.PathFinder;

import java.util.*;

/**
 * Helper class for <code>escape()</code> phase. Contains multiple useful methods which can be used
 * by multiple EscapeStrategy implementations.
 */
public class EscapeHelper {

    /**
     * Gets a path from a starting Node, viaQueue a Queue of other Nodes, and ending at a final Node.
     * The Nodes specified in <code>viaQueue</code> will be visited in order.
     * @param from the starting Node
     * @param viaQueue the Nodes to visit along the way
     * @param end the ending Node
     * @return a <code>List<Node></code> representing the shortest path going from the first Node
     * to the end Node, visiting the specified viaQueue Nodes in order
     */
    public static List<Node> findPathFromViaTo(Node from, Queue<Node> viaQueue, Node end) {
        // Initialise the path with the from Node as its first element
        List<Node> overallPath = new LinkedList<>(List.of(from));

        // Starting at the start, add route to each of the Nodes in viaQueue, in order
        Node start = from;
        while (!viaQueue.isEmpty()) {
            // Get the next Node to visit, and find the shortest path to it
            Node goal = viaQueue.remove();
            List<Node> subPath = findShortestPath(start, goal);

            // check if the shortest path found has some value in it
            // if it does, add it to the overall path excluding the starting point (current node).
            if (getValueOfPath(subPath, overallPath) > 0) {
                overallPath.addAll(subPath.subList(1, subPath.size()));
                // update the starting node to the current goal node for next iteration
                start = goal;
            }
        }

        // Add the final path, to reach the end Node excluding the current node:
        List<Node> finalPath = findShortestPath(start, end);
        overallPath.addAll(finalPath.subList(1, finalPath.size()));

        return overallPath;
    }

    /**
     * Get the total length of a path.
     * @param path the path to check
     * @return the length of the path
     */
    public static int getLengthOfPath(List<Node> path) {
        int length = 0;
        Node thisNode = path.get(0);

        // Iterate over all the edges in the path, and add their lengths to the total
        //start from index 1 to avoid double counting the edge between the current node and the next node
        //as it is counted when we count the next node and the one following.
        for (Node nextNode : path.subList(1, path.size())) {
            Edge e = thisNode.getEdge(nextNode);
            length += e.length();
            thisNode = nextNode;
        }
        return length;
    }

    /**
     * Gets the cash value of a particular path, assuming all the Nodes listed in cashAlreadyTaken
     * have zero cash left on them.
     * @param path the path to be checked
     * @param cashAlreadyTaken a List of Nodes which have had all their cash removed
     * @return the remaining cash value of the path
     */
    public static int getValueOfPath(List<Node> path, List<Node> cashAlreadyTaken) {
        int cash = 0;
        Set<Node> uniqueNodes = new HashSet<>(path);

        // Go through each of the uniqueNodes, and add any gold which hasn't been taken to the total
        for (Node node : uniqueNodes) {
            if (!cashAlreadyTaken.contains(node)) {
                cash += node.getTile().getGold();
            }
        }
        return cash;
    }

    /**
     * Gets the Manhattan distance between two Nodes.
     * @param first one of the Nodes
     * @param second the other Node
     * @return the Manhattan distance between the two Nodes
     */
    public static int getManhattanDistance(Node first, Node second) {
        int firstRow  =  first.getTile().getRow();
        int firstCol  =  first.getTile().getColumn();
        int secondRow = second.getTile().getRow();
        int secondCol = second.getTile().getColumn();

        return Math.abs(firstRow - secondRow) + Math.abs(firstCol - secondCol);
    }

    /**
     * Gets a List of the n most valuable Nodes in the provided Collection of Nodes.
     * @param allNodes the Nodes to find the most valuable in
     * @param n the number of Nodes to return
     * @return a List containing the n most valuable Nodes in the given Collection of Nodes
     */
    public static List<Node> getTopNNodes(Collection<Node> allNodes, int n) {
        return allNodes.stream()
                .filter(x -> x.getTile().getGold() > 0) // reduces the number of Nodes to sort
                .sorted(Comparator.comparingInt(x -> -x.getTile().getGold()))
                .limit(n)
                .toList();
    }

    /**
     * Method to find the shortest path between two nodes using a modified version of Dijkstra's
     * algorithm. (Used in <code>findPathFromViaTo()</code>. Essentially takes the output from
     * <code>PathFinder.findShortestPath()</code> and converts the result from
     * <code>List<EscapeNode></code> to <code>List<Node></code>.)
     * @param start the start node
     * @param goal  the goal node
     * @return the shortest path between start and goal
     */
    private static List<Node> findShortestPath(Node start, Node goal) {
        // Convert start and goal to their EscapeNode equivalents, and call
        // PathFinder.findShortestPath() using them:
        List<EscapeNode> pathAsEscapeNodes = PathFinder.findShortestPath(
                EscapeNode.getEscapeNode(start),
                EscapeNode.getEscapeNode(goal)
        );

        // Convert the resulting List<EscapeNode> into a List<Node>:
        return pathAsEscapeNodes.stream()
                .map(EscapeNode::node)
                .toList();
    }
}
