
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
public class GraphDep {
    private String dependency;
    private boolean variability;
    private ArrayList<String> products;
    
    public GraphDep()
    {
        dependency = null;
        variability = true;
        products = new ArrayList<>(10); 
    }

    public String getDependency() {
        return dependency;
    }

    public void setDependency(String dependency) {
        this.dependency = dependency;
    }
  
    public boolean isVariability() {
        return variability;
    }

    public void setVariability(boolean variability) {
        this.variability = variability;
    }

    public ArrayList<String> getProducts() {
        return products;
    }

    public void setProducts(String products) {
        this.products.add(products);
    }
    
    
}
