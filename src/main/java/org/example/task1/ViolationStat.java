package org.example.task1;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class ViolationStat {
    private static HashMap<ViolationsList, Violation> violations = new HashMap<ViolationsList, Violation>();

    public static HashMap<ViolationsList, Violation> FeeCounter(int startYear, int endYear) throws ParserConfigurationException, SAXException, IOException {
        violations = new HashMap<ViolationsList, Violation>();
        for (int actYear = startYear; actYear <= endYear; actYear++){
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser parser = factory.newSAXParser();
            XMLHandler handler = new XMLHandler();
            parser.parse(new File("src/main/resources/" + actYear + ".xml"), handler);
        }
        return violations;
    }

    public static ViolationsList StringToType(String str){
        switch (str){
            case "CAR_CRASH":
                return ViolationsList.CAR_CRASH;
            case "NO_LICENCE":
                return ViolationsList.NO_LICENCE;
            case "SPEEDING":
                return ViolationsList.SPEEDING;
            case "RED_LIGHT":
                return ViolationsList.RED_LIGHT;
            default:
                return ViolationsList.UNDEFINED;
        }
    }

    private static class XMLHandler extends DefaultHandler {
        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
            if (qName.equals("violation")) {
                ViolationsList type = StringToType(attributes.getValue("type"));
                float fineAmount = Float.parseFloat(attributes.getValue("fine_amount"));
                Fee temp = new Fee(type, fineAmount);

                if(!violations.containsKey(type)){
                    violations.put(type, new Violation(temp));
                } else {
                    violations.put(type, violations.get(type).setNewAmounts(temp));
                }
            }
        }
    }
}
