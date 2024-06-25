/**
 * Exception to be thrown if user enters an index for a control node or if the input index is negative
 */
public class InvalidIndexException extends Exception {
    public InvalidIndexException(String message) {
        super(message);
    }
}
