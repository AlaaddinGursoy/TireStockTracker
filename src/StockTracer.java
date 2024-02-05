import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class StockTracer {

    private JPanel mainPanel;
    private JTextField textField1;
    private JButton araButton;
    private JTable table1;
    private JButton kaydetButton;
    private JButton tumunuGosterButton;
    private JButton varsayilanButton;
    private JTextField textField2;
    private JButton ekleButton;
    private JTextField field1;
    private JTextField field2;
    private JTextField field3;
    private JTextField field4;
    private JTextField field5;
    private JLabel LabelToplamLastik;
    private JTextField textField3;
    private JLabel SonKaydedilme;
    private JButton sifirlaButton;
    private DefaultTableModel tableModel;

    Stocks stocks;

    void stocksCreator(){

        try {
            ObjectInputStream ois=new ObjectInputStream(new FileInputStream("Stoklar.bin"));
            try {
                stocks= (Stocks) ois.readObject();
                SonKaydedilme.setText(""+ois.readObject());
                ois.close();
            } catch (ClassNotFoundException e) {
                SonKaydedilme.setText("Son Kaydedilme:--/--/---- ----");
                stocks= new Stocks();
                ois.close();
            }


        } catch (IOException e) {
            SonKaydedilme.setText("Son Kaydedilme:--/--/---- ----");
            stocks= new Stocks();
        }


    }


    public StockTracer() {

        stocksCreator();

        tableModel = new DefaultTableModel(){
            @Override
            public boolean isCellEditable(int row, int column) {

                return column >=3;// Make all cells non-editable
            }
        };
        table1.setModel(tableModel);
        fillTable(stocks);
        LabelToplamLastik.setText("Toplam Lastik:"+stocks.tireCount+"  ");



        JFrame frame = new JFrame("Stok Takibi");
        frame.setContentPane(mainPanel);
        frame.setSize(960, 600);
        frame.setMinimumSize(new Dimension(720,500));
        frame.setVisible(true);
        frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                Object[] options = {"Çık", "İptal"};
                int result = JOptionPane.showOptionDialog(frame,
                        "Çıkmak istediğinize emin misiniz?\n(Kaydetmediğiniz veriler geçersiz olacaktir!)", "Çıkışı onayla",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE,
                        null,
                        options,
                        options[0]);
                if (result == JOptionPane.YES_OPTION) {

                    System.exit(0);
                }
            }
        });


        araButton.addActionListener(e -> {
            String text1 = textField1.getText();
            String text2 = textField2.getText();

            if(text1.isEmpty() && text2.isEmpty())
                return;

            tableModel = new DefaultTableModel(){
                @Override
                public boolean isCellEditable(int row, int column) {

                    return column >=3;
                }
            };
            table1.setModel(tableModel);

            try {
                Search(stocks, text1, text2);
            }catch (Exception ignored){

            }
        });

        tumunuGosterButton.addActionListener(e -> {

            textField1.setText("");
            textField2.setText("");

            tableModel = new DefaultTableModel(){
                @Override
                public boolean isCellEditable(int row, int column) {

                    return column >=3;
                }
            };
            table1.setModel(tableModel);
            fillTable(stocks);
            LabelToplamLastik.setText("Toplam Lastik:"+stocks.tireCount+"  ");

        });

        varsayilanButton.addActionListener(e -> {


            try {
                for (int j=0;j<table1.getRowCount();j++) {

                    for (Tire tire: stocks.tires) {
                        if(table1.getValueAt(j,0).toString().equalsIgnoreCase(tire.tireSize+"      "+tire.series)
                                &&table1.getValueAt(j,1).toString().equalsIgnoreCase(tire.getTireSeason())
                                &&table1.getValueAt(j,2).toString().equalsIgnoreCase(tire.getBrand())
                                &&(tire.productionYear==0 ||(table1.getValueAt(j,3).toString().equals("-")?0:Integer.parseInt(table1.getValueAt(j, 3).toString()))==(tire.getProductionYear()))) {

                            stocks.tireCount-=tire.quantity-Integer.parseInt(table1.getValueAt(j,4).toString());
                            tire.quantity=Integer.parseInt(table1.getValueAt(j,4).toString());



                            if(!table1.getValueAt(j, 3).equals("-") && tire.productionYear==0)
                                tire.productionYear = Integer.parseInt(table1.getValueAt(j, 3).toString());



                            if(tire.quantity<=0) {
                                stocks.tires.remove(tire);
                                tableModel = new DefaultTableModel(){
                                    @Override
                                    public boolean isCellEditable(int row, int column) {

                                        return column >=3;
                                    }
                                };
                                table1.setModel(tableModel);
                                fillTable(stocks);
                                LabelToplamLastik.setText("Toplam Lastik:"+stocks.tireCount+"  ");
                            }

                            break;
                        }
                    }

                }
                int sum=0;

                for (int i=0;i<table1.getRowCount();i++){
                    sum+=Integer.parseInt(table1.getValueAt(i,4).toString());
                }
                LabelToplamLastik.setText("Toplam Lastik:"+sum+"  ");
            } catch (NumberFormatException ex) {
                throw new RuntimeException(ex);
            }


        });
        ekleButton.addActionListener(e -> {


            try {
                int stocksBeforeAdd=stocks.tireCount;

                String ebat=(field1.getText().isEmpty())?"":field1.getText().toUpperCase();

                String mevsim=(field2.getText().isEmpty())?"":field2.getText().toUpperCase();


                String s2=(textField3.getText().isEmpty())?"":textField3.getText().toUpperCase();
                String s4 =(field3.getText().isEmpty())?"": field3.getText().toUpperCase();//marka

                if(!s4.isEmpty()){
                    String s4Adv = s4.charAt(0) + "";

                    for (int i = 1; i < s4.length(); i++) {
                        if (s4.charAt(i - 1) != ' ')
                            s4Adv += (s4.charAt(i) + "").toLowerCase();
                        else
                            s4Adv += (s4.charAt(i) + "");
                    }

                    s4 = s4Adv;
                }

                int s5 = (field4.getText().isEmpty())?0:Integer.parseInt(field4.getText());//yil
                int s6= (field5.getText().isEmpty())?0:Integer.parseInt(field5.getText());//miktar

                if(s5<0 || s5>9999 || s6<=0 || ebat.isEmpty() || mevsim.isEmpty()) {
                    ekleAnimation("Eklenemedi!");
                    return;
                }


                stocks.addTire(ebat,s2, mevsim,s4,s5,s6);

                int stocksAfterAdd=stocks.tireCount;


                ekleAnimation((stocksAfterAdd>stocksBeforeAdd)?"Eklendi":"Eklenemedi!");


                tableModel = new DefaultTableModel(){
                    @Override
                    public boolean isCellEditable(int row, int column) {

                        return column >=3;
                    }
                };
                table1.setModel(tableModel);
                fillTable(stocks);

                LabelToplamLastik.setText("Toplam Lastik:"+stocks.tireCount+"  ");
            } catch (NumberFormatException ex) {
                throw new RuntimeException(ex);
            }


            //System.out.println(field1.toString());


        });
        kaydetButton.addActionListener(e -> {



            try {


                ObjectOutputStream oos=new ObjectOutputStream(new FileOutputStream("Stoklar.bin"));
                oos.writeObject(stocks);

                kaydetButton.setText("Kaydedildi");

                LocalDate currentDate = LocalDate.now();
                LocalTime currentTime = LocalTime.now();

                // Tarih formatı
                DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                String formattedDate = currentDate.format(dateFormatter);

                // Saat formatı
                DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
                String formattedTime = currentTime.format(timeFormatter);

                // Sonuç
                String formattedDateTime = formattedDate + " " + formattedTime;

                if(!sifirlaButton.getText().equals("Son Kaydedilme:--/--/---- ----") && !stocks.tires.isEmpty())
                    SonKaydedilme.setText("Son Kaydedilme:"+formattedDateTime);

                oos.writeObject(SonKaydedilme.getText());

                Timer timer=new Timer(2000, actionEvent -> kaydetButton.setText("Kaydet"));
                timer.setRepeats(false);
                timer.start();

                oos.close();



            } catch (IOException ex) {
                kaydetButton.setText("Kaydedilemedi!");

                Timer timer=new Timer(2000, actionEvent -> kaydetButton.setText("Kaydet"));
                timer.setRepeats(false);
                timer.start();
            }
        });
        sifirlaButton.addActionListener(actionEvent -> {
            Object[] options = {"Sıfırla", "İptal"};
            int result = JOptionPane.showOptionDialog(frame,
                    "Sıfırlamak istediğinize emin misiniz?\n(Tüm stok bilgileri silinecektir!)", "Sıfırlamayı onayla",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    options,
                    options[0]);
            if (result == JOptionPane.YES_OPTION) {

                sifirlaButton.setText("Sıfırlandı!");

                Timer timer=new Timer(2000, actionEvent1 -> sifirlaButton.setText("Sıfırla"));
                timer.setRepeats(false);
                timer.start();

                stocks=new Stocks();
                SonKaydedilme.setText("Son Kaydedilme:--/--/---- ----");
                tableModel = new DefaultTableModel(){
                    @Override
                    public boolean isCellEditable(int row, int column) {

                        return column >=3;
                    }
                };
                table1.setModel(tableModel);
                fillTable(stocks);
                LabelToplamLastik.setText("Toplam Lastik:"+stocks.tireCount+"  ");
            }
        });


        Timer timer = new Timer(250, new ActionListener() {
            private int count1 = -1;
            private int count2 = -1;

            @Override
            public void actionPerformed(ActionEvent e) {
                if (count1 != textField1.getText().length() || count2 != textField2.getText().length()) {
                    count1 = textField1.getText().length();
                    count2 = textField2.getText().length();

                    tableModel = new DefaultTableModel() {
                        @Override
                        public boolean isCellEditable(int row, int column) {
                            return column >= 3;
                        }
                    };
                    table1.setModel(tableModel);


                    try {
                        Search(stocks, textField1.getText(), textField2.getText());
                    } catch (Exception ex) {
                        throw new RuntimeException(ex);
                    }

                }
            }
        });

        timer.start();






    }

    void ekleAnimation(String text){

        ekleButton.setText(text);

        Timer timer=new Timer(2000, actionEvent -> ekleButton.setText("Ekle"));
        timer.setRepeats(false);
        timer.start();



    }


    void Search(Stocks stocks,String text1,String text2) {

        tableModel.addColumn("Lastik Ebatı");
        tableModel.addColumn("Mevsim");
        tableModel.addColumn("Marka");
        tableModel.addColumn("Uretim Yılı");
        tableModel.addColumn("Adet");

        for (Tire tire : stocks.tires) {

            String helper="";

            for(int i=0;i<tire.tireSize.length();i++) {
                if(tire.tireSize.charAt(i)>='0' && tire.tireSize.charAt(i)<='9')
                    helper += tire.tireSize.charAt(i);
            }

            String valid="";
            for (int i=0;i<text1.length();i++){

                if(text1.charAt(i)>='0' && text1.charAt(i)<='9')
                    valid+=text1.charAt(i);

            }


            helper=helper.substring(0, Math.min(helper.length(), valid.length()));

            if(tire.getBrand().length()<text2.length())
                continue;

            if(!text1.isEmpty() && !helper.contains(valid))
                continue;
            if(!text2.isEmpty() && tire.getBrand().length()>=text2.length() && !tire.getBrand().toUpperCase().substring(0,text2.length()).contains(text2.toUpperCase()))
                continue;

            tableModel.addRow(new Object[]{
                    tire.tireSize+"      "+tire.series,
                    tire.getTireSeason(),
                    tire.getBrand(),
                    tire.getProductionYear()==0?"-":tire.getProductionYear(),
                    tire.getQuantity()
            });

        }

        int sum=0;

        for (int i=0;i<table1.getRowCount();i++){
            sum+=Integer.parseInt(table1.getValueAt(i,4).toString());
        }
        LabelToplamLastik.setText("Toplam Lastik:"+sum+"  ");


    }
    void fillTable(Stocks stocks) {
        tableModel.addColumn("Lastik Ebatı");
        tableModel.addColumn("Mevsim");
        tableModel.addColumn("Marka");
        tableModel.addColumn("Uretim Yılı");
        tableModel.addColumn("Adet");

        for (Tire tire : stocks.tires) {
            tableModel.addRow(new Object[]{
                    tire.tireSize+"      "+tire.series,
                    tire.getTireSeason(),
                    tire.getBrand(),
                    tire.getProductionYear()==0?"-":tire.getProductionYear(),
                    tire.getQuantity()
            });
        }
    }


    public static void main(String[] args) {
        new StockTracer();


    }
}
