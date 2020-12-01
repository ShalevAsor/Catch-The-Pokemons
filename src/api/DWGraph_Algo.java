package api;
import com.google.gson.*;
import java.io.*;
import java.lang.reflect.Type;
import java.util.*;

/**
 * This class represent directed weighted graph algorithms, it support basic methods like init,copy and getGraph.
 * it also support few algorithms like BFS and Dijkstra's algorithm that used for methods like isConnected
 * and shortestPath.
 * each method in this class is attached with explanations.
 */

public class DWGraph_Algo implements dw_graph_algorithms {
    directed_weighted_graph graph=new DWGraph_DS();

    //-------------------subNode class------------------//

    /**
     * This class used in Dijkstra's Algorithm
     */
    private static class subNode implements Comparable<subNode>{
        private double _weight = Double.POSITIVE_INFINITY;
        private final int _parent;//represent the node thats this subNode came from
        private final int _currentKey;

        //-----------constructors-------------//
        public subNode(double weight, int parent, int key) {
            this._parent = parent;
            this._weight = weight;
            this._currentKey = key;
        }
        public subNode(int parentKey ,int key) {
            this._currentKey = key;
            this._parent=parentKey;
        }
        //------------setters and getters----------------//
        public void setWeight(double w) {
            this._weight = w;
        }

        public double getWeight() {
            return this._weight;
        }

        public int getParent() {
            return this._parent;
        }

        public int getCurrentKey() {
            return this._currentKey;
        }
        @Override
        public int compareTo(subNode o) {
            int ans=0;
            if(this._weight>o.getWeight()){
                ans=1;
            }
            else if(this._weight<o.getWeight()){
                ans=-1;
            }
            return ans;
        }
    }

    //-------------------------------Json class-------------------------------//

    /**
     * This class is necessary for the Deserialization it support two type of graphs
     * the first one is the graph that i made with: edges,vertices,ModeCount,Neighbors
     * the second one is the graph that made with:Edges,Nodes
     */
    static class GraphJsonDeserializer implements JsonDeserializer<DWGraph_DS> {

