import java.io.*;
import java.util.*;

class Node {
    int value;
    ArrayList<Node> dependant;
    public Node(int data) {
        this.value = data;
        this.dependant = new ArrayList<Node>();
    }

    @Override
    public String toString() {
        return ("" + this.value);
    }
}

public class WGraph{
    ArrayList<Node> nodes = new ArrayList<Node>();
    int nb_nodes = 0;
    Node source = null;
    Node destination = null;
    HashMap<Integer, Node> map = new HashMap<>();

    WGraph(String file_question, String file_dependencies, String file_task_ids,
           boolean fwd) throws RuntimeException {
        try {
            Scanner f = new Scanner(new File(file_question));
            String[] source_dest = new String[2];
            int count = 0;
            while(f.hasNextLine()) {
                String s = f.nextLine();
                for(int i = 0; i < s.length(); i++){
                    if(s.charAt(i) == ':'){
                        String tmp = "";
                        for(int j = i+1; j < s.length(); j++){
                            if(Character.isDigit(s.charAt(j))){
                                tmp += s.charAt(j);
                            }
                        }
                        source_dest[count] = tmp;
                        count++;
                    }
                }
            }
            int source = Integer.parseInt(source_dest[0]);
            int destination = Integer.parseInt(source_dest[1]);
            f.close();

            f = new Scanner(new File(file_task_ids));
            String[] l = f.nextLine().split(",");
            this.nb_nodes = l.length;
            for(int i = 0; i < this.nb_nodes; i++){
                int x = Integer.parseInt(l[i]);
                Node n = new Node(x);
                map.put(x, n);
                this.nodes.add(n);
                if(n.value == source){
                    this.source = n;
                }
                if(n.value == destination){
                    this.destination = n;
                }
            }
            f.close();

            f = new Scanner(new File(file_dependencies));
            while (f.hasNext()){
                String[] line = f.nextLine().split("->");
                /*Make sure there is 2 elements on the line*/
                if (line.length != 2) {
                    continue;
                }
                Node i = map.get(Integer.parseInt(line[0]));
                Node j = map.get(Integer.parseInt(line[1]));
                if(fwd){
                    i.dependant.add((j));
                }
                else{
                    j.dependant.add((i));
                }
            }
            f.close();

            /*Sanity checks*/
            if(!map.containsKey(this.source.value)){
                throw new RuntimeException("The source must be one of the nodes");
            }
            if(!map.containsKey(this.destination.value)){
                throw new RuntimeException("The destination must be one of the nodes");
            }
        }
        catch (FileNotFoundException e){
            System.out.println("File not found!");
            System.exit(1);
        }
    }

    public Node getSource(){
        return this.source;
    }

    public Node getDestination(){
        return this.destination;
    }

    public int getNbNodes(){
        return this.nb_nodes;
    }

    public String toString() {
        String out = Integer.toString(this.source.value) + " " + Integer.toString(this.destination.value) + "\n";
        out += Integer.toString(this.nb_nodes);
        for (int i = 0; i < this.nb_nodes; i++) {
            Node n = this.nodes.get(i);
            out += "\n" + n.value + "-> ";
            for (int j = 0; j < n.dependant.size(); j++) {
                out += n.dependant.get(j).value + " ";
            }
        }
        return out;
    }
}

