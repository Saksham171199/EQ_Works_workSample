import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Stack;

public class PipelineDependancy {
    static void topologicalSort(WGraph g, Node v, HashSet<Node> visited, Stack<Node> stack) {
        //mark node as visited
        visited.add(v);
        Node i;
        //iterate through this node dependancy list
        Iterator<Node> it = g.map.get(v.value).dependant.iterator();
        while (it.hasNext()) {
            i = it.next();
            if (!visited.contains(i))
                //recursive call
                topologicalSort(g, i, visited, stack);
        }
        //push node to stack (all recursions should have lead to the node in the stack below it
        stack.push(v);
    }
    static ArrayList<Node> topologicalSort(WGraph g) {
        Stack<Node> stack = new Stack<Node>();
        ArrayList<Node> toReturn = new ArrayList<>();
        HashSet<Node> visited = new HashSet<>();
        //topological sort the graph
        for (int i = 0; i < g.nb_nodes; i++)
            if (!visited.contains(g.nodes.get(i))) {
                topologicalSort(g, g.nodes.get(i), visited, stack);
            }
        //return all nodes in graph sorted in topological order
        while(!stack.empty()) {
            toReturn.add(stack.pop());
        }
        return toReturn;
    }
    public static void DFS(Node dest, HashSet<Integer> reachable){
        reachable.add(dest.value);
        for(Node n : dest.dependant){
            if(!reachable.contains(n.value)){
                DFS(n, reachable);
            }
        }
    }
    public static void main(String[] args) {
        WGraph g = new WGraph("/Users/admin/IdeaProjects/EQWorks_PipelineDependancy/src/question.txt",
                "/Users/admin/IdeaProjects/EQWorks_PipelineDependancy/src/relations.txt",
                "/Users/admin/IdeaProjects/EQWorks_PipelineDependancy/src/task_ids.txt", true);

        ArrayList<Node> topologicalOrder = topologicalSort(g);

        //reverse graph's edges so that we can find all nodes reachable from the destination
        //the solution only needs to contain the nodes necessary for the goal task and not the other nodes
        //hence by finding the SCC containing the goal task, we find all projects that need to be done for this to succeed
        WGraph gf = new WGraph("/Users/admin/IdeaProjects/EQWorks_PipelineDependancy/src/question.txt",
                "/Users/admin/IdeaProjects/EQWorks_PipelineDependancy/src/relations.txt",
                "/Users/admin/IdeaProjects/EQWorks_PipelineDependancy/src/task_ids.txt", false);
        Node dest = gf.getDestination();
        HashSet<Integer> reachable = new HashSet<>();
        DFS(dest, reachable);

        //filter out unecessary projects that we don't need (necessity and sufficiency)
        ArrayList<Node> final_order = new ArrayList<>();
        for(int i = 0; i < topologicalOrder.size(); i++){
            if(reachable.contains(topologicalOrder.get(i).value)){
                final_order.add(topologicalOrder.get(i));
            }
        }
        //print the projects in order to the screen
        for(int i = 0; i < final_order.size(); i++){
            if(i != final_order.size()-1){
                System.out.print(final_order.get(i) + " -> ");
            }
            else{
                System.out.print(final_order.get(i) + "\n");
            }
        }
    }
}
