/**
 * Exception to be thrown if user tries to add a child to a node that has a full children array
 */
public class FullNodeException extends Exception {
    public FullNodeException(String message) {
        super(message);
    }
}
