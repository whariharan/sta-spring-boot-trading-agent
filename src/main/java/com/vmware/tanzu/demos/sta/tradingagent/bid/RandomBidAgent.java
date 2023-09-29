package com.vmware.tanzu.demos.sta.tradingagent.bid;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Component
@ConditionalOnProperty(name = "app.agent.strategy", havingValue = "random")
class RandomBidAgent implements BidAgent {
    private final Random random = new Random();

    @Override
    public List<BidAgentRequest> execute(Context ctx) {
        List<BidAgentRequest> res = new ArrayList<BidAgentRequest>();
//        res.add(new BidAgentRequest("aapl",-52));
//        res.add(new BidAgentRequest("amzn",-320));
//        res.add(new BidAgentRequest("googl",-119));
//        res.add(new BidAgentRequest("meta",-55));
//        res.add(new BidAgentRequest("msft",-47));
//        res.add(new BidAgentRequest("vmw",-95));
        return res;
    }

    @Override
    public String toString() {
        return "RANDOM";
    }
}
