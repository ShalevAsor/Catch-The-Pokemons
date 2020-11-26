import static org.junit.jupiter.api.Assertions.*;
import ex2.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.Iterator;

/**
 * This is a test class for DWGraph_DS, each method has a separate test.
 * each test examines edge cases
 */
class DWGraph_DSTest {
    /**
     * simple graph creator-add new nodes to the graph by the v_size given
     * @param v_size -the number of vertices
     * @return directed_weighted_graph
     */
   public static directed_weighted_graph graph_creator(int v_size){
       directed_weighted_graph g=new DWGraph_DS();
       for(int i=1;i<=v_size;i++){
           g.addNode(new NodeData(i));
       }
       return g;
   }
   public static directed_weighted_graph graph_creator2(int v_size){
       directed_weighted_graph g=new DWGraph_DS();
       for(int i=1;i<=v_size;i++){
           g.addNode(new NodeData(i));
       }
       return g;
   }

    /**
     * verify that getNode return the correct node_data
     */


   @Test
      void getNodeTest(){
       directed_weighted_graph g1=graph_creator(5);//five vertices graph
       Assertions.assertEquals(5, g1.getNode(5).getKey());//add the node successfully
       g1.getNode(5).setTag(10);//setting node5 tag
       Assertions.assertEquals(10, g1.getNode(5).getTag());//set successfully
       g1.addNode(new NodeData(5));//trying to add new node .
       Assertions.assertEquals(5, g1.getNode(5).getKey());//this is still the original node with the key 6
       Assertions.assertEquals(5, g1.nodeSize());//there is 5 vertices in this graph
       Assertions.assertEquals(1,g1.getNode(1).getKey());
       Assertions.assertNull(g1.getNode(10));//node10 is not in this graph- should return null
   }

    /**
     *This test verify that addNode is adding the node only if he is not in the graph already
     */
   @Test
     void addNodeTest(){
       directed_weighted_graph g2=new DWGraph_DS();
       g2.addNode(new NodeData(1));
       Assertions.assertEquals(1,g2.nodeSize());//nodesize should be one because there is one node in g2
       g2.addNode(new NodeData(7));
       Assertions.assertEquals(2,g2.nodeSize());//g2 nodesize=2
       NodeData n1=new NodeData(3);
       n1.setKey(7);//this key is already associate with a node in the graph
       g2.addNode(n1);
       Assertions.assertEquals(2,g2.nodeSize());//the size should stay 2

   }

    /**
     * This test verify that connect (node1,node2) is not equal to connect (node2,node1),
     * also that negative weight and connect nodes that are not in the graph is not allow
     */
   @Test
    void connectTest(){
       directed_weighted_graph g3=graph_creator(5);
       g3.connect(1,2,1);//connecting two nodes
       edge_data e=new EdgeData(1,2,1);//create new edge with the same value
       Assertions.assertEquals(1,g3.getEdge(1,2).getWeight());//verify the weight is 1
       Assertions.assertNull(g3.getEdge(4,5));//there is no edge between (4,5)
       Assertions.assertEquals(e,g3.getEdge(1,2));//they should be equals
       g3.connect(1,2,2);//already has an edge-should update the new weight
       Assertions.assertEquals(2,g3.getEdge(1,2).getWeight());
       g3.connect(10,20,1);//10 and 20 is not in this graph
       Assertions.assertNull(g3.getEdge(10,20));//should be null
       g3.connect(2,1,-1);//negative weight does not allow
       Assertions.assertNull(g3.getEdge(2,1));
       g3.connect(2,3,-1);//negative weight does not allow
       Assertions.assertEquals(2,g3.getEdge(1,2).getWeight());
       g3.connect(4,4,1);//should not allowed to connect node to himself
       Assertions.assertNull(g3.getEdge(4,4));

   }

