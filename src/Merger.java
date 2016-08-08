
import java.io.*;
import java.util.Collections;
import java.util.Vector;
import java.util.regex.Pattern;

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

    public Merger(File folder) throws FileNotFoundException, IOException {
        this.folder = folder;
    }

    public void getDependencias() throws IOException {
        no1 = new Vector(10, 1);
        no2 = new Vector(10, 1);
        dependencias1 = new Vector(10, 1);
        dependencias2 = new Vector(10, 1);
        int c1 = -1;
        int c2 = -1;
        int counter = 0;
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
                if (((c1 >= 48 && c1 <= 57) && counter <= 2) || c1 == 32) {
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
                    counter = 0;
                    continue;
                }
                counter++;
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
        counter = 0;
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
                if (((c2 >= 48 && c2 <= 57) && counter <= 2) || c2 == 32) {
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
                    counter = 0;
                    continue;
                }
                counter++;
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
                ebuffer1.write("\"" + comp1 + "\"" + "[label=" + "\"" + comp1 + "\"" + ",shape=ellipse,color=purple, style = dotted,fontcolor=black];\n");
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
                ebuffer1.write("\"" + comp2 + "\"" + "[label=" + "\"" + comp2 + "\"" + ",shape=ellipse,color=green, style = dashed,fontcolor=black];\n");
                var += "\"" + no2.get(j) + "\";\n";
                j++;
            }
        }

        while (i < no1.size()) {
            comp1 = (String) no1.get(i);
            ebuffer1.write("\"" + comp1 + "\"" + "[label=" + "\"" + comp1 + "\"" + ",shape=ellipse,color=purple,fontcolor=black];\n");
            var += "\"" + no1.get(i) + "\";\n";
            i++;
        }

        while (j < no2.size()) {
            comp2 = (String) no2.get(j);
            ebuffer1.write("\"" + comp2 + "\"" + "[label=" + "\"" + comp2 + "\"" + ",shape=ellipse,color=green,fontcolor=black];\n");
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
                ebuffer1.write("\"" + aux1.getNode() + "\"" + " -> " + "\"" + aux1.getDepends() + "\" " + "[color=purple, style = dotted,font=6];\n");
                i++;
            }
            if (dep1.compareTo(dep2) == 0) {
                ebuffer1.write("\"" + aux2.getNode() + "\"" + " -> " + "\"" + aux2.getDepends() + "\" " + "[color=blue,font=6];\n");
                i++;
                j++;
            }

            if (dep1.compareTo(dep2) > 0) {
                ebuffer1.write("\"" + aux2.getNode() + "\"" + " -> " + "\"" + aux2.getDepends() + "\" " + "[color=green, style = dashed,font=6];\n");
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
        ebuffer1.write("\"Product 1\"[color=purple, style = dotted];\n ");
        ebuffer1.write("\"Product 2\"[color=green, style = dashed];\n ");
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
            int counter = 0;
            
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
                    if (((c1 >= 48 && c1 <= 57) && counter <= 2) || c1 == 32) {
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
                        counter = 0;
                        nome1 = "";
                        continue;
                    }

                    ch = (char) c1;
                    nome1 += Character.toString(ch);
                    counter++;
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
            String product[] = files[i].getName().split(Pattern.quote(".tgf"));
            aux.setPName(product[0]);
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
                        nodes.get(k).setProducts(folderdep.get(i).getPname());
                        nodes.add(new GraphNode());
                        break;
                    }
                    if (nodes.get(k).getNodename().equals(naux.getNodename())) {
                        //nodes.get(k).setVariability(false);
                        nodes.get(k).setProducts(folderdep.get(i).getPname());
                        if(nodes.get(k).getProducts().size() == folderdep.size())
                        {
                          nodes.get(k).setVariability(false);  
                        }
                        break;
                    }

                }
            }

            for (int a = 0; a < depnames.size(); a++) {
                GraphDep daux = new GraphDep();
                daux.setDependency(depnames.get(a).getNode() + " " + depnames.get(a).getDepends());
                daux.setNode(depnames.get(a).getNode());
                daux.setDepends(depnames.get(a).getDepends());
                daux.setProducts(folderdep.get(i).getPname());

                if (deps.isEmpty()) {
                    //deps.add(daux);
                    deps.add(new GraphDep());
                }

                for (int b = 0; b < deps.size(); b++) {
                    if (deps.get(b).getDependency() == null) {
                        deps.get(b).setDependency(daux.getDependency());
                        deps.get(b).setNode(daux.getNode());
                        deps.get(b).setDepends(daux.getDepends());
                        deps.get(b).setProducts(folderdep.get(i).getPname());
                        deps.add(new GraphDep());
                        break;
                    }
                    if (deps.get(b).getDependency().equals(daux.getDependency())) {
                        //deps.get(b).setVariability(false);
                        deps.get(b).setProducts(folderdep.get(i).getPname());
                        if(deps.get(b).getProducts().size() == folderdep.size())
                        {
                          deps.get(b).setVariability(false);  
                        }
                        break;
                    }
                }
            }
        }
        nodes.remove(nodes.size()-1);
        deps.remove(deps.size()-1);
        Collections.sort(nodes);
        Collections.sort(deps);
        printReport(nodes, deps);
        //printBUNCH(deps);
        //printACDC(deps);
        printGraph(nodes, deps);
        metrics(nodes, deps);
        printUML(nodes,deps);
        printMatrix(nodes,deps);
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
        File report = new File("output/report.html");
        FileWriter writer = new FileWriter(report);
        BufferedWriter w = new BufferedWriter(writer);
        
        w.write("<html>\n\t<body>\n\t\t<div style=\"font-family:Helvetica, Arial, sans-serif\">");
        w.write("<p style = \"font-weight:bold\">Nodes:</p>\n");
        for (int i = 0; i < node.size(); i++) {
            String aux = "<p>";
            aux += node.get(i).getNodename() + " ";
            for (int j = 0; j < node.get(i).getProducts().size(); j++) {
                aux += node.get(i).getProducts().get(j) + " ";
            }
            aux += "</p>\n";
            w.write(aux);
        }
        w.write("<p style = \"font-weight:bold\">Dependencies:</p>\n");
        for (int a = 0; a < dep.size(); a++) {
            String aux = "<p>";
            aux += dep.get(a).getDependency() + " ";
            for (int b = 0; b < dep.get(a).getProducts().size(); b++) {
                aux += dep.get(a).getProducts().get(b) + " ";
            }
            aux += "</p>\n";
            w.write(aux);
        }
        w.write("\t\t</div>\n\t</body>\n</html>");
        w.close();
    }

    private void printBUNCH(Vector<GraphDep> dep) throws IOException {
        File bunch = new File("output/bunchinput");
        FileWriter writer = new FileWriter(bunch);
        BufferedWriter w = new BufferedWriter(writer);

        for (int i = 0; i < dep.size(); i++) {
            w.write(dep.get(i).getDependency() + "\n");
        }
        w.close();
    }

    private void printGraph(Vector<GraphNode> nodes, Vector<GraphDep> deps) throws IOException {
        File graph = new File("output/mergedgraph.dot");
        FileWriter writer = new FileWriter(graph);
        BufferedWriter w = new BufferedWriter(writer);
        String sim = "", var = "";
        
        w.write("digraph G {\n" + "size= \"" + nodes.size() + "," + nodes.size() + "\";\n" + "rotate = 180;\n");

        for (int i = 0; i < nodes.size(); i++) {
            if (nodes.get(i).getProducts().size() == folderdep.size()) {
                w.write("\"" + nodes.get(i).getNodename() + "\"" + "[label=" + "\"" + nodes.get(i).getNodename() + "\"" + ",shape=ellipse,color=blue,fontcolor=black,style=\"\"];\n");
                sim += "\"" + nodes.get(i).getNodename() + "\"" + "\n";
            } else {
                w.write("\"" + nodes.get(i).getNodename() + "\"" + "[label=" + "\"" + nodes.get(i).getNodename() + "\"" + ",shape=ellipse, color=\"#FF000055\", style = dotted,fontcolor=black];\n");
                var += "\"" + nodes.get(i).getNodename() + "\"" + "\n";
            }
        }

        for (int j = 0; j < deps.size(); j++) {
            if (deps.get(j).getProducts().size() == folderdep.size()) {
                w.write("\"" + deps.get(j).getNode() + "\"" + " -> " + "\"" + deps.get(j).getDepends() + "\" " + "[color=blue,font=6];\n");
            } else {
                w.write("\"" + deps.get(j).getNode() + "\"" + " -> " + "\"" + deps.get(j).getDepends() + "\" " + "[color=\"#FF000055\", font=6];\n");
            }
        }
        w.write("subgraph cluster_0{\nlabel = \"Variability\";\n");
        w.write(var);
        w.write("}\n");

        w.write("subgraph cluster_1{\nlabel = \"Similarities\" ;\n");
        w.write(sim);
        w.write("}\n");
        w.write("}");
        w.close();
        
        //Checking OS to do proper SVG conversion
       if(System.getProperty("os.name").startsWith("Windows"))
       {
        Runtime.getRuntime().exec("cmd /c cd output && dot -Tsvg mergedgraph.dot -o svggraph.svg");
        Runtime.getRuntime().exec("cmd /c copy resources\\Interactive.html output ");
       }else
       if(System.getProperty("os.name").startsWith("Linux"))
       {
        Runtime.getRuntime().exec("sh -c cd output && dot -Tsvg mergedgraph.dot -o svggraph.svg");
        Runtime.getRuntime().exec("sh -c cp resources\\Interactive.html output ");
       }else
       if(System.getProperty("os.name").startsWith("Mac"))
       {
        Runtime.getRuntime().exec("sh -c cd output && dot -Tsvg mergedgraph.dot -o svggraph.svg");
        Runtime.getRuntime().exec("sh -c cp resources\\Interactive.html output ");
       }
    }

    private void printACDC(Vector<GraphDep> deps) throws IOException {
        File acdc = new File("output/acdcinput.rsf");
        FileWriter writer = new FileWriter(acdc);
        BufferedWriter w = new BufferedWriter(writer);

        for (int i = 0; i < deps.size(); i++) {
            w.write("depends " + deps.get(i).getDependency() + "\n");
        }
        w.close();
    }

    private void metrics(Vector<GraphNode> nodes, Vector<GraphDep> deps) throws IOException {
        File metrics = new File("output/metrics.html");
        FileWriter writer = new FileWriter(metrics);
        BufferedWriter w = new BufferedWriter(writer);
        //Components Metrics
        float Cc, Cv, SSC, SVC, CCR;
        Cc = Cv = SSC = SVC = CCR = 0;

        //Relation Metrics
        float Rc, Rv, RSSC, RSVC, RCCR;
        Rc = Rv = RSSC = RSVC = RCCR = 0;

        for (int i = 0; i < nodes.size(); i++) {
            if (nodes.get(i).isVariability() == true) {
                Cv++;
            } else {
                Cc++;
            }
        }
        SSC = Cc / (Cc + Cv);
        SVC = Cv / (Cc + Cv);

        for (int a = 0; a < deps.size(); a++) {
            if (deps.get(a).isVariability() == true) {
                Rv++;
            } else {
                Rc++;
            }
        }
        RSSC = Rc / (Rc + Rv);
        RSVC = Rv / (Rc + Rv);
        
        w.write("<html>\n\t<body>\n");
        w.write("\t\t<div style=\"font-family:Helvetica, Arial, sans-serif\">");
        w.write("\t\t\t<h1 style=\"text-align: center;\">METRICS REPORT</h1>\n");
        w.write("\t\t\t<p>COMPONENT METRICS</p>\n");
        w.write("\t\t\t<p>SSC:" + SSC + "</p>\n");
        w.write("\t\t\t<p>SVC:" + SVC + "</p>\n");
        w.write("\t\t\t<p>COMMON COMPONENTS TOTAL:" + (int) Cc + "</p>\n");
        w.write("\t\t\t<p>VARIABILITY COMPONENTS TOTAL:" + (int) Cv + "</p>\n");
        w.write("\t\t\t<p>RELATION METRICS</p>\n");
        w.write("\t\t\t<p>RSSC:" + RSSC + "</p>\n");
        w.write("\t\t\t<p>RSVC:" + RSVC + "</p>\n");
        w.write("\t\t\t<p>COMMON RELATIONS TOTAL:" + (int) Rc + "</p>\n");
        w.write("\t\t\t<p>VARIABILITY RELATIONS TOTAL:" + (int) Rv + "</p>\n");
        w.write("\t\t\t<p>CRR FOR EACH COMPONENT</p>\n");
        for (int j = 0; j < nodes.size(); j++) {
            CCR = ((float) nodes.get(j).getProducts().size() / (float) folderdep.size()) * 100;
            w.write("\t\t\t<p>"+nodes.get(j).getNodename() + " CRR:" + CCR + "</p>\n");

        }
        w.write("\t\t\t<p>CRR FOR EACH RELATION</p>\n");
        for (int b = 0; b < deps.size(); b++) {
            RCCR = ((float) deps.get(b).getProducts().size() / (float) folderdep.size()) * 100;
            w.write("\t\t\t<p>"+deps.get(b).getDependency() + " CRR:" + RCCR + "</p>\n");
        }
        w.write("\t\t</div>\n\t</body>\n</html>");
        w.close();
    }

    private void printUML(Vector<GraphNode> nodes, Vector<GraphDep> deps) throws IOException {
        File graph = new File("output/umlclassdiagram.txt");
        FileWriter writer = new FileWriter(graph);
        BufferedWriter w = new BufferedWriter(writer);
        String sim = "", var = "";

        w.write("@startuml\nskinparam class{\nBackgroundColor<<Sim>> Blue\nArrowColor<<Sim>> Blue\nBackgroundColor<<Var>> Red\nArrowColor<<Var>> Red\n}");

        for (int i = 0; i < nodes.size(); i++) {
            if (nodes.get(i).getProducts().size() == folderdep.size()) {
                sim += "class " + nodes.get(i).getNodename() + " <<Sim>>" + "\n";
            } else {
                var += "class " + nodes.get(i).getNodename() + " <<Var>>" + "\n";
            }
        }
        
        w.write("\npackage Variability <<Rect>>{\n");
        w.write(var);
        w.write("}\n");
        
         w.write("package Similarities <<Rect>>{\n");
        w.write(sim);
        w.write("}\n");
        
        for (int j = 0; j < deps.size(); j++) {
            
                w.write(deps.get(j).getNode() + " --> " + deps.get(j).getDepends() + "\n");   
        }
        
        w.write("@enduml");
        w.close();
    }

    private void printMatrix(Vector<GraphNode> nodes, Vector<GraphDep> deps) throws IOException {
        File matrix = new File("output/matrix.html");
        FileWriter writer = new FileWriter(matrix);
        BufferedWriter w = new BufferedWriter(writer);
        
        w.write("<!DOCTYPE html>\n<html>\n<head>\n<style>\ntable, td, th {\n\tborder: 2px solid black;\n}\n"
                + "td{\n\toverflow:hidden;\n\twhite-space:nowrap;\n}\n "
                + "</style>\n</head>\n<body>\n");
        w.write("<h2 style=\"text-align: center\">Design Structure Matrix</h2>\n");
        w.write("<table style=\"border-collapse: collapse;\">\n");
        w.write("\t<tr>\n");
        w.write("\t\t<td></td>\n");
        for (int i = 0; i < nodes.size(); i++) {
            if (i < 9 && nodes.size() < 100) {
                if (nodes.get(i).isVariability()) {
                    w.write("\t\t<td style = \"color: white; background: red;font-weight: bold;width:20px;text-align: center;\">" + "0" + (i + 1) + "</td>\n");
                } else {
                    w.write("\t\t<td style = \"color: white; background: blue;font-weight: bold;width:20px;text-align: center;\">" + "0" + (i + 1) + "</td>\n");
                }
            } else {
                if (i < 9 && (nodes.size() > 100 && nodes.size() < 1000)) {
                    if (nodes.get(i).isVariability()) {
                        w.write("\t\t<td style = \"color: white; background: red;font-weight: bold;width:20px;text-align: center;\">" + "00" + (i + 1) + "</td>\n");
                    } else {
                        w.write("\t\t<td style = \"color: white; background: blue;font-weight: bold;width:20px;text-align: center;\">" + "00" + (i + 1) + "</td>\n");
                    }
                } else {
                    if ((i >= 9 && i < 99) && (nodes.size() > 100 && nodes.size() < 1000)) {
                        if (nodes.get(i).isVariability()) {
                            w.write("\t\t<td style = \"color: white; background: red;font-weight: bold;width:20px;text-align: center;\">" + "0" + (i + 1) + "</td>\n");
                        } else {
                            w.write("\t\t<td style = \"color: white; background: blue;font-weight: bold;width:20px;text-align: center;\">" + "0" + (i + 1) + "</td>\n");
                        }
                    } else if (nodes.get(i).isVariability()) {
                        w.write("\t\t<td style = \"color: white; background: red;font-weight: bold;width:20px;text-align: center;\">" + (i + 1) + "</td>\n");
                    } else {
                        w.write("\t\t<td style = \"color: white; background: blue;font-weight: bold;width:20px;text-align: center;\">" + (i + 1) + "</td>\n");
                    }
                }
            }
        }
        w.write("\t</tr>\n");
        for(int a = 0;a<nodes.size(); a++){
            w.write("\t<tr>\n");
            if(nodes.get(a).isVariability()) w.write("\t\t<td style = \"color: white; background: red;font-weight: bold;\">"+(a+1)+" "+nodes.get(a).getNodename()+"</td>\n");
            else w.write("\t\t<td style = \"color: white; background: blue;font-weight: bold;\">"+(a+1)+" "+nodes.get(a).getNodename()+"</td>\n"); 
            for(int b = 0;b<nodes.size();b++)
            {
                boolean found = false;
                for(int c = 0; c<deps.size();c++)
                {
                    if(nodes.get(a).getNodename().equals(deps.get(c).getNode())&& 
                            nodes.get(b).getNodename().equals(deps.get(c).getDepends()))
                    {
                        found = true;
                       if(deps.get(c).isVariability()){
                           w.write("\t\t<td style=\"background: red\"></td>\n");
                           break;
                       } else{
                           w.write("\t\t<td style=\"background: blue\"></td>\n");
                           break;
                       }
                    }
                }
                if(a == b)
                {
                    w.write("\t\t<td style=\"background: black\"></td>\n");
                    continue;
                }
                if(found == false) w.write("\t\t<td></td>\n");       
            }
            w.write("\t</tr>\n");
        }           
        w.write("</table>\n</body>\n</html>");
        
        w.close();
        }
}

