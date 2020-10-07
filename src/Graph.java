import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Random;
import java.util.Vector;

public class Graph{
    private int vertexCount;
    private Vector<Vertex> vertices;
    private Vector<Student> students;
    private int takenColorCount;
    private int studentCount;
    private DATASET dataset;

    public Graph(int vertexCount, int studentCount, DATASET dataset) {
        this.vertexCount = vertexCount;
        vertices = new Vector<>(vertexCount);
        for (int i=0; i<vertexCount; i++)
            vertices.add(new Vertex(vertexCount, i));

        this.studentCount = studentCount;
        students = new Vector<>(studentCount);

        takenColorCount = 0;

        this.dataset = dataset;
    }

    public void addStudent(int[] courses) {
        Vector<Vertex> v = new Vector<>(courses.length);

        for (int course : courses)
            v.add(vertices.get(course));

        students.add(new Student(students.size(), v));
    }

    public void addEdge(int u, int v) {
        vertices.get(u).addEdge(vertices.get(v));
        vertices.get(v).addEdge(vertices.get(u));
    }

    public double getPenalty() {
        int penalty = 0;

        for (int i=0; i<studentCount; i++)
            penalty += students.get(i).getPenalty();

        return (double) penalty/studentCount;
    }

    public void applyRandom() {
        Vertex v;
        Random random = new Random();

        HashSet<Integer> hashSet = new HashSet<>(vertexCount);
        while (hashSet.size() != vertexCount){
            hashSet.add(random.nextInt(vertexCount));
        }

        for (int i:hashSet){
            v = vertices.get(i);
            assignScheduling(v);
            updateNeighboursSaturation(v);
        }
    }

    public void applyMaxDegree() {
        Vertex v;

        for (int i=0; i<vertexCount; i++){
            v = getMaxDegreeVertex();
            assignScheduling(v);
            updateNeighboursSaturation(v);
        }
    }

    public Vertex getMaxDegreeVertex() {
        Vertex max = null;
        for (Vertex v: vertices) {
            if(v.isNotScheduled() &&
                    (max == null || max.getDegree() < v.getDegree()))
                    max = v;
        }
        return max;
    }

    public void applyDSatur() {
        Vertex v;
//        PrintWriter pr = null;
//
//        try {
//            pr = new PrintWriter(new File("csv/vertex Ordering "+dataset+".csv"));
//            pr.println("Vertex index, Degree, Saturation Degree, Color");
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }

        for (int i=0; i<vertexCount; i++) {
            v = getMaxSaturatedDegreeVertex();
            assignScheduling(v);
            updateNeighboursSaturation(v);
//            pr.println(v.getNb() + ","+
//                    v.getNeighbours().size()+ "," +
//                    v.getSaturatedColors().size() + "," +
//                    v.getScheduling() + ",");
        }
//
//        pr.close();
    }

    public Vertex getMaxSaturatedDegreeVertex() {
        Vertex max = null;
        for (Vertex v: vertices) {
            if (v.isNotScheduled()) {
                if(max == null)
                    max = v;
                else if(max.getSaturatedDegree() < v.getSaturatedDegree())
                    max = v;
                else if(max.getSaturatedDegree() == v.getSaturatedDegree() &&
                        max.getDegree() < v.getDegree())
                    max = v;
            }
        }
        return max;
    }

    public void assignScheduling(Vertex v) {
        if (v.getSaturatedDegree() == takenColorCount) {
            v.setScheduling(takenColorCount);
            takenColorCount++;
            return;
        }

        boolean[] flags = new boolean[takenColorCount];
        for (int i: v.getSaturatedColors()) {
            flags[i] = true;
        }

        for (int i=0; i<flags.length; i++) {
            if (!flags[i]) {
                v.setScheduling(i);
                return;
            }
        }
    }

    public void updateNeighboursSaturation(Vertex v) {
        if(v.isNotScheduled())
            return;

        for (Vertex neighbour: v.getNeighbours()) {
            neighbour.addSaturatedColor(v.getScheduling());
        }
    }

    public void print() {
        System.out.println();
        System.out.println("Dataset: "+dataset.toString());
        System.out.println("Timeslots: " + takenColorCount);
        System.out.printf("Penalty: %.2f\n" ,getPenalty());
        System.out.println();
        //for (int i=0; i<vertexCount; i++)
        //    System.out.println(vertices.get(i).getScheduling());

        System.out.println();
    }

    public Graph deepCopy() {
//        Graph graph = Main.generateGraph(dataset);
//
//        for (int i = 0; i < graph.getVertexCount(); i++) {
//            graph.vertices.get(i).setScheduling(this.vertices.get(i).getScheduling());
//        }
//
//        return graph;

        Graph graph = new Graph(vertexCount, studentCount, dataset);

        for (int i = 0; i < vertexCount; i++) {
            this.vertices.get(i).deepCopy(graph.vertices.get(i), graph.vertices);
        }

        for (int i = 0; i < studentCount; i++) {
            graph.students.add(this.students.get(i).deepCopy(graph.vertices));
        }

        graph.takenColorCount = this.takenColorCount;

        return graph;
    }

    public int getVertexCount() {
        return vertexCount;
    }

    public void setVertexCount(int vertexCount) {
        this.vertexCount = vertexCount;
    }

    public int getTakenColorCount() {
        return takenColorCount;
    }

    public void setTakenColorCount(int takenColorCount) {
        this.takenColorCount = takenColorCount;
    }

    public int getStudentCount() {
        return studentCount;
    }

    public void setStudentCount(int studentCount) {
        this.studentCount = studentCount;
    }

    public Vertex getVertex(int index){
        return vertices.get(index);
    }

    public void printSchedule(){
        try {
            PrintWriter printWriter = new PrintWriter(new File("Schedule"));

            for (Vertex v: vertices) {
                printWriter.printf("%s -> %5d -> %5d\n", v, v.getNb(), v.getScheduling());
            }

            printWriter.flush();
            printWriter.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
