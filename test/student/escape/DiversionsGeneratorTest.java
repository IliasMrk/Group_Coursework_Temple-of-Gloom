package student.escape;
import game.Edge;
import game.Node;
import game.Tile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class DiversionsGeneratorTest {

    @Mock
    private Node startNode, midNode, endNode, diversionNode;
    @Mock
    private Tile startTile, midTile, endTile, diversionTile;
    @Mock
    private Edge startToMid, midToEnd, midToDiversion;

    private static long  idCounter = 500; // Increment to give each mocked Node a unique ID

    /**
     * Set up the mocks for the test.
     */
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        when(startNode.getId()).thenReturn(idCounter++);
        when(midNode.getId()).thenReturn(idCounter++);
        when(endNode.getId()).thenReturn(idCounter++);
        when(diversionNode.getId()).thenReturn(idCounter++);

        when(startNode.getTile()).thenReturn(startTile);
        when(midNode.getTile()).thenReturn(midTile);
        when(endNode.getTile()).thenReturn(endTile);
        when(diversionNode.getTile()).thenReturn(diversionTile);

        when(startTile.getGold()).thenReturn(0);
        when(midTile.getGold()).thenReturn(0);
        when(endTile.getGold()).thenReturn(0);
        when(diversionTile.getGold()).thenReturn(100);

        when(startNode.getNeighbours()).thenReturn(new HashSet<>(Arrays.asList(midNode)));
        when(midNode.getNeighbours()).thenReturn(new HashSet<>(Arrays.asList(startNode, diversionNode, endNode)));
        when(diversionNode.getNeighbours()).thenReturn(new HashSet<>(Arrays.asList(midNode)));
        when(endNode.getNeighbours()).thenReturn(new HashSet<>(Arrays.asList(midNode)));

        when(startNode.getEdge(midNode)).thenReturn(startToMid);
        when(midNode.getEdge(startNode)).thenReturn(startToMid);
        when(midNode.getEdge(diversionNode)).thenReturn(midToDiversion);
        when(diversionNode.getEdge(midNode)).thenReturn(midToDiversion);
        when(midNode.getEdge(endNode)).thenReturn(midToEnd);
        when(endNode.getEdge(midNode)).thenReturn(midToEnd);

        when(startToMid.length()).thenReturn(1);
        when(midToDiversion.length()).thenReturn(1);
        when(midToEnd.length()).thenReturn(1);
    }


    /**
     * Test the case where there is no time for diversions.
     */
    @Test
    public void getPathWithDiversions_NoTimeForDiversions_ReturnsOriginalPath() {
        List<Node> proposedPath = Arrays.asList(startNode, midNode, endNode);
        int depth = 1;
        int time = 1;

        List<Node> pathWithDiversions = DiversionsGenerator.getPathWithDiversions(proposedPath, depth, time);

        assertEquals(proposedPath, pathWithDiversions, "Expected path to match the proposed path.");
    }

    /**
     * Test the case where there is time and depth for diversions.
     */
    @Test
    public void getPathWithDiversions_WithTimeAndDepthForDiversions_IncludesDiversion() {
        List<Node> proposedPath = Arrays.asList(startNode, midNode, endNode);
        int depth = 1;
        int time = 5;

        List<Node> pathWithDiversions = DiversionsGenerator.getPathWithDiversions(proposedPath, depth, time);
        System.out.println(pathWithDiversions);


        assertTrue(pathWithDiversions.contains(diversionNode), "Expected path to include the diversion node.");
        assertTrue(pathWithDiversions.size() > proposedPath.size(), "Expected path to be longer than the original due to diversion.");
    }

}

