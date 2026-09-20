package blockchain;

import interfaces.BlockchainVerifiable;
import java.util.ArrayList;
import java.util.List;

public class Blockchain implements BlockchainVerifiable {
    private List<Block> chain;

    public Blockchain() {
        chain = new ArrayList<>();
        chain.add(new Block(0, "0", "GENESIS_BLOCK"));
    }

    public void addBlock(String voteTransactionId) {
        Block previousBlock = chain.get(chain.size() - 1);
        Block newBlock = new Block(chain.size(), previousBlock.getHash(), voteTransactionId);
        chain.add(newBlock);
    }

    public List<Block> getChain() { return chain; }

    @Override
    public boolean verifyIntegrity() {
        for (int i = 1; i < chain.size(); i++) {
            Block currentBlock = chain.get(i);
            Block previousBlock = chain.get(i - 1);

            if (!currentBlock.getHash().equals(currentBlock.calculateHash())) {
                return false;
            }
            if (!currentBlock.getPreviousHash().equals(previousBlock.getHash())) {
                return false;
            }
        }
        return true;
    }

    public void tamperBlock(int index, String newTxId) {
        if (index >= 0 && index < chain.size()) {
            chain.get(index).setVoteTransactionId(newTxId);
        }
    }
}
