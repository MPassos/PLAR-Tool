
import java.io.*;
import java.util.Vector;

/**
 *
 * @author Mateus
 */
public class Merger {

    private File product1;
    private File product2;
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
                 * When the character read is a \n A new node is added to the
                 * Vector
                 */
                if (c1 == 13) {
                    no1.add(nome1);
                    nome1 = "";
                    continue;
                }
                /**
                 * Ignores carriage returns
                 */
                if (c1 == 10) {
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
                    dependencias1.add(node);
                    node = new Node("", "");
                    nome1 = "";
                    continue;
                }

                if (c1 == 10) {
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
                 * When the character read is a \n A new node is added to the
                 * Vector
                 */
                if (c2 == 13) {
                    no2.add(nome2);
                    nome2 = "";
                    continue;
                }
                /**
                 * Ignores carriage returns
                 */
                if (c2 == 10) {
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
                    dependencias2.add(node);
                    node = new Node("", "");
                    nome2 = "";
                    continue;
                }

                if (c2 == 10) {
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

    public void gerarGrafo() throws IOException {
        int size1 = no1.size();
        int size2 = no2.size();
        int match = 0;
        String comp1, comp2, com, var;

        if (size1 > size2) {
            ebuffer1.write("digraph G {\n" + "size= \"" + size1 + "," + size1 + "\";\n" + "rotate = 180;\n");
            for (int i = 0; i < no1.size(); i++) {
                match = 0;
                comp1 = (String) no1.get(i);
                for (int j = 0; j < no2.size(); j++) {
                    comp2 = (String) no2.get(j);
                    if (comp1.equals(comp2)) {
                        ebuffer1.write("\"" + comp2 + "\"" + "[label=" + "\"" + comp2 + "\"" + ",shape=ellipse,color=blue,fontcolor=black,style=\"\"];\n");
                        match = 1;
                    }
                }
                if (match == 0) {
                    ebuffer1.write("\"" + comp1 + "\"" + "[label=" + "\"" + comp1 + "\"" + ",shape=ellipse,color=red,fontcolor=black,style=\"\"];\n");
                }
            }
        } else {
            ebuffer1.write("digraph G {\n" + "size= \"" + size2 + "," + size2 + "\";\n" + "rotate = 180;\n");
            for (int i = 0; i < no2.size(); i++) {
                match = 0;
                comp1 = (String) no2.get(i);
                for (int j = 0; j < no1.size(); j++) {
                    comp2 = (String) no1.get(j);
                    if (comp1.equals(comp2)) {
                        ebuffer1.write("\"" + comp2 + "\"" + "[label=" + "\"" + comp2 + "\"" + ",shape=ellipse,color=blue,fontcolor=black,style=\"\"];\n");
                        match = 1;
                    }
                }
                if (match == 0) {
                    ebuffer1.write("\"" + comp1 + "\"" + "[label=" + "\"" + comp1 + "\"" + ",shape=ellipse,color=red,fontcolor=black,style=\"\"];\n");
                }
            }
        }
        ebuffer1.write("}");
        ebuffer1.close();
    }

}
