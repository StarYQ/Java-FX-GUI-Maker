import java.io.FileNotFoundException;
import java.util.Scanner;
import java.io.File;
import java.io.PrintWriter;

/**
 * Represents a tree representation of a JavaFX GUI modeled after SceneBuilder
 */
public class FXComponentTree {
    private FXTreeNode root; //root of the tree
    private FXTreeNode cursor; //cursor to traverse through the tree when user inputs are taken

    /**
     * returns a tree with the root node being the AnchorPane and the cursor set at the root
     */
    public FXComponentTree(){
        root=new FXTreeNode(ComponentType.AnchorPane, null);
        cursor=root;
    }

    /**
     * Sets the tree's cursor to its root node
     */
    public void cursorToRoot(){
        cursor=root;
    }

    /**
     * Removes the child at the specified index of the FXComponentTree, as well as all of its children.
     * @param index index of the child node to delete within children array of the node at which the cursor is
     * @return the deleted child node
     * @throws IndexOutOfBoundsException if the index is negative or greater than the highest index at which the array is not null
     */
    public FXTreeNode deleteChild(int index) throws IndexOutOfBoundsException{
        FXTreeNode deletedNode = null;
        if (index>cursor.numChildren()-1 || index<0){
            throw new IndexOutOfBoundsException("IndexOutOfBounds");
        }
        else {
            deletedNode = cursor.getChildren()[index];
            for (int i = index; i < cursor.numChildren(); i++) {
                cursor.getChildren()[i] = cursor.getChildren()[i + 1];
            }
        }
        return deletedNode;
    }
    /**
     * Changes the text attribute of the node at which the cursor is
     * @param text text to change the text attribute of the node to
     */
    public void setTextAtCursor(String text){
        cursor.setText(text);
    }

    /**
     * Moves the cursor to the child node of the cursor corresponding to the specified index
     * @param index index of the children array of the node at which the cursor is to move the cursor to
     * @throws InvalidIndexException if the index is not 0 if the cursor is at the root, since the root can only have 1 child
     */
    public void cursorToChild(int index) throws InvalidIndexException{
        if (cursor==root){
            if (index!=0){
                throw new InvalidIndexException("Invalid");
            }
        }
        cursor=cursor.getChildren()[index];
    }

    /**
     * Moves the cursor to the parent of the current node
     */
    public void cursorToParent(){
        cursor=cursor.getParent();
    }

    /**
     * Getter method for the tree's cursor
     * @return the tree's cursor node
     */
    public FXTreeNode getCursor(){
        return cursor;
    }

    /**
     * Finds the depth of a specified node of the tree
     * @param node node of the tree to find the depth of
     * @return the depth of the node
     */
    public int findDepth(FXTreeNode node){
        FXTreeNode curr=node;
        int depth=0;
        while (curr!=root){
            depth++;
            curr=curr.getParent();
        }
        return depth;
    }

    /**
     * Provides a string containing how many tabs a node should have printed before it when the tree is printed
     * @param depth the depth of the node within the tree, which is how many tabs should be printed before it
     * @return a string containing how many tabs a node should have printed before it
     */
    public String addTabs(int depth){
        String str= "";
        for (int i=0; i<depth; i++){
            str+="\t";
        }
        return str;
    }

    /**
     * Recursively prints the nodes of the tree
     * @param curr the root node of the tree
     */
    public void printTree(FXTreeNode curr){
        if (curr == null) {
            return;
        }
        System.out.print(addTabs(findDepth(curr)));
        System.out.println(curr.toTreeString(this));
        for (int i=0; i<curr.numChildren(); i++){
            if (curr.getChildren()==null){
                continue;
            }
            else {
                printTree(curr.getChildren()[i]);
            }
        }
    }

    /**
     * Method to return a string representation of the contents of an FXML file
     * @param filename name of file to print contents of
     * @return a string representation of an FXML file's contents
     * @throws FileNotFoundException if a file with the specified name cannot be found
     */
    public String readFromFXMLFile(String filename) throws FileNotFoundException{
        String str="";
        File file = new File(filename);
        Scanner input = new Scanner(file);
        while (input.hasNextLine()){
            String line = input.nextLine();
            str+=line;
            str+="\n";
        }
        input.close();
        return str;
    }

