public class CandidateInfo {
    private String party;
    private String program;
    private String biography;
    private String promises;

    public CandidateInfo(String party, String program, String biography, String promises) {
        this.party = party;
        this.program = program;
        this.biography = biography;
        this.promises = promises;
    }

    // Геттеры и сеттеры
    public String getParty() { return party; }
    public String getProgram() { return program; }
    public String getBiography() { return biography; }
    public String getPromises() { return promises; }

    public void setParty(String party) { this.party = party; }
    public void setProgram(String program) { this.program = program; }
    public void setBiography(String biography) { this.biography = biography; }
    public void setPromises(String promises) { this.promises = promises; }

    @Override
    public String toString() {
        return "CandidateInfo{" +
                "party='" + party + '\'' +
                ", program='" + program + '\'' +
                ", biography='" + biography + '\'' +
                ", promises='" + promises + '\'' +
                '}';
    }
}