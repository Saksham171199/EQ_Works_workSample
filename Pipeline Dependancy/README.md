I chose to do 4b.Pipeline Dependancy

To solve this problem I created a WGraph java class that allows me to build a directed graph from
the relations.txt, task_ids.txt and question.txt files. A node n represents a task, and each node has
a dependancy ArrayList attached to it containing all the tasks that depend on node n.
Ex: If Node n = 73 and n.dependancy = [21, 98, 100] then the the task n = 73 needs to be completed before
tasks 21, 98 and 100 can be completed, ie those tasks depend on task n = 73.
I also fix the source vertex = starting task and destination vertex = goal task while building my WGraph.

Then I wrote a class called PipelineDependancy that would do the job of finding a topological ordering of the tasks given.
When an ordering has been found, I reverse the edges of WGraph to find the Strongly Connected Component containing the destination/
goal task. We need to find this SCC since these are the nodes/tasks that are necessary for the realization of the goal task. By DFS we
can find these tasks and then simply keep the nodes found in the topological ordering that are reachable from the goal task. This
fulfills the necessity and sufficiency condition imposed -> every task necessary for the realization of the goal task are kept in the path
and no task that are not prerequisites of the goal task are kept. The path is then printed to the screen.

If you want to run the code:
  1) Make sure the file WGraph.java and PipelineDependancy.java are in the same directory
  2) In main: in the function new WGraph(file_question, file_dependancies, file_task_ids) fill in:
            - file_question : question.txt
            - file_dependancies : relations.txt
            - file_task_ids : task_ids.txt
  3) Run the code and the result should be printed to the screen
  
