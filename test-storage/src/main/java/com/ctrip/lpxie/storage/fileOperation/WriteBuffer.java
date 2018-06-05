package com.ctrip.lpxie.storage.fileOperation;

import java.util.Map;

import static com.ctrip.lpxie.storage.GlobalConstant.*;
/**
 * Created by lpxie on 2017/3/30.
 * 主要用于写缓存，比如一个块=4096，对应的key=偏移量 值就是当前偏移量对应的字节
 * 性能提优点：对于顺序写，或者写的数据刚好在缓存里面，可以减少读磁盘的时间
 * 缺点：如果数据十分随机，导致每次写的位置分散在不同的磁盘块，在写的位置超过了缓存的总个数后，会把新的磁盘块放进缓存，老的写入磁盘，
 * 这样就会带来额外的性能开销！
 */
public class WriteBuffer {
    private int capacity = 1024;
    private int bufferSize = capacity * blockSize;
    private Map<Long,byte[]> buffer;
}
