import java.util.ArrayList;
import java.util.Random;

public class HamiltonianCycle {
    Vertex[] path;

    public Vertex[] getPath() {
        return path;
    }

    // used to find TSP tour from Eulerian tour
    // doesn't include the starting vertex at the end of the cycle
    private HamiltonianCycle(DoublyLinkedList<Vertex> path){
        int size = path.convertToHamiltonianCycle();
        this.path = new Vertex[size];
        int i = 0;
        for (Vertex vertex : path) {
            this.path[i++] = vertex;
        }
    }

    private HamiltonianCycle(Vertex[] path){
        this.path = path;
    }

    // assumes the given list is an eulerian tour
    public static HamiltonianCycle FromEulerianPath(DoublyLinkedList<Vertex> path){
        return new HamiltonianCycle(path);
    }

    // assumes the given array is an eulerian tour
    public static HamiltonianCycle FromVertexArray(Vertex[] path){
        return new HamiltonianCycle(path);
    }

    // this could be improved by using a better data structure such that remove is O(1)
    // but it's greedy so it's lower than Christofides anyways
    public static HamiltonianCycle GreedyAlgorithm(Vertex[] vertices){
        ArrayList<Vertex> myVertices = new ArrayList<>(vertices.length);
        for (Vertex vertex : vertices) {
            myVertices.add(vertex);
        }
        Vertex[] path = new Vertex[vertices.length];
        int currentVertex = 0;
        Edge minEdge = getMinimumWeightEdge(myVertices);
        if(minEdge == null) return null;

        path[currentVertex++] = minEdge.getU();
        myVertices.remove(minEdge.getU());
        path[currentVertex++] = minEdge.getV();
        myVertices.remove(minEdge.getV());
        Vertex u = minEdge.getV();
        while(currentVertex < vertices.length){
            Vertex v = getMinimumWeightVertex(myVertices, u);
            path[currentVertex++] = v;
            myVertices.remove(v);
            u = v;
        }
        return new HamiltonianCycle(path);
    }

    private static Vertex getMinimumWeightVertex(ArrayList<Vertex> vertices, Vertex u){
        Vertex minVertex = null;
        double minWeight = Double.POSITIVE_INFINITY;
        Random random = new Random();
        for (int i = 0; i < vertices.size(); i++) {
            double currentWeight = Edge.computeDoubleWeight(u, vertices.get(i));
            if(currentWeight < minWeight || (currentWeight == minWeight && random.nextBoolean())){
                minVertex = vertices.get(i);
                minWeight = currentWeight;
            }
        }
        return minVertex;
    }

    private static Edge getMinimumWeightEdge(ArrayList<Vertex> vertices){
        Edge minEdge = null;
        double minWeight = Double.POSITIVE_INFINITY;
        Random random = new Random();
        for (int i = 0; i < vertices.size(); i++) {
            for (int j = 0; j < vertices.size(); j++) {
                if(i == j) continue;
                Vertex u = vertices.get(i);
                Vertex v = vertices.get(j);
                double currentWeight = Edge.computeDoubleWeight(u, v);
                if(currentWeight < minWeight || (currentWeight == minWeight && random.nextBoolean())){
                    minEdge = new Edge(u, v);
                    minWeight = currentWeight;
                }
            }
        }
        return minEdge;
    }

    public String getTourString(){
        String str = path[0].getVertexId() + "";
        for (int i = 1; i < path.length; i++) {
            str += " " + path[i].getVertexId();
        }
        return str;
    }

    public float getTourCost(){
        float tourCost = 0f;
        Vertex u = this.getPath()[0];
        int n = this.getPath().length;
        for (int i = 1; i <= n; i++) {
            Vertex v = this.getPath()[i % n];
            tourCost += Math.sqrt(Edge.computeWeight(u, v));
            u = v;
        }
        return tourCost;
    }

    public static float getTourWeight(Vertex[] vertices){
        float tourWeight = 0f;
        Vertex u = vertices[0];
        int n = vertices.length;
        for (int i = 1; i <= n; i++) {
            Vertex v = vertices[i % n];
            tourWeight += Edge.computeWeight(u, v);
            u = v;
        }
        return tourWeight;
    }

    public void printList(){
        System.out.println("Hamiltonian Cycle:");
        for (int i = 0; i < path.length; i++) {
            System.out.println("-> " + path[i].toString());
        }
    }
}
