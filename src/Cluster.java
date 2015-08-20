
import java.util.Vector;

public class Cluster {
    
    private String clustername;
    private Vector elements;
    
    /**
     * Class Constructor
     * @param clustername receives the name of the cluster 
     */
    public Cluster(String clustername) {
        this.clustername = clustername;
    }
    
    /**
     * Method to get cluster's name
     * @return cluster's name
     */
    public String getClustername() {
        return clustername;
    }

    /**
     * Method to set the cluster name
     * @param clustername name to be set
     */
    public void setClustername(String clustername) {
        this.clustername = clustername;
    }
    
    /**
     * Method to get vector storing all the cluster's elements
     * @return Vector with cluster's elements
     */
    public Vector getElements() {
        return elements;
    }

    /**
     * Method to set the Vector containing all the elements of the cluster
     * @param elements Vector that will be set.
     */
    public void setElements(Vector elements) {
        this.elements = new Vector(elements); // É necessário criar um novo objeto e não uma referência pro vetor.
    }
    
    
    
}
