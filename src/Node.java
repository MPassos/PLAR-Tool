
public class Node {
    private String node;
    private String depends;
    
    /**
     * Class constructor
     * @param node receives the node name
     * @param depends receives the node dependency
     */
    public Node(String node, String depends) {
        this.node = node;
        this.depends = depends;
    }
    
    /**
     * Method to get node's name
     * @return node's name
     */
    public String getNode() {
        return node;
    }

    /**
     * Method to set node's name
     * @param node name that will be seted
     */
    public void setNode(String node) {
        this.node = node;
    }
    
    /**
     * Method to get the node dependency
     * @return node dependency name
     */
    public String getDepends() {
        return depends;
    }
    
    /**
     * Method to set the node dependency
     * @param depends dependency to be set
     */
    public void setDepends(String depends) {
        this.depends = depends;
    }
    
    
}
