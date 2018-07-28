package kundan.app.ipresults.Models;

/**
 * Created by kkr on 20-07-2017.
 */

public class Subject {

    private String SubjectId = null;

    private String Credits = null;

    private String Internal = null;

    private String External = null;

    private int Total = -1;

    private String SubjectName = null;

    public Subject(String subjectId, String credits, String internal, String external, int total, String subjectName) {
        SubjectId = subjectId;
        Credits = credits;
        Internal = internal;
        External = external;
        Total = total;
        SubjectName = subjectName;
    }

    public String getSubjectId() {
        return SubjectId;
    }

    public String getCredits() {
        return Credits;
    }

    public String getInternal() {
        return Internal;
    }

    public String getExternal() {
        return External;
    }

    public int getTotal() {
        return Total;
    }

    public String getSubjectName() {
        return SubjectName;
    }
}
