/**
 * Exception to be thrown if the user attempts to add to a node a child at an index greater than the greatest
 * non-null index or delete a child node at such an index
 */
public class IndexOutOfBoundsException extends Exception {
    public IndexOutOfBoundsException(String message) {
        super(message);
    }
}
