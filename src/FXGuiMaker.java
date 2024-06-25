import java.io.*;
import java.util.Scanner;

/**
 * Allows the user to load an FXComponentTree with information from a text file and edit it, as well to export it
 */
public class FXGuiMaker {
    private static FXComponentTree tree = new FXComponentTree();
    private static String FXMLFile;

    /**
     * Runs a menu-driven application which first creates an FXComponentTree based on the passed in file and then prompts
     * the user for a menu command selecting the operation. The required information is then requested from the user
     * based on the selected operation
     * @param args command line arguments
     * @throws InvalidIndexException if an index is entered for control node or if an input index is negative
     * @throws FullNodeException if user attempts to add a child node to a node with full children array
     * @throws IndexOutOfBoundsException if user attempts to add to a node a child at an index greater than the greatest
     * non-null index or delete a child node at such an index
     */
    public static void main(String[] args) throws InvalidIndexException, FullNodeException, IndexOutOfBoundsException {
        Scanner sc = new Scanner(System.in);
        System.out.println("Welcome to counterfeit SceneBuilder.");
        while (true){
            printMenu();
            System.out.print("Please select an option: ");
            String option = sc.nextLine().toUpperCase();
            switch (option) {
                case "L":
                    System.out.println("Please enter filename:");
                    String filename = sc.nextLine();
                    if (filename.substring(filename.length()-4).equals("fxml")){
                        try{
                            FXMLFile = tree.readFromFXMLFile(filename);
                            System.out.println(filename + " loaded");
                        }
                        catch (Exception e){
                            System.out.println("File not found");
                            break;
                        }
                        break;
                    }
                    else {
                        try {
                            tree = tree.readFromFile(filename);
                            System.out.println(filename + " loaded");
                        } catch (FileNotFoundException e) {
                            System.out.println(filename + " not found");
                            break;
                        }
                        break;
                    }
                case "P":
                    if (FXMLFile!=null){
                        System.out.println(FXMLFile);
                    }
                    else{
                        tree.printTree(tree.getRoot());
                    }
                    break;
                case "C":
                    System.out.println("Please enter number of child (starting with 1):");
                    int index = sc.nextInt();
                    sc.nextLine();
                    try{
                        if (index>tree.getCursor().numChildren() || index<1){
                            throw new InvalidIndexException("Invalid");
                        }
                        tree.cursorToChild(index-1);
                        System.out.println("Cursor moved to " + tree.getCursor().toString());
                    }
                    catch (Exception e){
                        System.out.println(e.getMessage());
                    }
                    break;
                case "R":
                    tree.cursorToRoot();
                    System.out.println("Cursor is at root.");
                    break;
                case "A":
                    System.out.println("Select component type (H - HBox, V - VBox, T - TextArea, B - Button, L - Label):");
                    String component = sc.nextLine().toUpperCase();
                    if (component.equals("H")){
                        component = "HBox";
                    }
                    else if (component.equals("V")){
                        component = "VBox";
                    }
                    else if (component.equals("B")){
                        component = "Button";
                    }
                    else if (component.equals("L")){
                        component = "Label";
                    }
                    String text="";
                    ComponentType type = tree.findType(component);
                    boolean control = (type!=ComponentType.HBox && type!=ComponentType.VBox &&
                            type!=ComponentType.AnchorPane);
                    if (control){
                        System.out.println("Please enter text: ");
                        text = sc.nextLine();
                    }
                    System.out.println("Please enter an index: ");
                    int addIndex;
                    try {
                        addIndex = sc.nextInt();
                        sc.nextLine();
                        FXTreeNode node = new FXTreeNode(tree.findType(component), tree.getCursor());
                        if (control) {
                            node.setText(text);
                        }
                        tree.addChild(addIndex - 1, node);
                    }
                    catch (Exception e){
                        System.out.println("Invalid");
                        sc.nextLine();
                        break;
                    }
                    tree.cursorToChild(addIndex - 1);
                    System.out.println("Added");
                    break;
                case "U":
                    if (tree.getCursor()==tree.getRoot()){
                        System.out.println("Cursor already at root");
                    }
                    else{
                        tree.cursorToParent();
                        System.out.println("Cursor Moved to " + tree.getCursor().toString());
                    }
                    break;
                case "E":
                    System.out.println("Please enter new text: ");
                    String txt = sc.nextLine();
                    if (tree.getCursor().getType()==ComponentType.HBox ||
                            tree.getCursor().getType()==ComponentType.VBox ||
                            tree.getCursor().getType()==ComponentType.AnchorPane){
                        System.out.println("Cannot edit text");
                        break;
                    }
                    else{
                        tree.setTextAtCursor(txt);
                        System.out.println("Text Edited.");
                        break;
                    }
                case "D":
                    System.out.println("Please enter number of child (starting with 1): ");
                    int deleteIndex = sc.nextInt();
                    sc.nextLine();
                    try {
                        FXTreeNode deletedNode = tree.deleteChild(deleteIndex - 1);
                        System.out.println(deletedNode.toString() + " removed");
                    }
                    catch (Exception e){
                        System.out.println(e.getMessage());
                        break;
                    }
                    break;
                case "S":
                    System.out.println("Please enter a filename: ");
                    String file = sc.nextLine();
                    try {
                        tree.writeToFile(file);
                    }
                    catch (FileNotFoundException e){
                        System.out.println("File not found");
                        sc.nextLine();
                        break;
                    }
                    System.out.println(file + " saved to computer");
                    break;
                case "X":
                    System.out.println("Please enter a filename: ");
                    String fileNameFXML = sc.nextLine();
                    try{
                        FXComponentTree.exportToFXML(tree, fileNameFXML);
                    }
                    catch (Exception e){
                        System.out.println(e.getMessage());
                        sc.nextLine();
                    }
                    break;
                case "Q":
                    System.out.println("Make like a tree and leave!");
                    System.exit(0);
                default:
                    System.out.println("Please enter a valid choice.");
                    break;
            }
        }
    }

    /**
     * Helper method to print the option menu each time it should be shown in the console
     */
    public static void printMenu(){
        System.out.println("Menu:");
        System.out.println("\tL) Load from file");
        System.out.println("\tP) Print tree");
        System.out.println("\tC) Move cursor to a child node");
        System.out.println("\tR) Move cursor to root");
        System.out.println("\tA) Add child");
        System.out.println("\tU) Cursor up (to parent)");
        System.out.println("\tE) Edit text of cursor");
        System.out.println("\tD) Delete child");
        System.out.println("\tS) Save to file");
        System.out.println("\tQ) Quit");
    }
}