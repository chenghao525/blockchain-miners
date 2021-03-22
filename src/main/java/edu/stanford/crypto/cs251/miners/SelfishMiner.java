package edu.stanford.crypto.cs251.miners;

import edu.stanford.crypto.cs251.blockchain.Block;
import edu.stanford.crypto.cs251.blockchain.NetworkStatistics;

public class SelfishMiner extends BaseMiner implements Miner {
    private Block currentHead, selfHead;
//    private double totalHashRate;

    public SelfishMiner(String id, int hashRate, int connectivity) {
        super(id, hashRate, connectivity);
    }

    @Override
    public Block currentlyMiningAt() {
        return this.selfHead;
    }

    @Override
    public Block currentHead() {
        return this.currentHead;
    }

    @Override
    public void blockMined(Block block, boolean isMinerMe) {

        if(isMinerMe) {
            if (this.selfHead == null || this.selfHead.getHeight() - block.getHeight() < 0)
                this.selfHead = block;
        }
        else{
            if (this.selfHead == null)
                this.selfHead = block;
            else if(this.selfHead.getHeight() >= block.getHeight() && this.selfHead.getHeight() - block.getHeight() <= 1)
                this.currentHead = this.selfHead;
            else if(this.selfHead.getHeight() < block.getHeight()) {
                this.currentHead = block;
                this.selfHead = block;
            }
        }
    }

    @Override
    public void initialize(Block genesis, NetworkStatistics networkStatistics) {
        this.selfHead = genesis;
    }

    @Override
    public void networkUpdate(NetworkStatistics statistics) {
    }
}
