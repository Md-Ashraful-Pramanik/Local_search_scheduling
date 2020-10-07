import java.util.Vector;

public class Student {
    private Vector<Vertex> courses;
    private int id;

    public Student(int id) {
        this.id = id;
        this.courses = new Vector<>();
    }

    public Student(int id, Vector<Vertex> courses) {
        this.id = id;
        this.courses = courses;
    }

    public void addCourse(Vertex course){
        courses.add(course);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPenalty(){
        courses.sort((o1, o2) -> o2.getScheduling() - o1.getScheduling());

        int penalty = 0;
        for (int i = 0; i<courses.size()-1; i++){
            penalty += calculatePenalty(courses.get(i).getScheduling(), courses.get(i+1).getScheduling());
        }

        return penalty;
    }

    public int getPenaltyAllPair(){
        int penalty = 0;
        for (int i = 0; i<courses.size(); i++){
            for (int j = i+1; j < courses.size(); j++) {
                penalty += calculatePenalty(courses.get(i).getScheduling(), courses.get(j).getScheduling());
            }
        }

        return penalty;
    }

    public int calculatePenalty(int i, int j) {
        switch (Math.abs(i - j)) {
            case 0:
                //System.out.println(courses.get(i));
                //System.out.println(courses.get(i+1));
                //System.out.println(courses.get(i).getNb());
                //System.out.println(courses.get(i+1).getNb());
                System.out.println("Wrong sceduling");
                break;
            case 1:
                return 16;
            case 2:
                return 8;
            case 3:
                return 4;
            case 4:
                return 2;
            case 5:
                return 1;
        }
        return 0;
    }

    public Student deepCopy(Vector<Vertex> vertices) {
        Student s = new Student(this.id);

        for (Vertex course: this.courses) {
            s.courses.add(vertices.get(course.getNb()));
        }

        return s;
    }
}