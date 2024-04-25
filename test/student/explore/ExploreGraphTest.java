package student.explore;

import game.NodeStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;


/**
 * Test class for Explore Graph class using Junit5 and mockito
 */
public class ExploreGraphTest {

    private ExploreGraph exploreGraph;

    //Creating a mock object
    @Mock
    private ExploreNode mockExploreNode;

    /**
     * Setup for the test class
     */
    @BeforeEach
    public void setUp() {
        //Initialize Mockito annotations before each test
        MockitoAnnotations.openMocks(this);
        exploreGraph = new ExploreGraph();
    }

    /**
     * Test add node to the graph method.
     */
    @Test
    public void testAddNodeToGraph() {
        ExploreNode node1 = exploreGraph.addNodeToGraph(1L, 5);
        ExploreNode node2 = exploreGraph.addNodeToGraph(2L, 10);

        // Assert the nodes
        assertEquals(1L, node1.id());
        assertEquals(5, node1.distanceToOrb());
        assertEquals(2L, node2.id());
        assertEquals(10, node2.distanceToOrb());

        System.out.println("The expected node is node 1 with id: 1. The actual node is 1 with id: " + node1.id());
        System.out.println("The expected node is node 1 with distanceToOrb: 5. The actual node is 1 with distanceToOrb: " + node1.distanceToOrb());
        System.out.println("The expected node is node 2 with id: 2. The actual node is 2 with id: " + node2.id());
        System.out.println("The expected node is node 2 with distanceToOrb: 10. The actual node is 2 with distanceToOrb: " + node2.distanceToOrb());
    }


    /**
     * Test get node from id method.
     */
    @Test
    public void testGetNodeFromID() {
        // Add a node to the graph
        ExploreNode node = exploreGraph.addNodeToGraph(1L, 5);

        // Get the node from the graph using its ID
        ExploreNode retrievedNode = exploreGraph.getNodeFromID(1L);

        // Assert that the expected node is the same as the one added
        assertEquals(node, retrievedNode);
        System.out.println("The expected is: "+ node);
        System.out.println("The actual is: " + retrievedNode);
    }

    /**
     * Test addNeighours method
     */
    @Test
    public void testAddNeighbours() {
        // Mock data
        NodeStatus neighbour1 = new NodeStatus(2L, 3);
        NodeStatus neighbour2 = new NodeStatus(3L, 4);
        Collection<NodeStatus> neighbours = new ArrayList<>();
        neighbours.add(neighbour1);
        neighbours.add(neighbour2);

        // Mock behaviour
        when(mockExploreNode.id()).thenReturn(1L);
        when(mockExploreNode.distanceToOrb()).thenReturn(5);

        // Create base node
        ExploreNode baseNode = new ExploreNode(1L, 5);

        // Add neighbours to the base node
        exploreGraph.addNeighbours(baseNode, neighbours);

        // Assert that baseNode has the correct number of neighbours
        assertEquals(2, baseNode.neighbours().size(), "Number of neighbors should be 2");
        System.out.println("The expected neighbours are: 2");
        System.out.println("The actual neighbours are: " + baseNode.neighbours().size());
    }
}