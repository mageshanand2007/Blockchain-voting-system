package services;

import model.Candidate;
import model.Vote;
import model.Voter;
import blockchain.Blockchain;
import exceptions.VotingException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ElectionService {
    private List<Candidate> candidates;
    private Map<String, Integer> results;
    private Blockchain blockchain;
    private boolean isElectionLive;
    private AuthenticationService authService;

    public ElectionService(AuthenticationService authService) {
        this.candidates = new ArrayList<>();
        this.results = new HashMap<>();
        this.blockchain = new Blockchain();
        this.isElectionLive = false;
        this.authService = authService;
        
        // Demo Candidates
        addCandidate(new Candidate("CAND001", "Candidate A", "Digital Future Party", "Focused on technological advancement.", "Digital Star"));
        addCandidate(new Candidate("CAND002", "Candidate B", "People First", "Prioritizing social welfare.", "Unity Circle"));
        addCandidate(new Candidate("CAND003", "Candidate C", "Future Citizens", "Building a sustainable future.", "Innovation Leaf"));
    }

    public void addCandidate(Candidate candidate) {
        candidates.add(candidate);
        results.put(candidate.getCandidateId(), 0);
    }
    
    public void removeCandidate(String candidateId) {
        candidates.removeIf(c -> c.getCandidateId().equals(candidateId));
        results.remove(candidateId);
    }

    public List<Candidate> getCandidates() {
        return candidates;
    }

    public void startElection() {
        isElectionLive = true;
    }

    public void endElection() {
        isElectionLive = false;
    }

    public boolean isElectionLive() {
        return isElectionLive;
    }

    public void castVote(String voterId, String candidateId) throws VotingException {
        if (!isElectionLive) {
            throw new VotingException("Voting is currently closed.");
        }

        model.User user = authService.getUsers().get(voterId);
        if (user == null || !(user instanceof Voter)) {
            throw new VotingException("Invalid voter.");
        }

        Voter voter = (Voter) user;
        if (voter.hasVoted()) {
            throw new VotingException("You have already voted.");
        }

        boolean validCandidate = candidates.stream().anyMatch(c -> c.getCandidateId().equals(candidateId));
        if (!validCandidate) {
            throw new VotingException("Invalid candidate.");
        }

        // Process vote
        Vote vote = new Vote(voterId, candidateId);
        blockchain.addBlock(vote.getVoteTransactionId());
        
        results.put(candidateId, results.getOrDefault(candidateId, 0) + 1);
        voter.setHasVoted(true);
    }

    public Blockchain getBlockchain() {
        return blockchain;
    }

    public Map<String, Integer> getResults() {
        return results;
    }
}
