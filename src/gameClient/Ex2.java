package gameClient;

import Server.Game_Server_Ex2;
import api.*;
import com.google.gson.*;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * TThis class represents the Pokemon game management,
 * it supports an algorithm that calculates the shortest path to Pokemon using algorithms supported by DWG_ALGO
 * this class uses Threads.
 */

public class Ex2 implements Runnable {
    private static GraphFrame _win;
    private static GameAlgo _ga;
    private static GraphPanel _gp;
    private static int _level = 11;
    private static int _id = 123;
    private static boolean _start;
    private static boolean runFromCmd = false;
    private static GraphLabel _gl;


    public static void main(String[] args) {
        if (args.length != 0) {//the user is running the program from the cmd/terminal
            runFromCmd = true;
            _id = Integer.parseInt(args[0]);
            _level = Integer.parseInt(args[1]);
        }
        ExecutorService executorService = Executors.newFixedThreadPool(1);
        executorService.execute(new Ex2());
    }

    @Override
    public void run() {
        if (!runFromCmd) {
            _gl = new GraphLabel();//_gl is the login frame
            while (!_gl.getUserIsDone()) {//as long as the user dont logged in
                try {
                    Thread.sleep(100);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

            }
            _level = _gl.getLevel();
            _id = _gl.getId();
            _gl.setVisible(false);
        }
        game_service myGame = Game_Server_Ex2.getServer(_level);
        myGame.login(_id);
        String gameGraphJson = myGame.getGraph();
        GsonBuilder gsbuilder = new GsonBuilder();//create a Gson object
        gsbuilder.registerTypeAdapter(DWGraph_DS.class, new DWGraph_Algo.GraphJsonDeserializer());//Performs the conversion using GraphJsonDeserializer
        Gson gson = gsbuilder.create();
        directed_weighted_graph gameGraph = gson.fromJson(gameGraphJson, DWGraph_DS.class);//create the graph by the Json format
        System.out.println(gameGraph.toString());
        _ga = new GameAlgo();
        _ga.getGraphAlgo().init(gameGraph);//init graphAlgo for shortestPath and shortestPathDis
        init(myGame);
        myGame.startGame();
        int ind = 0;
        long dt = 100;
        while (myGame.isRunning()) {
            GameAlgo.moveAgents(myGame, gameGraph);
            try {
                if (ind % 1 == 0) {
                    _win.repaint();
                }
                Thread.sleep(dt);
                ind++;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        String res = myGame.toString();
        System.out.println(res);
        System.exit(0);

    }

    private void init(game_service game) {
        int index = 0;
        String gameGraphJson = game.getGraph();
        String fs = game.getPokemons();
        GsonBuilder gsbuilder = new GsonBuilder();//create a Gson object
        gsbuilder.registerTypeAdapter(DWGraph_DS.class, new DWGraph_Algo.GraphJsonDeserializer());//Performs the conversion using GraphJsonDeserializer
        Gson gson = gsbuilder.create();
        directed_weighted_graph gameGraph = gson.fromJson(gameGraphJson, DWGraph_DS.class);
        GameAlgo._ar = new Arena();//create new Arena for the game
        GameAlgo._ar.setGraph(gameGraph);//set the graph and the pokemons
        GameAlgo._ar.setPokemons(Arena.json2Pokemons(fs));
        _win = new GraphFrame(gameGraph);//create new GraphFrame
        _win.update(GameAlgo._ar);//update the current arena
        _gp = new GraphPanel(gameGraph);//create new panel (to avoid the flicker of the frame)
        _gp.update(GameAlgo._ar, gameGraph);
        _gp.paint(_win.getGraphics());//drawing all the current data
        _win.add(_gp);//add GraphPanel to GraphFrame
        _win.setVisible(true);//show the frame
        String info = game.toString();
        JSONObject line;
        try {
            line = new JSONObject(info);
            JSONObject ttt = line.getJSONObject("GameServer");
            int rs = ttt.getInt("agents");
            System.out.println(info);
            System.out.println(game.getPokemons());
            ArrayList<CL_Pokemon> cl_fs = Arena.json2Pokemons(game.getPokemons());
            for (int a = 0; a < cl_fs.size(); a++) {
                Arena.updateEdge(cl_fs.get(a), gameGraph);
            }
            for (int a = 0; a < rs; a++) {//add the agent to the src/dest node of the pokemon
                int agentSrcNode;
                int ind = a % cl_fs.size();
                CL_Pokemon c = cl_fs.get(ind);
                if (c.getType() == -1) {
                    agentSrcNode = c.get_edge().getSrc();
                } else {
                    agentSrcNode = c.get_edge().getDest();
                }
                game.addAgent(agentSrcNode);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
