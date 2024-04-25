package student;

import static org.mockito.Mockito.*;
import game.ExplorationState;
import game.EscapeState;
import game.Node;
import game.Tile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Test class for Explorer using Junit and Mockito
 */
class ExplorerTest {

    private Explorer explorer;
    private EscapeState mockEscapeState;
    private Node mockCurrentNode;
    private Node mockExitNode;

    /**
     * Setup for the test class works before each test
     */
    @BeforeEach
    void setUp() {
        // Initiliasing various mock objects
        explorer = new Explorer();
        mockEscapeState = mock(EscapeState.class);

        // Setup for Node objects
        mockCurrentNode = mock(Node.class);
        mockExitNode = mock(Node.class);

        // Mocking the escape state
        when(mockEscapeState.getCurrentNode()).thenReturn(mockCurrentNode);
        when(mockEscapeState.getExit()).thenReturn(mockExitNode);
        when(mockCurrentNode.getTile()).thenReturn(new Tile(0, 0, 0, Tile.Type.FLOOR));
        when(mockExitNode.getTile()).thenReturn(new Tile(1, 1, 0, Tile.Type.ENTRANCE));
    }

    /**
     * Tests if orb was found
     */
    @Test
    void exploreFindsOrbSuccessfully() {
        ExplorationState mockState = mock(ExplorationState.class);

        when(mockState.getCurrentLocation()).thenReturn(1L);
        when(mockState.getDistanceToTarget()).thenReturn(0); // I am on top of the ....Orb :)

        explorer.explore(mockState);

        // No movement occurs if the orb is found:
        verify(mockState, times(0)).moveTo(anyLong());
    }

    /**
     * Test the escape method
     */
    @Test
    void escapeInvokesEscapeStrategy() {
        explorer.escape(mockEscapeState);
        verify(mockEscapeState, atLeastOnce()).getCurrentNode();
    }
}
