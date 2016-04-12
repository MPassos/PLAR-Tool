
import java.util.ArrayList;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Mateus
 */
public class GraphNode implements Comparable<GraphNode> {
    private String nodename;
    private boolean variability;
    private ArrayList<String> products;
    
    
    public GraphNode()
    {
        nodename = null;
        variability = true;
        products = new ArrayList<>(10);
    }

    public String getNodename() {
        return nodename;
    }

    public void setNodename(String nodename) {
        this.nodename = nodename;
    }
    
    public boolean isVariability() {
        return variability;
    }

    public void setVariability(boolean variability) {
        this.variability = variability;
    }

    public ArrayList getProducts() {
        return products;
    }

    public void setProducts(String products) {
        this.products.add(products);
    }

    @Override
    public int compareTo(GraphNode c) {
        return nodename.compareTo(c.nodename);
    }   
}
