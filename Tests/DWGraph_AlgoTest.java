import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ex2.*;


import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DWGraph_AlgoTest {

    public dw_graph_algorithms graphCreator1() {
        directed_weighted_graph graph = new DWGraph_DS();
        int i = 1, size = 5;
        while (i<=size) {
            graph.addNode(new NodeData(i));
            i++;
        }
        dw_graph_algorithms g=new DWGraph_Algo();
        g.init(graph);
        return g;
    }
    public directed_weighted_graph graphCreator2() {
        directed_weighted_graph graph = new DWGraph_DS();
        int i = 1, size = 5;
        while (i<=size) {
            graph.addNode(new NodeData(i));
            i++;
        }
        return graph;
    }
    /*
    At this test i will init g to g0 , i will remove node from g and make sure that the node removed
    from g0.
     */
    @Test
    public void initTest(){
        directed_weighted_graph g=this.graphCreator2();
        DWGraph_Algo g0=new DWGraph_Algo();
        g0.init(g);
        Assertions.assertEquals(5,g0.getGraph().nodeSize());
        g.removeNode(5);
        Assertions.assertEquals(4,g0.getGraph().nodeSize());

    }
    /*
    At this test i will verify that the graph return from the method getGraph
    it is the correct graph
     */
    @Test
    public void getGraphTest(){
        dw_graph_algorithms g1=this.graphCreator1();
        Assertions.assertEquals(5,g1.getGraph().nodeSize());
        Assertions.assertEquals(0,g1.getGraph().edgeSize());

    }
    /*
    At the first part of this test i will check if the graph was copied with all of his vertices and his neighbors
    at the second part i will test that it is a deep copy, i will remove an  vertex from the copy, and test if there is
    any change in the original graph.
     */
    @Test
    public void copyTest(){
        dw_graph_algorithms g2=this.graphCreator1();
        directed_weighted_graph g3=g2.copy();
        //part one
        Assertions.assertEquals(g2.getGraph().nodeSize(),g3.nodeSize());
        Assertions.assertEquals(g2.getGraph().edgeSize(),g3.edgeSize());
        int i=1,index =0,size=g3.nodeSize();
        while(index<size){
            Assertions.assertEquals(g3.getE(i).size(),g2.getGraph().getE(i).size());
            index++;
        }
        //part two
        g3.removeNode(1);
        Assertions.assertNotEquals(g3.nodeSize(),g2.getGraph().nodeSize());
        g3.addNode(new NodeData(6));
        g3.connect(2,3,1);
        g3.connect(6,4,1);
        Assertions.assertNotEquals(g3.getE(2).size(),g2.getGraph().getE(2).size());
        Assertions.assertNotEquals(g3.getE(6).size(),g2.getGraph().getE(6).size());


    }

    /**
     * This test verify if there is a path from src node to every other node int the graph
     */
    @Test
    public void isWeaklyConnectedTest(){
        directed_weighted_graph g4=new DWGraph_DS();
        DWGraph_Algo tGraph=new DWGraph_Algo();
        int i=1,size=500;
        while(i<=size){
            g4.addNode(new NodeData(i));
            i++;}
        int index=0,numOfNodes=g4.nodeSize();
        while(index<numOfNodes-1){
            g4.connect(index+1,index+2,1);
            index++;
        }
        tGraph.init(g4);

        Assertions.assertTrue(tGraph.isWeaklyConnected(1));//basic connected graph with 500 nodes 499 edges
        Assertions.assertTrue(tGraph.isWeaklyConnected(1));
        tGraph.getGraph().removeEdge(149,150);
        Assertions.assertFalse(tGraph.isWeaklyConnected(1));
    }

    /**
     * This test verify that isConnected return true only if there is a path from each node in the graph
     * to every other node
     */
    @Test
    void isConnectedTest(){
        /*
                 (v1) <--------> (v2)
                    ^               ^
                    |               |
                    |               |
                    v               v
                  (v3) <--------> (v4)
                  for example from v1 it is possible to reach v2,v3,v4 from v2:v1,v3,v4
                  from v3:v1,v2,v4 from v4:v1,v2,v3 so this is a strongly connected graph
         */
        directed_weighted_graph g5=new DWGraph_DS();
        int i=1,size=4;
        while(i<=size){
            g5.addNode(new NodeData(i));
            i++;
        }
        //create the graph above
        g5.connect(1,2,1);
        g5.connect(2,1,2);
        g5.connect(1,3,1);
        g5.connect(3,1,2);
        g5.connect(2,4,1);
        g5.connect(4,2,2);
        g5.connect(3,4,1);
        g5.connect(4,3,2);
        dw_graph_algorithms g6=new DWGraph_Algo();
        g6.init(g5);
        Assertions.assertTrue(g6.isConnected());
        /*
        after remove one edge from (v3,v4) the graph above should stay connected but after
          removing the edge (v3,v1) the graph will be disconnected
         */
        g5.removeEdge(3,4);
        Assertions.assertTrue(g6.isConnected());
        g5.removeEdge(3,1);
        Assertions.assertFalse(g6.isConnected());
        //graph with 1 node is strongly connected
        directed_weighted_graph g7=new DWGraph_DS();
        g7.addNode( new NodeData(1));
        g6.init(g7);
        Assertions.assertTrue(g6.isConnected());
    }
    /**
  Test for shortestPathDist, make sure this method return the correct distance in a different cases
   */
    @Test
    public void shortestPathDistTest(){
        directed_weighted_graph g6=new DWGraph_DS();
        dw_graph_algorithms g7=new DWGraph_Algo();
        g7.init(g6);
        g6.addNode(new NodeData(1));
        g6.addNode(new NodeData(2));
        g6.addNode(new NodeData(3));
        g6.connect(1,2,1);
        g6.connect(1,3,8);
        g6.connect(2,3,2);
        Assertions.assertEquals(3,g7.shortestPathDist(1,3));//simple test the sortestPathDist:1-->2-->3(0+1+2=3)
        Assertions.assertEquals(-1,g7.shortestPathDist(3,1));//there is no path from 3 to 1!
        g6.addNode(new NodeData(4));
        g6.addNode(new NodeData(5));
        g6.connect(4,5,0.1);
        Assertions.assertEquals(3,g7.shortestPathDist(1,3));//the shortest path between (1,3) is still 3
        g6.connect(2,4,0.3);
        g6.connect(5,3,0.5);
        Assertions.assertEquals(1.9,g7.shortestPathDist(1,3),0.00000001);//shortest path is:1-->2-->4-->5-->3
        Assertions.assertEquals(-1,g7.shortestPathDist(3,1),0.00000001);//there is no path from 3 to 1!
        g6.removeEdge(1,2);
        g6.removeEdge(1,3);
        Assertions.assertEquals(-1,g7.shortestPathDist(1,3));//there is no path between (1,3)
        Assertions.assertEquals(-1,g7.shortestPathDist(1,18));//18 is not in this graph
        Assertions.assertEquals(-1,g7.shortestPathDist(18,1));//should return the same -1
        Assertions.assertEquals(0,g7.shortestPathDist(1,1));//the shortestPathDist between (1,1) is 0

    }
    /**
     * This test verify shortestPath return the correct path for src node to dest node
     */
    @Test
    void shortestPathTest(){
        dw_graph_algorithms g8=this.graphCreator1();
        g8.getGraph().connect(1,2,1);
        g8.getGraph().connect(1,3,0.5);
        g8.getGraph().connect(3,5,10);
        g8.getGraph().connect(2,4,2);
        g8.getGraph().connect(4,5,1);
        List<node_data> myList=new LinkedList<>();
        myList.add(0,g8.getGraph().getNode(1));
        myList.add(1,g8.getGraph().getNode(2));
        myList.add(2,g8.getGraph().getNode(4));
        myList.add(3,g8.getGraph().getNode(5));
        assertEquals(g8.shortestPath(1, 5), myList);//the shortest path should be:1-->2-->4-->5
        directed_weighted_graph g9=this.graphCreator2();
        g9.connect(1,2,0.1);
        g9.connect(2,3,0.2);
        g9.connect(3,4,14);
        g9.connect(4,5,2.5);
        g9.connect(2,4,0.3);
        g8.init(g9);
        myList.clear();
        myList.add(0,g9.getNode(1));
        myList.add(1,g9.getNode(2));
        myList.add(2,g9.getNode(4));
        myList.add(3,g9.getNode(5));
        assertEquals(g8.shortestPath(1, 5), myList);//the shortest path should be:1-->2-->4-->5
        g9.removeEdge(4,5);
        Assertions.assertNull(g8.shortestPath(1,5));//should return null because there is no path between (1,5)
        directed_weighted_graph g10=new DWGraph_DS();
        g10.addNode(new NodeData(1));
        g10.addNode(new NodeData(2));
        g8.init(g10);
        Assertions.assertNull(g8.shortestPath(1,2));//should return null because there is no path between (1,2)
        Assertions.assertNull(g8.shortestPath(1,66));//node66 is not in the graph then this method should return null
        myList.clear();
        myList.add(0,g10.getNode(1));
        assertEquals(g8.shortestPath(1, 1), myList);//the shortest path between (1,1) is: 1
    }
    /**
  At this test i will create a simple weighted graph with five vertices and four edges, each node have one neighbors
  node1 connecting to node2 and node2 connecting to node 3... until node5(node 5 not connecting to node1 at the first part
  of this test) then i will save this graph (g11) and create a new graph(g12) and try to load to g12 the graph that g11 worked with
  and then iam using equals that i made in WGraph_DS to verify that g12 equals to g11.
  at the second part i will connect node5 to node1 and use equals again.
   */
    @Test
    public void saveAndLoadTest(){
        //part one
        dw_graph_algorithms g11=this.graphCreator1();
        dw_graph_algorithms g12=new DWGraph_Algo();
        int i=0,size=g11.getGraph().nodeSize();
        g11.getGraph().connect(1,2,1);
        g11.getGraph().connect(2,3,2);
        g11.getGraph().connect(3,4,3);
        g11.getGraph().getNode(1).setLocation(new NodeData.GeoLocation(2,1,3));
        g11.getGraph().connect(4,5,4);
        Assertions.assertTrue(g11.save("myGraph"));
        Assertions.assertTrue(g12.load("myGraph"));
        Assertions.assertEquals(g11,g12);
        //part two
        g11.getGraph().connect(5,1,5);
        Assertions.assertTrue(g11.save("myGraph"));
        Assertions.assertTrue(g12.load("myGraph"));
        assertEquals(g12.getGraph(), g11.getGraph());
    }
    @Test
    void A0GRAPH(){
        dw_graph_algorithms g=new DWGraph_Algo();
        g.load("C:\\Users\\Shalev Asor\\IdeaProjects\\Ex2\\Ariel_OOP_2020\\data\\A5");
        System.out.println(g.getGraph().toString());
    }

}