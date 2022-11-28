import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class BipartiteGraph {
    private int currentEdgeNumber;
    private Vertex[] vertices;
    private Edge[] edges;

    private BipartiteGraph(Vertex[] vertices){
        this.currentEdgeNumber = 0;
        this.vertices = vertices;
        // this.edges = new ArrayList<>(vertices.size() - 1);
        this.edges = new Edge[vertices.length / 2];
    }

    public static BipartiteGraph GreedyMinWeightPerfectMatching(CompleteGraph graph, Vertex[] oddDegreeVertices){
        BipartiteGraph bipartiteGraph = new BipartiteGraph(oddDegreeVertices);
        // get edges connecting all odd vertices
        Edge[] edges = graph.getEdgesFromConnectedVertices(oddDegreeVertices);
        // O(E log(E)...
        Arrays.sort(edges);
        // edges.sort(Comparator.naturalOrder());
        //
        Map<Vertex, Boolean> vertexAdded = new HashMap<>();
        for (Vertex vertex : oddDegreeVertices) {
            vertexAdded.put(vertex, false);
        }
        int c = oddDegreeVertices.length;// this is always even (when following the main algorithm)
        // for all edges, if both vertices haven't been used yet, add it
        // can be improved by finding two closest vertices with book's algorithm
        for (Edge edge : edges) {
            Vertex u = edge.getU();
            Vertex v = edge.getV();
            if(!vertexAdded.get(u) && !vertexAdded.get(v)){
                vertexAdded.put(u, true);
                vertexAdded.put(v, true);
                bipartiteGraph.addEdge(edge);
                // bipartiteGraph.edges.add(edge);
                c -= 2;
                if(c == 0) break;
            }
        }
        return bipartiteGraph;
    }

    public void addEdge(Edge edge){
        this.edges[this.currentEdgeNumber] = edge;
        this.currentEdgeNumber++;
    }

    public Vertex[] getVertices() {
        return vertices;
    }
    
    public Edge[] getEdges() {
        return edges;
    }

    public void printPerfectMatchingEdges(){
        System.out.println("Perfect Matching Edges:");
        for(Edge edge : edges){
            System.out.println(edge.toString());
        }
    }
}
