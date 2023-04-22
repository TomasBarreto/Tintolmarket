package src.domain;

import java.io.Serializable;
import java.util.List;

public class Block implements Serializable {

    private String hash;

    private int block_id;

    private int nr_transacions;

    private List<String> transactions;

    public Block(String hash, int block_id, int nr_transacions, List<String> transactions) {
        this.hash = hash;
        this.block_id = block_id;
        this.nr_transacions = nr_transacions;
        this.transactions = transactions;
    }

    public String getHash() {
        return hash;
    }

    public int getBlock_id() {
        return block_id;
    }

    public int getNr_transacions() {
        return nr_transacions;
    }

    public List<String> getTransactions() {
        return transactions;
    }
}
