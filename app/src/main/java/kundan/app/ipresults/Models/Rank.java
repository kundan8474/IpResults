package kundan.app.ipresults.Models;

/**
 * Created by kkr on 22-07-2017.
 */

public class Rank {

    private  String Name;
    private String EnrollmentNumber;
    private String Score;


    public Rank(String name, String enrollmentNumber, String score) {
        Name = name;
        EnrollmentNumber = enrollmentNumber;
        Score = score;
    }


    public String getName() {
        return Name;
    }

    public String getEnrollmentNumber() {
        return EnrollmentNumber;
    }

    public String getScore() {
        return Score;
    }
}
