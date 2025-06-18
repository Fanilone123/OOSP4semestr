import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class VotingSystem {
    private List<User> users;
    private List<Voting> votings;
    private User currentUser;

    public VotingSystem() {
        this.users = new ArrayList<>();
        this.votings = new ArrayList<>();
        // Добавляем администратора по умолчанию
        users.add(new User("admin", "admin123", "ADMIN", "Admin Adminovich", LocalDate.now().minusYears(30), "000-000-000 00"));
    }

    // Методы для работы с пользователями
    public boolean registerUser(String username, String password, String fullName, LocalDate birthDate, String snils) {
        if (users.stream().anyMatch(u -> u.getUsername().equals(username))) {
            return false;
        }
        users.add(new User(username, password, "USER", fullName, birthDate, snils));
        return true;
    }

    public boolean login(String username, String password) {
        Optional<User> user = users.stream()
                .filter(u -> u.getUsername().equals(username) && u.getPassword().equals(password))
                .findFirst();
        if (user.isPresent()) {
            currentUser = user.get();
            return true;
        }
        return false;
    }

    public void logout() {
        currentUser = null;
    }

    // Методы для администратора
    public List<User> getAllUsers() {
        return new ArrayList<>(users);
    }

    public boolean deleteUser(String username) {
        return users.removeIf(u -> u.getUsername().equals(username) && !u.getRole().equals("ADMIN"));
    }

    public boolean createCikUser(String username, String password) {
        if (users.stream().anyMatch(u -> u.getUsername().equals(username))) {
            return false;
        }
        users.add(new User(username, password, "CIK", "CIK User", LocalDate.now().minusYears(30), "111-111-111 11"));
        return true;
    }

    public List<User> getCikUsers() {
        return users.stream().filter(u -> u.getRole().equals("CIK")).collect(Collectors.toList());
    }

    public boolean deleteCikUser(String username) {
        return users.removeIf(u -> u.getUsername().equals(username) && u.getRole().equals("CIK"));
    }

    public List<User> getCandidates() {
        return users.stream().filter(u -> u.getCandidateInfo() != null).collect(Collectors.toList());
    }

    public boolean deleteCandidate(String username) {
        Optional<User> user = users.stream().filter(u -> u.getUsername().equals(username)).findFirst();
        if (user.isPresent() && user.get().getCandidateInfo() != null) {
            user.get().setCandidateInfo(null);
            return true;
        }
        return false;
    }

    // Методы для ЦИК
    public boolean createVoting(String title, String description, LocalDateTime endDate, List<User> candidates) {
        String id = "vote_" + System.currentTimeMillis();
        Voting voting = new Voting(id, title, description, LocalDateTime.now(), endDate, candidates);
        votings.add(voting);
        
        // Добавляем голосование кандидатам
        candidates.forEach(c -> c.addParticipatedVoting(voting));
        return true;
    }

    public boolean addCandidate(String username, String password, String party, String program, String bio, String promises) {
        Optional<User> user = users.stream().filter(u -> u.getUsername().equals(username)).findFirst();
        if (user.isPresent()) {
            user.get().setCandidateInfo(new CandidateInfo(party, program, bio, promises));
            return true;
        } else {
            // Создаем нового пользователя как кандидата
            User newUser = new User(username, password, "CANDIDATE", "Candidate Name", LocalDate.now().minusYears(25), "222-222-222 22");
            newUser.setCandidateInfo(new CandidateInfo(party, program, bio, promises));
            users.add(newUser);
            return true;
        }
    }

    public void printResultsToPdf(Voting voting, String filePath) {
        // Здесь должна быть реализация генерации PDF
        // Для примера просто сохраним текстовый файл
        try (PrintWriter writer = new PrintWriter(filePath)) {
            writer.println("Результаты голосования: " + voting.getTitle());
            writer.println("Дата окончания: " + voting.getEndDate());
            writer.println("Кандидаты и голоса:");
            
            voting.getVotes().forEach((candidate, votes) -> {
                Optional<User> user = users.stream().filter(u -> u.getUsername().equals(candidate)).findFirst();
                user.ifPresent(u -> writer.println(u.getFullName() + ": " + votes + " голосов"));
            });
            
            System.out.println("Результаты сохранены в файл: " + filePath);
        } catch (FileNotFoundException e) {
            System.out.println("Ошибка при сохранении файла: " + e.getMessage());
        }
    }

    // Методы для кандидата
    public boolean updateCandidateInfo(String party, String program, String bio, String promises) {
        if (currentUser != null && currentUser.getCandidateInfo() != null) {
            currentUser.getCandidateInfo().setParty(party);
            currentUser.getCandidateInfo().setProgram(program);
            currentUser.getCandidateInfo().setBiography(bio);
            currentUser.getCandidateInfo().setPromises(promises);
            return true;
        }
        return false;
    }

    public List<Voting> getCandidateVotings() {
        if (currentUser != null) {
            return currentUser.getParticipatedVotings();
        }
        return new ArrayList<>();
    }

    // Методы для пользователя
    public List<Voting> getActiveVotings() {
        return votings.stream().filter(Voting::isActive).collect(Collectors.toList());
    }

    public List<Voting> getAllVotings() {
        return new ArrayList<>(votings);
    }

    public boolean vote(String votingId, String candidateUsername) {
        Optional<Voting> votingOpt = votings.stream().filter(v -> v.getId().equals(votingId)).findFirst();
        if (votingOpt.isPresent() && votingOpt.get().isActive()) {
            Voting voting = votingOpt.get();
            voting.vote(candidateUsername);
            currentUser.addVotedVoting(voting);
            return true;
        }
        return false;
    }

    public List<User> getVotingCandidates(String votingId) {
        Optional<Voting> voting = votings.stream().filter(v -> v.getId().equals(votingId)).findFirst();
        return voting.map(Voting::getCandidates).orElse(new ArrayList<>());
    }

    // Методы для выгрузки результатов
    public void exportResults(List<Voting> votingsToExport, boolean singleFile, String directoryPath, String fileName) {
        if (votingsToExport.isEmpty()) {
            System.out.println("Нет голосований для экспорта");
            return;
        }

        if (singleFile) {
            String filePath = directoryPath + File.separator + 
                            (fileName != null ? fileName : "results_" + System.currentTimeMillis() + ".txt");
            exportToSingleFile(votingsToExport, filePath);
        } else {
            for (Voting voting : votingsToExport) {
                String filePath = directoryPath + File.separator + 
                                (fileName != null ? fileName + "_" + voting.getId() : 
                                 "results_" + voting.getId() + "_" + System.currentTimeMillis() + ".txt");
                exportSingleVoting(voting, filePath);
            }
        }
    }

    private void exportToSingleFile(List<Voting> votings, String filePath) {
        try (PrintWriter writer = new PrintWriter(filePath)) {
            for (Voting voting : votings) {
                writer.println("Голосование: " + voting.getTitle());
                writer.println("ID: " + voting.getId());
                writer.println("Дата окончания: " + voting.getEndDate());
                writer.println("Результаты:");
                
                voting.getVotes().forEach((candidate, votes) -> {
                    Optional<User> user = users.stream().filter(u -> u.getUsername().equals(candidate)).findFirst();
                    user.ifPresent(u -> writer.println(u.getFullName() + ": " + votes + " голосов"));
                });
                
                writer.println("----------------------------------------");
            }
            System.out.println("Результаты сохранены в файл: " + filePath);
        } catch (FileNotFoundException e) {
            System.out.println("Ошибка при сохранении файла: " + e.getMessage());
        }
    }

    private void exportSingleVoting(Voting voting, String filePath) {
        try (PrintWriter writer = new PrintWriter(filePath)) {
            writer.println("Голосование: " + voting.getTitle());
            writer.println("ID: " + voting.getId());
            writer.println("Дата окончания: " + voting.getEndDate());
            writer.println("Результаты:");
            
            voting.getVotes().forEach((candidate, votes) -> {
                Optional<User> user = users.stream().filter(u -> u.getUsername().equals(candidate)).findFirst();
                user.ifPresent(u -> writer.println(u.getFullName() + ": " + votes + " голосов"));
            });
            
            System.out.println("Результаты сохранены в файл: " + filePath);
        } catch (FileNotFoundException e) {
            System.out.println("Ошибка при сохранении файла: " + e.getMessage());
        }
    }

    // Геттеры
    public User getCurrentUser() { return currentUser; }
    public List<Voting> getVotings() { return votings; }
}