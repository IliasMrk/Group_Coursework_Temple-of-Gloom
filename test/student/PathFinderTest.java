package student;

import game.Edge;
import game.Node;
import org.junit.jupiter.api.Test;
import student.escape.EscapeNode;
import student.explore.ExploreNode;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Test class for PathFinder
 */
public class PathFinderTest {
    private static long idCounter = 300; // Increment to give unique IDs to each mocked Node

    /**
     * Tests findShortestPath(), using ExploreNodes, when the start and end points are the same
     */
    @Test
    void findShortestPathWithExploreNodesAlreadyThere() {
        ExploreNode en = new ExploreNode(idCounter++, 10);
        List<ExploreNode> path = PathFinder.findShortestPath(en, en);
        // Should contain one ExploreNode - en itself
        assertEquals(1, path.size());
        assertEquals(en, path.get(0));
    }

    /**
     * Tests findShortestPath(), using ExploreNodes, when the start and end points are next to each
     * other
     */
    @Test
    void findShortestPathWithExploreNodesOneStepAway() {
        ExploreNode en1 = new ExploreNode(idCounter++, 15);
        ExploreNode en2 = new ExploreNode(idCounter++, 16);
        en1.addEdge(en2);
        List<ExploreNode> path = PathFinder.findShortestPath(en1, en2);
        // Should contain en1 and en2
        assertEquals(2, path.size());
        assertEquals(en1, path.get(0));
        assertEquals(en2, path.get(1));
    }

    /**
     * Tests findShortestPath(), using ExploreNodes, when there are two possible paths from the
     * start to the end point, but one is shorter than the other
     */
    @Test
    void findShortestPathWithExploreNodesTwoPossiblePaths() {
        // Uses the following ExploreNodes:
        // a b c
        // d   e
        // f g h
        // Find a path from a to g (should find a-d-f-g)

        // Set up the ExploreNodes with appropriate edges
        ExploreNode a = new ExploreNode(idCounter++, 6);
        ExploreNode b = new ExploreNode(idCounter++, 5);
        ExploreNode c = new ExploreNode(idCounter++, 4);
        ExploreNode d = new ExploreNode(idCounter++, 5);
        ExploreNode e = new ExploreNode(idCounter++, 3);
        ExploreNode f = new ExploreNode(idCounter++, 6);
        ExploreNode g = new ExploreNode(idCounter++, 5);
        ExploreNode h = new ExploreNode(idCounter++, 4);
        a.addEdge(b);
        a.addEdge(d);
        b.addEdge(c);
        c.addEdge(e);
        d.addEdge(f);
        e.addEdge(h);
        f.addEdge(g);
        g.addEdge(h);

        List<ExploreNode> path = PathFinder.findShortestPath(a, g);
        // Should contain a, d, f, g
        assertEquals(4, path.size());
        assertEquals(a, path.get(0));
        assertEquals(d, path.get(1));
        assertEquals(f, path.get(2));
        assertEquals(g, path.get(3));
    }

    /**
     * Tests findShortestPath(), using EscapeNodes, when the start and end points are the same
     */
    @Test
    void findShortestPathWithEscapeNodesAlreadyThere() {
        Node n = mock(Node.class);
        when(n.getId()).thenReturn(idCounter++);
        EscapeNode en = EscapeNode.getEscapeNode(n);
        List<EscapeNode> path = PathFinder.findShortestPath(en, en);
        // Should contain one ExploreNode - en itself
        assertEquals(1, path.size());
        assertEquals(en, path.get(0));
    }

    /**
     * Tests findShortestPath(), using EscapeNodes, when the start and end points are next to each
     * other
     */
    @Test
    void findShortestPathWithEscapeNodesOneStepAway() {
        // Set up the underlying Nodes
        Node n1 = mock(Node.class);
        Node n2 = mock(Node.class);
        when(n1.getId()).thenReturn(idCounter++);
        when(n2.getId()).thenReturn(idCounter++);
        when(n1.getNeighbours()).thenReturn(Set.of(n2));
        when(n2.getNeighbours()).thenReturn(Set.of(n1));

        // Set up the underlying Edges
        Edge e = mock(Edge.class);
        when(e.length()).thenReturn(50);
        when(n1.getEdge(n2)).thenReturn(e);
        when(n2.getEdge(n1)).thenReturn(e);

        // Create EscapeNodes using the underlying Nodes
        EscapeNode en3 = EscapeNode.getEscapeNode(n1);
        EscapeNode en4 = EscapeNode.getEscapeNode(n2);

        List<EscapeNode> path = PathFinder.findShortestPath(en3, en4);
        // Should contain en1 and en2
        assertEquals(2, path.size());
        assertEquals(en3, path.get(0));
        assertEquals(en4, path.get(1));
    }