    /**
     * This test verify that getEdge return the correct edge and if there is no edge
     * between node1 and node2 return null.
     */
   @Test
    void getEdgeTest(){
       directed_weighted_graph  g4=new DWGraph_DS();//create graph
       g4.addNode(new NodeData(1));
       g4.addNode(new NodeData(2));
       g4.addNode(new NodeData(3));
       g4.connect(1,2,1);//legal connecting
       Assertions.assertEquals(1,g4.getEdge(1,2).getWeight());
       g4.connect(2,1,0.5);
       Assertions.assertEquals(0.5,g4.getEdge(2,1).getWeight());
       Assertions.assertEquals(1,g4.getEdge(1,2).getWeight());//(1,2) should not change
       g4.connect(2,6,1);//node6 is not in this graph
       Assertions.assertNull(g4.getEdge(2,6));
       g4.connect(2,3,-2);
       Assertions.assertNull(g4.getEdge(2,3));//negative weight does not allow
       g4.connect(2,2,1);
       Assertions.assertNull(g4.getEdge(2,2));//connect node to himself does not allow, should return null

   }

    /**
     * This test verify that getV return all the vertices in this graph
     */
    @Test
    public void getVTest() {
        int i=1;
        directed_weighted_graph g5 = graph_creator2(5);
        Assertions.assertEquals(5, g5.getV().size());//make sure getV size is 5
        Iterator<node_data> it = g5.getV().iterator();
        while (it.hasNext()) {
            node_data pointer = it.next();
            node_data n=new NodeData(i++);
            Assertions.assertEquals(n,pointer);///verify every vertex in this graph equal to getV vertices
        }
    }
    /**
     * This test verify that getE return the correct edge collection
     */
    @Test
    void getETest(){
        directed_weighted_graph g6 = graph_creator2(5);
        g6.connect(1,2,1);
        g6.connect(1,3,2);
        g6.connect(1,4,3);
        g6.connect(1,5,4);
        int i=2,z=1,size=g6.getE(1).size();
        edge_data[] edgeArray=new edge_data[size+1];
        Assertions.assertEquals(5, g6.getV().size());//make sure getV size is 5
        Assertions.assertEquals(4, size);//verify node1 has 4 edges
        for (edge_data pointer : g6.getE(1)) { //all node1 edges
            int index = (int) pointer.getWeight();
            edgeArray[index] = pointer;
        }
        for(int t=1;t<=size;t++){
            edge_data e=new EdgeData(1,i++,z++);
            Assertions.assertEquals(e,edgeArray[t]);
        }


    }
    /**
     *This test verify that the method removeEdge is removing the correct edge.
     */
    @Test
    void removeEdgeTest(){
        directed_weighted_graph g7=graph_creator2(5);
        g7.connect(1,2,1);
        edge_data e=new EdgeData(1,2,1);
        Assertions.assertEquals(e,g7.removeEdge(1,2));//e is equal to the edge between (1,2)
        Assertions.assertNull(g7.removeEdge(1,2));//there is no edge between(1,2) so the method should return null
        node_data n=new NodeData(6);//this node is not in the graph
        g7.connect(1,6,2);
        Assertions.assertNull(g7.removeEdge(1,6));//node6 is not the graph
        g7.connect(2,2,1);
        Assertions.assertNull(g7.removeEdge(2,2));//edge between node to himself does not allowed-should return null
        g7.connect(2,3,1);
        g7.connect(3,2,2);
        edge_data e1=new EdgeData(2,3,1);
        edge_data e2=new EdgeData(3,2,2);
        Assertions.assertEquals(e1,g7.removeEdge(2,3));//not the same edge as (3,2)
        Assertions.assertEquals(e2,g7.removeEdge(3,2));

    }

