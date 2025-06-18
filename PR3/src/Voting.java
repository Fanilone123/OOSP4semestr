import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Voting {
    private String id;
    private String title;
    private String description;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private List<User> candidates;
    private Map<String, Integer> votes; // key: candidate username, value: vote count
    private boolean isActive;

    public Voting(String id, String title, String description, LocalDateTime startDate, LocalDateTime endDate, List<User> candidates) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.candidates = candidates;
        this.votes = new HashMap<>();
        for (User candidate : candidates) {
            votes.put(candidate.getUsername(), 0);
        }
        this.isActive = LocalDateTime.now().isBefore(endDate);
    }

    // Геттеры и сеттеры
    public String getId() { return id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public LocalDateTime getStartDate() { return startDate; }
    public LocalDateTime getEndDate() { return endDate; }
    public List<User> getCandidates() { return candidates; }
    public Map<String, Integer> getVotes() { return votes; }
    public boolean isActive() { return isActive; }

    public void updateStatus() {
        this.isActive = LocalDateTime.now().isBefore(endDate);
    }

    public void vote(String candidateUsername) {
        if (votes.containsKey(candidateUsername)) {
            votes.put(candidateUsername, votes.get(candidateUsername) + 1);
        }
    }

    @Override
    public String toString() {
        return "Voting{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", isActive=" + isActive +
                '}';
    }
}