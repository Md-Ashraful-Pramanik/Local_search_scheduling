import java.io.File;
import java.util.Scanner;
import java.util.Vector;

public class Graph {
    private int vertexCount;
    private Vector<Vertex> vertices;
    private int takenColorCount;

    public Graph(int vertexCount) {
        this.vertexCount = vertexCount;
        vertices = new Vector<>(vertexCount);
        for (int i=0; i<vertexCount; i++)
            vertices.add(new Vertex(vertexCount, i));

        takenColorCount = 0;
    }

    public void addEdge(int u, int v){
        vertices.get(u).addEdge(vertices.get(v));
        vertices.get(v).addEdge(vertices.get(u));
    }

    public void applyDSatur(){
        for (int i=0; i<vertexCount; i++){
            Vertex v = getNextVertex();
            assignScheduling(v);
            updateNeighboursSaturation(v);
        }
    }

    public Vertex getNextVertex(){
        Vertex max = new Vertex(0, 0);
        for (Vertex v: vertices) {
            if (v.isNotScheduled()){
                if(max.getSaturatedDegree() < v.getSaturatedDegree())
                    max = v;
                else if(max.getDegree() < v.getDegree())
                    max = v;
            }
        }
        return max;
    }

    public void assignScheduling(Vertex v){
        if (v.getSaturatedDegree() == takenColorCount){
            v.setScheduling(takenColorCount);
            takenColorCount++;
            return;
        }

        boolean[] flags = new boolean[takenColorCount];
        for (int i: v.getSaturatedColors()){
            flags[i] = true;
        }

        for (int i=0; i<flags.length; i++){
            if (!flags[i]) {
                v.setScheduling(i);
                return;
            }
        }
    }

    public void updateNeighboursSaturation(Vertex v){
        if(v.isNotScheduled())
            return;

        for (Vertex neighbour: v.getNeighbours()) {
            neighbour.addSaturatedColor(v.getScheduling());
        }
    }

    public static void main(String[] args) throws Exception{
        String path = "Toronto/yor-f-83.stu"; // 26 (21)
        int courseCount = 181;

        //String path = "Toronto/car-s-91.stu";// 42 (35)
        //int courseCount = 682;

        //String path = "Toronto/car-f-92.stu";// 36 (32)
        //int courseCount = 543;

        //String path = "Toronto/kfu-s-93.stu";// 22 (20)
        //int courseCount = 461;

        //String path = "Toronto/tre-s-92.stu";// 26 (23)
        //int courseCount = 261;

        //String path = "test_graph";// 26 (3)
        //int courseCount = 8;

        Scanner scanner = new Scanner(new File(path));

        Graph graph = new Graph(courseCount);

        String line;
        String[] coursesNo;
        int[] courses;

        while (scanner.hasNext()){
            line = scanner.nextLine();

            coursesNo = line.split(" ");
            courses = new int[coursesNo.length];

            for (int i=0; i<coursesNo.length; i++)
                courses[i] = Integer.parseInt(coursesNo[i]) - 1;

            for (int i=0; i<courses.length; i++){
                for (int j=i+1; j<courses.length; j++){
                    graph.addEdge(courses[i], courses[j]);
                }
            }
        }


        graph.applyDSatur();

        System.out.println(graph.takenColorCount);
    }
}
