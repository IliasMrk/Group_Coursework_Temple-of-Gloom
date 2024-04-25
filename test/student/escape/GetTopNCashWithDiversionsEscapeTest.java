package student.escape;

import game.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

/**
 * Test class for GetTopNCashWithDiversionsEscape
 */
public class GetTopNCashWithDiversionsEscapeTest {
    // Fields needed for setting up a mock maze
    private GetTopNCashWithDiversionsEscape strategy;
    private EscapeState mockState;
    private Node na, nb, nc, nd, ne, nf, ng, nh, ni, nj, nk;
    private Tile ta, tb, tc, td, te, tf, tg, th, ti, tj, tk;
    private Edge ab, bc, ce, ad, df, eh, fg, gh, hi, ij, jk;

    private static long idCounter = 0; // Increment this to give unique IDs to all mocked Nodes

    /**
     * Sets up the grid as follows:
     *
     * a b c
     * d   e
     * f g h i j k
     */
    @BeforeEach
    void setUp() {
        // Set up the strategy and the overall state:
        strategy = new GetTopNCashWithDiversionsEscape();
        mockState = mock(EscapeState.class);

        // Set up the Nodes:
        na = mock(Node.class); when(na.getId()).thenReturn(idCounter++);
        nb = mock(Node.class); when(nb.getId()).thenReturn(idCounter++);
        nc = mock(Node.class); when(nc.getId()).thenReturn(idCounter++);
        nd = mock(Node.class); when(nd.getId()).thenReturn(idCounter++);
        ne = mock(Node.class); when(ne.getId()).thenReturn(idCounter++);
        nf = mock(Node.class); when(nf.getId()).thenReturn(idCounter++);
        ng = mock(Node.class); when(ng.getId()).thenReturn(idCounter++);
        nh = mock(Node.class); when(nh.getId()).thenReturn(idCounter++);
        ni = mock(Node.class); when(ni.getId()).thenReturn(idCounter++);
        nj = mock(Node.class); when(nj.getId()).thenReturn(idCounter++);
        nk = mock(Node.class); when(nk.getId()).thenReturn(idCounter++);

        // Mock the neighbour-links between the nodes
        when(na.getNeighbours()).thenReturn(Set.of(nb, nd));
        when(nb.getNeighbours()).thenReturn(Set.of(na, nc));
        when(nc.getNeighbours()).thenReturn(Set.of(nb, ne));
        when(nd.getNeighbours()).thenReturn(Set.of(na, nf));
        when(ne.getNeighbours()).thenReturn(Set.of(nc, nh));
        when(nf.getNeighbours()).thenReturn(Set.of(nd, ng));
        when(ng.getNeighbours()).thenReturn(Set.of(nf, nh));
        when(nh.getNeighbours()).thenReturn(Set.of(ne, ng, ni));
        when(ni.getNeighbours()).thenReturn(Set.of(nh, nj));
        when(nj.getNeighbours()).thenReturn(Set.of(ni, nk));
        when(nk.getNeighbours()).thenReturn(Set.of(nj));

        // Set up the Tiles, and link them to the Nodes
        ta = mock(Tile.class); when(na.getTile()).thenReturn(ta);
        tb = mock(Tile.class); when(nb.getTile()).thenReturn(tb);
        tc = mock(Tile.class); when(nc.getTile()).thenReturn(tc);
        td = mock(Tile.class); when(nd.getTile()).thenReturn(td);
        te = mock(Tile.class); when(ne.getTile()).thenReturn(te);
        tf = mock(Tile.class); when(nf.getTile()).thenReturn(tf);
        tg = mock(Tile.class); when(ng.getTile()).thenReturn(tg);
        th = mock(Tile.class); when(nh.getTile()).thenReturn(th);
        ti = mock(Tile.class); when(ni.getTile()).thenReturn(ti);
        tj = mock(Tile.class); when(nj.getTile()).thenReturn(tj);
        tk = mock(Tile.class); when(nk.getTile()).thenReturn(tk);

        // Set up the Edges
        ab = mock(Edge.class); when(ab.length()).thenReturn(50);
        bc = mock(Edge.class); when(bc.length()).thenReturn(50);
        ce = mock(Edge.class); when(ce.length()).thenReturn(50);
        ad = mock(Edge.class); when(ad.length()).thenReturn(50);
        df = mock(Edge.class); when(df.length()).thenReturn(50);
        eh = mock(Edge.class); when(eh.length()).thenReturn(50);
        fg = mock(Edge.class); when(fg.length()).thenReturn(50);
        gh = mock(Edge.class); when(gh.length()).thenReturn(50);
        hi = mock(Edge.class); when(hi.length()).thenReturn(50);
        ij = mock(Edge.class); when(ij.length()).thenReturn(50);
        jk = mock(Edge.class); when(jk.length()).thenReturn(50);
        // Link the Nodes and Edges to each other
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
        when(nh.getEdge(ni)).thenReturn(hi);
        when(ni.getEdge(nh)).thenReturn(hi);
        when(ni.getEdge(nj)).thenReturn(ij);
        when(nj.getEdge(ni)).thenReturn(ij);
        when(nj.getEdge(nk)).thenReturn(jk);
        when(nk.getEdge(nj)).thenReturn(jk);

        // Finally, give the mockState its known set of Nodes:
        when(mockState.getVertices())
                .thenReturn(Set.of(na, nb, nc, nd, ne, nf, ng, nh, ni, nj, nk));
    }

