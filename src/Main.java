import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Main {

    public static Graph generateGraph(DATASET dataset) {
        Scanner scanner = null;
        try {
            scanner = new Scanner(new File(dataset.toString()));
        } catch (FileNotFoundException e) {
            System.out.println("Can not read file");
            System.exit(-1);
        }

        final String path = scanner.nextLine();
        final int courseCount = scanner.nextInt();
        final int studentCount = scanner.nextInt();

        try {
            scanner = new Scanner(new File(path));
        } catch (FileNotFoundException e) {
            System.out.println("Can not read file");
            System.exit(-1);
        }

        Graph graph = new Graph(courseCount, studentCount, dataset);

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
        return graph;
    }

    public static void main(String[] args) throws Exception {
        final boolean useDSatur = true;

        for (DATASET dataset: DATASET.values()) {

            /*
            Scanner scanner;
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
            */

            Graph graph = generateGraph(dataset);

            //if (useDSatur) graph.applyDSatur();
            //graph.applyMaxDegree();
            //graph.applyDSatur();
            graph.applyRandom();

            LocalSearch localSearch = new LocalSearch();
            Graph localSearchResult = localSearch.applyLocalSearch(graph);

            graph.print();
            System.out.println("Using Kempe chain in Local Search");
            localSearchResult.print();
            System.out.println("------------------------");
        }
    }
}
