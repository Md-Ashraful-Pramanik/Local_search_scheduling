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
        for (int i=0; i<courses.size()-1; i++){
            switch (courses.get(i).getScheduling() - courses.get(i+1).getScheduling()){
                case 0:
                    //System.out.println(courses.get(i));
                    //System.out.println(courses.get(i+1));
                    //System.out.println(courses.get(i).getNb());
                    //System.out.println(courses.get(i+1).getNb());
                    System.out.println("Wrong sceduling");
                    break;
                case 1: penalty += 16; break;
                case 2: penalty += 8; break;
                case 3: penalty += 4; break;
                case 4: penalty += 2; break;
                case 5: penalty += 1; break;
            }
        }

        return penalty;
    }

    public Student deepCopy(Vector<Vertex> vertices) {
        Student s = new Student(this.id);

        for (Vertex course: this.courses) {
            s.courses.add(vertices.get(course.getNb()));
        }

        return s;
    }
}