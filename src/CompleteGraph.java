import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.Scanner;
import java.util.HashMap;

public class CompleteGraph{
    private int verticesNumber, edgesNumber, currentVertexNumber, currentEdgeNumber;
    private Vertex[] vertices;
    private Edge[] edges;
    private Map<Vertex, Map<Vertex, Edge>> edgeMap;

    private CompleteGraph(int verticesNumber){
        this.currentVertexNumber = 0;
        this.currentEdgeNumber = 0;
        this.verticesNumber = verticesNumber;
        this.vertices = new Vertex[this.verticesNumber];
        this.edgesNumber = this.verticesNumber * (this.verticesNumber - 1) / 2;
        this.edges = new Edge[this.edgesNumber];
        this.edgeMap = new HashMap<>();
    }

    private CompleteGraph(Vertex[] vertices){
        this.currentVertexNumber = 0;
        this.currentEdgeNumber = 0;
        this.verticesNumber = vertices.length;
        this.vertices = vertices;
        this.edgesNumber = this.verticesNumber * (this.verticesNumber - 1) / 2;
        this.edges = new Edge[this.edgesNumber];
        this.edgeMap = new HashMap<>();

        for(int i = 0; i < vertices.length; i++){
            // create a hashmap for it
            edgeMap.put(vertices[i], new HashMap<>());
            for(int j = 0; j < i; j++){
                // notice that the new edge is always the second parameter
                addEdge(getVertex(j), vertices[i]);
            }
        }
    }

    public static Vertex[] readVerticesFromFile(String filepath){
        try {
            File file = new File(filepath);
            Scanner sc = new Scanner(file);
            if(!sc.hasNextLine()){
                System.out.println("Empty file.");
                sc.close();
                return null;
            }
            int n = Integer.parseInt(sc.nextLine());
            
            Vertex[] vertices = new Vertex[n];

            for(int i = 0; i < n; i++){
                if(!sc.hasNextLine()){
                    System.out.println("File is missing lines.");
                    sc.close();
                    return null;
                }
                // read the vertices
                float x = sc.nextFloat();
                float y = sc.nextFloat();
                // System.out.println(x + " " + y);
                Vertex v = new Vertex(x, y, i);
                vertices[i] = v;
            }
            sc.close();
            // graph.printEdges();
            return vertices;
        } catch (FileNotFoundException e) {
            System.out.println("Error while reading the file.");
            e.printStackTrace();
        }
        return null;
    }

    public static CompleteGraph FromVertices(Vertex[] vertices){
        CompleteGraph graph = new CompleteGraph(vertices);
        // graph.printEdges();
        return graph;
    }

    public static CompleteGraph readGraphFromFile(String filepath){
        File file = new File(filepath);
        try {
            Scanner sc = new Scanner(file);
            if(!sc.hasNextLine()){
                System.out.println("Empty file.");
                sc.close();
                return null;
            }
            int n = Integer.parseInt(sc.nextLine());
            
            CompleteGraph graph = new CompleteGraph(n);

            for(int i = 0; i < n; i++){
                if(!sc.hasNextLine()){
                    System.out.println("File is missing lines.");
                    sc.close();
                    return null;
                }
                // read the vertices
                float x = sc.nextFloat();
                float y = sc.nextFloat();
                // System.out.println(x + " " + y);
                Vertex v = new Vertex(x, y, i);
                graph.addVertex(v);
            }
            sc.close();
            // graph.printEdges();
            return graph;
        } catch (FileNotFoundException e) {
            System.out.println("Error while reading the file.");
            e.printStackTrace();
        }
        return null;
    }

    public void addVertex(Vertex v){
        vertices[currentVertexNumber] = v;
        // create a hashmap for it
        edgeMap.put(v, new HashMap<>());
        for(int i = 0; i < currentVertexNumber; i++){
            // notice that the new edge is always the second parameter
            addEdge(getVertex(i), v);
        }
        currentVertexNumber++;
    }

    private void addEdge(Vertex u, Vertex v){
        edges[currentEdgeNumber] = new Edge(u, v);
        edgeMap.get(u).put(v, edges[currentEdgeNumber]);
        currentEdgeNumber++;
    }

    private Vertex getVertex(int index){
        return this.vertices[index];
    }

    private Edge getEdgeFromIndex(int index){
        return this.edges[index];
    }

    public Edge getEdgeFromVertices(Vertex u, Vertex v, boolean bidirected){
        Edge e = edgeMap.get(u).get(v);
        if(e == null && bidirected)
            return edgeMap.get(v).get(u);
        return e;
    }

    public Map<Vertex, Edge> getEdgesFromVertex(Vertex v){
        return edgeMap.get(v);
    }

    public Edge[] getEdgesFromConnectedVertices(Vertex[] vertices){
        ArrayList<Edge> newEdges = new ArrayList<>();
        for (Vertex u : vertices) {
            for (Vertex v : vertices) {
                Edge e = this.getEdgeFromVertices(u, v, false);
                if(e == null) continue;
                newEdges.add(e);
            }
        }
        Edge[] newEdgesArray = new Edge[newEdges.size()];
        return newEdges.toArray(newEdgesArray);
    }

    public Vertex[] getVertices() {
        return vertices;
    }

    public Edge[] getEdges() {
        return edges;
    }

    public void sortEdges(){
        Arrays.sort(this.edges);
    }

    public void printEdges(){
        for(int i = 0; i < currentEdgeNumber; i++){
            Edge e = getEdgeFromIndex(i);
            System.out.println(e.toString());
        }
    }
}
