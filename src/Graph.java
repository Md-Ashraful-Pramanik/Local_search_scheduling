import java.io.File;
import java.util.Scanner;
import java.util.Vector;

public class Graph {
    private int vertexCount;
    private Vector<Vertex> vertices;
    private Vector<Student> students;
    private int takenColorCount;
    private int studentCount;

    public Graph(int vertexCount, int studentCount) {
        this.vertexCount = vertexCount;
        vertices = new Vector<>(vertexCount);
        for (int i=0; i<vertexCount; i++)
            vertices.add(new Vertex(vertexCount, i));

        this.studentCount = studentCount;
        students = new Vector<>(studentCount);

        takenColorCount = 0;
    }

    public void addStudent(int[] courses){
        Vector<Vertex> v = new Vector<>(courses.length);

        for (int course : courses)
            v.add(vertices.get(course));

        students.add(new Student(students.size(), v));
    }

    public void addEdge(int u, int v){
        vertices.get(u).addEdge(vertices.get(v));
        vertices.get(v).addEdge(vertices.get(u));
    }

    public double getPenalty(){
        int penalty = 0;

        for (int i=0; i<studentCount; i++)
            penalty += students.get(i).getPenalty();

        return (double) penalty/studentCount;
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

    public void print(){
        System.out.println();
        System.out.println("Timeslots: " + takenColorCount);
        System.out.printf("Penalty: %.2f\n" ,getPenalty());
        System.out.println();
        //for (int i=0; i<vertexCount; i++)
        //    System.out.println(vertices.get(i).getScheduling());

        System.out.println();
    }

    public static void main(String[] args) throws Exception{
        Scanner scanner;
        DATASET dataset = null;
        Graph graph;

        TAKE_DATASET_CHOICE: {
            scanner = new Scanner(System.in);

            String choice;

            while (dataset == null){
                System.out.println("Select a dataset.");
                System.out.println("1 for CAR91");
                System.out.println("2 for CAR92");
                System.out.println("3 for KFU93");
                System.out.println("4 for TRE92");
                System.out.println("5 for YOR83");
                System.out.print("Enter Choice: ");
                choice = scanner.nextLine();
                switch (choice){
                    case "1": dataset = DATASET.car91; break;
                    case "2": dataset = DATASET.car92; break;
                    case "3": dataset = DATASET.kfu93; break;
                    case "4": dataset = DATASET.tre92; break;
                    case "5": dataset = DATASET.yor83; break;
                    default:
                        System.out.println("Please enter Correctly.\nTry Again.\n");
                }
            }
        }

        GENERATE_GRAPH: {
            scanner = new Scanner(new File(dataset.toString()));

            final String path = scanner.nextLine();
            final int courseCount = scanner.nextInt();
            final int studentCount = scanner.nextInt();

            scanner = new Scanner(new File(path));

            graph = new Graph(courseCount, studentCount);

            String line;
            String[] coursesNo;
            int[] courses;

            while (scanner.hasNext()){
                line = scanner.nextLine();

                coursesNo = line.split(" ");
                courses = new int[coursesNo.length];

                for (int i=0; i<coursesNo.length; i++)
                    courses[i] = Integer.parseInt(coursesNo[i]) - 1;

                graph.addStudent(courses);

                for (int i=0; i<courses.length; i++){
                    for (int j=i+1; j<courses.length; j++){
                        graph.addEdge(courses[i], courses[j]);
                    }
                }
            }
        }

        graph.applyDSatur();
        graph.print();
    }
}