    /**
     * Tests the algorithm's approach when no cash is available (should go straight to exit)
     */
    @Test
    void escapeWithNoCashAvailable() {
        // Uses the following ExploreNodes:
        // a b c
        // d   e
        // f g h i j k
        // Where a is the start Node and c is the exit Node
        // No cash is available

        when(mockState.getCurrentNode()).thenReturn(na);
        when(mockState.getExit()).thenReturn(nc);
        strategy.escape(mockState);

        // Should move twice - once to nb, and once to nc
        verify(mockState, times(2)).moveTo(any(Node.class));
        verify(mockState, times(1)).moveTo(nb);
        verify(mockState, times(1)).moveTo(nc);
    }

    /**
     * Tests the algorithm's approach when cash is available, but cannot be reached in time (should
     * go straight to exit)
     */
    @Test
    void escapeWithInsufficientTime() {
        // Uses the following ExploreNodes:
        // a b c
        // d   e
        // f g h i j k
        // Where a is the start Node and c is the exit Node
        // Only f and j have cash, but they are too far to reach within time
        // Should visit a-b-c
        when(mockState.getCurrentNode()).thenReturn(na);
        when(mockState.getExit()).thenReturn(nc);
        when(mockState.getTimeRemaining()).thenReturn(250);
        when(tf.getGold()).thenReturn(10);
        when(tj.getGold()).thenReturn(20);
        strategy.escape(mockState);

        // Should move twice - once to nb, and once to nc
        verify(mockState, times(2)).moveTo(any(Node.class));
        verify(mockState, times(1)).moveTo(nb);
        verify(mockState, times(1)).moveTo(nc);
    }

    /**
     * Tests the algorithm's approach when there is gold to be taken as part of the top n nodes, but
     * no further cash, so no diversions will be added.
     * Should go to the Nodes where cash is available, then go to the exit.
     */
    @Test
    void escapeWithNoDiversionsPossible() {
        // Uses the following ExploreNodes:
        // a b c
        // d   e
        // f g h i j k
        // Where a is the start Node and c is the exit Node
        // Only f and j have cash
        // Should visit a-d-f-g-h-i-j-i-h-e-c
        when(mockState.getCurrentNode()).thenReturn(na);
        when(mockState.getExit()).thenReturn(nc);
        when(mockState.getTimeRemaining()).thenReturn(2500);
        when(tf.getGold()).thenReturn(30);
        when(tj.getGold()).thenReturn(20);
        strategy.escape(mockState);

        // Should move ten times - a-d-f-h-i-j-i-h-e-c
        verify(mockState, times(10)).moveTo(any(Node.class));
        verify(mockState, times(1)).moveTo(nd);
        verify(mockState, times(1)).moveTo(nf);
        verify(mockState, times(1)).moveTo(ng);
        verify(mockState, times(2)).moveTo(nh);
        verify(mockState, times(2)).moveTo(ni);
        verify(mockState, times(1)).moveTo(nj);
        verify(mockState, times(1)).moveTo(ne);
        verify(mockState, times(1)).moveTo(nc);
    }

    /**
     * Tests the algorithm's approach when the top n nodes cannot be reached in time, but a
     * diversion can be made before exiting.
     * Should go to the nearest cash node, then to the exit.
     */
    @Test
    void escapeWithOneDiversionPossible() {
        // Uses the following ExploreNodes:
        // a b c
        // d   e
        // f g h i j k
        // Where a is the start Node and c is the exit Node
        // Only f and j have cash
        // j is worth more than f, but too far away; so initial path will fail to reach j
        // Diversion to f should be added
        // Should visit a-d-f-d-a-b-c
        when(mockState.getCurrentNode()).thenReturn(na);
        when(mockState.getExit()).thenReturn(nc);
        when(mockState.getTimeRemaining()).thenReturn(350);
        when(tf.getGold()).thenReturn(30);
        when(tj.getGold()).thenReturn(60);
        strategy.escape(mockState);

        // Should move six times - a-d-f-d-a-b-c
        verify(mockState, times(6)).moveTo(any(Node.class));
        verify(mockState, times(2)).moveTo(nd);
        verify(mockState, times(1)).moveTo(nf);
        verify(mockState, times(1)).moveTo(na);
        verify(mockState, times(1)).moveTo(nb);
        verify(mockState, times(1)).moveTo(nc);
    }
}
