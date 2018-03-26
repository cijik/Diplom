import com.bulenkov.darcula.DarculaLaf;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import javax.swing.*;
import javax.swing.plaf.basic.BasicLookAndFeel;
import java.awt.event.ItemEvent;
import java.io.IOException;
import java.util.ArrayList;

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

    private Banking() throws IOException {

        setTitle("BankingApp");
        setSize(100, 500);

        //panel1.setLayout(new GridLayout(1, 0, 5, 1));


        Document doc = Jsoup.connect("https://point.md/ru/").get();

        ArrayList<Element> rates = new ArrayList<>();
        rates.add(doc.select("td#money-trade-buy-usd").first());
        rates.add(doc.select("td#money-trade-buy-eur").first());
        rates.add(doc.select("td#money-trade-buy-rub").first());
        rates.add(doc.select("td#money-trade-buy-ron").first());
        rates.add(doc.select("td#money-trade-buy-uah").first());
        rates.add(doc.select("td#money-trade-buy-gbp").first());

        comboBox1.addItemListener((ItemEvent e) -> {
            switch((String)e.getItem()){
                case "USD":
                    fillFieldFromMDL(rates.get(0));break;
                case "EUR":
                    fillFieldFromMDL(rates.get(1));break;
                case "RUB":
                    fillFieldFromMDL(rates.get(2));break;
                case "RON":
                    fillFieldFromMDL(rates.get(3));break;
                case "UAH":
                    fillFieldFromMDL(rates.get(4));break;
                case "GBP":
                    fillFieldFromMDL(rates.get(5));break;
            }
        });
        comboBox3.addItemListener((ItemEvent e) ->{
            switch((String)e.getItem()){
                case "USD":
                    fillFieldToMDL(rates.get(0));break;
                case "EUR":
                    fillFieldToMDL(rates.get(1));break;
                case "RUB":
                    fillFieldToMDL(rates.get(2));break;
                case "RON":
                    fillFieldToMDL(rates.get(3));break;
                case "UAH":
                    fillFieldToMDL(rates.get(4));break;
                case "GBP":
                    fillFieldToMDL(rates.get(5));break;
            }
        });

    }

    private String calculateFromMDL(Element e){
        return Float.toString(Float.parseFloat(textField2.getText()) / Float.parseFloat(e.text()));
    }

    private String calculateToMDL(Element e){
        return Float.toString(Float.parseFloat(textField3.getText()) * Float.parseFloat(e.text()));
    }

    private void fillFieldFromMDL(Element e){
        textField1.setText(calculateFromMDL(e));
    }

    private void fillFieldToMDL(Element e){
        textField4.setText(calculateToMDL(e));
    }

    public static void main(String[] args) throws IOException {
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