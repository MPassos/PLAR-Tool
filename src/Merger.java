
import java.io.*;
import java.util.Vector;

/**
 *
 * @author Mateus
 */
public class Merger {

    private File product1;
    private File product2;
    private File folder;
    private FileReader leitor1;
    private FileReader leitor2;
    private FileWriter escritor;
    private BufferedReader lbuffer1;
    private BufferedReader lbuffer2;
    private BufferedWriter ebuffer1;
    private Vector no1;
    private Vector dependencias1;
    private Vector no2;
    private Vector dependencias2;
    private Vector<NodeCompare> folderdep;

    public Merger(File product1, File product2, String nomedoarquivo) throws FileNotFoundException {
        this.product1 = product1;
        this.product2 = product2;

        try {
            leitor1 = new FileReader(product1);
            leitor2 = new FileReader(product2);
            escritor = new FileWriter(nomedoarquivo);
            lbuffer1 = new BufferedReader(leitor1);
            lbuffer2 = new BufferedReader(leitor2);
            ebuffer1 = new BufferedWriter(escritor);
        } catch (IOException e) {
            System.out.println("Arquivo não encontrado " + e);
        }
    }

    public Merger(File folder, String output) throws FileNotFoundException, IOException {
        this.folder = folder;

        escritor = new FileWriter(output);
        ebuffer1 = new BufferedWriter(escritor);
    }

    public void getDependencias() throws IOException {
        no1 = new Vector(10, 1);
        no2 = new Vector(10, 1);
        dependencias1 = new Vector(10, 1);
        dependencias2 = new Vector(10, 1);
        int c1 = -1;
        int c2 = -1;
        char ch;
        Node node = new Node("", "");
        String nome1, nome2;
        nome1 = nome2 = "";

        while (lbuffer1.ready()) {
            while (c1 != 35) {
                /**
                 * The input file is subdivided into two sections that are
                 * seperated by the # character
                 */
                c1 = lbuffer1.read();
                /**
                 * Ignore numbers and space
                 */
                if ((c1 >= 48 && c1 <= 57) || c1 == 32) {
                    continue;
                }

                /**
                 * Ignores carriage \n
                 */
                if (c1 == 13) {
                    continue;
                }

                /**
                 * When the character read is a ENTER A new node is added to the
                 * Vector
                 */
                if (c1 == 10) {
                    no1.add(nome1);
                    nome1 = "";
                    continue;
                }

                ch = (char) c1;
                nome1 += Character.toString(ch);
            }
            nome1 = "";
            lbuffer1.readLine();

            while (c1 != -1) {
                c1 = lbuffer1.read();

                if (c1 == 32) {
                    if (node.getNode().equals("")) {
                        node.setNode((String) no1.get(Integer.parseInt(nome1) - 1));
                    } else {
                        node.setDepends((String) no1.get(Integer.parseInt(nome1) - 1));
                    }
                    nome1 = "";
                    continue;
                }

                if (c1 == 13) {
                    continue;
                }

                if (c1 == 10) {
                    dependencias1.add(node);
                    node = new Node("", "");
                    nome1 = "";
                    continue;
                }

                ch = (char) c1;
                nome1 += Character.toString(ch);
            }
        }

        while (lbuffer2.ready()) {
            while (c2 != 35) {
                /**
                 * The input file is subdivided into two sections that are
                 * seperated by the # character
                 */
                c2 = lbuffer2.read();
                /**
                 * Ignore numbers and space
                 */
                if ((c2 >= 48 && c2 <= 57) || c2 == 32) {
                    continue;
                }

                /**
                 * Ignores \n
                 */
                if (c2 == 13) {
                    continue;
                }

                /**
                 * When the character read is a ENTER A new node is added to the
                 * Vector
                 */
                if (c2 == 10) {
                    no2.add(nome2);
                    nome2 = "";
                    continue;
                }

                ch = (char) c2;
                nome2 += Character.toString(ch);
            }
            nome2 = "";
            lbuffer2.readLine();

            while (c2 != -1) {
                c2 = lbuffer2.read();

                if (c2 == 32) {
                    if (node.getNode().equals("")) {
                        node.setNode((String) no2.get(Integer.parseInt(nome2) - 1));
                    } else {
                        node.setDepends((String) no2.get(Integer.parseInt(nome2) - 1));
                    }
                    nome2 = "";
                    continue;
                }

                if (c2 == 13) {
                    continue;
                }

                if (c2 == 10) {
                    dependencias2.add(node);
                    node = new Node("", "");
                    nome2 = "";
                    continue;
                }

                ch = (char) c2;
                nome2 += Character.toString(ch);
            }
        }
    }

