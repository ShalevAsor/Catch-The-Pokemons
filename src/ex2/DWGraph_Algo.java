package ex2;
import com.google.gson.*;
import com.google.gson.stream.JsonReader;


import java.io.*;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

/**
 * This class represent directed weighted graph algorithms, it support basic methods like init,copy and getGraph.
 * it also support few algorithms like BFS and Dijkstra's algorithm that used for methods like isConnected
 * and shortestPath.
 * each method in this class is attached with explanations.
 */

public class DWGraph_Algo implements dw_graph_algorithms {
    directed_weighted_graph graph=new DWGraph_DS();
    static  String SAVED_GRAPH="graph.json";

    //-------------------subNode class------------------//

    /**
     * This class used in Dijkstra's Algorithm
     */
    private static class subNode implements Comparable<subNode>{
        private double _weight = Double.POSITIVE_INFINITY;
        private final int _parent;//represent the node thats this subNode came from
        private final int _currentKey;

        public subNode(double weight, int parent, int key) {
            this._parent = parent;
            this._weight = weight;
            this._currentKey = key;
        }
        public subNode(int parentKey ,int key) {
            this._currentKey = key;
            this._parent=parentKey;
        }
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
    class GraphJsonDeserializer implements JsonDeserializer<DWGraph_DS> {


