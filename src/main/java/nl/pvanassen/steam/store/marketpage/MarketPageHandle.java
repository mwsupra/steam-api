package nl.pvanassen.steam.store.marketpage;

import nl.pvanassen.steam.http.DefaultHandle;
import org.cyberneko.html.parsers.DOMParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.xpath.XPathExpressionException;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.List;

class MarketPageHandle extends DefaultHandle {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private MarketPage outstandings;
    private boolean error;

    /**
     * {@inheritDoc}
     *
     * @see nl.pvanassen.steam.http.DefaultHandle#handle(java.io.InputStream)
     */
    @Override
    public void handle(InputStream stream) throws IOException {
        DOMParser parser = new DOMParser();
        try {
            parser.parse(new InputSource(stream));
            Document document = parser.getDocument();
            int wallet = Wallet.getWallet(document);
            List<OutstandingItem> items = Outstandings.getOutstandingItems(document);
            int numberOfItems = items.size();
            int amount = items.stream().mapToInt(item -> item.getPrice()).sum();
            List<MarketPageBuyOrder> buyOrders = BuyOrder.getBuyOrders(document);
            outstandings = new MarketPage(wallet, numberOfItems, amount, items, buyOrders);
        }
        catch (ParseException | RuntimeException e) {
            logger.error("Error getting outstanding items", e);
            error = true;
        }
        catch (SAXException | XPathExpressionException e) {
            logger.error("Error getting outstanding items", e);
            error = true;
        }
    }
    
    /**
     * {@inheritDoc}
     *
     * @see nl.pvanassen.steam.http.DefaultHandle#handleError(java.io.InputStream)
     */
    @Override
    public void handleError(InputStream stream) {
        logger.error("Error getting outstanding items, error unknown");
        error = true;
    }
    
    @Override
    public void handleException(Exception exception) {
        super.handleException(exception);
        error = true;
    }

    MarketPage getOutstandings() {
        return outstandings;
    }
    
    boolean isError() {
        return error;
    }
}
