public class Vertex implements Comparable<Vertex> {
    // make the vertex coordinates final
    private final float x, y;
    private int degree, vertexId;

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }
    
    public int getDegree() {
        return degree;
    }
    
    public int getVertexId() {
        return vertexId;
    }

    public void incrementDegree() {
        this.degree++;
    }

    public Vertex(float x, float y, int vertexId){
        this.x = x;
        this.y = y;
        this.degree = 0;
        this.vertexId = vertexId;
    }

    public String toString(){
        return "<" + this.x + ", " + this.y + ">";
    }

    @Override
    public int compareTo(Vertex other) {
        return this.vertexId - other.vertexId;
    }
}
