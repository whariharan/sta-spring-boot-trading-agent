package com.vmware.tanzu.demos.sta.tradingagent.bid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

@Component
@ConditionalOnProperty(name = "app.agent.strategy", havingValue = "buy-lower-stock")
class BuyLowerStockBidAgent implements BidAgent {
    private final Logger logger = LoggerFactory.getLogger(BuyLowerStockBidAgent.class);

    HashMap<String, BigDecimal> bought = new HashMap<String, BigDecimal>();
    HashMap<String, BigInteger> quan = new HashMap<String, BigInteger>();

    private final BigDecimal EACH_PRICE = new BigDecimal("15000.00");

    @Override
    public List<BidAgentRequest> execute(Context ctx) {
        // Sort input stocks against price.
        final List<Stock> sortedStocks = new ArrayList<>(ctx.stocks());
        sortedStocks.sort(Comparator.comparing(Stock::price));
        List<BidAgentRequest> res = new ArrayList<BidAgentRequest>();
        final Stock lowerStock = sortedStocks.get(0);
        logger.info("Found a stock with the lower value: {}", lowerStock.symbol());
        ctx.stocks().forEach(stock -> {
            if (!bought.containsKey(stock.symbol())) {
                BigInteger quantity = floor(EACH_PRICE.divide(stock.price(), 2, RoundingMode.HALF_UP));
                res.add(new BidAgentRequest(stock.symbol(), quantity.intValue()));
                bought.put(stock.symbol(), stock.price());
                quan.put(stock.symbol(), quantity);
            } else if (stock.price().compareTo(bought.get(stock.symbol())) > 0) {
                BigInteger quantity = quan.get(stock.symbol());
                res.add(new BidAgentRequest(stock.symbol(), -1 * quantity.intValue()));
                bought.remove(stock.symbol());
                quan.remove(stock.symbol());
            }
        });
        return res;
    }

    private BigInteger floor(BigDecimal x) {
        return x.setScale(0, RoundingMode.FLOOR).unscaledValue();
    }

    @Override
    public String toString() {
        return "BUY_LOWER_STOCK";
    }
}