package main;

import services.AuthenticationService;
import services.ElectionService;
import model.User;
import model.Candidate;
import model.Voter;
import blockchain.Block;
import exceptions.VotingException;

import java.util.Scanner;

public class ConsoleApp {
    private AuthenticationService authService;
    private ElectionService electionService;
    private Scanner scanner;

    public ConsoleApp() {
        authService = new AuthenticationService();
        electionService = new ElectionService(authService);
        scanner = new Scanner(System.in);
    }

    public void start() {
        while (true) {
            System.out.println("\n===============================");
            System.out.println("CHAINVOTE SYSTEM");
            System.out.println("===============================");
            System.out.println("1. Admin Login");
            System.out.println("2. Voter Login");
            System.out.println("3. Register Voter");
            System.out.println("4. View Candidates");
            System.out.println("5. Verify Blockchain");
            System.out.println("6. Exit");
            System.out.print("Select an option: ");

            String choice = scanner.nextLine();
            switch (choice) {
                case "1":
                    handleLogin("ADMIN");
                    break;
                case "2":
                    handleLogin("VOTER");
                    break;
                case "3":
                    handleRegister();
                    break;
                case "4":
                    viewCandidates();
                    break;
                case "5":
                    verifyBlockchain();
                    break;
                case "6":
                    System.out.println("Exiting System...");
                    return;
                default:
                    System.out.println("Invalid option.");
            }
        }
    }