    /**
     * Method to load in an FXComponentTree based on the information from a provided text file
     * @param filename name of file to load from
     * @return an FXComponentTree matching the node information provided in the text file
     * @throws FileNotFoundException if a file with the specified file name is not found
     */
    public FXComponentTree readFromFile(String filename) throws FileNotFoundException {
        File file = new File(filename);
        Scanner input = new Scanner(file);
        while (input.hasNextLine()){
            String line=input.nextLine();
            String[] parts=line.split(" ", 2);
            String pos=parts[0];
            int index=Integer.parseInt(pos.substring(pos.length()-1));
            if (parts[1].contains(" ")){
                String[] parts2=parts[1].split(" ", 2);
                String component=parts2[0];
                String text=parts2[1];
                FXTreeNode node=new FXTreeNode(findType(component), this.findParent(pos));
                node.setText(text);
                setChild(index, node, this.findParent(pos));
            }
            else{
                String component=parts[1];
                FXTreeNode node=new FXTreeNode(findType(component), this.findParent(pos));
                setChild(index, node, this.findParent(pos));
            }
        }
        return this;
    }

    /**
     * Adds the given node to the corresponding index of the children array
     * @param index index at which to add the new child node to
     * @param node the child node to add to the cursor node's children array
     * @throws InvalidIndexException if the node is a control, meaning it cannot have child nodes
     * @throws IndexOutOfBoundsException if adding the node at the specified index makes a hole in the array
     * @throws FullNodeException if the children array of the node is already full
     */
    public void addChild(int index, FXTreeNode node) throws
            InvalidIndexException, IndexOutOfBoundsException, FullNodeException{
        if (cursor.getType()!=ComponentType.HBox && cursor.getType()!=ComponentType.VBox
        && cursor.getType()!=ComponentType.AnchorPane){
            throw new InvalidIndexException("Invalid");
        }
        else if (index>cursor.numChildren()){
            throw new IndexOutOfBoundsException("IndexOutOfBounds");
        }
        else if (cursor.numChildren()==10){
            throw new FullNodeException("Full node");
        }
        else if (index==cursor.numChildren()){
            cursor.getChildren()[index]=node;
        }
        else if (index<cursor.numChildren()){
            for (int i=cursor.numChildren(); i>index; i--){
                cursor.getChildren()[i]=cursor.getChildren()[i-1];
            }
            cursor.getChildren()[index]=node;
        }
    }

    /**
     * Sets the element at a specified index of a children array of a specified parent node to a specified child node
     * @param index index of the children array at which to set the element as the child node
     * @param node child node to be added to the parent node's children array
     * @param parent parent node to add the child node to
     */
    public void setChild(int index, FXTreeNode node, FXTreeNode parent){
        parent.getChildren()[index]=node;
    }

    /**
     * Finds the parent node of a node to be added to the tree based on a string specifying the node's position within the tree
     * @param pos string containing node's position within the tree
     * @return the node that should be the parent of the specified node in the treee
     */
    public FXTreeNode findParent(String pos){
        String[] indices=pos.split("-", 0);
        if (indices.length==2){
            return root;
        }
        else{
            FXTreeNode cursor2=root;
            for (int i=1; i<indices.length-1; i++){ //the last index stores the children array index for the parent node
                cursor2=cursor2.getChildren()[Integer.parseInt(indices[i])];
            }
            return cursor2;
        }
    }

    /**
     * Finds what ComponentType enum a node's type attribute should be based on its string version
     * @param str the string version of the ComponentType of a node
     * @return ComponentType enum matching the string input
     */
    public ComponentType findType(String str){
        if (str.equals("Button")){
            return ComponentType.Button;
        }
        else if (str.equals("Label")){
            return ComponentType.Label;
        }
        else if (str.equals("TextArea")){
            return ComponentType.TextArea;
        }
        else if (str.equals("HBox")){
            return ComponentType.HBox;
        }
        else{
            return ComponentType.VBox;
        }
    }