    //Test method
    public void printNode() {
        /*
         for (int i = 0; i < no1.size();i++)
         {
         System.out.println(no1.get(i));
         }
         for (int i = 0; i < no2.size();i++)
         {
         System.out.println(no2.get(i));
         }
         */
        Node no;
        for (int i = 0; i < dependencias1.size(); i++) {
            no = (Node) dependencias1.get(i);
            System.out.println(no.getNode() + " " + no.getDepends());
        }
    }

    public void generateGraph() throws IOException {
        int size1 = no1.size();
        int size2 = no2.size();
        int graphsize = size1 + size2;
        int i, j;
        i = j = 0;
        String comp1, comp2, dep1, dep2, var, com;
        var = com = "";

        ebuffer1.write("digraph G {\n" + "size= \"" + graphsize + "," + graphsize + "\";\n" + "rotate = 180;\n");
        while (i < size1 && j < size2) {
            comp1 = (String) no1.get(i);
            comp2 = (String) no2.get(j);

            if (comp1.compareTo(comp2) < 0) {
                ebuffer1.write("\"" + comp1 + "\"" + "[label=" + "\"" + comp1 + "\"" + ",shape=ellipse,color=purple,fontcolor=black,style=\"\"];\n");
                var += "\"" + no1.get(i) + "\";\n";
                i++;
            }
            if (comp1.compareTo(comp2) == 0) {
                ebuffer1.write("\"" + comp2 + "\"" + "[label=" + "\"" + comp2 + "\"" + ",shape=ellipse,color=blue,fontcolor=black,style=\"\"];\n");
                com += "\"" + no1.get(i) + "\";\n";
                i++;
                j++;
            }
            if (comp1.compareTo(comp2) > 0) {
                ebuffer1.write("\"" + comp2 + "\"" + "[label=" + "\"" + comp2 + "\"" + ",shape=ellipse,color=green,fontcolor=black,style=\"\"];\n");
                var += "\"" + no2.get(j) + "\";\n";
                j++;
            }
        }

        while (i < no1.size()) {
            comp1 = (String) no1.get(i);
            ebuffer1.write("\"" + comp1 + "\"" + "[label=" + "\"" + comp1 + "\"" + ",shape=ellipse,color=purple,fontcolor=black,style=\"\"];\n");
            var += "\"" + no1.get(i) + "\";\n";
            i++;
        }

        while (j < no2.size()) {
            comp2 = (String) no2.get(j);
            ebuffer1.write("\"" + comp2 + "\"" + "[label=" + "\"" + comp2 + "\"" + ",shape=ellipse,color=green,fontcolor=black,style=\"\"];\n");
            var += "\"" + no2.get(j) + "\";\n";
            j++;
        }

        i = j = 0;

        while (i < dependencias1.size() && j < dependencias2.size()) {
            Node aux1 = (Node) dependencias1.get(i);
            dep1 = aux1.getNode() + " " + aux1.getDepends();
            Node aux2 = (Node) dependencias2.get(j);
            dep2 = aux2.getNode() + " " + aux2.getDepends();

            if (dep1.compareTo(dep2) < 0) {
                ebuffer1.write("\"" + aux1.getNode() + "\"" + " -> " + "\"" + aux1.getDepends() + "\" " + "[color=purple,font=6];\n");
                i++;
            }
            if (dep1.compareTo(dep2) == 0) {
                ebuffer1.write("\"" + aux2.getNode() + "\"" + " -> " + "\"" + aux2.getDepends() + "\" " + "[color=\"#3366FF55\",font=6];\n");
                i++;
                j++;
            }

            if (dep1.compareTo(dep2) > 0) {
                ebuffer1.write("\"" + aux2.getNode() + "\"" + " -> " + "\"" + aux2.getDepends() + "\" " + "[color=green,font=6];\n");
                j++;
            }
        }

        while (i < dependencias1.size()) {
            Node aux1 = (Node) dependencias1.get(i);
            ebuffer1.write("\"" + aux1.getNode() + "\"" + " -> " + "\"" + aux1.getDepends() + "\" " + "[color=purple,font=6];\n");
            i++;
        }

        while (j < dependencias2.size()) {
            Node aux2 = (Node) dependencias2.get(j);
            ebuffer1.write("\"" + aux2.getNode() + "\"" + " -> " + "\"" + aux2.getDepends() + "\" " + "[color=green,font=6];\n");
            j++;
        }

        ebuffer1.write("subgraph cluster_0{\nlabel = \"Variability\";\n");
        ebuffer1.write(var);
        ebuffer1.write("}\n");

        ebuffer1.write("subgraph cluster_1{\nlabel = \"Similarities\" ;\n");
        ebuffer1.write(com);
        ebuffer1.write("}\n");

        ebuffer1.write("subgraph cluster_2{\nlabel = \"Legend\" ;\n");
        ebuffer1.write("\"Product 1\"[color=purple];\n ");
        ebuffer1.write("\"Product 2\"[color=green];\n ");
        ebuffer1.write("}\n");
        ebuffer1.write("}");
        ebuffer1.close();
    }

