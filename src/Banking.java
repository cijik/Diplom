import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import javax.swing.*;
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

    private Banking() throws IOException {

        setTitle("BankingApp");
        setSize(500, 500);
        try {
            UIManager.setLookAndFeel(UIManager
                    .getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

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
                case "USD":fillField(rates.get(0));break;
                case "EUR":fillField(rates.get(1));break;
                case "RUB":fillField(rates.get(2));break;
                case "RON":fillField(rates.get(3));break;
                case "UAH":fillField(rates.get(4));break;
                case "GBP":fillField(rates.get(5));break;
            }
        });
    }

    private String calculate(Element e){
        return Float.toString(Float.parseFloat(textField2.getText()) / Float.parseFloat(e.text()));
    }

    private void fillField(Element e){
        textField1.setText(calculate(e));
    }

    public static void main(String[] args) throws IOException {
        JFrame frame = new JFrame("Banking");
        frame.setContentPane(new Banking().panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}