package src.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * A Block is a unit of data that contains information about transactions in a blockchain.
 * It contains a hash, a block id, a number of transactions, and a list of transactions.
 */
public class Block implements Serializable {

    private byte[] hash;

    private long block_id;

    private int nrTransacions;

    private List<String> transactions;

    /**
     * Creates a new Block with the specified hash, block id, and number of transactions.
     * Initializes the list of transactions to an empty ArrayList.
     * @param hash the hash of the block
     * @param block_id the unique identifier of the block
     * @param nrTransacions the number of transactions in the block
     */
    public Block(byte[] hash, long block_id, int nrTransacions) {
        this.hash = hash;
        this.block_id = block_id;
        this.nrTransacions = nrTransacions;
        this.transactions = new ArrayList<>();
    }

    /**
     * Returns the hash of the block.
     * @return the hash of the block
     */
    public byte[] getHash() {
        return hash;
    }

    /**
     * Returns the unique identifier of the block.
     * @return the unique identifier of the block
     */
    public long getBlock_id() {
        return block_id;
    }

    /**
     * Returns the number of transactions in the block.
     * @return the number of transactions in the block
     */
    public int getNrTransacions() {
        return nrTransacions;
    }

    /**
     * Returns the list of transactions in the block.
     * @return the list of transactions in the block
     */
    public List<String> getTransactions() {
        return transactions;
    }

    /**
     * Adds a transaction to the block.
     * Increments the number of transactions by 1.
     * @param transaction the transaction to be added to the block
     */
    public void addTransaction(String transaction) {
        this.transactions.add(transaction);
        this.nrTransacions++;
    }
}