        @Override
        public DWGraph_DS deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            DWGraph_DS graph_ds = new DWGraph_DS();
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            if (jsonObject.get("Edges") != null) {
                JsonArray vertices = jsonObject.get("Nodes").getAsJsonArray();
                JsonArray edges = jsonObject.get("Edges").getAsJsonArray();
                for (int i=0;i<vertices.size();i++) {
                    NodeData.GeoLocation glPointer = null;
                    JsonElement node = vertices.get(i);
                    int nodeKey = node.getAsJsonObject().get("id").getAsInt();
                    //  double weight=node.getAsJsonObject().get("_weight").getAsDouble();
                    //  int nodeTag=node.getAsJsonObject().get("_tag").getAsInt();
                    // String nodeInfo=node.getAsJsonObject().get("_info").getAsString();
                    if (node.getAsJsonObject().get("pos") != null) {
                        String[] arr= new String[3];
                        String location=node.getAsJsonObject().get("pos").getAsString();
                        arr=location.split(",");
                        double _x = Double.parseDouble(arr[0]);
                        double _y = Double.parseDouble(arr[1]);
                        double _z = Double.parseDouble(arr[2]);
                        NodeData.GeoLocation nodeGl = new NodeData.GeoLocation(_x, _y, _z);
                        glPointer = nodeGl;
                    }
                    NodeData n = new NodeData(nodeKey);
                    n.setLocation(glPointer);
                    graph_ds.addNode(n);
                }
                for (int i=0;i<edges.size();i++) {
                    JsonElement edge = edges.get(i);
                    int srcKey = edge.getAsJsonObject().get("src").getAsInt();
                    int destKey = edge.getAsJsonObject().get("dest").getAsInt();
                    double weight = edge.getAsJsonObject().get("w").getAsDouble();
                    //  int tag=edge.getAsJsonObject().get("_tag").getAsInt();
                    graph_ds.connect(srcKey, destKey, weight);
                    // graph_ds.getEdge(srcKey,destKey).setTag(tag);
                }
//                int mc = jsonObject.get("ModeCount").getAsInt();
//                graph_ds.setModeCount(mc);
                return graph_ds;
            } else {
                JsonObject vertices = jsonObject.get("vertices").getAsJsonObject();
                JsonObject edges = jsonObject.get("edges").getAsJsonObject();

                for (Map.Entry<String, JsonElement> set : vertices.entrySet()) {
                    NodeData.GeoLocation glPointer = null;
                    JsonElement node = set.getValue();
                    int nodeKey = node.getAsJsonObject().get("_key").getAsInt();
                    double weight = node.getAsJsonObject().get("_weight").getAsDouble();
                    int nodeTag = node.getAsJsonObject().get("_tag").getAsInt();
                    String nodeInfo = node.getAsJsonObject().get("_info").getAsString();
                    if (node.getAsJsonObject().get("_gl") != null) {
                        double _x = node.getAsJsonObject().get("_gl").getAsJsonObject().get("_x").getAsDouble();
                        double _y = node.getAsJsonObject().get("_gl").getAsJsonObject().get("_y").getAsDouble();
                        double _z = node.getAsJsonObject().get("_gl").getAsJsonObject().get("_z").getAsDouble();
                        NodeData.GeoLocation nodeGl = new NodeData.GeoLocation(_x, _y, _z);
                        glPointer = nodeGl;
                    }
                    NodeData n = new NodeData(nodeKey, weight, nodeTag, nodeInfo);
                    n.setLocation(glPointer);
                    graph_ds.addNode(n);
                }
                for (Map.Entry<String, JsonElement> set : edges.entrySet()) {
                    JsonElement edge = set.getValue();
                    int srcKey = edge.getAsJsonObject().get("_src").getAsInt();
                    int destKey = edge.getAsJsonObject().get("_dest").getAsInt();
                    double weight = edge.getAsJsonObject().get("_weight").getAsDouble();
                    int tag = edge.getAsJsonObject().get("_tag").getAsInt();
                    graph_ds.connect(srcKey, destKey, weight);
                    graph_ds.getEdge(srcKey, destKey).setTag(tag);
                }
                int mc = jsonObject.get("ModeCount").getAsInt();
                graph_ds.setModeCount(mc);
                return graph_ds;
            }
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
        Iterator<node_data> it1 = this.graph.getV().iterator();
        while (it1.hasNext()) {
            node_data pointer = it1.next();//pointer to this graph vertices
            copy.addNode(new NodeData(pointer.getKey()));//add vertex to copy
            copy.getVertices().get(pointer.getKey()).setTag(pointer.getTag());//set the original tag of this node
            copy.getVertices().get(pointer.getKey()).setInfo(pointer.getInfo());//set the original info of this node
        }
        Iterator<node_data> it2 = this.graph.getV().iterator();
        while (it2.hasNext()) {
            node_data pointer2 = it2.next();
            Iterator<edge_data> it3 = this.graph.getE(pointer2.getKey()).iterator();//pointer2 neighbors
            while (it3.hasNext()) {
                edge_data pointer3 = it3.next();//
                double weight = this.graph.getEdge(pointer2.getKey(), pointer3.getDest()).getWeight();//get the original weight
                copy.connect(pointer2.getKey(), pointer3.getDest(), weight);//connect on the copy.
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

    @Override
    public boolean save(String file) {
        boolean saved;
//        Path myfile = Paths.get(file);
//        Gson gson = new Gson();
//        String js = gson.toJson(this.graph);
        Gson gson=new GsonBuilder().setPrettyPrinting().create();
        String jsGraph= gson.toJson(this.graph);
        try {

            PrintWriter pw=new PrintWriter(new File(file));
            pw.write(jsGraph);
            pw.close();
            saved=true;
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
            saved= false;
        }
        return saved;
    }

    @Override
    public boolean load(String file)  {
        boolean loaded;
        try{
            GsonBuilder gsbuilder=new GsonBuilder();
            gsbuilder.registerTypeAdapter(DWGraph_DS.class, new GraphJsonDeserializer());
                    Gson gson=gsbuilder.create();
            FileReader fr=new FileReader(file);
            this.graph=gson.fromJson(fr,DWGraph_DS.class);
//            Gson gson=new Gson();
//            directed_weighted_graph lGraph;
//            Reader reader = Files.newBufferedReader(Paths.get(file));
//            //Reader reader = Files.newBufferedReader(Paths.get(file));
//           //  br =new BufferedReader(new FileReader(file));
//           // JsonReader JS=new JsonReader(new FileReader(file));
//
//            this.graph= gson.fromJson(reader,DWGraph_DS.class);
//            //reader.close();
//           // this.graph=gson.fromJson(json_str,DWGraph_DS.class);
//            reader.close();

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
     * @param src
     * @return
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
     * @param list
     * @return
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
