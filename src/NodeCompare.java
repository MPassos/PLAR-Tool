
import java.util.Vector;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Mateus
 */
public class NodeCompare{
   private Vector<String> nodename;
   private Vector<Node> dep;
   private String pname;

    public NodeCompare() {
        nodename = new Vector(10,1);
        dep = new Vector(10,1);
    }
    
    public Vector getNodename() {
        return nodename;
    }

    public void setNodename(Vector nodename) {
        this.nodename = nodename;
    }

    public Vector<Node> getDep() {
        return dep;
    }

    public void setDep(Vector<Node> dep) {
        this.dep = dep;
    }
    
    public String getPname()
    {
        return pname;
    }
    
    public void setPName(String p)
    {
        pname = p;
    }
    
}
