package ex2;

import java.util.*;

/**
 * This class implements directed_weighted_graph and supports basic methods like addNode, removeNode , addEdge
 * and more, each method in this class attached with detailed explanations about how it works and Complexity.
 * the data structure of the vertices and edges is HashMap, the neighbors represented with HashMap ass well
 * but there is neighbors HashSet for each vertex in this graph and all those HashSets contained in another HashMap
 */

public class DWGraph_DS implements directed_weighted_graph {
    private final HashMap<Integer, node_data> vertices = new HashMap<>();
    private final HashMap<Integer, edge_data> edges = new HashMap<>();
    private final HashMap<Integer, HashSet<node_data>> neighbors = new HashMap<>();
    private int ModeCount = 0;


    //----------------------DWGraph_DS-methods-----------------//
    //------------empty constructor-------------//
    public DWGraph_DS() {
    }

    /**
     * This method return the node_data that associate with the given key
     * Complexity-O(1)
     *
     * @param key - the node_id
     * @return node_data, if this graph does not contains this node return null
     */
    @Override
    public node_data getNode(int key) {
        return this.vertices.get(key);
    }

    /**
     * return the edge_data between the src node and dest node, if there is no edge then return null
     * Complexity-O(1)
     *
     * @param src-the  source of this edge
     * @param dest-the destination of this edge
     * @return edge_data, null if none
     */

    @Override
    public edge_data getEdge(int src, int dest) {
        return this.edges.get(hashCode(src,dest));//if this map contains the key return the edge else return null
    }

    /**
     * Allows to add a new node(vertex) into this graph
     * Complexity-O(1)
     *
     * @param n-the new node
     */
    @Override
    public void addNode(node_data n) {
        this.vertices.putIfAbsent(n.getKey(), n);//add the node only if this graph does not contains n.getKey()
        HashSet<node_data> ni = new HashSet<>();//create HashMap to represent his neighbors
        this.neighbors.putIfAbsent(n.getKey(), ni);//put it in the neighbors hashmap
        ModeCount++;//increase the MC by 1
    }

    /**
     * connect the edge with the give w (weight) between the src node and the dest node
     * Complexity-O(1)
     *
     * @param src  - the source of the edge.
     * @param dest - the destination of the edge.
     * @param w    - positive weight representing the cost (aka time, price, etc) between src-->dest.
     */
    @Override
    public void connect(int src, int dest, double w) {
        if (w > 0&&src!=dest) {//if the weight is positive number
            if (isInTheGraph(src, dest)) {//this method return true if src and dest in this graph
                this.edges.put(hashCode(src, dest), new EdgeData(src, dest, w));//add the new edge (or override the old value)
                ModeCount++;//increase the MC by 1
                this.neighbors.get(src).add(this.vertices.get(dest));//add dest to src neighbors
                this.neighbors.get(dest).add(this.getNode(src));//add src
            }
        }
    }

    /**
     * return a Collection represent this graph vertices
     * Complexity-O(1)
     *
     * @return Collection of this graph nodes
     */
    @Override
    public Collection<node_data> getV() {
        return this.vertices.values();
    }

    /**
     * This method return the Collection represent the edges that associate with the given node_id
     * Complexity- if k is the number of this node_id neighbors-O(k)
     *
     * @param node_id-the node key
     * @return Collection of this node_id edges
     */
    @Override
    public Collection<edge_data> getE(int node_id) {
        List<edge_data> Collection = new LinkedList<>();
        if (!this.vertices.containsKey(node_id)) {//if this node is not in this graph return null
            return Collection;
        }
        else {//at this point if node_id has no neighbors the method will return an empty list
            //iterate of all his neighbors
            for (ex2.node_data n : this.neighbors.get(node_id)) {
                edge_data edgePointer = this.getEdge(node_id, n.getKey());//pointer to the edge between src and his neighbors
                if(edgePointer!=null){
                Collection.add(edgePointer);//add the edge to the collection
            }}
        }
        return Collection;//if node_id does not have any neighbors return empty Collection
    }

