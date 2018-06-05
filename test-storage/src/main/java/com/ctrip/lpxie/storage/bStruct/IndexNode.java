package com.ctrip.lpxie.storage.bStruct;

/**
 * Created by lpxie on 2017/3/21.
 * 磁盘块号和该号对应的地址
 */
public class IndexNode {
    private long indexOffset;//磁盘块号对应的节点地址 = 文件中这个数据的偏移量

    private byte[] realData;//B+树中只有叶子节点还有这个数据，非叶子节点这个值为空

    public long getIndexOffset() {
        return indexOffset;
    }

    public void setIndexOffset(long indexOffset) {
        this.indexOffset = indexOffset;
    }

    public byte[] getRealData() {
        return realData;
    }

    public void setRealData(byte[] realData) {
        this.realData = realData;
    }
}
