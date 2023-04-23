package src.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Block implements Serializable {

    private byte[] hash;

    private long block_id;

    private int nrTransacions;

    private List<String> transactions;

    public Block(byte[] hash, long block_id, int nrTransacions) {
        this.hash = hash;
        this.block_id = block_id;
        this.nrTransacions = nrTransacions;
        this.transactions = new ArrayList<>();
    }

    public byte[] getHash() {
        return hash;
    }

    public long getBlock_id() {
        return block_id;
    }

    public int getNrTransacions() {
        return nrTransacions;
    }

    public List<String> getTransactions() {
        return transactions;
    }

    public void addTransaction(String transaction) {
        this.transactions.add(transaction);
        this.nrTransacions++;
    }
}
