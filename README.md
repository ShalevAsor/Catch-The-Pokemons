# Catch the Pokemons

**Catch the Pokemons** is a game designed to demonstrate various graph implementations, including both weighted and unweighted graphs. The game uses graph algorithms to create an engaging experience where agents capture Pokémon through a series of levels.

## Graph Implementation

The project features a directed weighted graph (DWG) implementation that supports:
- **Low Complexity:** Efficiently handles large-scale graphs.
- **High Capacity:** Supports up to one million vertices and ten million edges.
- **Algorithm Support:** Includes algorithms like Dijkstra's and BFS.
- **JSON Compatibility:** Save and load graphs in JSON format.

### Graph Types

- **Directed Weighted Graph (DWG):** Each edge has a direction and weight, which impacts the shortest path calculations.
- **Directed vs. Undirected Graphs:** Directed graphs have edges with specific directions, whereas undirected graphs do not.

For more details about directed graphs, visit [here](https://en.wikipedia.org/wiki/Directed_graph).

## Algorithms

- **BFS (Breadth-First Search):** Used to determine if the graph is strongly connected.
- **Dijkstra's Algorithm:** Computes the shortest path distance between two nodes.

### Strongly Connected Graphs

A strongly connected graph has a path between each pair of nodes. For more information, see [here](https://en.wikipedia.org/wiki/Strongly_connected_component).

### Shortest Path

- **Shortest Path Distance:** Returns the minimal distance between two nodes.
- **Shortest Path:** Returns the list of nodes representing the shortest path.

## Catch the Pokemons Game

The game consists of 24 levels, each with agents and Pokémon. The agents must catch Pokémon using graph algorithms.

### Game Mechanics

- **Levels:** 24 different levels with increasing difficulty.
- **Agents:** Utilize graph traversal algorithms to capture Pokémon.
- **Pokémon Types:**
  - **Positive Pokémon:** Located where the source node is greater than the destination node.
  - **Negative Pokémon:** Located where the source node is less than the destination node.

## Operating Instructions

1. **Clone the Repository:**

    ```sh
    git clone https://github.com/ShalevAsor/Ex2.git
    ```

2. **Download ZIP:**

    [Download ZIP](#)

3. **Include Libraries:**

   Ensure to include the necessary libraries from the `libs` directory in your project.

4. **Run the Project:**

   You can run the project either from your workspace or using the `Ex2.jar` file:

    ```sh
    java -jar Ex2.jar <id> <level>
    ```


## 🌐 Connect with Me

- **Website:** [My Website](https://shalevasor.github.io/)
- **Contact:** [shalevasor@gmail.com](mailto:shalevasor@gmail.com)
- **Connect with me on LinkedIn:** [Shalev Asor](https://www.linkedin.com/in/shalev-asor)




