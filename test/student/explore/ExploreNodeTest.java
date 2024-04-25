package student.explore;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;


/**
 * Test class for Explore Node class using Junit5 and mockito
 */
public class ExploreNodeTest {

    //Creating a mock object
    @Mock
    private ExploreNode mockExploreNode;

    private ExploreNode node;

    /**
     * Setup for the test class
     */
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        //mocking the explore node
        when(mockExploreNode.id()).thenReturn(10L);
        when(mockExploreNode.distanceToOrb()).thenReturn(5);

        node = new ExploreNode(mockExploreNode.id(), mockExploreNode.distanceToOrb());
    }


    /**
     * Test method that returns the id of a node.
     */
    @Test
    public void testId() {
        assertEquals(10L, node.id());

        System.out.println("The expected id is: 10, and the actual is: "+node.id());
    }

    /**
     * Test method that returns the distance to the Orb.
     */
    @Test
    public void testDistanceToOrb() {
        assertEquals(5, node.distanceToOrb());
        System.out.println("The expected distance to Orb is: 5, and the actual distance is: "+node.distanceToOrb());
    }

    /**
     * Test method for adding a neighbour.
     */
    @Test
    public void testAddEdge() {
        ExploreNode neighbourNode = new ExploreNode(20L, 3);
        node.addEdge(neighbourNode);
        Set<ExploreNode> neighbours = node.neighbours();
        Assertions.assertTrue(neighbours.contains(neighbourNode));
        System.out.println("The added neighbour is: " + neighbourNode);
    }

    /**
     * Test method for retrieving neighbours.
     */
    @Test
    public void testNeighbours() {
        ExploreNode neighbourNode1 = new ExploreNode(20L, 3);
        ExploreNode neighbourNode2 = new ExploreNode(30L, 7);
        node.addEdge(neighbourNode1);
        node.addEdge(neighbourNode2);

        Set<ExploreNode> neighbours = node.neighbours();
        assertEquals(2, neighbours.size());
        Assertions.assertTrue(neighbours.contains(neighbourNode1));
        Assertions.assertTrue(neighbours.contains(neighbourNode2));
        System.out.println("The neighbours are: " + neighbours);
    }

    /**
     * Test method for getting the length between two ExploreNodes (should always be 1)
     */
    @Test
    public void testLength() {
        ExploreNode neighbourNode1 = new ExploreNode(20L, 3);
        ExploreNode neighbourNode2 = new ExploreNode(30L, 7);
        node.addEdge(neighbourNode1);
        node.addEdge(neighbourNode2);

        assertEquals(1, node.lengthTo(neighbourNode1));
        assertEquals(1, node.lengthTo(neighbourNode2));
        assertEquals(1, neighbourNode1.lengthTo(node));
        assertEquals(1, neighbourNode2.lengthTo(node));
    }
}
