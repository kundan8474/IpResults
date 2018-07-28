package kundan.app.ipresults.Models;

import java.util.ArrayList;

/**
 * Created by kkr on 20-07-2017.
 */

public class Semester {

    private ArrayList <Subject> Subjects;

    private String Sem =null;

    private String Programme =null;

    private String Batch =null;

    private String Examination =null;

    private String EnrollmentNumber =null;

    private String StudentName =null;

    private String CreditsScored =null;

    private String CollegeCode =null;

    private String Score = null;

    public Semester(ArrayList<Subject> subjects, String sem, String programme, String batch,
                    String examination, String enrollmentNumber, String studentName,
                    String creditsScored, String collegeCode, String score) {
        Subjects = subjects;
        Sem = sem;
        Programme = programme;
        Batch = batch;
        Examination = examination;
        EnrollmentNumber = enrollmentNumber;
        StudentName = studentName;
        CreditsScored = creditsScored;
        CollegeCode = collegeCode;
        Score = score;
    }

    public ArrayList<Subject> getSubjects() {
        return Subjects;
    }

    public String getSem() {
        return Sem;
    }

    public String getProgramme() {
        return Programme;
    }

    public String getBatch() {
        return Batch;
    }

    public String getExamination() {
        return Examination;
    }

    public String getEnrollmentNumber() {
        return EnrollmentNumber;
    }

    public String getStudentName() {
        return StudentName;
    }

    public String getCreditsScored() {
        return CreditsScored;
    }

    public String getCollegeCode() {
        return CollegeCode;
    }

    public String getScore() {
        return Score;
    }
}