    /**
     * Getter method for the tree's root
     * @return the tree's root node
     */
    public FXTreeNode getRoot(){
        return this.root;
    }

    /**
     * Generates a text file that reflects the structure of the FXComponentTree
     * @param filename name of the file to write the tree in
     * @throws FileNotFoundException if a file with the given name cannot be written in
     */
    public void writeToFile(String filename) throws FileNotFoundException {
        File file = new File(filename);
        PrintWriter pw = new PrintWriter(file);
        FXTreeNode curr = root;
        printNodesInFile(pw, curr);
        pw.close();
    }

    /**
     * Helper method to recursively write the tree nodes in a text file
     * @param pw PrintWriter object to write in the file
     * @param curr root node of the tree
     */
    public void printNodesInFile(PrintWriter pw, FXTreeNode curr){
        if (curr == null) {
            return;
        }
        pw.print(addTabs(findDepth(curr)));
        pw.println(curr.toTreeString(this));
        for (int i=0; i<curr.numChildren(); i++){
            if (curr.getChildren()==null){
                continue;
            }
            else {
                printNodesInFile(pw, curr.getChildren()[i]);
            }
        }
    }
    //for FXML file output
    /**
     * Creates a valid FXML file that can be opened in SceneBuilder
     * @param tree FXComponentTree to write the contents of in fxml
     * @param filename name of FXML file to export to
     * @throws FileNotFoundException if unable to write in file with specified name
     */
    public static void exportToFXML(FXComponentTree tree, String filename) throws FileNotFoundException {
        File file = new File(filename);
        PrintWriter pw = new PrintWriter(file);
        pw.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        pw.println("<?import javafx.scene.control.*?>");
        pw.println("<?import javafx.scene.layout.*?>");
        pw.println("<AnchorPane xmlns:fx=\"http://javafx.com/fxml\">");
        if (tree.getRoot().numChildren()!=0){
            FXTreeNode curr = tree.getRoot().getChildren()[0];
            tree.printNodesInFXMLFile(pw, curr);
        }
        pw.println("</AnchorPane>");
        pw.close();
        System.out.println(filename + " saved to computer");
    }

    /**
     * Recursively prints the nodes of the FXComponentTree while abiding by FXML format
     * @param pw PrintWriter object to write the contents of the file
     * @param curr the first child of the tree's root node if applicable (root node is already printed regardless)
     */
    public void printNodesInFXMLFile(PrintWriter pw, FXTreeNode curr){
        if (curr == root) {
            return;
        }
        pw.print(addTabs(findDepth(curr)));
        if (curr.getType()==ComponentType.Button || curr.getType()==ComponentType.Label) {
            pw.println("<" + curr.getType().toString() + " text=\"" + curr.getText() + "\"/>");
        }
        else if (curr.getType()==ComponentType.TextArea){
            String str = curr.getText();
            String id = "\"";
            str = str.trim();
            String delims = "\\W+";
            String[] words = str.split(delims);
            id+=words[0].substring(0,1).toLowerCase()+words[0].substring(1);
            if (words.length>1){
                for (int i=1; i<words.length; i++){
                    id+=words[i].substring(0,1).toUpperCase()+words[i].substring(1);
                }
            }
            id+="\"";
            pw.println("<" + curr.getType().toString() + " fx:id=" + id + " text=\"" + curr.getText() + "\"/>");
        }
        else{
            pw.println("<" + curr.getType().toString() + ">");
        }
        for (int i=0; i<curr.numChildren(); i++){
            if (curr.getChildren()==null){
                continue;
            }
            else {
                printNodesInFXMLFile(pw, curr.getChildren()[i]);
            }
        }
        if (curr.getType()==ComponentType.HBox || curr.getType()==ComponentType.VBox){
            pw.print(addTabs(findDepth(curr)));
            pw.println("</" + curr.getType().toString() + ">");
        }
    }
}
