import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Random;
import java.util.Map.Entry;

public class Multigraph {
    private ArrayList<Vertex> vertices;
    private LinkedHashMap<Edge, Integer> edges;
    private LinkedHashMap<Vertex, LinkedHashMap<Vertex, Edge>> edgeMap;
    private Random random = new Random();

    private Multigraph(Vertex[] vertices){
        this.vertices = new ArrayList<>(vertices.length);
        // the amount of edges may be lower
        // there are at least vertices.length - 1 edges
        this.edges = new LinkedHashMap<>(vertices.length - 1);
        this.edgeMap = new LinkedHashMap<>(vertices.length - 1);
        Collections.addAll(this.vertices, vertices);
        for(Vertex vertex : vertices){
            edgeMap.put(vertex, new LinkedHashMap<>());
        }
    }

    // assume both graphs use the same vertices
    public static Multigraph CombineBipartiteAndMST(MinimumSpanningTree mst, BipartiteGraph bi){
        Multigraph mg = new Multigraph(mst.getVertices());
        int i = 0, j = 0;
        // merge the graphs!
        while(i < mst.getEdges().length && j < bi.getEdges().length){
            if(mst.getEdges()[i].compareTo(bi.getEdges()[j]) < 0)
                mg.addEdge(mst.getEdges()[i++]);
            else
                mg.addEdge(bi.getEdges()[j++]);
        }
        while(i < mst.getEdges().length){
            mg.addEdge(mst.getEdges()[i++]);
        }
        while(j < bi.getEdges().length){
            mg.addEdge(bi.getEdges()[j++]);
        }
        return mg;
    }

    private void addEdge(Edge edge){
        // set edge mapped to number of times it was added (0 by default)
        this.edges.put(edge, this.edges.getOrDefault(edge, 0) + 1);
        // add both ways to the edge map
        this.edgeMap.get(edge.getU()).put(edge.getV(), edge);
        this.edgeMap.get(edge.getV()).put(edge.getU(), edge);
    }

    public void printEdges(){
        System.out.println("Multigraph Edges:");
        for(Edge edge : this.edges.keySet()){
            System.out.println(edge.toString() + " (" + this.edges.get(edge) + ")");
        }
    }

    public static DoublyLinkedList<Vertex> HierholzerAlgorithm(Multigraph graph){
        HashMap<Vertex, Boolean> isInPath = new HashMap<>(graph.vertices.size());
        for(Vertex vertex : graph.vertices)
            isInPath.put(vertex, false);
        // choose a random vertex to start with
        int r = graph.random.nextInt(graph.vertices.size());
        Vertex startV = graph.vertices.get(r);
        DoublyLinkedList<Vertex> eulerianPath = graph.getEulerianPath(startV, isInPath);
        // keep looping until there are no more vertices
        while(graph.vertices.size() > 0){
            // choose a random vertex so that it is in the eulerian path
            while(!isInPath.getOrDefault(startV, false)){
                r = graph.random.nextInt(graph.vertices.size());
                startV = graph.vertices.get(r);
            }
            eulerianPath.replaceNodeByList(startV, graph.getEulerianPath(startV, isInPath));
        }
        return eulerianPath;
    }

    private DoublyLinkedList<Vertex> getEulerianPath(Vertex startVertex, HashMap<Vertex, Boolean> isInPath){
        DoublyLinkedList<Vertex> eulerianPath = new DoublyLinkedList<Vertex>();
        // add start vertex to the eulerian path
        eulerianPath.push(startVertex);
        // edgeMap is sorted by edge weight in ascending order (by construction)
        // get the smallest vertex-edge pair
        Entry<Vertex, Edge> nextEntry = getNextEntry(startVertex);
        while(startVertex != nextEntry.getKey()) {
            // append the vertex to the eulerian path and set isInPath to true
            eulerianPath.append(nextEntry.getKey());
            isInPath.put(nextEntry.getKey(), true);
            // decrease the edge (or delete it) and get the next entry pair
            decreaseEdge(nextEntry.getValue(), isInPath);
            nextEntry = getNextEntry(nextEntry.getKey());
        }
        // add the returning to start vertex too
        eulerianPath.append(startVertex);
        isInPath.put(nextEntry.getKey(), true);
        decreaseEdge(nextEntry.getValue(), isInPath);
        return eulerianPath;
    }

    private Entry<Vertex, Edge> getNextEntry(Vertex vertex){
        return edgeMap.get(vertex).entrySet().iterator().next();
    }

    // decrease the given edge count by 1 or delete it if there is only 1 such edge
    private void decreaseEdge(Edge edge, HashMap<Vertex, Boolean> isInPath){
        int i = edges.get(edge);
        // check if there is only one edge remaining
        if(i == 1){
            // given Edge(U, V)
            // get adjacent vertices of U
            LinkedHashMap<Vertex, Edge> adjU = edgeMap.get(edge.getU());
            // remove V from them
            adjU.remove(edge.getV());
            // check if there are no remaining adjacent vertices
            if(adjU.size() == 0){
                // remove U from edgeMap, from the vertices list, and from isInPath
                edgeMap.remove(edge.getU());
                vertices.remove(edge.getU());
                isInPath.remove(edge.getU());
            }
            // get adjacent vertices of V
            LinkedHashMap<Vertex, Edge> adjV = edgeMap.get(edge.getV());
            // remove U from them
            adjV.remove(edge.getU());
            // check if there are no remaining adjacent vertices
            if(adjV.size() == 0){
                // remove V from edgeMap, from the vertices list, and from isInPath
                edgeMap.remove(edge.getV());
                vertices.remove(edge.getV());
                isInPath.remove(edge.getV());
            }
            // finally remove edge from the edges list (HashMap)
            edges.remove(edge);
        }
        // else there is at least more than 1 of such edge, so just decrease it
        else
            edges.put(edge, i - 1);
    }
}
