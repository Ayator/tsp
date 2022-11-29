import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MinimumSpanningTree {
    private int currentEdgeNumber;
    private Vertex[] vertices;
    private Edge[] edges;
    private Map<Vertex, Integer> vertexDegree;

    private MinimumSpanningTree(Vertex[] vertices){
        this.currentEdgeNumber = 0;
        this.vertices = vertices;
        this.edges = new Edge[vertices.length - 1];
        this.vertexDegree = new HashMap<>();
        for (Vertex vertex : vertices) {
            vertexDegree.put(vertex, 0);
        }
    }

    public static MinimumSpanningTree KruskalAlgorithm(CompleteGraph graph){
        MinimumSpanningTree mst = new MinimumSpanningTree(graph.getVertices());
        DisjointSet<Vertex> disjointSet = new DisjointSet<>();
        disjointSet.MakeSets(mst.vertices);
        graph.sortEdges();
        Edge[] graphEdges = graph.getEdges();
        for (Edge edge : graphEdges) {
            Vertex u = edge.getU();
            Vertex v = edge.getV();
            if(disjointSet.FindSet(u) != disjointSet.FindSet(v)){
                mst.addEdgeToMST(edge);
                disjointSet.UnionSet(u, v);
                if(mst.currentEdgeNumber == mst.vertices.length - 1)
                    break;
            }
        }
        return mst;
    }

    public Vertex[] getVertices() {
        return vertices;
    }

    public Edge[] getEdges() {
        return edges;
    }

    // add edge to array and increase degrees of vertices
    private void addEdgeToMST(Edge edge){
        this.edges[this.currentEdgeNumber++] = edge;
        Vertex u = edge.getU();
        Vertex v = edge.getV();
        this.vertexDegree.put(u, this.vertexDegree.get(u) + 1);
        this.vertexDegree.put(v, this.vertexDegree.get(v) + 1);
    }

    public Vertex[] getOddDegVertices(){
        ArrayList<Vertex> oddDegree = new ArrayList<>();
        for(Vertex v : vertices){
            if(vertexDegree.get(v) % 2 != 0){
                oddDegree.add(v);
            }
        }
        Vertex[] oddD = new Vertex[oddDegree.size()];
        oddDegree.toArray(oddD);
        return oddD;
    }

    public void printEdges(){
        System.out.println("MST Edges:");
        for(int i = 0; i < currentEdgeNumber; i++){
            Edge e = edges[i];
            System.out.println(e.toString());
        }
    }

    // Algorithm minimizing usage of edges
    // decided it wasn't necessary since enough low results were found
    // public static MinimumSpanningTree KruskalAlgorithm2(Vertex[] vertices){
    //     MinimumSpanningTree mst = new MinimumSpanningTree(vertices);
    //     DisjointSet<Vertex> disjointSet = new DisjointSet<>();
    //     disjointSet.MakeSets(vertices);
    //     float minWeight = 0f;
    //     while(mst.currentEdgeNumber < vertices.length - 1){
    //         ArrayList<Edge> minEdges = getMinimumEdges(vertices, minWeight);
    //         minWeight = minEdges.get(0).getWeight();
    //         for (Edge edge : minEdges) {
    //             Vertex u = edge.getU();
    //             Vertex v = edge.getV();
    //             if(disjointSet.FindSet(u) != disjointSet.FindSet(v)){
    //                 mst.addEdgeToMST(edge);
    //                 disjointSet.UnionSet(u, v);
    //             }
    //         }
    //     }
    //     return mst;
    // }

    // private static ArrayList<Edge> getMinimumEdges(Vertex[] vertices, float minWeight){
    //     ArrayList<Edge> minEdges = new ArrayList<>();
    //     float maxWeight = Float.POSITIVE_INFINITY;
    //     for (Vertex u : vertices) {
    //         for (Vertex v : vertices) {
    //             if(u == v) continue;
    //             float currentWeight = Edge.computeWeight(u, v);
    //             if(minWeight < currentWeight){
    //                 if(currentWeight < maxWeight){
    //                     minEdges.clear();
    //                     maxWeight = currentWeight;
    //                     minEdges.add(new Edge(u, v));
    //                 }
    //                 else if(currentWeight == maxWeight){
    //                     minEdges.add(new Edge(u, v));
    //                 }
    //             }
    //         }
    //     }
    //     return minEdges;
    // }
}
