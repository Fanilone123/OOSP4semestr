import java.io.Console;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class Main {
    private static VotingSystem votingSystem = new VotingSystem();
    private static Scanner scanner = new Scanner(System.in);
    private static DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    private static DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

    public static void main(String[] args) {
        boolean running = true;
        while (running) {
            if (votingSystem.getCurrentUser() == null) {
                showLoginMenu();
            } else {
                switch (votingSystem.getCurrentUser().getRole()) {
                    case "ADMIN":
                        showAdminMenu();
                        break;
                    case "CIK":
                        showCikMenu();
                        break;
                    case "CANDIDATE":
                        showCandidateMenu();
                        break;
                    case "USER":
                        default:
                        showUserMenu();
                        break;
                }
            }
        }
    }

    private static void showLoginMenu() {
        System.out.println("\n=== Система электронного голосования ===");
        System.out.println("1. Вход");
        System.out.println("2. Регистрация");
        System.out.println("0. Выход");
        System.out.print("Выберите действие: ");

        int choice = scanner.nextInt();
        scanner.nextLine(); // consume newline

        switch (choice) {
            case 1:
                login();
                break;
            case 2:
                register();
                break;
            case 0:
                System.exit(0);
                break;
            default:
                System.out.println("Неверный выбор");
        }
    }

    private static void login() {
        System.out.print("Введите логин: ");
        String username = scanner.nextLine();
        System.out.print("Введите пароль: ");
        String password = scanner.nextLine();

        if (votingSystem.login(username, password)) {
            System.out.println("Вход выполнен успешно");
        } else {
            System.out.println("Неверный логин или пароль");
        }
    }

    private static void register() {
        System.out.print("Введите ФИО: ");
        String fullName = scanner.nextLine();
        System.out.print("Введите дату рождения (дд.мм.гггг): ");
        LocalDate birthDate = LocalDate.parse(scanner.nextLine(), dateFormatter);
        System.out.print("Введите СНИЛС: ");
        String snils = scanner.nextLine();
        System.out.print("Введите логин: ");
        String username = scanner.nextLine();
        System.out.print("Введите пароль: ");
        String password = scanner.nextLine();

        if (votingSystem.registerUser(username, password, fullName, birthDate, snils)) {
            System.out.println("Регистрация прошла успешно");
        } else {
            System.out.println("Пользователь с таким логином уже существует");
        }
    }

    private static void showAdminMenu() {
        System.out.println("\n=== Меню администратора ===");
        System.out.println("1. Просмотр списка пользователей");
        System.out.println("2. Удаление пользователя");
        System.out.println("3. Просмотр списка ЦИК");
        System.out.println("4. Удаление ЦИК");
        System.out.println("5. Создание ЦИК");
        System.out.println("6. Просмотр списка кандидатов");
        System.out.println("7. Удаление кандидата");
        System.out.println("0. Выход");
        System.out.print("Выберите действие: ");

        int choice = scanner.nextInt();
        scanner.nextLine(); // consume newline

        switch (choice) {
            case 1:
                viewUsers();
                break;
            case 2:
                deleteUser();
                break;
            case 3:
                viewCikUsers();
                break;
            case 4:
                deleteCikUser();
                break;
            case 5:
                createCikUser();
                break;
            case 6:
                viewCandidates();
                break;
            case 7:
                deleteCandidate();
                break;
            case 0:
                votingSystem.logout();
                break;
            default:
                System.out.println("Неверный выбор");
        }
    }

    private static void viewUsers() {
        System.out.println("\nСписок пользователей:");
        votingSystem.getAllUsers().forEach(System.out::println);
    }

    private static void deleteUser() {
        System.out.print("Введите логин пользователя для удаления: ");
        String username = scanner.nextLine();
        if (votingSystem.deleteUser(username)) {
            System.out.println("Пользователь удален");
        } else {
            System.out.println("Не удалось удалить пользователя (возможно, не существует или это администратор)");
        }
    }

    private static void viewCikUsers() {
        System.out.println("\nСписок ЦИК:");
        votingSystem.getCikUsers().forEach(System.out::println);
    }

    private static void deleteCikUser() {
        System.out.print("Введите логин ЦИК для удаления: ");
        String username = scanner.nextLine();
        if (votingSystem.deleteCikUser(username)) {
            System.out.println("ЦИК удален");
        } else {
            System.out.println("Не удалось удалить ЦИК (возможно, не существует)");
        }
    }

    private static void createCikUser() {
        System.out.print("Введите логин для нового ЦИК: ");
        String username = scanner.nextLine();
        System.out.print("Введите пароль для нового ЦИК: ");
        String password = scanner.nextLine();
        if (votingSystem.createCikUser(username, password)) {
            System.out.println("ЦИК создан");
        } else {
            System.out.println("Не удалось создать ЦИК (возможно, логин уже занят)");
        }
    }

    private static void viewCandidates() {
        System.out.println("\nСписок кандидатов:");
        votingSystem.getCandidates().forEach(c -> {
            System.out.println(c);
            System.out.println(c.getCandidateInfo());
        });
    }

    private static void deleteCandidate() {
        System.out.print("Введите логин кандидата для удаления: ");
        String username = scanner.nextLine();
        if (votingSystem.deleteCandidate(username)) {
            System.out.println("Кандидат удален");
        } else {
            System.out.println("Не удалось удалить кандидата (возможно, не существует)");
        }
    }

    private static void showCikMenu() {
        System.out.println("\n=== Меню ЦИК ===");
        System.out.println("1. Создать голосование");
        System.out.println("2. Добавить кандидата");
        System.out.println("3. Печать результатов (PDF)");
        System.out.println("4. Экспорт результатов");
        System.out.println("0. Выход");
        System.out.print("Выберите действие: ");

        int choice = scanner.nextInt();
        scanner.nextLine(); // consume newline

        switch (choice) {
            case 1:
                createVoting();
                break;
            case 2:
                addCandidate();
                break;
            case 3:
                printResultsToPdf();
                break;
            case 4:
                exportResults();
                break;
            case 0:
                votingSystem.logout();
                break;
            default:
                System.out.println("Неверный выбор");
        }
    }

    private static void createVoting() {
        System.out.print("Введите название голосования: ");
        String title = scanner.nextLine();
        System.out.print("Введите описание: ");
        String description = scanner.nextLine();
        System.out.print("Введите дату окончания (дд.мм.гггг чч:мм): ");
        LocalDateTime endDate = LocalDateTime.parse(scanner.nextLine(), dateTimeFormatter);

        // Выбор кандидатов (упрощенная версия)
        System.out.println("Доступные кандидаты:");
        List<User> candidates = votingSystem.getCandidates();
        for (int i = 0; i < candidates.size(); i++) {
            System.out.println((i+1) + ". " + candidates.get(i).getFullName());
        }
        System.out.print("Введите номера кандидатов через запятую: ");
        String[] indices = scanner.nextLine().split(",");
        List<User> selectedCandidates = new ArrayList<>();
        for (String index : indices) {
            try {
                int i = Integer.parseInt(index.trim()) - 1;
                if (i >= 0 && i < candidates.size()) {
                    selectedCandidates.add(candidates.get(i));
                }
            } catch (NumberFormatException e) {
                // ignore
            }
        }

        if (votingSystem.createVoting(title, description, endDate, selectedCandidates)) {
            System.out.println("Голосование создано");
        } else {
            System.out.println("Не удалось создать голосование");
        }
    }

    private static void addCandidate() {
        System.out.print("Введите логин кандидата: ");
        String username = scanner.nextLine();
        System.out.print("Введите пароль кандидата: ");
        String password = scanner.nextLine();
        System.out.print("Введите партию: ");
        String party = scanner.nextLine();
        System.out.print("Введите программу: ");
        String program = scanner.nextLine();
        System.out.print("Введите биографию: ");
        String bio = scanner.nextLine();
        System.out.print("Введите обещания: ");
        String promises = scanner.nextLine();

        if (votingSystem.addCandidate(username, password, party, program, bio, promises)) {
            System.out.println("Кандидат добавлен");
        } else {
            System.out.println("Не удалось добавить кандидата");
        }
    }

    private static void printResultsToPdf() {
        System.out.println("Доступные голосования:");
        List<Voting> votings = votingSystem.getVotings();
        for (int i = 0; i < votings.size(); i++) {
            System.out.println((i+1) + ". " + votings.get(i).getTitle() + 
                             " (активно: " + votings.get(i).isActive() + ")");
        }
        System.out.print("Выберите голосование: ");
        int index = scanner.nextInt() - 1;
        scanner.nextLine(); // consume newline

        if (index >= 0 && index < votings.size()) {
            System.out.print("Введите путь для сохранения PDF: ");
            String filePath = scanner.nextLine();
            votingSystem.printResultsToPdf(votings.get(index), filePath);
        } else {
            System.out.println("Неверный выбор");
        }
    }

    private static void exportResults() {
        System.out.println("Доступные голосования:");
        List<Voting> votings = votingSystem.getVotings();
        for (int i = 0; i < votings.size(); i++) {
            System.out.println((i+1) + ". " + votings.get(i).getTitle() + 
                             " (активно: " + votings.get(i).isActive() + ")");
        }
        System.out.print("Выберите голосования через запятую (0 для всех): ");
        String input = scanner.nextLine();

        List<Voting> selectedVotings = new ArrayList<>();
        if (input.trim().equals("0")) {
            selectedVotings.addAll(votings);
        } else {
            String[] indices = input.split(",");
            for (String index : indices) {
                try {
                    int i = Integer.parseInt(index.trim()) - 1;
                    if (i >= 0 && i < votings.size()) {
                        selectedVotings.add(votings.get(i));
                    }
                } catch (NumberFormatException e) {
                    // ignore
                }
            }
        }

        if (selectedVotings.isEmpty()) {
            System.out.println("Не выбрано ни одного голосования");
            return;
        }

        System.out.print("Экспортировать в один файл? (y/n): ");
        boolean singleFile = scanner.nextLine().equalsIgnoreCase("y");
        System.out.print("Введите путь для сохранения: ");
        String directoryPath = scanner.nextLine();
        System.out.print("Введите имя файла (оставьте пустым для автоматического): ");
        String fileName = scanner.nextLine();
        if (fileName.trim().isEmpty()) {
            fileName = null;
        }

        votingSystem.exportResults(selectedVotings, singleFile, directoryPath, fileName);
    }

    private static void showCandidateMenu() {
        System.out.println("\n=== Меню кандидата ===");
        System.out.println("1. Заполнить/изменить данные о себе");
        System.out.println("2. Просмотреть результаты предыдущего голосования");
        System.out.println("3. Просмотреть все голосования, в которых участвовал");
        System.out.println("0. Выход");
        System.out.print("Выберите действие: ");

        int choice = scanner.nextInt();
        scanner.nextLine(); // consume newline

        switch (choice) {
            case 1:
                updateCandidateInfo();
                break;
            case 2:
                viewPreviousResults();
                break;
            case 3:
                viewParticipatedVotings();
                break;
            case 0:
                votingSystem.logout();
                break;
            default:
                System.out.println("Неверный выбор");
        }
    }

    private static void updateCandidateInfo() {
        CandidateInfo currentInfo = votingSystem.getCurrentUser().getCandidateInfo();
        System.out.print("Введите партию [" + (currentInfo != null ? currentInfo.getParty() : "") + "]: ");
        String party = scanner.nextLine();
        System.out.print("Введите программу [" + (currentInfo != null ? currentInfo.getProgram() : "") + "]: ");
        String program = scanner.nextLine();
        System.out.print("Введите биографию [" + (currentInfo != null ? currentInfo.getBiography() : "") + "]: ");
        String bio = scanner.nextLine();
        System.out.print("Введите обещания [" + (currentInfo != null ? currentInfo.getPromises() : "") + "]: ");
        String promises = scanner.nextLine();

        if (votingSystem.updateCandidateInfo(
                party.isEmpty() && currentInfo != null ? currentInfo.getParty() : party,
                program.isEmpty() && currentInfo != null ? currentInfo.getProgram() : program,
                bio.isEmpty() && currentInfo != null ? currentInfo.getBiography() : bio,
                promises.isEmpty() && currentInfo != null ? currentInfo.getPromises() : promises)) {
            System.out.println("Данные обновлены");
        } else {
            System.out.println("Не удалось обновить данные");
        }
    }

    private static void viewPreviousResults() {
        List<Voting> votings = votingSystem.getCandidateVotings();
        if (votings.isEmpty()) {
            System.out.println("Вы не участвовали ни в одном голосовании");
            return;
        }

        System.out.println("Выберите голосование для просмотра результатов:");
        for (int i = 0; i < votings.size(); i++) {
            System.out.println((i+1) + ". " + votings.get(i).getTitle());
        }
        System.out.print("Ваш выбор: ");
        int index = scanner.nextInt() - 1;
        scanner.nextLine(); // consume newline

        if (index >= 0 && index < votings.size()) {
            Voting voting = votings.get(index);
            System.out.println("\nРезультаты голосования: " + voting.getTitle());
            voting.getVotes().forEach((candidate, votes) -> {
Optional<User> user = votingSystem.getAllUsers().stream()
                        .filter(u -> u.getUsername().equals(candidate))
                        .findFirst();
                user.ifPresent(u -> System.out.println(u.getFullName() + ": " + votes + " голосов"));
            });
        } else {
            System.out.println("Неверный выбор");
        }
    }

    private static void viewParticipatedVotings() {
        List<Voting> votings = votingSystem.getCandidateVotings();
        if (votings.isEmpty()) {
            System.out.println("Вы не участвовали ни в одном голосовании");
        } else {
            System.out.println("Голосования, в которых вы участвовали:");
            votings.forEach(v -> {
                System.out.println(v.getTitle() + " (активно: " + v.isActive() + ")");
                System.out.println("Ваш результат: " + v.getVotes().get(votingSystem.getCurrentUser().getUsername()) + " голосов");
            });
        }
    }

    private static void showUserMenu() {
        System.out.println("\n=== Меню пользователя ===");
        System.out.println("1. Просмотреть активные голосования");
        System.out.println("2. Проголосовать");
        System.out.println("3. Просмотреть список кандидатов");
        System.out.println("4. Просмотреть все голосования");
        System.out.println("0. Выход");
        System.out.print("Выберите действие: ");

        int choice = scanner.nextInt();
        scanner.nextLine(); // consume newline

        switch (choice) {
            case 1:
                viewActiveVotings();
                break;
            case 2:
                vote();
                break;
            case 3:
                viewAllCandidates();
                break;
            case 4:
                viewAllVotings();
                break;
            case 0:
                votingSystem.logout();
                break;
            default:
                System.out.println("Неверный выбор");
        }
    }

    private static void viewActiveVotings() {
        System.out.println("\nАктивные голосования:");
        List<Voting> activeVotings = votingSystem.getActiveVotings();
        if (activeVotings.isEmpty()) {
            System.out.println("Нет активных голосований");
        } else {
            activeVotings.forEach(v -> System.out.println(v.getTitle() + " (ID: " + v.getId() + ")"));
        }
    }

    private static void vote() {
        System.out.println("\nАктивные голосования:");
        List<Voting> activeVotings = votingSystem.getActiveVotings();
        if (activeVotings.isEmpty()) {
            System.out.println("Нет активных голосований");
            return;
        }

        for (int i = 0; i < activeVotings.size(); i++) {
            System.out.println((i+1) + ". " + activeVotings.get(i).getTitle());
        }
        System.out.print("Выберите голосование: ");
        int votingIndex = scanner.nextInt() - 1;
        scanner.nextLine(); // consume newline

        if (votingIndex < 0 || votingIndex >= activeVotings.size()) {
            System.out.println("Неверный выбор");
            return;
        }

        Voting voting = activeVotings.get(votingIndex);
        System.out.println("\nКандидаты:");
        List<User> candidates = votingSystem.getVotingCandidates(voting.getId());
        for (int i = 0; i < candidates.size(); i++) {
            System.out.println((i+1) + ". " + candidates.get(i).getFullName());
            if (candidates.get(i).getCandidateInfo() != null) {
                System.out.println("   Партия: " + candidates.get(i).getCandidateInfo().getParty());
                System.out.println("   Программа: " + candidates.get(i).getCandidateInfo().getProgram());
            }
        }
        System.out.print("Выберите кандидата: ");
        int candidateIndex = scanner.nextInt() - 1;
        scanner.nextLine(); // consume newline

        if (candidateIndex < 0 || candidateIndex >= candidates.size()) {
            System.out.println("Неверный выбор");
            return;
        }

        if (votingSystem.vote(voting.getId(), candidates.get(candidateIndex).getUsername())) {
            System.out.println("Ваш голос учтен");
        } else {
            System.out.println("Не удалось проголосовать");
        }
    }

    private static void viewAllCandidates() {
        System.out.println("\nВсе кандидаты:");
        List<User> candidates = votingSystem.getCandidates();
        if (candidates.isEmpty()) {
            System.out.println("Нет зарегистрированных кандидатов");
        } else {
            candidates.forEach(c -> {
                System.out.println(c.getFullName());
                System.out.println("Партия: " + c.getCandidateInfo().getParty());
                System.out.println("Программа: " + c.getCandidateInfo().getProgram());
                System.out.println("------------------------");
            });
        }
    }

    private static void viewAllVotings() {
        System.out.println("\nВсе голосования:");
        List<Voting> allVotings = votingSystem.getAllVotings();
        if (allVotings.isEmpty()) {
            System.out.println("Нет созданных голосований");
        } else {
            allVotings.forEach(v -> {
                System.out.println(v.getTitle() + " (ID: " + v.getId() + ")");
                System.out.println("Активно: " + v.isActive());
                System.out.println("Вы участвовали: " + 
                        (votingSystem.getCurrentUser().getVotedVotings().contains(v) ? "Да" : "Нет"));
                System.out.println("------------------------");
            });
        }
    }
}