    /**
     * This test verify that node is removed and all of the edges he was connected with
     */
    @Test
    void removeNodeTest(){
        directed_weighted_graph g8=graph_creator2(6);
        g8.connect(1,2,1);
        Assertions.assertEquals(1,g8.getE(1).size());//node1 has one edge
        g8.removeNode(1);
        Assertions.assertEquals(0,g8.getE(2).size());//node2 has no edges
        edge_data e=new EdgeData(1,2,1);
        Assertions.assertNull(g8.getNode(1));//node 1 removed from the graph
        g8.addNode(new NodeData(1));
        g8.connect(1,2,0.5);
        g8.connect(2,1,0.6);
        Assertions.assertEquals(1,g8.getE(1).size());//node1 edge: (1,2)
        Assertions.assertEquals(1,g8.getE(2).size());//node2 edge: (1,2)
        g8.removeNode(2);
        Assertions.assertEquals(0,g8.getE(1).size());//node 1 has no edges because node2 has been removed
        g8.connect(1,3,2.5);
        g8.connect(1,4,0.3);
        g8.connect(1,5,6);
        g8.connect(1,6,9);
        Assertions.assertEquals(4,g8.getE(1).size());//node1 has 4 edges
        g8.removeNode(3);
        g8.removeNode(4);
        g8.removeNode(5);
        g8.removeNode(6);
        Assertions.assertEquals(0,g8.getE(1).size());//node1 has node edges
    }
    /**
     * This test verify that the method nodeSize return the correct number of vertices in the graph
     */
    @Test
    void nodeSizeTest(){
        directed_weighted_graph g9=graph_creator2(100);
        Assertions.assertEquals(100,g9.nodeSize());//started with 100 nodes
        g9.removeNode(101);//101 is not in the graph
        Assertions.assertEquals(100,g9.nodeSize());//the size should stay 100
        g9.removeNode(100);//remove one node from the graph
        Assertions.assertEquals(99,g9.nodeSize());//the size decrease by one
        g9.addNode(new NodeData(100));//add one node into the graph
        Assertions.assertEquals(100,g9.nodeSize());//the size increase by one
        int i=1,size=50;
        while(i<=size){
            g9.removeNode(i);//remove 50 nodes from the graph
            i++;
        }
        Assertions.assertEquals(50,g9.nodeSize());//the node size decrease by 50

    }

    /**
     * This test verify that edgeSize return the correct number of edges in the graph
     */
    @Test
    void edgeSizeTest(){
        directed_weighted_graph g10=graph_creator2(10);//started with 10 node 0 edges
        g10.connect(1,2,1);//add one edge to the graph
        Assertions.assertEquals(1,g10.edgeSize());
        g10.removeEdge(1,2);//remove the edge
        Assertions.assertEquals(0,g10.edgeSize());//number of edges should be zero
        g10.connect(1,2,1);//(1,2)
        g10.connect(2,1,1);//(2,1)
        Assertions.assertEquals(2,g10.edgeSize());
        g10.removeNode(1);
        Assertions.assertEquals(0,g10.edgeSize());//node1 removed so there is no edges in the graph
        g10.connect(2,3,1);
        g10.connect(2,4,1);
        g10.connect(2,5,1);
        g10.connect(10,2,2);
        g10.connect(9,2,2);
        g10.connect(3,2,1);
        Assertions.assertEquals(6,g10.edgeSize());
        g10.connect(6,8,-1);//the edge size should stay 6
        Assertions.assertEquals(6,g10.edgeSize());
        g10.connect(6,6,1);//the edge size should stay 6
        Assertions.assertEquals(6,g10.edgeSize());
    }

    /**
     * This test verify that getMC (number of changes made in the graph) return the correct number
     */
    @Test
    void getMCTest(){
        directed_weighted_graph g11=graph_creator2(5);
        Assertions.assertEquals(5,g11.getMC());//g11 has 5 nodes,MC start value is 5
        g11.connect(1,2,1);
        Assertions.assertEquals(6,g11.getMC());//connected one edge, MC value:6
        g11.removeEdge(1,2);
        Assertions.assertEquals(7,g11.getMC());//removed one edge, MC value:7
        g11.connect(1,2,1);
        g11.connect(1,3,1);
        Assertions.assertEquals(9,g11.getMC());//connected two edges,MC value:9
        g11.removeNode(1);
        Assertions.assertEquals(12,g11.getMC());//removed node1 MC +1 for the node that removed and +2 for his edges
        g11.connect(2,2,1);//illegal MC should stay 12
        Assertions.assertEquals(12,g11.getMC());
        g11.connect(2,3,-1);//illegal weight-MC should stay 12
        g11.connect(11,2,2);//11 is not in the graph-MC should stay 12
        Assertions.assertEquals(12,g11.getMC());
    }

}