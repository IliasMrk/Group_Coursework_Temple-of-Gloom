package student.escape;

import game.Edge;
import game.Node;
import game.Tile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;


/**
 * Test class for EscapeHelper class using Junit5 and mockito
 */

public class EscapeHelperTest {

    private Node start;
    private Node viaNode1;
    private Node viaNode2;
    private Node end;

    private static long idCounter = 999; // Increment to give each mocked Node a unique ID

    /**
     * Setup for the test class
     */
    @BeforeEach
    public void setUp() {
        //initialising
        Node mockNode = mock(Node.class);
        Tile mockTile = mock(Tile.class);


        when(mockTile.getGold()).thenReturn(10);


        when(mockNode.getTile()).thenReturn(mockTile);
        // Initialize mock nodes
        start = mock(Node.class);
        viaNode1 = mock(Node.class);
        viaNode2 = mock(Node.class);
        end = mock(Node.class);
        when(start.getId()).thenReturn(idCounter++);
        when(viaNode1.getId()).thenReturn(idCounter++);
        when(viaNode2.getId()).thenReturn(idCounter++);
        when(end.getId()).thenReturn(idCounter++);

        // Setup for start, viaNode1, viaNode2, and end nodes with their tiles and gold values
        setupNodeWithTileAndGold(start, 0);
        setupNodeWithTileAndGold(viaNode1, 1);
        setupNodeWithTileAndGold(viaNode2, 1);
        setupNodeWithTileAndGold(end, 0);

        // Add edges between nodes
        when(start.getNeighbours()).thenReturn(Set.of(viaNode1, end));
        when(viaNode1.getNeighbours()).thenReturn(Set.of(start, viaNode2));
        when(viaNode2.getNeighbours()).thenReturn(Set.of(viaNode1, end));
        when(end.getNeighbours()).thenReturn(Set.of(start, viaNode2));
        Edge startVN1 = mock(Edge.class); when(startVN1.length()).thenReturn(1);
        Edge vN1VN2 = mock(Edge.class); when(vN1VN2.length()).thenReturn(1);
        Edge startEnd = mock(Edge.class); when(startEnd.length()).thenReturn(1);
        Edge vN2End = mock(Edge.class); when(vN2End.length()).thenReturn(1);
        when(start.getEdge(viaNode1)).thenReturn(startVN1);
        when(viaNode1.getEdge(start)).thenReturn(startVN1);
        when(viaNode2.getEdge(viaNode1)).thenReturn(vN1VN2);
        when(viaNode1.getEdge(viaNode2)).thenReturn(vN1VN2);
        when(start.getEdge(end)).thenReturn(startEnd);
        when(end.getEdge(start)).thenReturn(startEnd);
        when(viaNode2.getEdge(end)).thenReturn(vN2End);
        when(end.getEdge(viaNode2)).thenReturn(vN2End);
    }

    private void setupNodeWithTileAndGold(Node node, int goldValue) {
        Tile mockTile = mock(Tile.class);
        when(mockTile.getGold()).thenReturn(goldValue);
        when(node.getTile()).thenReturn(mockTile);
    }

    /**
     * Test the find path from via to method.
     */
    @Test
    public void testFindPathFromViaTo() {


        Queue<Node> viaNodes = new LinkedList<>(Arrays.asList(viaNode1, viaNode2));

        System.out.println("Calling escapeHelper.findPathFromViaTo(start, viaNodes, end)");
        System.out.println("Start " + start + " via " + viaNodes + " end " + end);
        List<Node> actualPath = EscapeHelper.findPathFromViaTo(start, viaNodes, end);


        List<Node> expectedPath = Arrays.asList(start, viaNode1, viaNode2, end);


        assertEquals(expectedPath.size(), actualPath.size(), "Path lengths  MUST match.");
        assertEquals(expectedPath, actualPath, "The actual path is not matching the expected path.");
    }

