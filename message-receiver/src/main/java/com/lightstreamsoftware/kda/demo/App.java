package com.lightstreamsoftware.kda.demo;


import com.amazonaws.services.kinesis.AmazonKinesis;
import com.amazonaws.services.kinesis.AmazonKinesisClientBuilder;
import com.amazonaws.services.kinesis.model.ListShardsRequest;
import com.amazonaws.services.kinesis.model.ListShardsResult;

import java.util.List;
import java.util.stream.Collectors;

public class App {
    public static void main(String[] args) {

        final AmazonKinesis kinesis = AmazonKinesisClientBuilder.defaultClient();

        String streamName = "output-stream";
        ListShardsResult listShardsResult = kinesis.listShards(new ListShardsRequest().withStreamName(streamName));
        List<Thread> collect = listShardsResult.getShards().stream()
                .map(shard -> new Thread(new ThreadRunner(kinesis, streamName, shard)))
                .peek(Thread::start)
                .collect(Collectors.toList());

        collect.forEach(x -> {
            try {
                x.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

    }
}
