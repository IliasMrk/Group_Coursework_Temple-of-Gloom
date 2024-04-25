package student.escape;

import game.Node;
import student.StudentNode;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Class for representing <code>Node</code>s in the escape phase.
 * Although <code>Node</code>s are directly available in the escape phase, adapting them using this
 * class allows them to be passed to the same method in the <code>PathFinder</code> class as
 * <code>ExploreNode</code>s, reducing code duplication.
 */
public class EscapeNode extends StudentNode<EscapeNode> {
    /**
     * The underlying <code>Node</code> of this instance
     */
    private final Node node;

    /**
     * Map of all <code>EscapeNode</code>s which have been instantiated.
     * By keeping track of what <code>EscapeNode</code>s have been created, the class can ensure
     * that new EscapeNodes are only created when needed.
     */
    private static final Map<Long, EscapeNode> escapeNodes = new HashMap<>();

    /**
     * Constructor for <code>EscapeNode</code>s, using the underlying Node for creating it.
     * Should ONLY be called by the getEscapeNode() method, to ensure that only one
     * <code>EscapeNode</code> is created per underlying <code>Node</code>.
     * @param node the underlying <code>Node</code>
     */
    private EscapeNode(Node node) {
        super(node.getId());
        this.node = node;
    }

    /**
     * Gets the <code>EscapeNode</code> corresponding to the given underlying <code>Node</code>.
     * If no such <code>EscapeNode</code> exists yet, it will be created.
     * @param node the underlying <code>Node</code>
     * @return the corresponding <code>EscapeNode</code>
     */
    public static EscapeNode getEscapeNode(Node node) {
        if (escapeNodes.containsKey(node.getId())) {
            return escapeNodes.get(node.getId());
        } else {
            EscapeNode newNode = new EscapeNode(node);
            escapeNodes.put(node.getId(), newNode);
            return newNode;
        }
    }

    /**
     * Gets the underlying <code>Node</code>.
     * @return the underlying <code>Node</code>
     */
    public Node node() { return this.node; }

    /**
     * Gets the neighbours of this <code>EscapeNode</code>.
     * @return the Set of the neighbours of this <code>EscapeNode</code>
     */
    @Override
    public Set<EscapeNode> neighbours() {
        return node.getNeighbours().stream()
                .map(EscapeNode::getEscapeNode)
                .collect(Collectors.toSet());
    }

    /**
     * Gets the length from this EscapeNode to a given neighbour
     * @param neighbour the given neighbour
     * @return the length from this EscapeNode to a given neighbour
     */
    @Override
    public int lengthTo(EscapeNode neighbour) {
        return this.node.getEdge(neighbour.node).length();
    }
}