    public void getFolderDep() throws FileNotFoundException, IOException {
        File files[];
        files = folder.listFiles();
        folderdep = new Vector(10, 1);
        Vector no;
        Vector dep;
        int c1 = -1;
        char ch;
        Node node = new Node("", "");
        String nome1 = "";

        for (int i = 0; i < files.length; i++) {
            leitor1 = new FileReader(files[i]);
            lbuffer1 = new BufferedReader(leitor1);
            NodeCompare aux = new NodeCompare();
            no = aux.getNodename();
            dep = aux.getDep();

            while (lbuffer1.ready()) {

                while (c1 != 35) {
                    /**
                     * The input file is subdivided into two sections that are
                     * seperated by the # character
                     */
                    c1 = lbuffer1.read();
                    /**
                     * Ignore numbers and space
                     */
                    if ((c1 >= 48 && c1 <= 57) || c1 == 32) {
                        continue;
                    }

                    /**
                     * Ignores carriage \n
                     */
                    if (c1 == 13) {
                        continue;
                    }

                    /**
                     * When the character read is a ENTER A new node is added to
                     * the Vector
                     */
                    if (c1 == 10) {
                        no.add(nome1);

                        nome1 = "";
                        continue;
                    }

                    ch = (char) c1;
                    nome1 += Character.toString(ch);

                    //To get rid of strange characters
                    if (nome1.charAt(0) > 256) {
                        char caux = nome1.charAt(1);
                        nome1 = "";
                        nome1 += caux;
                    }

                }
                nome1 = "";
                lbuffer1.readLine();

                while (c1 != -1) {
                    c1 = lbuffer1.read();

                    if (c1 == 32) {
                        if (node.getNode().equals("")) {
                            node.setNode((String) no.get(Integer.parseInt(nome1) - 1));
                        } else {
                            node.setDepends((String) no.get(Integer.parseInt(nome1) - 1));
                        }
                        nome1 = "";
                        continue;
                    }

                    if (c1 == 13) {
                        continue;
                    }

                    if (c1 == 10) {
                        dep.add(node);
                        node = new Node("", "");
                        nome1 = "";
                        continue;
                    }

                    ch = (char) c1;
                    nome1 += Character.toString(ch);
                }

            }
            folderdep.add(aux);
        }
    }