    private void handleLogin(String expectedRole) {
        System.out.print("Enter ID: ");
        String id = scanner.nextLine();
        System.out.print("Enter Password: ");
        String password = scanner.nextLine();

        try {
            User user = authService.login(id, password);
            if (!user.getRole().equals(expectedRole)) {
                System.out.println("Access denied. Role mismatch.");
                authService.logout();
                return;
            }
            System.out.println("Login successful.");
            if (expectedRole.equals("ADMIN")) {
                adminDashboard();
            } else {
                voterDashboard();
            }
        } catch (VotingException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void handleRegister() {
        System.out.print("Enter New Voter ID: ");
        String id = scanner.nextLine();
        System.out.print("Enter Username: ");
        String username = scanner.nextLine();
        System.out.print("Enter Password: ");
        String password = scanner.nextLine();

        try {
            authService.registerVoter(id, username, password);
            System.out.println("Voter registered successfully.");
        } catch (VotingException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void voterDashboard() {
        while (true) {
            System.out.println("\n===============================");
            System.out.println("VOTER DASHBOARD");
            System.out.println("===============================");
            System.out.println("1. View Candidates");
            System.out.println("2. Cast Vote");
            System.out.println("3. Voting Status");
            System.out.println("4. View Blockchain");
            System.out.println("5. Logout");
            System.out.print("Select an option: ");

            String choice = scanner.nextLine();
            switch (choice) {
                case "1":
                    viewCandidates();
                    break;
                case "2":
                    castVote();
                    break;
                case "3":
                    votingStatus();
                    break;
                case "4":
                    viewBlockchain();
                    break;
                case "5":
                    authService.logout();
                    System.out.println("Logged out.");
                    return;
                default:
                    System.out.println("Invalid option.");
            }
        }
    }

    private void adminDashboard() {
        while (true) {
            System.out.println("\n===============================");
            System.out.println("ADMIN DASHBOARD");
            System.out.println("===============================");
            System.out.println("1. Add Candidate");
            System.out.println("2. Remove Candidate");
            System.out.println("3. View Candidates");
            System.out.println("4. Start Election");
            System.out.println("5. End Election");
            System.out.println("6. View Results");
            System.out.println("7. Verify Blockchain");
            System.out.println("8. View Blockchain");
            System.out.println("9. Tamper Blockchain (Demo)");
            System.out.println("10. Logout");
            System.out.print("Select an option: ");

            String choice = scanner.nextLine();
            switch (choice) {
                case "1":
                    addCandidate();
                    break;
                case "2":
                    removeCandidate();
                    break;
                case "3":
                    viewCandidates();
                    break;
                case "4":
                    electionService.startElection();
                    System.out.println("Election started. Voting is now open.");
                    break;
                case "5":
                    electionService.endElection();
                    System.out.println("Election ended. Voting is closed.");
                    break;
                case "6":
                    viewResults();
                    break;
                case "7":
                    verifyBlockchain();
                    break;
                case "8":
                    viewBlockchain();
                    break;
                case "9":
                    tamperDemo();
                    break;
                case "10":
                    authService.logout();
                    System.out.println("Logged out.");
                    return;
                default:
                    System.out.println("Invalid option.");
            }
        }
    }

    private void viewCandidates() {
        System.out.println("\n--- Candidates ---");
        for (Candidate c : electionService.getCandidates()) {
            System.out.println(c.getCandidateId() + " - " + c.getName() + " (" + c.getParty() + ") Symbol: " + c.getSymbol());
            System.out.println("Description: " + c.getDescription());
            System.out.println("------------------");
        }
    }

    private void addCandidate() {
        System.out.print("Enter Candidate ID: ");
        String id = scanner.nextLine();
        System.out.print("Enter Name: ");
        String name = scanner.nextLine();
        System.out.print("Enter Party: ");
        String party = scanner.nextLine();
        System.out.print("Enter Description: ");
        String description = scanner.nextLine();
        System.out.print("Enter Symbol: ");
        String symbol = scanner.nextLine();
        
        electionService.addCandidate(new Candidate(id, name, party, description, symbol));
        System.out.println("Candidate added.");
    }

    private void removeCandidate() {
        System.out.print("Enter Candidate ID to remove: ");
        String id = scanner.nextLine();
        electionService.removeCandidate(id);
        System.out.println("Candidate removed (if existed).");
    }

    private void castVote() {
        try {
            User user = authService.getLoggedInUser();
            System.out.print("Enter Candidate ID to vote for: ");
            String candidateId = scanner.nextLine();
            
            System.out.print("Are you sure? Once submitted, your vote cannot be changed (y/n): ");
            String confirm = scanner.nextLine();
            if (confirm.equalsIgnoreCase("y")) {
                electionService.castVote(user.getId(), candidateId);
                System.out.println("✓ VOTE RECORDED. Your vote has been securely added to the blockchain.");
            } else {
                System.out.println("Vote cancelled.");
            }
        } catch (VotingException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void votingStatus() {
        User user = authService.getLoggedInUser();
        if (user instanceof Voter) {
            Voter v = (Voter) user;
            if (v.hasVoted()) {
                System.out.println("Status: You have already cast your vote.");
            } else {
                System.out.println("Status: You have not voted yet.");
            }
        }
    }

    private void viewResults() {
        System.out.println("\n--- Election Results ---");
        int totalVotes = 0;
        for (int count : electionService.getResults().values()) {
            totalVotes += count;
        }
        
        for (Candidate c : electionService.getCandidates()) {
            int votes = electionService.getResults().getOrDefault(c.getCandidateId(), 0);
            double percentage = totalVotes == 0 ? 0 : ((double) votes / totalVotes) * 100;
            System.out.printf("%s: %d votes (%.2f%%)\n", c.getName(), votes, percentage);
        }
        System.out.println("Total Votes Cast: " + totalVotes);
    }

    private void viewBlockchain() {
        System.out.println("\n--- Blockchain Explorer ---");
        for (Block b : electionService.getBlockchain().getChain()) {
            System.out.println("┌─────────────────────┐");
            System.out.println("│ BLOCK #" + String.format("%03d", b.getBlockIndex()));
            System.out.println("│ Timestamp: " + b.getTimestamp());
            System.out.println("│ Prev Hash: " + truncate(b.getPreviousHash(), 12) + "...");
            System.out.println("│ Curr Hash: " + truncate(b.getHash(), 12) + "...");
            System.out.println("│ TxID: " + b.getVoteTransactionId());
            System.out.println("└─────────────────────┘");
            System.out.println("          ↓");
        }
    }
    
    private String truncate(String s, int len) {
        if (s.length() <= len) return s;
        return s.substring(0, len);
    }

    private void verifyBlockchain() {
        System.out.println("\nVerifying Chain Integrity...");
        boolean isValid = electionService.getBlockchain().verifyIntegrity();
        if (isValid) {
            System.out.println("✓ BLOCKCHAIN VERIFIED");
            System.out.println("All blocks are correctly linked and no tampering was detected.");
        } else {
            System.out.println("⚠ INTEGRITY FAILURE");
            System.out.println("Blockchain validation detected a modified block.");
        }
    }
    
    private void tamperDemo() {
        System.out.print("Enter Block Index to tamper (e.g., 1): ");
        try {
            int index = Integer.parseInt(scanner.nextLine());
            electionService.getBlockchain().tamperBlock(index, "TAMPERED_TX_DATA");
            System.out.println("Block tampered. Run Verify Blockchain to see failure.");
        } catch (Exception e) {
            System.out.println("Invalid input.");
        }
    }
}
