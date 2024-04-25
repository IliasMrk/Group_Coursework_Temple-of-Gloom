package student.explore;

import game.ExplorationState;
import game.NodeStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import student.explore.BasicFindBestNodeExplore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.*;

/**
 * JUnit Class test for BasicFindBestNodeExplore
 */
class BasicFindBestNodeExploreTest {

    private BasicFindBestNodeExplore strategy;
    private ExplorationState mockState;

    /**
     * Sets up.
     */
    @BeforeEach
    void setUp() {
        strategy = new BasicFindBestNodeExplore();
        mockState = mock(ExplorationState.class);
    }

    /**
     *JUnit test for exploreWithImmediateOrbProximity
     */
    @Test
    void exploreWithImmediateOrbProximity() {
        when(mockState.getCurrentLocation()).thenReturn(1L);
        when(mockState.getDistanceToTarget()).thenReturn(0);

        strategy.explore(mockState);

        verify(mockState, never()).moveTo(anyLong());
    }

    /**
     * exploreWithImmediateOrbProximity testing distant orb
     */
    @Test
    void exploreWithDistantOrb() {
        NodeStatus start = new NodeStatus(1L, 10);
        NodeStatus neighbour1 = new NodeStatus(2L, 5);
        NodeStatus neighbour2 = new NodeStatus(3L, 0);
        List<NodeStatus> allNodes = Arrays.asList(start, neighbour1, neighbour2);

        // Keep track of how many times moveTo() has been called
        // Use an array because moveToCounter needs to be final (because it's used in a lambda)
        final int[] moveToCounter = {0};

        when(mockState.getCurrentLocation()).thenAnswer(invocation -> allNodes.get(moveToCounter[0]).nodeID());
        when(mockState.getDistanceToTarget()).thenAnswer(invocation -> allNodes.get(moveToCounter[0]).distanceToTarget());
        when(mockState.getNeighbours()).thenAnswer(invocation -> Arrays.asList(allNodes.get(moveToCounter[0] + 1)));

        doAnswer(invocation -> moveToCounter[0]++).when(mockState).moveTo(anyLong());

        strategy.explore(mockState);

        verify(mockState, times(2)).moveTo(anyLong());
    }



}