    public void merge() throws IOException {
        Vector<GraphNode> nodes = new Vector(10, 1);
        Vector<GraphDep> deps = new Vector(10, 1);

        for (int i = 0; i < folderdep.size(); i++) {
            Vector<String> nodenames = folderdep.get(i).getNodename();
            Vector<Node> depnames = folderdep.get(i).getDep();
            for (int j = 0; j < nodenames.size(); j++) {
                GraphNode naux = new GraphNode();
                naux.setNodename(nodenames.get(j));
                naux.setProducts("Product " + (i + 1));

                if (nodes.isEmpty()) {
                    //nodes.add(naux);
                    nodes.add(new GraphNode()); //So the comparison can proceed

                }

                for (int k = 0; k < nodes.size(); k++) {

                    if (nodes.get(k).getNodename() == null) {
                        nodes.get(k).setNodename(naux.getNodename());
                        nodes.get(k).setProducts("Product " + (i + 1));
                        nodes.add(new GraphNode());
                        break;
                    }
                    if (nodes.get(k).getNodename().equals(naux.getNodename())) {
                        nodes.get(k).setVariability(false);
                        nodes.get(k).setProducts("Product " + (i + 1));
                        break;
                    }

                }
            }

            for (int a = 0; a < depnames.size(); a++) {
                GraphDep daux = new GraphDep();
                daux.setDependency(depnames.get(a).getNode() + " " + depnames.get(a).getDepends());
                daux.setProducts("Product " + (i + 1));

                if (deps.isEmpty()) {
                    //deps.add(daux);
                    deps.add(new GraphDep());
                }

                for (int b = 0; b < deps.size(); b++) {
                    if (deps.get(b).getDependency() == null) {
                        deps.get(b).setDependency(daux.getDependency());
                        deps.get(b).setProducts("Product " + (i + 1));
                        deps.add(new GraphDep());
                        break;
                    }
                    if (deps.get(b).getDependency().equals(daux.getDependency())) {
                        deps.get(b).setVariability(false);
                        deps.get(b).setProducts("Product " + (i + 1));
                        break;
                    }
                }
            }
        }
        printReport(nodes, deps);
        printBUNCH(deps);
    }

    public void printAll() {
        Vector<String> no;
        Vector<Node> dep;
        NodeCompare aux;
        for (int i = 0; i < folderdep.size(); i++) {
            System.out.println("Product " + (i + 1));
            no = folderdep.get(i).getNodename();
            dep = folderdep.get(i).getDep();
            for (int j = 0; j < no.size(); j++) {
                System.out.println(no.get(j));
            }
            for (int k = 0; k < dep.size(); k++) {
                System.out.println(dep.get(k).getNode() + " " + dep.get(k).getDepends());
            }
        }

    }

    private void printReport(Vector<GraphNode> node, Vector<GraphDep> dep) throws IOException {
        File report = new File("report.txt");
        FileWriter writer = new FileWriter(report);
        BufferedWriter w = new BufferedWriter(writer);

        w.write("Nodes:\n");
        for (int i = 0; i < node.size()-1; i++) {
          String aux = "";
          aux += node.get(i).getNodename()+" ";
          for(int j = 0; j<node.get(i).getProducts().size();j++)
          {
              aux += node.get(i).getProducts().get(j)+" ";
          }
          aux += "\n";
          w.write(aux);
        }
        w.write("\nDependencies:\n");
        for(int a = 0; a < dep.size()-1;a++)
        {
            String aux = "";
            aux += dep.get(a).getDependency()+" ";
            for(int b = 0; b< dep.get(a).getProducts().size();b++)
            {
                aux += dep.get(a).getProducts().get(b) +" ";
            }
            aux += "\n";
            w.write(aux);
        }
        w.close();
    }

    private void printBUNCH(Vector<GraphDep> dep) throws IOException {
        File bunch = new File("bunchinput");
        FileWriter writer = new FileWriter(bunch);
        BufferedWriter w = new BufferedWriter(writer);
        
        for(int i = 0;i<dep.size()-1;i++)
        {
            w.write(dep.get(i).getDependency()+"\n");
        }
        w.close();
    }
}