    /**
     * This method remove the node associate with the given key, if this node
     * has neighbors (connected to another nodes in this graph) then remove this node
     * from all his neighbors and all his edges.
     * Complexity-if k is the number of this node_id neighbors-O(k)
     *
     * @param key-the key of the node should be remove
     * @return the value of the removed node
     */
    @Override
    public node_data removeNode(int key) {
        if (this.vertices.containsKey(key)) {//if this node is in the graph
            for (node_data pointer : this.neighbors.get(key)) {//while this node has neighbors
                this.neighbors.get(pointer.getKey()).remove(getNode(key));//remove this node from each of his neighbors
                this.edges.remove(hashCode(key, pointer.getKey()));//remove the edge from this node to his neighbor
                this.edges.remove(hashCode(pointer.getKey(), key));//remove the edge from his neighbor to this node
                ModeCount++;// increase the ModeCount every neighbor that is removed(should i increase the MC by 2?)
            }
            this.neighbors.remove(key);//remove him from the HashMap that is keeping the neighbors HasSet
            ModeCount++;//increase the ModeCount again for removing this node from the graph
            return this.vertices.remove(key);
        }
        return null;//if this node is not in the graph return null
    }

    /**
     * remove the edge between the src node and the dest node
     * return the value of the removed node-if there is no edge between src and dest-return null
     * Complexity- O(1)
     *
     * @param src  - the source node
     * @param dest - the destination node
     * @return the value of the removed node- null if none
     */
    @Override
    public edge_data removeEdge(int src, int dest) {
        this.neighbors.get(src).remove(getNode(dest));//remove dest from src neighbors
        ModeCount++;
        return this.edges.remove(hashCode(src,dest));//return the edge that has been remove



    }

    /**
     * return the number of the vertices in this graph
     * Complexity-O(1)
     *
     * @return number of nodes
     */
    @Override
    public int nodeSize() {
        return this.vertices.size();
    }

    /**
     * return the number of the edges in this graph
     * Complexity-O(1)
     *
     * @return number of edges
     */
    @Override
    public int edgeSize() {
        return this.edges.size();
    }

    /**
     * return the ModeCount (number of changes that made in this graph)
     *
     * @return ModeCount
     */
    @Override
    public int getMC() {
        return ModeCount;
    }
    //-----hashcode------//

    /**
     * return the hashcode of to integers- src and dest
     *
     * @param src-the  source node
     * @param dest-the destination node
     * @return -hashcode of two nodes key
     */
    public int hashCode(int src, int dest) {
        return Objects.hash(src, dest);
    }

    /**
     * Auxiliary method-return the edge hashcode if there is an edge between node1 and node2
     * else return -1
     * Complexity-O(1)
     */
    public boolean hasEdge(int src, int dest) {
        boolean ans = false;
        if (this.neighbors.get(src).contains(this.vertices.get(dest))) {//if src connected to dest
            ans = true;
        }
        return ans;
    }

    public boolean isInTheGraph(int src, int dest) {
        boolean ans;
        if (this.vertices.containsKey(src) && this.vertices.containsKey(dest)) {
            ans = true;
        } else {
            ans = false;
        }
        return ans;
    }
    //--------------setters and getters---------------------//

    public HashMap<Integer, node_data> getVertices() {
        return this.vertices;
    }

    public HashMap<Integer, edge_data> getEdges() {
        return this.edges;
    }

    public HashMap<Integer, HashSet<node_data>> getNeighbors() {
        return this.neighbors;
    }

    public int getModeCount() {
        return this.ModeCount;
    }

    public void setModeCount(int modeCount) {
        this.ModeCount = modeCount;
    }
    //-------toString--------//

    @Override
    public String toString() {
        return "DWGraph_DS{" +
                "vertices=" + vertices +
                ", edges=" + edges +
                ", neighbors=" + neighbors +
                ", ModeCount=" + ModeCount +
                '}';
    }
    //-----equals-----//

    @Override
    public boolean equals(Object o) {
        boolean ans=false;
        if (this == o) return true;
        if (!(o instanceof DWGraph_DS)) return false;
        DWGraph_DS that = (DWGraph_DS) o;
        for(node_data n: that.getV()) {
            if (!getVertices().containsKey(n.getKey())) {
                return false;
            }
            for(edge_data e: that.getE(n.getKey())){
                if(!getEdges().containsKey(hashCode(e.getSrc(),e.getDest()))){
                    return false;
                }

        }
        }
        return true;

    }
}
