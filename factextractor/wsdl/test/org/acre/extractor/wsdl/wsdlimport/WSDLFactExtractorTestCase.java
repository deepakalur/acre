package org.acre.extractor.wsdl.wsdlimport;

import junit.framework.TestCase;

import java.util.Set;
import java.util.HashSet;

/**
 * User: Administrator
 * Date: Oct 17, 2004
 * Time: 10:22:16 PM
 */
public class WSDLFactExtractorTestCase extends TestCase {
    public WSDLFactExtractorTestCase() {
        super();    //To change body of overridden methods use File | Settings | File Templates.
    }

    public WSDLFactExtractorTestCase(String s) {
        super(s);    //To change body of overridden methods use File | Settings | File Templates.
    }



    public void testMethod1() {
        String fileName = "factextractor\\wsdl\\samples\\stockquote.wsdl";
        WSDLFactDAO factWriter = new WSDLFactDAO("wsdlFacts.sql");
        WSDLFactExtractor extractor = new WSDLFactExtractor(fileName, factWriter);
        extractor.extractFacts();
        WSDLSymbols symbols = extractor.getWSDLSymbols();

        Set<String> keys = symbols.getKeys();

        for ( String k : keys )
            System.out.println(k);

        Set refKeySet = getStockQuoteWSDLSymbols(keys);

        assertEquals(refKeySet, keys);

    }

    public void testMethod2() {
        assertTrue(true);
    }



    private Set<String> getStockQuoteWSDLSymbols(Set<String> keys) {
        Set<String> refKeys = new HashSet<String>();

        // <definitions name="StockQuote" targetNamespace="http://example.com/stockquote/definitions" xmlns:tns="http://example.com/stockquote/definitions" xmlns:xsd1="http://example.com/stockquote/schemas" xmlns="http://schemas.xmlsoap.org/wsdl/">
        refKeys.add("http://example.com/stockquote/definitions");

        // <import namespace="http://example.com/stockquote/schemas" location="stockquote2.wsdl"/>
        refKeys.add("http://example.com/stockquote/definitions&&IMPORT&&http://example.com/stockquote/schemas");

        //<definitions name="StockQuoteData" xmlns="http://schemas.xmlsoap.org/wsdl/">
        refKeys.add("http://example.com/stockquote/schemas");

        /** <message name="GetLastTradePriceInput">
            <part name="body" element="xsd1:TradePriceRequest"/>
        </message> */
        refKeys.add("http://example.com/stockquote/definitions&&MESSAGE&&GetLastTradePriceOutput");
        refKeys.add("http://example.com/stockquote/definitions&&MESSAGE&&GetLastTradePriceOutput&&body");

        /** <message name="GetLastTradePriceOutput">
            <part name="body" element="xsd1:TradePrice"/>
        </message> */
        refKeys.add("http://example.com/stockquote/definitions&&MESSAGE&&GetLastTradePriceInput");
        refKeys.add("http://example.com/stockquote/definitions&&MESSAGE&&GetLastTradePriceInput&&body");

        /**<definitions name="StockQuoteData" xmlns="http://schemas.xmlsoap.org/wsdl/">
        ...........
        <message name="TradePrice">
            <part name="body" element="TradePriceRequest"/>
        </message> **/
        refKeys.add("http://example.com/stockquote/schemas&&MESSAGE&&TradePrice");
        refKeys.add("http://example.com/stockquote/schemas&&MESSAGE&&TradePrice&&body");

        /** <portType name="StockQuotePortType">
            <operation name="Test">
                <input message="xsd1:TradePrice"/>
                <output message="xsd1:TradePrice"/>
            </operation>
            <operation name="GetLastTradePrice">
                <input message="tns:GetLastTradePriceInput"/>
                <output message="tns:GetLastTradePriceOutput"/>
            </operation>
        </portType> */
        refKeys.add("http://example.com/stockquote/definitions&&PORTTYPE&&StockQuotePortType");
        refKeys.add("http://example.com/stockquote/definitions&&PORTTYPE&&StockQuotePortType&&GetLastTradePrice");
        refKeys.add("http://example.com/stockquote/definitions&&PORTTYPE&&StockQuotePortType&&Test");

        return refKeys;
    }
}
