/**
 * Represents the component type of a node
 */
public enum ComponentType {
    Button, Label, TextArea, HBox, VBox, AnchorPane; //possible component types of a node

    /**
     * Provides a matching string representation of the component type
     * @return a string representation of the component type
     */
    public String toString(){
        if (this==ComponentType.Button){
            return "Button";
        }
        else if (this==ComponentType.Label){
            return "Label";
        }
        else if (this==ComponentType.TextArea){
            return "TextArea";
        }
        else if (this==ComponentType.HBox){
            return "HBox";
        }
        else if (this==ComponentType.VBox){
            return "VBox";
        }
        else{
            return "AnchorPane";
        }
    }
}
