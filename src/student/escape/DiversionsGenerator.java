package student.escape;

import game.Node;

import java.util.*;

import static student.escape.EscapeHelper.getLengthOfPath;
import static student.escape.EscapeHelper.getValueOfPath;

/**
 * Class to generate diversions for given paths.
 * Once a path is provided to it, the class can find additional steps to take within the path, thus
 * improving the cash received by the player, while still reaching the exit in time.
 */
public class DiversionsGenerator {

    /**
     * Given a path, this method will return an updated path, which incorporates the "best"
     * diversions to take, while still reaching the exit within the given remaining time. The
     * diversions considered will not exceed the given depth (so, e.g., with a depth of 1, the
     * diversions will only visit Nodes which are adjacent to the given path).
     * @param proposedPath the path before diversions have been added
     * @param depth        how many steps away from the path a diversion is allowed to take
     * @param time         how much total time the diversions can take
     * @return an updated path, incorporating the diversions
     */
    public static List<Node> getPathWithDiversions(List<Node> proposedPath, int depth, int time) {
        // Get info on all Nodes reachable from the path within given depth
        Map<Node, Node> reachableFrom = getNodesReachableFrom(proposedPath, depth);
        List<Node> reachableNodesWithCash = reachableFrom.keySet().stream()
                .filter(x -> x.getTile().getGold() > 0)
                .toList();

        // Get the fastest paths to each of the reachable Nodes
        List<List<Node>> diversions = getFastestPathsToAll(reachableNodesWithCash, reachableFrom);

        // Sort the potential diversions by (Value / Length)
        List<List<Node>> sortedDiversions = diversions.stream()
                .sorted((x, y) -> compareValueDividedByLength(x, y, proposedPath))
                .toList();

        // Initialise the return path, using the proposedPath as its base:
        List<Node> pathWithDiversions = new LinkedList<>(proposedPath);
        // Go through each of the proposed diversions:
        for (List<Node> diversion : sortedDiversions) {
            // If the proposed diversion can be done within the time remaining, and gives more than
            // zero gold, insert it into the path.
            int diversionTime = getLengthOfPath(diversion);
            if (diversionTime <= time &&
                    getValueOfPath(diversion, pathWithDiversions) > 0) {
                // Find where to insert the diversion, and insert it
                int insertionIndex = pathWithDiversions.indexOf(diversion.get(0)) + 1;
                //insert all nodes of the diversion to the path (excluding the node where diversion begins)
                pathWithDiversions.addAll(insertionIndex, diversion.subList(1, diversion.size()));
                time -= diversionTime; // update the time remaining
            }
        }

        return pathWithDiversions;
    }

    /**
     * Gets a Map, holding, for each Node reachable from the proposedPath within the given depth,
     * which Node on the proposedPath it is reachable from.
     * @param proposedPath the path from which the reachable Nodes should be found
     * @param depth        the depth from the path to interrogate (uses number of steps in path,
     *                     rather than Edge length)
     * @return a Map where each of the keys is a Node which is reachable from the path within the
     * given depth, and each of the values is the Node on the path from which that Node is
     * reachable
     */
    private static Map<Node, Node> getNodesReachableFrom(List<Node> proposedPath, int depth) {
        // Initialise the return Map:
        Map<Node, Node> reachableFrom = new HashMap<>();
        // Keep track of all Nodes already seen, starting with those in proposedPath:
        Set<Node> seenNodes = new HashSet<>(proposedPath);

        // At each level of depth d, keep a list of what is reachable at depth d,
        // and what is reachable at depth d-1:
        List<Node> reachableAtDepthD;
        List<Node> reachableAtDepthDMinus1 = proposedPath;
        // Iterate over each level of depth d:
        for (int d = 1; d <= depth; d++) {
            reachableAtDepthD = new LinkedList<>();

            // Iterate over the Nodes reachable at depth d-1:
            for (Node node : reachableAtDepthDMinus1) {

                // Iterate over the Nodes reachable at depth d:
                for (Node neighbour : node.getNeighbours()) {
                    if (!seenNodes.contains(neighbour)) {
                        // Map the neighbour, to the Node on the original path from which it can be
                        // reached:
                        reachableFrom.put(neighbour, reachableFrom.getOrDefault(node, node));

                        // Update seenNodes and reachableAtDepthD:
                        seenNodes.add(neighbour);
                        reachableAtDepthD.add(neighbour);
                    }
                }
            }
            reachableAtDepthDMinus1 = reachableAtDepthD;
        }

        return reachableFrom;
    }

    /**
     * Finds the shortest path to and from each node in the given List. The path starts and ends at
     * the Node to which each Node is mapped in the given Map.
     * @param nodes         a List of Nodes to find paths to and from
     * @param reachableFrom a Map, indicating where each path to any given Node should start and
     *                      finish
     * @return a List of paths - one for each Node in the provided List. Each path will start and
     * end at the relevant Node, as per the reachableFrom Map.
     */
    private static List<List<Node>> getFastestPathsToAll(
            List<Node> nodes, Map<Node, Node> reachableFrom) {

        List<List<Node>> fastestPaths = new ArrayList<>();
        // Go through each of the target Nodes, and get the shortest path, from the original path,
        // to that Node, and returning to the original path.
        for (Node targetNode : nodes) {
            Node from = reachableFrom.get(targetNode);
            Queue<Node> via = new LinkedList<>(List.of(targetNode));
            fastestPaths.add(EscapeHelper.findPathFromViaTo(from, via, from));
        }
        return fastestPaths;
    }

    /**
     * Gets the value of a path, divided by its length, given a list of Nodes from which all the
     * gold has already been taken.
     * @param path the path to measure
     * @param cashAlreadyTaken a list of Nodes from which all gold has been taken (may be empty)
     * @return the remaining value of the path, divided by its length
     */
    private static double getValueDividedByLength(List<Node> path, List<Node> cashAlreadyTaken) {
        // cast getValueOfPath() to ensure double-division, not integer division
        return (double) getValueOfPath(path, cashAlreadyTaken) / getLengthOfPath(path);
    }

    /**
     * Comparator for two paths, based on their relative values divided by their lengths.
     * @param path1 the first path to be compared
     * @param path2 the second path to be compared
     * @param cashAlreadyTaken a list of Nodes from which the cash has already been taken
     * @return a negative value if path1 is less relatively valuable than path2; a positive value if
     * path1 is more relatively valuable than path2; zero if they are equally relatively valuable.
     */
    private static int compareValueDividedByLength(List<Node> path1,
                                                   List<Node> path2,
                                                   List<Node> cashAlreadyTaken) {

        double x = getValueDividedByLength(path1, cashAlreadyTaken);
        double y = getValueDividedByLength(path2, cashAlreadyTaken);
        return Double.compare(x, y);
    }
}
