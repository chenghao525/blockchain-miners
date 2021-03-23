package edu.stanford.crypto.cs251.miners;

import edu.stanford.crypto.cs251.blockchain.Block;
import edu.stanford.crypto.cs251.blockchain.NetworkStatistics;

public class FeeSnipingMiner extends BaseMiner implements Miner {
    private Block currentHead;
    private double feeThreshold = 26;
    private double selfHashPower;

    public FeeSnipingMiner(String id, int hashRate, int connectivity) {
        super(id, hashRate, connectivity);
    }

    @Override
    public Block currentlyMiningAt() {
        return this.currentHead;
    }

    @Override
    public Block currentHead() {
        return this.currentHead;
    }

    @Override
    public void blockMined(Block block, boolean isMinerMe) {

        if(isMinerMe) {
            if (this.currentHead.getHeight() < block.getHeight())
                this.currentHead = block;
        }
        else {
            if(block != null && currentHead.getHeight() < block.getHeight()) {
                if (this.currentHead == null)
                    this.currentHead = block;
                else if(this.reachFeeThreshold(block)){
                    Block prevBlock = block.getPreviousBlock();
                    if(prevBlock != null){this.currentHead = prevBlock;}
                }else{
                    this.currentHead = block;
                }
            }
        }
    }

    private boolean reachFeeThreshold(Block block){
        return block.getBlockValue() >= this.feeThreshold;
    }

    @Override
    public void initialize(Block genesis, NetworkStatistics networkStatistics) {
        this.currentHead = genesis;
    }

    @Override
    public void networkUpdate(NetworkStatistics statistics) {
        this.selfHashPower = (double)(this.getHashRate()/statistics.getTotalConnectivity());
    }
}
