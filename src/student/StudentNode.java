package student;

import java.util.Set;

/**
 * Abstract class representing <code>Node</code>s. Used by the <code>Pathfinder</code> class, so
 * that the same method can be used for finding the shortest path in both the explore phase (which
 * has no access to the <code>Node</code> class) and the escape phase (which does have access to
 * the <code>Node</code> class).
 * @param <T> the subclass itself - this parameter allows the neighbours() method to return a
 *           <code>Set</code> of instances of the subclass, rather than a <code>Set</code> of
 *           <code>StudentNode</code>s
 */
public abstract class StudentNode<T extends StudentNode<T>> {
    /**
     * The id of the node
     */
    final private long id;

    /**
     * Constructor for StudentNode.
     * @param id the node's id
     */
    public StudentNode(long id) {
        this.id = id;
    }

    /**
     * Gets the id of the StudentNode
     * @return the id of the StudentNode
     */
    public long id() {
        return id;
    }

    /**
     * Returns the set of nodes which are known to neighbour this node. <code><T></code> will be the
     * subclass, meaning that any concrete implementation of <code>StudentNode<T></code> will return
     * a Set of its own instances, not a <code>Set<StudentNode></code>.
     * @return the neighbours of this node
     */
    public abstract Set<T> neighbours();

    /**
     * Gets the length from this StudentNode to a given neighbour
     * @param neighbour the given neighbour
     * @return the length from this StudentNode to a given neighbour
     */
    public abstract int lengthTo(T neighbour);
}