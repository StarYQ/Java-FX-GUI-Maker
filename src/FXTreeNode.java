/**
 * Represents a node of an FXComponentTree
 */
public class FXTreeNode {
    private String text; //text attribute of a node
    private ComponentType type; //component type of node
    private FXTreeNode parent; //parent node of the node
    private FXTreeNode[] children; //children array of the node
    final int maxChildren=10; //capacity for the children array of the node

    /**
     * Creates a new node with a specified type and parent node
     * @param type the component type of the node
     * @param parent the parent node of the node
     */
    public FXTreeNode(ComponentType type, FXTreeNode parent){
        if (type==ComponentType.VBox || type==ComponentType.HBox
        || type==ComponentType.AnchorPane){
            this.text=null;
        }
        this.type=type;
        this.parent=parent;
        if (type!=ComponentType.VBox && type!=ComponentType.HBox && type!=ComponentType.AnchorPane){
            this.children=null;
        }
        else{
            this.children=new FXTreeNode[10];
        }
    }

    /**
     * Finds the number of children a node has
     * @return the number of children a node has
     */
    public int numChildren(){
        int count=0;
        FXTreeNode[] children=this.children;
        if (children==null){
            return -1;
        }
        else {
            for (int i = 0; i < maxChildren; i++) {
                if (children[i] == null) {
                    break;
                }
                count++;
            }
            return count;
        }
    }

    /**
     * Provides a string representation of a node
     * @return a string representation of a node's type and, if applicable, text
     */
    public String toString(){
        String str=this.getType().toString();
        if (this.getType()!=ComponentType.HBox && this.getType()!=ComponentType.VBox
                && this.getType()!=ComponentType.AnchorPane) {
            str+=": " + this.getText();
        }
        return str;
    }

    /**
     * Provides a string representation of a node's attributes when the tree it is in is printed
     * @param tree the tree which the node is a part of
     * @return a string representation of a node's attributes for when the tree it is in is printed
     */
    public String toTreeString(FXComponentTree tree){
        String str="";
        if (this==tree.getCursor()){
            str+="==>";
        }
        else{
            str+="+--";
        }
        str+=this.getType().toString();
        if (this.getType()!=ComponentType.HBox && this.getType()!=ComponentType.VBox
        && this.getType()!=ComponentType.AnchorPane) {
            str+=": " + this.getText();
        }
        return str;
    }

    /**
     * Getter method for a node's text attribute
     * @return a node's text
     */
    public String getText(){
        return this.text;
    }

    /**
     * Getter method for a node's component type
     * @return a node's component type enum
     */
    public ComponentType getType(){
        return this.type;
    }

    /**
     * Getter method for a node's parent node
     * @return a node's parent node
     */
    public FXTreeNode getParent(){
        return this.parent;
    }

    /**
     * Getter method for a node's children array
     * @return a node's children array
     */
    public FXTreeNode[] getChildren(){
        return this.children;
    }

    /**
     * Setter method for a node's text attribute
     * @param text the text to change a node's text attribute to
     */
    public void setText(String text){
        this.text=text;
    }

    /**
     * Setter method for a node's component type
     * @param type a node's component type
     */
    public void setType(ComponentType type){
        this.type=type;
    }

    /**
     * Setter method for a node's parent node
     * @param parent a node's parent node
     */
    public void setParent(FXTreeNode parent){
        this.parent=parent;
    }

    /**
     * Setter method for a node's children array
     * @param children an array to change a node's children array to
     */
    public void setChildren(FXTreeNode[] children){
        this.children=children;
    }
}