        @Override
        public DWGraph_DS deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            DWGraph_DS graph_ds = new DWGraph_DS();
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            if(jsonObject.get("Edges") != null) {//if this is the "Edges" and "Nodes" type of graph
                JsonArray vertices = jsonObject.get("Nodes").getAsJsonArray();//get the nodes as Json object
                JsonArray edges = jsonObject.get("Edges").getAsJsonArray();//same for the Edges
                for (int i=0;i<vertices.size();i++) {//for loop to get accesses to each node in this graph
                    NodeData.GeoLocation glPointer = null;
                    JsonElement node = vertices.get(i);//pointer the the "i" node
                    int nodeKey = node.getAsJsonObject().get("id").getAsInt();//get his key
                    if (node.getAsJsonObject().get("pos") != null) {//if thos node has a location
                        String[] arr;//creat String Array to keep the x,y,z values
                        String location=node.getAsJsonObject().get("pos").getAsString();//get the json object that keep the location of this node
                        arr=location.split(",");// split the string to get x y z in separately
                        double _x = Double.parseDouble(arr[0]);//convert the string into a double
                        double _y = Double.parseDouble(arr[1]);
                        double _z = Double.parseDouble(arr[2]);
                        NodeData.GeoLocation nodeGl = new NodeData.GeoLocation(_x, _y, _z);//create a new geo_location
                        glPointer = nodeGl;
                    }
                    NodeData n = new NodeData(nodeKey);
                    n.setLocation(glPointer);//add the new geo_location to this node
                    graph_ds.addNode(n);//finally add this node into the graph
                }
                for (int i=0;i<edges.size();i++) {//for loop to get accesses to each edge in this graph
                    JsonElement edge = edges.get(i);//pointer to the edge
                    int srcKey = edge.getAsJsonObject().get("src").getAsInt();//get the src of this edge
                    int destKey = edge.getAsJsonObject().get("dest").getAsInt();//get the dest of this edge
                    double weight = edge.getAsJsonObject().get("w").getAsDouble();//get the weight of this edge
                    graph_ds.connect(srcKey, destKey, weight);//connect the src and the dest by this weight
                }
            } else {//now we are working on the first type of graph
                JsonObject vertices = jsonObject.get("vertices").getAsJsonObject();//get the nodes of this graph
                JsonObject edges = jsonObject.get("edges").getAsJsonObject();//get the edges of this graph
                for (Map.Entry<String, JsonElement> set : vertices.entrySet()) {//get accesses to each node in this graph
                    NodeData.GeoLocation glPointer = null;
                    JsonElement node = set.getValue();//pointer to the node
                    int nodeKey = node.getAsJsonObject().get("_key").getAsInt();//get this node key
                    double weight = node.getAsJsonObject().get("_weight").getAsDouble();//get this node weight
                    int nodeTag = node.getAsJsonObject().get("_tag").getAsInt();//get this node tag
                    String nodeInfo = node.getAsJsonObject().get("_info").getAsString();//get this node info
                    if (node.getAsJsonObject().get("_gl") != null) {//if this node has a geo_location than get the x,y,z as well
                        double _x = node.getAsJsonObject().get("_gl").getAsJsonObject().get("_x").getAsDouble();
                        double _y = node.getAsJsonObject().get("_gl").getAsJsonObject().get("_y").getAsDouble();
                        double _z = node.getAsJsonObject().get("_gl").getAsJsonObject().get("_z").getAsDouble();
                        glPointer = new NodeData.GeoLocation(_x, _y, _z);////create a new geo_loctaion
                    }
                    NodeData n = new NodeData(nodeKey, weight, nodeTag, nodeInfo);//create a new node with the data above
                    n.setLocation(glPointer);//set the location
                    graph_ds.addNode(n);//finally add the new node into this graph
                }
                for (Map.Entry<String, JsonElement> set : edges.entrySet()) {//same process to the edges
                    JsonElement edge = set.getValue();
                    int srcKey = edge.getAsJsonObject().get("_src").getAsInt();
                    int destKey = edge.getAsJsonObject().get("_dest").getAsInt();
                    double weight = edge.getAsJsonObject().get("_weight").getAsDouble();
                    int tag = edge.getAsJsonObject().get("_tag").getAsInt();
                    graph_ds.connect(srcKey, destKey, weight);
                    graph_ds.getEdge(srcKey, destKey).setTag(tag);
                }
                int mc = jsonObject.get("ModeCount").getAsInt();//update the ModeCount after the graph is ready
                graph_ds.setModeCount(mc);
            }
            return graph_ds;
        }
    }
    //----------------------DWGraph_Algo methods-----------------------------------//

    /**
     * This method initialization this graph to the given graph
     * Complexity-O(1)
     * @param g - the new graph
     */

    @Override
    public void init(directed_weighted_graph g) {
        this.graph=g;

    }

    /**
     * This method return the directed weighted graph that this class works on
     * Complexity-O(1)
     * @return this class graph
     */

    @Override
    public directed_weighted_graph getGraph() {
        return this.graph;
    }

    /**
     * This method compute a deep copy of this graph
     * Complexity-if n is the sum of the vertices and V is the sum of the edges -->O(v*n)
     * @return copy of this graph
     */
    @Override
    public directed_weighted_graph copy() {
        DWGraph_DS copy = new DWGraph_DS();
        for (node_data Vpointer : this.graph.getV()) {//pointer to this graph vertices
            copy.addNode(new NodeData(Vpointer.getKey()));//add vertex to copy
            copy.getVertices().get(Vpointer.getKey()).setTag(Vpointer.getTag());//set the original tag of this node
            copy.getVertices().get(Vpointer.getKey()).setInfo(Vpointer.getInfo());//set the original info of this node
        }
        for (node_data NeiPointer : this.graph.getV()) { //pointer2 neighbors
            for (edge_data edgePointer : this.graph.getE(NeiPointer.getKey())) {
                double weight = this.graph.getEdge(NeiPointer.getKey(), edgePointer.getDest()).getWeight();//get the original weight
                copy.connect(NeiPointer.getKey(), edgePointer.getDest(), weight);//connect on the copy.
            }
        }
        return copy;
    }

    /**
     * This method return true iff this graph is strongly connected(there is a path from each node in the graph
     * to every other node
     * Complexity-
     * @return ture if this graph is Strongly connected
     */
    @Override
    public boolean isConnected() {
        boolean ans=false;
        for(node_data vPointer: this.graph.getV()){
            if(isWeaklyConnected(vPointer.getKey())){
                ans=true;
            }
            else{
                ans=false;
                break;
            }
        }
        return ans;
    }

    /**
     * This method return the length of the shortest path from the source vertex to the destination vertex
     * this method based on Dijkstra's algorithm.
     * Complexity- O(v^2) (v=vertices)
     * @param src - source node
     * @param dest - destination (target) node
     * @return the length of the shortest path
     */
    @Override
    public double shortestPathDist(int src, int dest) {
        double ans=-1;
        if(this.graph.getNode(src)==null||this.graph.getNode(dest)==null){//if one of this nodes is not in this graph there is no path.
            return ans;
        }
        else{
            List<subNode> myList=this.Dijkstras(this.graph.getNode(src),this.graph.getNode(dest));
            if(myList==null){return -1;}//if the list is null there is no path
            int i=0,size=myList.size();
            while(i<size){//looking for dest weight
                if(dest==myList.get(i)._currentKey){
                    ans=myList.get(i)._weight;
                }
                i++;
            }
            return ans;}
    }

    /**
     * This method return list represent the shortest path from the source vertex to the destination vertex
     * src--->node1---->node2----->...----->dest
     * this method based on Dijkstra's algorithm.
     * Complexity- O(v^2) (v=vertices)
     * @param src - source node
     * @param dest - destination node
     * @return the list represent the shortest path if none then return null
     */

    @Override
    public List<node_data> shortestPath(int src, int dest) {
        if(this.graph.getNode(src)==null||this.graph.getNode(dest)==null)return null;//if on of this nodes are not in the graph there is no path
        List<node_data> myList=new LinkedList<node_data>();//create list that will return with the shortest path
        //myList.add(0,this.graph.getNode(src));//add the src node to the first index of the list
        List<subNode> list= this.Dijkstras(this.graph.getNode(src),this.graph.getNode(dest));//pointer to the list that returned from Dijkstras algo
        if(list==null){return null;}
        int i=0,size=list.size(),thereIsNoPath=0;//init the size

        subNode pointer=this.getSubNode(list,dest);//looking for the subNode with the dest key
        if(pointer==null){return null;}
        myList.add(this.graph.getNode(pointer._currentKey));
        while(pointer._currentKey!=src){
            myList.add(this.graph.getNode(pointer.getParent()));
            pointer=this.getSubNode(list, pointer.getParent());
            if(thereIsNoPath==size-1){
                return null;
            }
            thereIsNoPath++;
        }
        return this.reverseList(myList);
    }

    /**
     * This method save this directed weighted graph by the given file name into a Json format
     *
     * @param file - the file name (may include a relative path).
     * @return true if this graph saved successfully
     */
    @Override
    public boolean save(String file) {
        boolean saved;
        Gson gson=new GsonBuilder().setPrettyPrinting().create();//create Gson object
        String jsGraph= gson.toJson(this.graph);//transfer this graph into a json format
        try {
            PrintWriter pw=new PrintWriter(new File(file));//create pw with the given file name
            pw.write(jsGraph);//write to pw the graph in the json format
            pw.close();//close the file
            saved=true;
        } catch (FileNotFoundException ex) {//if there is no such file throw exception
            ex.printStackTrace();
            saved= false;
        }
        return saved;
    }

    /**
     * This method load a graph by the given json file to this graph.
     * and return true if this graph was loaded successfully
     * @param file - file name of JSON file
     * @return true if this graph loaded successfully
     */
    @Override
    public boolean load(String file)  {
        boolean loaded;
        try{
            GsonBuilder gsbuilder=new GsonBuilder();//create a Gson object
            gsbuilder.registerTypeAdapter(DWGraph_DS.class, new GraphJsonDeserializer());//Performs the conversion using GraphJsonDeserializer
                    Gson gson=gsbuilder.create();
            FileReader fr=new FileReader(file);//file reader
            this.graph=gson.fromJson(fr,DWGraph_DS.class);//pointe to the gson.fromJson
          loaded= true;
        }
        catch (FileNotFoundException e){
            e.printStackTrace();
            loaded= false;

        }
         return loaded;
        }

    //---------------------Algorithms--------------------------//
    //---------------BFS ALGO-------------//
    public List<node_data> Bfs(int src) {
        directed_weighted_graph g = this.graph;
        Queue<node_data> queue = new LinkedList<node_data>();
        List<node_data> list = new ArrayList<node_data>();
        g.getNode(src).setInfo("grey");//-mark as "grey" (visited)
        g.getNode(src).setTag(0);//first node tag as 0
        queue.add(g.getNode(src));//add the start node to the queue
        list.add(g.getNode(src));//add the first node to the list
        while (!queue.isEmpty()) {
            Collection<edge_data> listOfNei = g.getE(queue.peek().getKey());//pointer to start node neighbors
            for (edge_data edge : listOfNei) {
                node_data destNode = this.graph.getNode(edge.getDest());
                if (destNode.getInfo().equals("white")) {//if the first nei isnt visited
                    queue.add(destNode);//add him to the queue
                    list.add(destNode);//add him to the list
                    destNode.setInfo("grey");//mark him as visited
                    if (list.size() == 1) {//if this is the fist nei
                        destNode.setTag(1);
                    } else {
                        destNode.setTag(queue.peek().getTag() + 1);
                    }
                }
            }
            queue.remove();
        }
        //remark all the vertices in the graph for the next use in this function
       remarkVertices(this.graph.getV());
        return list;
    }
    //---------------------Dijkstra's Algorithm-----------------------//
    public List<subNode> Dijkstras(node_data sNode, node_data dNode) {
        List<subNode> solutionList = new LinkedList<subNode>();
        HashSet<Integer> visited =new HashSet<>();//hashset of all the visited nodes keys
        PriorityQueue<subNode> myPriorityqueue = new PriorityQueue<subNode>();//this queue is gonna keep all the vertices in this graph
        ////set the tag of the src node to zero (the distance from node to himself is zero)
        myPriorityqueue.add(new subNode(0, sNode.getKey(), sNode.getKey()));//add the src subNode to the queue
        while (!myPriorityqueue.isEmpty()) {//as long as this queue is not empty(the meaning is there are still vertices in the graph that has not visited)
            subNode subNodePointer = myPriorityqueue.poll();// pointer to the subNode with the lowest weight
            if (subNodePointer.getCurrentKey() == dNode.getKey()) {//the shortest path was found
                solutionList.add(subNodePointer);
                return solutionList;
            }
            if (!visited.contains(subNodePointer._currentKey)) {
                visited.add(subNodePointer._currentKey);//mark him as visited
                solutionList.add(subNodePointer);//add him to the list
                Iterator<edge_data> it = this.graph.getE(subNodePointer.getCurrentKey()).iterator();//iterate all his neighbors
                while (it.hasNext()) {
                    edge_data neiPointer = it.next();//pointer to each of the subNodePointer neighbors
                    if (!visited.contains(neiPointer.getDest())) {//if the neighbor has not visited
                        subNode subNodeNeiPointer=new subNode(subNodePointer._currentKey,neiPointer.getDest());//crete a subNode for him
                        double weight = (this.graph.getEdge(subNodePointer._currentKey, neiPointer.getDest())).getWeight()+subNodePointer._weight;//the weight of the node
                        if (weight<subNodeNeiPointer._weight){//if the path is shorter
                            subNodeNeiPointer.setWeight(weight);
                            myPriorityqueue.add(subNodeNeiPointer);
                        }
                    }
                }
            }
        }
        return solutionList;
    }


    //-----------------auxiliary methods--------------------//

    /**
     * This metohd return true if the graph is weakly connected-
     * @param src-the source node
     * @return true if this graph connected (weakly)
     */
    public boolean isWeaklyConnected(int src){
        if (this.graph.nodeSize() == 0) return true;//empty graph is connected
        //if the list size that was returned from the Bfs=this graph size>>>this graph is connected
        return this.Bfs(src).size() == this.graph.nodeSize();
    }

    /**
     * This method remark all the vertices in the graph to the original data
     * @param c- the node_data Collection
     */
    private void remarkVertices(Collection<node_data> c){
        for(node_data n: c){
            n.setInfo("white");
            n.setTag(-1);
        }
    }

    /**
     * This method reverses the order in the list
     * for example- input :(a3,a2,a1) ,output: (a1,a2,a3)
     * @param list-the list that needed to be reversed
     * @return the reversed list
     */
    //if this method input is (a3,a2,a1) the  output is (a1,a2,a3)
    public List<node_data> reverseList(List<node_data> list){
        List<node_data> reversed =new LinkedList<node_data>();
        int i=0,size=list.size(),indexOf=size-1;
        while(i<size){
            reversed.add(list.get(indexOf));
            indexOf--;
            i++;
        }
        return reversed;
    }

    /**
     * This method return the subNode with the given key from the list
     * @param list-list of subNodes
     * @param key-the current key of the subNode
     * @return -the subNode associated with the given key
     */
    public subNode getSubNode(List<subNode> list,int key) {
        int i = 0, size = list.size();
        while (i < size) {//looking for the subNode with the dest key
            if (list.get(i)._currentKey == key) {
                return list.get(i);}
            i++;
        }
        return null;
    }
    //---------------equals-------------------//

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DWGraph_Algo)) return false;
        DWGraph_Algo that = (DWGraph_Algo) o;
        return getGraph().equals(((DWGraph_Algo) o).getGraph());
    }
}
