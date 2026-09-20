package blockchain;

import java.security.MessageDigest;
import java.nio.charset.StandardCharsets;

public class Block {
    private int blockIndex;
    private long timestamp;
    private String previousHash;
    private String hash;
    private String voteTransactionId;

    public Block(int blockIndex, String previousHash, String voteTransactionId) {
        this.blockIndex = blockIndex;
        this.previousHash = previousHash;
        this.voteTransactionId = voteTransactionId;
        this.timestamp = System.currentTimeMillis();
        this.hash = calculateHash();
    }

    public String calculateHash() {
        String dataToHash = blockIndex + Long.toString(timestamp) + previousHash + voteTransactionId;
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] bytes = digest.digest(dataToHash.getBytes(StandardCharsets.UTF_8));
            StringBuilder buffer = new StringBuilder();
            for (byte b : bytes) {
                buffer.append(String.format("%02x", b));
            }
            return buffer.toString();
        } catch (Exception e) {
            throw new RuntimeException("SHA-256 algorithm not found.", e);
        }
    }

    public int getBlockIndex() { return blockIndex; }
    public long getTimestamp() { return timestamp; }
    public String getPreviousHash() { return previousHash; }
    public String getHash() { return hash; }
    public String getVoteTransactionId() { return voteTransactionId; }

    public void setVoteTransactionId(String modifiedId) {
        this.voteTransactionId = modifiedId;
    }
}
