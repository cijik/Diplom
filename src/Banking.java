import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.IOException;


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

        Element rateUsd = doc.select("td#money-trade-buy-usd").first();
        Element rateEur = doc.select("td#money-trade-buy-eur").first();
        Element rateRub = doc.select("td#money-trade-buy-rub").first();
        Element rateRon = doc.select("td#money-trade-buy-ron").first();
        Element rateUah = doc.select("td#money-trade-buy-uah").first();
        Element rateGbp = doc.select("td#money-trade-buy-gbp").first();
        comboBox1.addItemListener((ItemEvent e) -> {
                if (e.getItem() == "USD") {
                    textField1.setText(Float.toString(Float.parseFloat(textField2.getText()) / Float.parseFloat(rateUsd.text())));
                }
                if (e.getItem() == "EUR") {
                    textField1.setText(Float.toString(Float.parseFloat(textField2.getText()) / Float.parseFloat(rateEur.text())));
                }
                if (e.getItem() == "RUB") {
                    textField1.setText(Float.toString(Float.parseFloat(textField2.getText()) / Float.parseFloat(rateRub.text())));
                }
                if (e.getItem() == "RON") {
                    textField1.setText(Float.toString(Float.parseFloat(textField2.getText()) / Float.parseFloat(rateRon.text())));
                }
                if (e.getItem() == "UAH") {
                    textField1.setText(Float.toString(Float.parseFloat(textField2.getText()) / Float.parseFloat(rateUah.text())));
                }
                if (e.getItem() == "GBP") {
                    textField1.setText(Float.toString(Float.parseFloat(textField2.getText()) / Float.parseFloat(rateGbp.text())));
                }

        });
    }

    public static void main(String[] args) throws IOException {
        JFrame frame = new JFrame("Banking");
        frame.setContentPane(new Banking().panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}