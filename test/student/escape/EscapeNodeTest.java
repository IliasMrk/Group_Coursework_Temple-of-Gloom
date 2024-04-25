package student.escape;

import game.Edge;
import game.Node;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Test class for EscapeNode
 */
public class EscapeNodeTest {

    private static long idCounter = 700; // increment to give each mocked Node a unique ID

    /**
     * Test getEscapeNode() when the node has not been seen before
     */
    @Test
    void getEscapeNodeNewNodeTest() {
        Node newNode = mock(Node.class);
        when(newNode.getId()).thenReturn(idCounter++);
        EscapeNode newEscapeNode = EscapeNode.getEscapeNode(newNode);
        assertEquals(newEscapeNode.node(), newNode);
    }

    /**
     * Test getEscapeNode() when the node has been seen before
     */
    @Test
    void getEscapeNodeExistingNodeTest() {
        Node existingNode = mock(Node.class);
        when(existingNode.getId()).thenReturn(idCounter++);
        EscapeNode newEscapeNode = EscapeNode.getEscapeNode(existingNode);
        EscapeNode existingEscapeNode = EscapeNode.getEscapeNode(existingNode);
        assertEquals(existingEscapeNode.node(), existingNode);
    }

    /**
     * Test id() method
     */
    @Test
    void nodeIdGetterTest() {
        Node nodeWithID = mock(Node.class);
        when(nodeWithID.getId()).thenReturn(idCounter++);
        EscapeNode escapeNodeWithID = EscapeNode.getEscapeNode(nodeWithID);
        assertEquals(escapeNodeWithID.id(), nodeWithID.getId());
    }

    /**
     * Test node() method
     */
    @Test
    void nodeGetterTest() {
        Node underlyingNode = mock(Node.class);
        when(underlyingNode.getId()).thenReturn(idCounter++);
        EscapeNode escapeNodeWithUnderlyingNode = EscapeNode.getEscapeNode(underlyingNode);
        assertEquals(escapeNodeWithUnderlyingNode.node(), underlyingNode);
    }

    /**
     * Test neighbours() method when a Node has the maximum possible neighbours (4)
     */
    @Test
    void neighboursGetterWithFourNeighboursTest() {
        // Set up the base node
        Node nodeWithFourNeighbours = mock(Node.class);
        when(nodeWithFourNeighbours.getId()).thenReturn(idCounter++);

        // Set up the neighbours
        Node neighbour1 = mock(Node.class); when(neighbour1.getId()).thenReturn(idCounter++);
        Node neighbour2 = mock(Node.class); when(neighbour2.getId()).thenReturn(idCounter++);
        Node neighbour3 = mock(Node.class); when(neighbour3.getId()).thenReturn(idCounter++);
        Node neighbour4 = mock(Node.class); when(neighbour4.getId()).thenReturn(idCounter++);
        Set<Node> fourNeighbours = Set.of(neighbour1, neighbour2, neighbour3, neighbour4);

        // Mock the link between the base node and the neighbours
        when(nodeWithFourNeighbours.getNeighbours()).thenReturn(fourNeighbours);

        // Create EscapeNodes from the Nodes
        EscapeNode escapeNodeWithFourNeighbours = EscapeNode.getEscapeNode(nodeWithFourNeighbours);
        EscapeNode eNNeighbour1 = EscapeNode.getEscapeNode(neighbour1);
        EscapeNode eNNeighbour2 = EscapeNode.getEscapeNode(neighbour2);
        EscapeNode eNNeighbour3 = EscapeNode.getEscapeNode(neighbour3);
        EscapeNode eNNeighbour4 = EscapeNode.getEscapeNode(neighbour4);

        // Make sure neighbours() returns the correct set of neighbouring EscapeNodes
        Set<EscapeNode> fourNeighboursENs = Set.of(eNNeighbour1, eNNeighbour2,
                eNNeighbour3, eNNeighbour4);
        assertEquals(escapeNodeWithFourNeighbours.neighbours(), fourNeighboursENs);
    }

    /**
     * Test neighbours() method when a Node has the minimum possible neighbours (1)
     */
    @Test
    void neighboursGetterWithOneNeighbourTest() {
        // Set up the base node
        Node nodeWithOneNeighbours = mock(Node.class);
        when(nodeWithOneNeighbours.getId()).thenReturn(idCounter++);

        // Set up the neighbour
        Node neighbour = mock(Node.class); when(neighbour.getId()).thenReturn(idCounter++);
        Set<Node> oneNeighbour = Set.of(neighbour);

        // Mock the link between the base node and the neighbour
        when(nodeWithOneNeighbours.getNeighbours()).thenReturn(oneNeighbour);

        // Create EscapeNodes from the Nodes
        EscapeNode eNNeighbour = EscapeNode.getEscapeNode(neighbour);
        EscapeNode escapeNodeWithOneNeighbour = EscapeNode.getEscapeNode(nodeWithOneNeighbours);

        // Make sure neighbours() returns the correct set of neighbouring EscapeNodes
        Set<EscapeNode> oneNeighbourEN = Set.of(eNNeighbour);
        assertEquals(escapeNodeWithOneNeighbour.neighbours(), oneNeighbourEN);
    }

    /**
     * Test length() method
     */
    @Test
    void lengthGetterTest() {
        // Mock the Node and Edge objects
        Node first = mock(Node.class);
        Node second = mock(Node.class);
        Edge edgeBetweenNodes = mock(Edge.class);


        when(first.getId()).thenReturn(idCounter++);
        when(second.getId()).thenReturn(idCounter++);

        when(edgeBetweenNodes.length()).thenReturn(50);

        when(first.getEdge(second)).thenReturn(edgeBetweenNodes);
        when(second.getEdge(first)).thenReturn(edgeBetweenNodes);


        EscapeNode firstEN = EscapeNode.getEscapeNode(first);
        EscapeNode secondEN = EscapeNode.getEscapeNode(second);


        assertEquals(50, firstEN.lengthTo(secondEN));
    }

}
