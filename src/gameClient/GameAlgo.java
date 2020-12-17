package gameClient;

import api.*;
import gameClient.util.Point3D;

import java.util.*;

public class GameAlgo {
    public static Arena _ar;
    private static final dw_graph_algorithms graphAlgo = new DWGraph_Algo();
    private static HashMap<Integer,geo_location> isReachToTheEdge=new HashMap<>();

    public GameAlgo(){

    }

    public static void moveAgents(game_service game, directed_weighted_graph gameGraph) {
        String lg =game.move();
        List<CL_Agent> log = Arena.getAgents(lg, gameGraph);
        _ar.setAgents(log);
        String fs = game.getPokemons();
        List<CL_Pokemon> ffs = Arena.json2Pokemons(fs);
        _ar.setPokemons(ffs);
        for (CL_Agent ag : log) {
            int id = ag.getID();
            int dest = ag.getNextNode();
            int src = ag.getSrcNode();
            double v = ag.getValue();
            dest = nextNode(ag, gameGraph, src);
            // isReachToTheEdge.put(ag.getID(),gameGraph.getNode(dest).getLocation());
               isReachToTheEdge.put(ag.getID(),gameGraph.getNode(dest).getLocation());
            game.chooseNextEdge(ag.getID(), dest);
          //  System.out.println("Agent: " + id + ", val: " + v + "   turned to node: " + dest);
        }
    }

    /**
     * This method return the next node for the given agent
     * the next node will be the next node in the closest path to the closest pokemon
     * of this agent
     *
     * @param g-the graph
     * @param src-the node source
     * @param agent-the agent that need to move to the next node
     * @return the next node
     */
    public static int nextNode(CL_Agent agent, directed_weighted_graph g, int src) {
        edge_data edge;
        Collection<edge_data> ee = g.getE(src);
        Iterator<edge_data> itr = ee.iterator();
        int nextNode= itr.next().getSrc();
        List<CL_Pokemon> POKLIST = _ar.getPokemons();//list of all the pokemons in the game
        for (CL_Pokemon cp : _ar.getPokemons()) {
            _ar.updateEdge(cp, g);
        }
        Object[] array = closestPok(POKLIST, agent, src);//array with the closest pokemon and this pokemon edge
        edge = (edge_data) array[0];
        CL_Pokemon pokemon = (CL_Pokemon) array[1];
        if (edge.getSrc() > edge.getDest()) {//this pokemon Type=-1
            if (src != edge.getSrc()) {//and this is not the last node
                List<node_data> nodelIST = graphAlgo.shortestPath(src, edge.getSrc());//get the shortest path to this node
                if (nodelIST.size() >= 2) {//if there are more then two nodes in the list
                    nextNode = nodelIST.get(1).getKey();//go to the second node ( the first node is the src)

                } else {
                    nextNode = nodelIST.get(0).getKey();//this is the last node
                    pokemon.setOnTarget(null);//that pokemon was cached and he is no longer a target
                }
            } else {
                nextNode = edge.getDest();//this is the last node go to the dest
            }
        } else if (edge.getSrc() < edge.getDest()) {//same for pokemon Type=1
            if (src != edge.getDest()) {
                List<node_data> nodelIST = graphAlgo.shortestPath(src, edge.getDest());
                if (nodelIST.size() >= 2) {
                    nextNode = nodelIST.get(1).getKey();
                } else {
                    nextNode = nodelIST.get(0).getKey();
                    pokemon.setOnTarget(null);
                }
            } else {
                nextNode = edge.getSrc();
            }
        }
        return nextNode;
    }

    /**
     * This method return an array with the closest pokemon to the given agent
     * and this pokemon edge
     * @param pokLIST - list represents all the pokemons in this level
     * @param agent - the agent that should move to the next node
     * @param src - the current node of this agent
     * @return Object array with closest pok and the edge
     */
    public static Object[] closestPok(List<CL_Pokemon> pokLIST, CL_Agent agent, int src) {
        int i = 0;
        CL_Pokemon pokPointer=new CL_Pokemon();
        Object[] array = new Object[2];
        double minDis = Double.POSITIVE_INFINITY;
        while (i < pokLIST.size()) {
            //if this pok dont on target
            if (pokLIST.get(i).getOnTarget() == null || pokLIST.get(i).getOnTarget()==agent) {
                double curDis;
                if(pokLIST.get(i).getType()==1) {
                    curDis = graphAlgo.shortestPathDist(src, pokLIST.get(i).get_edge().getSrc());//shortest path to this pok
                }
                else{
                    curDis = graphAlgo.shortestPathDist(src, pokLIST.get(i).get_edge().getDest());
                }
                if (curDis <= minDis) {
                    minDis = curDis;
                    array[0] = pokLIST.get(i).get_edge();
                    array[1] = pokLIST.get(i);
                    pokPointer=pokLIST.get(i);
                }}
            i++;
        }
        pokPointer.setOnTarget(agent);//set this pok on target
        return array;
    }

    public dw_graph_algorithms getGraphAlgo(){
        return this.graphAlgo;
    }
    public Arena getAr(){
        return this._ar;
    }

    public static boolean readyToMove(List<CL_Agent> clc,directed_weighted_graph gameGraph){
        boolean ans=false;
        if(clc==null)return true;
        for(CL_Agent agent:clc){
            if(agent.getNextNode()==-1){
                ans=true;
                break;
            }
            if(isReachToTheEdge.containsValue(gameGraph.getNode(agent.getID()).getLocation())){
                ans=true;
                break;
            }
        }

        return ans;
    }
    public static void updateCurrentAgentLocation(List<CL_Agent> clc,directed_weighted_graph graph){

        if(clc==null){
            //dont update anything
        }
        else{
            for(CL_Agent agent:clc){
                int node=agent.getSrcNode(),id=agent.getID();
                Point3D p=new Point3D(graph.getNode(node).getLocation().x(),graph.getNode(node).getLocation().y(),
                        graph.getNode(node).getLocation().z());
                isReachToTheEdge.putIfAbsent(id,p);
            }

        }

    }


}
