package com.lightstreamsoftware.kda.demo;

import com.amazonaws.services.kinesis.AmazonKinesis;
import com.amazonaws.services.kinesis.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.Charset;

public class ThreadRunner implements Runnable {
    private final static Logger logger = LoggerFactory.getLogger(ThreadRunner.class);
    private final AmazonKinesis kinesis;
    private String streamName;
    private final Shard shard;

    public ThreadRunner(AmazonKinesis kinesis, String streamName, Shard shard) {
        this.kinesis = kinesis;
        this.streamName = streamName;
        this.shard = shard;
    }

    @Override
    public void run() {

        GetShardIteratorResult initialShardIterator = kinesis.getShardIterator(new GetShardIteratorRequest()
                .withStreamName(streamName)
                .withShardIteratorType(ShardIteratorType.LATEST)
                .withShardId(shard.getShardId()));

        String shardIterator = initialShardIterator.getShardIterator();

        do {
            GetRecordsResult records = kinesis.getRecords(new GetRecordsRequest()
                    .withShardIterator(shardIterator)
                    .withLimit(100));

            records.getRecords().forEach(x -> logger.info(new String(x.getData().array(), Charset.forName("UTF-8"))));

            shardIterator = records.getNextShardIterator();
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                logger.error("Failed to sleep",e);
            }
        }while(shardIterator != null);
    }
}
