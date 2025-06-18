import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class User {
    private String username;
    private String password;
    private String role;
    private String fullName;
    private LocalDate birthDate;
    private String snils;
    private List<Voting> participatedVotings;
    private List<Voting> votedVotings;
    private CandidateInfo candidateInfo;

    public User(String username, String password, String role, String fullName, LocalDate birthDate, String snils) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.fullName = fullName;
        this.birthDate = birthDate;
        this.snils = snils;
        this.participatedVotings = new ArrayList<>();
        this.votedVotings = new ArrayList<>();
    }

    // Геттеры и сеттеры
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getRole() { return role; }
    public String getFullName() { return fullName; }
    public LocalDate getBirthDate() { return birthDate; }
    public String getSnils() { return snils; }
    public List<Voting> getParticipatedVotings() { return participatedVotings; }
    public List<Voting> getVotedVotings() { return votedVotings; }
    public CandidateInfo getCandidateInfo() { return candidateInfo; }
    public void setCandidateInfo(CandidateInfo candidateInfo) { this.candidateInfo = candidateInfo; }

    public void addParticipatedVoting(Voting voting) {
        this.participatedVotings.add(voting);
    }

    public void addVotedVoting(Voting voting) {
        this.votedVotings.add(voting);
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", role='" + role + '\'' +
                ", fullName='" + fullName + '\'' +
                ", birthDate=" + birthDate +
                ", snils='" + snils + '\'' +
                '}';
    }
}