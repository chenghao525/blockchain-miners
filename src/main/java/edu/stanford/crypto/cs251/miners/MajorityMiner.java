package edu.stanford.crypto.cs251.miners;

import edu.stanford.crypto.cs251.blockchain.Block;
import edu.stanford.crypto.cs251.blockchain.NetworkStatistics;

public class MajorityMiner extends BaseMiner implements Miner {
    private Block currentHead;
    private double totalHashRate;

    public MajorityMiner(String id, int hashRate, int connectivity) {
        super(id, hashRate, connectivity);

    }

    @Override
    public Block currentlyMiningAt() {
        return currentHead;
    }

    @Override
    public Block currentHead() {
        return currentHead;
    }

    @Override
    public void blockMined(Block block, boolean isMinerMe) {
        if(isMinerMe) {
            if (block.getHeight() > currentHead.getHeight()) {
                this.currentHead = block;
            }
        }
        else{
            if (currentHead == null) {
                currentHead = block;
            }
            else if (this.getHashRate() / this.totalHashRate < 0.51 && block.getHeight() > currentHead.getHeight())    {
                currentHead = block;
            } else {
                return;
            }
        }
    }

    @Override
    public void initialize(Block genesis, NetworkStatistics networkStatistics) {
        this.currentHead = genesis;
        this.totalHashRate = networkStatistics.getTotalHashRate();
    }

    @Override
    public void networkUpdate(NetworkStatistics statistics) {
        this.totalHashRate = statistics.getTotalHashRate();
    }
}
