package student.escape;

import game.EscapeState;
import game.Node;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.stream.Collectors;

/**
 * Strategy for <code>escape()</code> phase. Determines the n most valuable tiles, then works out
 * if it can reach all of them and still reach the exit. If it can, it does so. If it can't, it
 * decrements n, and tries again.
 * Also looks for diversions from this path, if any can be made without exceeding the time limit.
 * <br />
 * 500 headless runs on 31/3/24:
 * Average gold: 13,955
 * % of gold scores at zero: 0.6%
 */
public class GetTopNCashWithDiversionsEscape implements EscapeStrategy {
    /**
     * Escape the maze while collecting as much gold as possible.
     * @param state the information available at the current state
     */
    @Override
    public void escape(EscapeState state) {
        // Get the initial path, which should hit all of the most valuable nodes in the grid
        List<Node> proposedPath = getPathViaTopNNodes(state);

        // Add in any diversions which might help
        int timeLeftOver = state.getTimeRemaining() - EscapeHelper.getLengthOfPath(proposedPath);
        List<Node> bestPathWithDiversions = getPathWithBestDiversions(proposedPath, timeLeftOver);

        //iterate through diverting path, excluding the starting node
        // Move along the path, and pick up gold as you go
        for (Node x : bestPathWithDiversions.subList(1, bestPathWithDiversions.size())) {
            state.moveTo(x);
            if (state.getCurrentNode().getTile().getGold() > 0) state.pickUpGold();
        }
    }

    /**
     * Finds a path from the start to the exit, via the most valuable n nodes in the grid. Will
     * maximise n, such that the path will still return to the exit on time.
     * @param state the information available at the current state
     * @return the shortest path via the top n nodes - will always reach the exit on time
     */
    private List<Node> getPathViaTopNNodes(EscapeState state) {
        // LinkedList to store the proposed path to take:
        List<Node> proposedPath = new LinkedList<>();

        // Until a viable route has been found, keep iterating, finding the best route via the top
        // n nodes each time. If the route exceeds the allowed time, decrement n and try again.
        final int maxN = 20; // Experiments suggest values of n over 20 are never any better
        for (int n = maxN; n >=0; n--) {
            // Get the top n nodes, sorted by their distance from the start node:
            Queue<Node> topNNodesSorted = getSortedTopNNodes(state, n);

            // Find quickest path from start, via each of the n nodes, to the end
            proposedPath = EscapeHelper.findPathFromViaTo(
                    state.getCurrentNode(), topNNodesSorted, state.getExit());

            // If this path gets to the exit in time, this is a viable path; return it
            if (state.getTimeRemaining() - EscapeHelper.getLengthOfPath(proposedPath) >= 0)
                return proposedPath;
        }
        return proposedPath;
    }

    /**
     * Gets the n most valuable nodes of the grid, and sorts them by their distyance from the start
     * point.
     * @param state the information available at the current state
     * @param n the number of most valuable nodes to return
     * @return a sorted Queue of the n most valuable Nodes on the grid
     */
    private Queue<Node> getSortedTopNNodes(EscapeState state, int n) {
        // Get the top n nodes, and sort them by their distance from the start node
        return EscapeHelper.getTopNNodes(state.getVertices(), n).stream()
                .sorted(Comparator.comparingInt(x ->
                        EscapeHelper.getManhattanDistance(x, state.getCurrentNode())))
                .collect(Collectors.toCollection(LinkedList<Node>::new));
    }

    /**
     * Gets a path, based on proposedPath, but with diversions added. The resulting path will always
     * return to the exit on time, and will also visit as many worthwhile Nodes near to the path as
     * possible. Nodes at different levels of depth from the path are considered, but only the most
     * valuable resulting path is returned.
     * @param proposedPath the initial path, without diversions
     * @param timeLeftOver the time remaining, after taking the proposedPath
     * @return a List of Nodes, representing the proposedPath with diversions inserted
     */
    private List<Node> getPathWithBestDiversions(List<Node> proposedPath, int timeLeftOver) {
        // Initialise the best results so far
        List<Node> bestPathWithDiversions = null;
        int bestValue = 0;

        // Find the most valuable path, by trying diversions from the proposedPath, up to different
        // levels of depth. Some levels of depth will produce more value overall than others - this
        // loop will find the best of the lot.
        final int maxDepth = 16; // Experiments suggest diversions over depth 16 aren't worth it
        for (int d = 1; d <= maxDepth; d++) {
            // Find the best path with diversions up to the current depth:
            List<Node> thisPathWithDiversions =
                    DiversionsGenerator.getPathWithDiversions(proposedPath, d, timeLeftOver);

            // Check if the diversions using the current depth are actually more valuable than
            // diversions at a lower depth - if they are, update the best path accordingly.
            int thisValue = EscapeHelper.getValueOfPath(thisPathWithDiversions, proposedPath);
            if (bestPathWithDiversions == null || thisValue > bestValue) {
                bestValue = thisValue;
                bestPathWithDiversions = thisPathWithDiversions;
            }
        }

        return bestPathWithDiversions;
    }
}