    /**
     * Tests findShortestPath(), using EscapeNodes, when there are two possible paths from the
     * start to the end point, but one is shorter than the other (both in terms of total steps and
     * total length)
     */
    @Test
    void findShortestPathWithEscapeNodesTwoPossiblePathsEdgesWithEqualLength() {
        // Uses the following EscapeNodes:
        // a b c
        // d   e
        // f g h
        // All edges are length 50
        // Find a path from a to g (should find a-d-f-g)

        // Set up the underlying Nodes
        Node na = mock(Node.class); when(na.getId()).thenReturn(idCounter++);
        Node nb = mock(Node.class); when(nb.getId()).thenReturn(idCounter++);
        Node nc = mock(Node.class); when(nc.getId()).thenReturn(idCounter++);
        Node nd = mock(Node.class); when(nd.getId()).thenReturn(idCounter++);
        Node ne = mock(Node.class); when(ne.getId()).thenReturn(idCounter++);
        Node nf = mock(Node.class); when(nf.getId()).thenReturn(idCounter++);
        Node ng = mock(Node.class); when(ng.getId()).thenReturn(idCounter++);
        Node nh = mock(Node.class); when(nh.getId()).thenReturn(idCounter++);
        when(na.getNeighbours()).thenReturn(Set.of(nb, nd));
        when(nb.getNeighbours()).thenReturn(Set.of(na, nc));
        when(nc.getNeighbours()).thenReturn(Set.of(nb, ne));
        when(nd.getNeighbours()).thenReturn(Set.of(na, nf));
        when(ne.getNeighbours()).thenReturn(Set.of(nc, nh));
        when(nf.getNeighbours()).thenReturn(Set.of(nd, ng));
        when(ng.getNeighbours()).thenReturn(Set.of(nf, nh));
        when(nh.getNeighbours()).thenReturn(Set.of(ne, ng));

        // Set up the underlying Edges
        Edge ab = mock(Edge.class); when(ab.length()).thenReturn(50);
        Edge bc = mock(Edge.class); when(bc.length()).thenReturn(50);
        Edge ce = mock(Edge.class); when(ce.length()).thenReturn(50);
        Edge ad = mock(Edge.class); when(ad.length()).thenReturn(50);
        Edge df = mock(Edge.class); when(df.length()).thenReturn(50);
        Edge eh = mock(Edge.class); when(eh.length()).thenReturn(50);
        Edge fg = mock(Edge.class); when(fg.length()).thenReturn(50);
        Edge gh = mock(Edge.class); when(gh.length()).thenReturn(50);

        // Link the Nodes and Edges
        when(na.getEdge(nb)).thenReturn(ab);
        when(nb.getEdge(na)).thenReturn(ab);
        when(nb.getEdge(nc)).thenReturn(bc);
        when(nc.getEdge(nb)).thenReturn(bc);
        when(nc.getEdge(ne)).thenReturn(ce);
        when(ne.getEdge(nc)).thenReturn(ce);
        when(na.getEdge(nd)).thenReturn(ad);
        when(nd.getEdge(na)).thenReturn(ad);
        when(nd.getEdge(nf)).thenReturn(df);
        when(nf.getEdge(nd)).thenReturn(df);
        when(ne.getEdge(nh)).thenReturn(eh);
        when(nh.getEdge(ne)).thenReturn(eh);
        when(nf.getEdge(ng)).thenReturn(fg);
        when(ng.getEdge(nf)).thenReturn(fg);
        when(ng.getEdge(nh)).thenReturn(gh);
        when(nh.getEdge(ng)).thenReturn(gh);

        // Create the EscapeNodes representing the underlying Nodes
        EscapeNode a = EscapeNode.getEscapeNode(na);
        EscapeNode b = EscapeNode.getEscapeNode(nb);
        EscapeNode c = EscapeNode.getEscapeNode(nc);
        EscapeNode d = EscapeNode.getEscapeNode(nd);
        EscapeNode e = EscapeNode.getEscapeNode(ne);
        EscapeNode f = EscapeNode.getEscapeNode(nf);
        EscapeNode g = EscapeNode.getEscapeNode(ng);
        EscapeNode h = EscapeNode.getEscapeNode(nh);

        List<EscapeNode> path = PathFinder.findShortestPath(a, g);
        // Should contain a, d, f, g
        assertEquals(4, path.size());
        assertEquals(a, path.get(0));
        assertEquals(d, path.get(1));
        assertEquals(f, path.get(2));
        assertEquals(g, path.get(3));
    }

