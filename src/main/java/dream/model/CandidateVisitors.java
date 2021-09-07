package dream.model;


public class CandidateVisitors {

    int id;
    private Candidate candidate;
    private int count;

    public CandidateVisitors(int id, Candidate candidate, int count) {
        this.id = id;
        this.candidate = candidate;
        this.count = count;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Candidate getCandidate() {
        return candidate;
    }

    public void setCandidate(Candidate candidate) {
        this.candidate = candidate;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
