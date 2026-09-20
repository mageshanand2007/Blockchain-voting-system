package model;
import java.util.UUID;
public class Vote {
    private String voteTransactionId; private String voterId; private String candidateId; private long timestamp;
    public Vote(String voterId, String candidateId) {
        this.voteTransactionId = UUID.randomUUID().toString(); this.voterId = voterId; this.candidateId = candidateId; this.timestamp = System.currentTimeMillis();
    }
    public String getVoteTransactionId() { return voteTransactionId; }
    public String getVoterId() { return voterId; }
    public String getCandidateId() { return candidateId; }
    public long getTimestamp() { return timestamp; }
}
