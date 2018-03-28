import com.bulenkov.darcula.DarculaLaf;
//import org.jsoup.Jsoup;
//import org.jsoup.nodes.Document;
//import org.jsoup.nodes.Element;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import javax.swing.*;
import javax.swing.plaf.basic.BasicLookAndFeel;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.awt.event.ItemEvent;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Logger;


public class Banking extends JFrame {

    private JTabbedPane tabbedPane1;
    private JPanel panel1;
    private JComboBox comboBox1;
    private JTextField textField1;
    private JTextField textField2;
    private JTextArea textArea1;
    private JComboBox comboBox2;
    private JTextField textField3;
    private JComboBox comboBox3;
    private JTextField textField4;

    private Banking() throws IOException, ParserConfigurationException, SAXException {

        setTitle("BankingApp");
        textArea1.setEditable(false);

        Logger logger = Logger.getLogger(Banking.class.getName());
        Logger rootLogger = Logger.getLogger("");
        Handler[] handlers = rootLogger.getHandlers();
        if (handlers[0] instanceof ConsoleHandler) {
            rootLogger.removeHandler(handlers[0]);
        }

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document doc;
        Element classElement;
        ArrayList<Element> rates = new ArrayList<>();
        String date;
        Path file = Paths.get("history.txt");

        if (Files.notExists(file)) {
            try {
                Files.createFile(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        OutputStream outputStream = Files.newOutputStream(file);

        for (int i = 0; i > -8; i--) {
            date = getDayString(i);
            outputStream.write(date.getBytes());
            outputStream.write(System.lineSeparator().getBytes());
            doc = db.parse(new URL("https://www.bnm.md/en/official_exchange_rates?get_xml=1&date=" + date).openStream());
            classElement = (Element) doc.getElementsByTagName("Valute").item(0); //TODO: fix the XML tag search
            switch (classElement.getAttribute("ID")) {
                case "44":
                    if (!rates.contains((Element)doc.getElementsByTagName("Value").item(1))) {
                        rates.add((Element) doc.getElementsByTagName("Value").item(1));
                        outputStream.write(doc.getElementsByTagName("CharCode").item(1).toString().getBytes());
                        outputStream.write(rates.get(0).toString().getBytes());
                        outputStream.write(System.lineSeparator().getBytes());
                    }
                    break;
                case "47":
                    if (!rates.contains((Element)doc.getElementsByTagName("Value").item(0))) {
                        rates.add((Element) doc.getElementsByTagName("Value").item(0));
                        outputStream.write(doc.getElementsByTagName("CharCode").item(0).toString().getBytes());
                        outputStream.write(rates.get(1).toString().getBytes());
                        outputStream.write(System.lineSeparator().getBytes());
                    }
                    break;
                case "36":
                    if (!rates.contains((Element)doc.getElementsByTagName("Value").item(2))) {
                        rates.add((Element) doc.getElementsByTagName("Value").item(2));
                        outputStream.write(doc.getElementsByTagName("CharCode").item(2).toString().getBytes());
                        outputStream.write(rates.get(2).toString().getBytes());
                        outputStream.write(System.lineSeparator().getBytes());
                    }
                    break;
                case "35":
                    if (!rates.contains((Element)doc.getElementsByTagName("Value").item(3))) {
                        rates.add((Element) doc.getElementsByTagName("Value").item(3));
                        outputStream.write(doc.getElementsByTagName("CharCode").item(3).toString().getBytes());
                        outputStream.write(rates.get(3).toString().getBytes());
                        outputStream.write(System.lineSeparator().getBytes());
                    }
                    break;
                case "43":
                    if (!rates.contains((Element)doc.getElementsByTagName("Value").item(4))) {
                        rates.add((Element) doc.getElementsByTagName("Value").item(4));
                        outputStream.write(doc.getElementsByTagName("CharCode").item(4).toString().getBytes());
                        outputStream.write(rates.get(4).toString().getBytes());
                        outputStream.write(System.lineSeparator().getBytes());
                    }
                    break;
                case "17":
                    if (!rates.contains((Element)doc.getElementsByTagName("Value").item(17))) {
                        rates.add((Element) doc.getElementsByTagName("Value").item(17));
                        outputStream.write(doc.getElementsByTagName("CharCode").item(17).toString().getBytes());
                        outputStream.write(rates.get(5).toString().getBytes());
                        outputStream.write(System.lineSeparator().getBytes());
                    }
                    break;
            }


        }


//        Document doc = Jsoup.connect("https://point.md/ru/").get();
//
//
//        rates.add(doc.select("td#money-trade-buy-usd").first());
//        rates.add(doc.select("td#money-trade-buy-eur").first());
//        rates.add(doc.select("td#money-trade-buy-rub").first());
//        rates.add(doc.select("td#money-trade-buy-ron").first());
//        rates.add(doc.select("td#money-trade-buy-uah").first());
//        rates.add(doc.select("td#money-trade-buy-gbp").first());

        comboBox1.addItemListener((ItemEvent e) -> {
            switch ((String) e.getItem()) {
                case "USD":
                    fillFieldFromMDL(rates.get(0));
                    break;
                case "EUR":
                    fillFieldFromMDL(rates.get(1));
                    break;
                case "RUB":
                    fillFieldFromMDL(rates.get(2));
                    break;
                case "RON":
                    fillFieldFromMDL(rates.get(3));
                    break;
                case "UAH":
                    fillFieldFromMDL(rates.get(4));
                    break;
                case "GBP":
                    fillFieldFromMDL(rates.get(5));
                    break;
            }
        });
        comboBox3.addItemListener((ItemEvent e) -> {
            switch ((String) e.getItem()) {
                case "USD":
                    fillFieldToMDL(rates.get(0));
                    break;
                case "EUR":
                    fillFieldToMDL(rates.get(1));
                    break;
                case "RUB":
                    fillFieldToMDL(rates.get(2));
                    break;
                case "RON":
                    fillFieldToMDL(rates.get(3));
                    break;
                case "UAH":
                    fillFieldToMDL(rates.get(4));
                    break;
                case "GBP":
                    fillFieldToMDL(rates.get(5));
                    break;
            }
        });

//        LocalDate date = LocalDate.now();

//
//        ArrayList<String> currencies = new ArrayList<>();
//        for (int i = 1; i < 7; i++){
//            currencies.add(comboBox1.getItemAt(i).toString());
//        }
//
//        for (int i = 0; i < currencies.size(); i++) {
//            outputStream.write((currencies.get(i)+": ").getBytes());
//
//        }


    }

    private Date getDay(int d) {
        final Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, d);
        return cal.getTime();
    }

    private String getDayString(int d) {
        DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        return dateFormat.format(getDay(d));
    }

    private String calculateFromMDL(Element e) {
        return Float.toString(Float.parseFloat(textField2.getText()) / Float.parseFloat(e.toString()));
    }

    private String calculateToMDL(Element e) {
        return Float.toString(Float.parseFloat(textField3.getText()) * Float.parseFloat(e.toString()));
    }

    private void fillFieldFromMDL(Element e) {
        textField1.setText(calculateFromMDL(e));
    }

    private void fillFieldToMDL(Element e) {
        textField4.setText(calculateToMDL(e));
    }

    public static void main(String[] args) throws IOException, ParserConfigurationException, SAXException {
        JFrame frame = new JFrame("Banking");
        BasicLookAndFeel darcula = new DarculaLaf();
        try {
            UIManager.setLookAndFeel(darcula);
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
        frame.setContentPane(new Banking().panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}