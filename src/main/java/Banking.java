import com.bulenkov.darcula.DarculaLaf;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import javax.swing.*;
import javax.swing.plaf.basic.BasicLookAndFeel;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.awt.event.ItemEvent;
import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

//import org.jsoup.Jsoup;
//import org.jsoup.nodes.Document;
//import org.jsoup.nodes.Element;

@SuppressWarnings("SuspiciousMethodCalls")
public class Banking extends JFrame {

    private JTabbedPane tabbedPane1;
    private JPanel panel1;
    private JComboBox comboBox1;
    private JTextField textField1;
    private JTextField textField2;
    private JTextArea textArea1;
    private JComboBox<String> comboBox2;
    private JTextField textField3;
    private JComboBox comboBox3;
    private JTextField textField4;
    private JTextPane textPane1;

    private static Logger logger = LoggerFactory.getLogger(Banking.class);
    private Path file = getFile();

    private Banking() throws IOException, ParserConfigurationException, SAXException {

        setTitle("BankingApp");
        textArea1.setEditable(false);
        textField1.setEditable(false);
        textField4.setEditable(false);

        DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
        Document doc;
        Element classElement;
        ArrayList<Element> rates = new ArrayList<>();
        String date;


        OutputStream outputStream = Files.newOutputStream(file);
        FileReader reader = new FileReader(file.toFile());
        BufferedReader bufferedReader = new BufferedReader(reader);
        String url;
        String[] ids = {"44", "47", "36", "35", "43", "17"};
        List<String> idList = new ArrayList<>(Arrays.asList(ids));

        for (int i = 0; i > -8; i--) {
            date = getDayString(i);
            comboBox2.addItem(date);
            outputStream.write(date.getBytes());
            outputStream.write(System.lineSeparator().getBytes());
            url = "https://www.bnm.md/en/official_exchange_rates?get_xml=1&date=" + date;
            doc = docBuilder.parse(new URL(url).openStream());
            rates.clear();
            for (int j = 0; j < doc.getElementsByTagName("Valute").getLength(); j++) {
                classElement = (Element) doc.getElementsByTagName("Valute").item(j);

                if (idList.contains(classElement.getAttribute("ID")) && !rates.contains(doc.getElementsByTagName("Value").item(j))) {
                    outputToFile(rates, outputStream, doc, j);
                }
            }
        }

        addListeners(rates, bufferedReader);

    }

    private Path getFile() {
        Path file = Paths.get("history.txt");

        if (Files.notExists(file)) {
            try {
                Files.createFile(file);
            } catch (IOException e) {
                logger.error("Cannot create file");
            }
        }
        return file;
    }

    private void addListeners(ArrayList<Element> rates, BufferedReader bufferedReader) {
        String[] currencies = {"EUR", "USD", "RUB", "RON", "UAH", "GBP"};
        List<String> currencyList = new ArrayList<>(Arrays.asList(currencies));

        comboBox1.addItemListener((ItemEvent e) -> {
            for (int j = 0; j < comboBox1.getItemCount(); j++){
                if (currencyList.contains(e.getItem())){
                    fillFieldFromMDL(rates.get(j));
                }
            }
        });
        comboBox3.addItemListener((ItemEvent e) -> {
            for (int j = 0; j < comboBox3.getItemCount(); j++){
                if (currencyList.contains(e.getItem())){
                    fillFieldToMDL(rates.get(j));
                }
            }
        });
        comboBox2.addItemListener((ItemEvent e) -> {
            textArea1.setText("");
            String s;
            StringBuilder endString = new StringBuilder();
            try {
                bufferedReader.mark((int)Files.size(file)+1);
                while ((s = bufferedReader.readLine()) != null){
                    if (s.equals(e.getItem())){
                        for (int i = 0; i < 6; i++){
                            s = bufferedReader.readLine();
                            endString.append(s);
                            endString.append("\n");
                        }
                        textArea1.setText(endString.toString());
                    }
                }
                bufferedReader.reset();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });
    }

    private void outputToFile(ArrayList<Element> rates, OutputStream outputStream, Document doc, int j) throws IOException {
        rates.add((Element) doc.getElementsByTagName("Value").item(j));
        outputStream.write((doc.getElementsByTagName("CharCode").item(j).getTextContent() + ": ").getBytes());
        if (j == 17) {
            outputStream.write(rates.get(5).getTextContent().getBytes());
        } else {
            outputStream.write(rates.get(j).getTextContent().getBytes());
        }
        outputStream.write(System.lineSeparator().getBytes());
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
        return Float.toString(Float.parseFloat(textField2.getText()) / Float.parseFloat(e.getTextContent()));
    }

    private String calculateToMDL(Element e) {
        return Float.toString(Float.parseFloat(textField3.getText()) * Float.parseFloat(e.getTextContent()));
    }

    private void fillFieldFromMDL(Element e) {
        try {
            textField1.setText(calculateFromMDL(e));
        } catch (NumberFormatException e1) {
            JOptionPane.showMessageDialog(panel1, "Enter a numeric value");
        }
    }

    private void fillFieldToMDL(Element e) {
        try {
            textField4.setText(calculateToMDL(e));
        } catch (NumberFormatException e1) {
            JOptionPane.showMessageDialog(panel1, "Enter a numeric value");
        }
    }

    private Connection getConnection() throws SQLException {
        final String dialect = "mysql";
        final String host = "localhost";
        final int port = 3306;
        final String schema = "banking";
        final String timezone = "UTC";
        final String user = "root";
        final String password = "stasik";
        String address = String.format("jdbc:%s://%s:%d/%s?serverTimezone=%s", dialect, host, port, schema, timezone);
        return DriverManager.getConnection(address, user, password);
    }

    public static void main(String[] args) throws IOException, ParserConfigurationException, SAXException {
        JFrame frame = new JFrame("Banking");
        BasicLookAndFeel lookAndFeel = new DarculaLaf();
        try {
            UIManager.setLookAndFeel(lookAndFeel);
        } catch (UnsupportedLookAndFeelException e) {
            logger.error("Unsupported Look and Feel: " + lookAndFeel.getClass());
        }
        frame.setContentPane(new Banking().panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}