    /**
     * Tests findShortestPath(), using EscapeNodes, when there are two possible paths from the
     * start to the end point, but the shorter path in terms of length is the longer path in terms
     * of steps. (In such instances, we care about which path is shorter in terms of length.)
     */
    @Test
    void findShortestPathWithEscapeNodesTwoPossiblePathsEdgesWithDifferentLengths() {
        // Uses the following EscapeNodes:
        // a b c
        // d   e
        // f g h
        // BUT this time a-b-c-e-h-g should be shorter, as the lengths of the edges are changed

        // Set up the underlying Nodes
        Node na = mock(Node.class); when(na.getId()).thenReturn(idCounter++);
        Node nb = mock(Node.class); when(nb.getId()).thenReturn(idCounter++);
        Node nc = mock(Node.class); when(nc.getId()).thenReturn(idCounter++);
        Node nd = mock(Node.class); when(nd.getId()).thenReturn(idCounter++);
        Node ne = mock(Node.class); when(ne.getId()).thenReturn(idCounter++);
        Node nf = mock(Node.class); when(nf.getId()).thenReturn(idCounter++);
        Node ng = mock(Node.class); when(ng.getId()).thenReturn(idCounter++);
        Node nh = mock(Node.class); when(nh.getId()).thenReturn(idCounter++);
        when(na.getNeighbours()).thenReturn(Set.of(nb, nd));
        when(nb.getNeighbours()).thenReturn(Set.of(na, nc));
        when(nc.getNeighbours()).thenReturn(Set.of(nb, ne));
        when(nd.getNeighbours()).thenReturn(Set.of(na, nf));
        when(ne.getNeighbours()).thenReturn(Set.of(nc, nh));
        when(nf.getNeighbours()).thenReturn(Set.of(nd, ng));
        when(ng.getNeighbours()).thenReturn(Set.of(nf, nh));
        when(nh.getNeighbours()).thenReturn(Set.of(ne, ng));

        // Set up the underlying Edges
        Edge ab = mock(Edge.class); when(ab.length()).thenReturn(4);
        Edge bc = mock(Edge.class); when(bc.length()).thenReturn(6);
        Edge ce = mock(Edge.class); when(ce.length()).thenReturn(3);
        Edge ad = mock(Edge.class); when(ad.length()).thenReturn(10);
        Edge df = mock(Edge.class); when(df.length()).thenReturn(2);
        Edge eh = mock(Edge.class); when(eh.length()).thenReturn(4);
        Edge fg = mock(Edge.class); when(fg.length()).thenReturn(12);
        Edge gh = mock(Edge.class); when(gh.length()).thenReturn(6);
        when(na.getEdge(nb)).thenReturn(ab);
        when(nb.getEdge(na)).thenReturn(ab);
        when(nb.getEdge(nc)).thenReturn(bc);
        when(nc.getEdge(nb)).thenReturn(bc);
        when(nc.getEdge(ne)).thenReturn(ce);
        when(ne.getEdge(nc)).thenReturn(ce);
        when(na.getEdge(nd)).thenReturn(ad);
        when(nd.getEdge(na)).thenReturn(ad);
        when(nd.getEdge(nf)).thenReturn(df);
        when(nf.getEdge(nd)).thenReturn(df);
        when(ne.getEdge(nh)).thenReturn(eh);
        when(nh.getEdge(ne)).thenReturn(eh);
        when(nf.getEdge(ng)).thenReturn(fg);
        when(ng.getEdge(nf)).thenReturn(fg);
        when(ng.getEdge(nh)).thenReturn(gh);
        when(nh.getEdge(ng)).thenReturn(gh);

        // Set up the EscapeNodes representing the underlying Nodes
        EscapeNode a = EscapeNode.getEscapeNode(na);
        EscapeNode b = EscapeNode.getEscapeNode(nb);
        EscapeNode c = EscapeNode.getEscapeNode(nc);
        EscapeNode d = EscapeNode.getEscapeNode(nd);
        EscapeNode e = EscapeNode.getEscapeNode(ne);
        EscapeNode f = EscapeNode.getEscapeNode(nf);
        EscapeNode g = EscapeNode.getEscapeNode(ng);
        EscapeNode h = EscapeNode.getEscapeNode(nh);

        List<EscapeNode> path = PathFinder.findShortestPath(a, g);
        // Should contain a, b, c, e, h, g
        assertEquals(6, path.size());
        assertEquals(a, path.get(0));
        assertEquals(b, path.get(1));
        assertEquals(c, path.get(2));
        assertEquals(e, path.get(3));
        assertEquals(h, path.get(4));
        assertEquals(g, path.get(5));
    }
}