    /**
     * Test the length of a path method.
     */
    @Test
    public void testGetLengthOfPath() {
        // Mock nodes and edges
        Node node1 = mock(Node.class);
        Node node2 = mock(Node.class);
        Node node3 = mock(Node.class);

        Edge edge1 = mock(Edge.class);
        Edge edge2 = mock(Edge.class);

        // behavior of nodes and edges
        when(node1.getEdge(node2)).thenReturn(edge1);
        when(node2.getEdge(node3)).thenReturn(edge2);
        when(edge1.length()).thenReturn(4); // Edge length between node1 and node2
        when(edge2.length()).thenReturn(5); // Edge length between node2 and node3


        // Set up the path
        List<Node> path = new ArrayList<>();
        path.add(node1);
        path.add(node2);
        path.add(node3);

        // Calculate the expected length
        int expectedLength = 4 + 5; // Length of edge1 + length of edge2

        // Test the getLengthOfPath method
        int actualLength = EscapeHelper.getLengthOfPath(path);

        // Verify that the returned length matches the expected length
        assertEquals(expectedLength, actualLength);

        System.out.println("The expected length is : " + expectedLength);
        System.out.println("The actual length is : " + actualLength);
    }


    /**
     * Test the manhattan distance method.
     */
    @Test
    public void testGetManhattanDistance() {
        // Mock nodes and tiles
        Node firstNode = mock(Node.class);
        Node secondNode = mock(Node.class);

        Tile firstTile = mock(Tile.class);
        Tile secondTile = mock(Tile.class);

        // behavior of tiles
        when(firstNode.getTile()).thenReturn(firstTile);
        when(secondNode.getTile()).thenReturn(secondTile);

        // row and column values for tiles
        when(firstTile.getRow()).thenReturn(1);
        when(firstTile.getColumn()).thenReturn(1);
        when(secondTile.getRow()).thenReturn(4);
        when(secondTile.getColumn()).thenReturn(5);


        // Calculate the expected Manhattan distance
        int expectedDistance = Math.abs(1 - 4) + Math.abs(1 - 5); // |1 - 4| + |1 - 5| = 3 + 4 = 7

        // Test the getManhattanDistance method
        int actualDistance = EscapeHelper.getManhattanDistance(firstNode, secondNode);

        // Verify that the returned distance matches the expected distance
        assertEquals(expectedDistance, actualDistance);

        System.out.println("the expected distance is : " + expectedDistance);
        System.out.println("the actual distance is : " + actualDistance);

    }


    /**
     * Test the gold Nodes method.
     */

    @Test
    public void testGetTopNNodes() {
        // Create mock nodes with mock tiles having gold values
        Node node1 = mock(Node.class);
        Tile tile1 = mock(Tile.class);
        when(node1.getTile()).thenReturn(tile1);
        when(tile1.getGold()).thenReturn(5);  // Gold value 5

        Node node2 = mock(Node.class);
        Tile tile2 = mock(Tile.class);
        when(node2.getTile()).thenReturn(tile2);
        when(tile2.getGold()).thenReturn(10);   // Gold value 10

        Node node3 = mock(Node.class);
        Tile tile3 = mock(Tile.class);
        when(node3.getTile()).thenReturn(tile3);
        when(tile3.getGold()).thenReturn(15);  // Gold value 15

        Node node4 = mock(Node.class);
        Tile tile4 = mock(Tile.class);
        when(node4.getTile()).thenReturn(tile4);
        when(tile4.getGold()).thenReturn(0);  // Gold value 0

        // Create a collection of mock nodes
        Collection<Node> allNodes = Arrays.asList(node1, node2, node3, node4);

        // Call the method under test
        List<Node> topNodes = EscapeHelper.getTopNNodes(allNodes, 3);  // Get top 2 nodes

        // Verify the result returning nodes in ascending order based on gold value
        assertEquals(3, topNodes.size());  // Ensure correct number of nodes
        assertEquals(node3, topNodes.get(0));
        assertEquals(node2, topNodes.get(1));
        assertEquals(node1, topNodes.get(2));


        // The result returns the gold valued nodes in ascending order
        System.out.println("The nodes are sorted in an ascending order.");
        System.out.println("The expected node with a value of 5 is: " + node1 + ". The actual node in top nodes is : " + topNodes.get(2));
        System.out.println("The expected node with a value of 10 is:  " + node2 + ". The actual node in top nodes is : " + topNodes.get(1));
        System.out.println("The expected node with a value of 15 is: " + node3 + ". The actual node in top nodes is : " + topNodes.get(0));

    }
}










