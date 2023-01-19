import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.UnknownHostException;
import java.util.*;

public class Data{
    /**
     * logger for Data class
     */
    final static Logger logger = LogManager.getLogger(Data.class);
    protected int count = 0;
    protected Object[][] newData;
    protected static final Object[] columnNamesClubs = {"Poz.", "Klub", "Kraj", "2018/2019", "2019/2020", "2020/2021", "2021/2022", "2022/2023", "Razem", "Krajowy"};
    protected String countryName;
    public void GettingClubData() {
        String url = "";
        Elements table = null;
        Document doc;
        try {
            url = "http://www.90minut.pl/ranking_uefa.php?i=1&id_sezon=101";
            logger.info("[RUN] Trying connect to: " + url);
            logger.info("Getting the content");
            doc = Jsoup.connect(url).get();
            table = doc.select("table");
        } catch (UnknownHostException e) {
            logger.error("[RUN] Cannot connect to: " + url);
        } catch (IOException e) {
            logger.error("[RUN] RuntimeException");
            throw new RuntimeException(e);
        }
        String filename = "dataClubs.csv";
        File file = new File(filename);
        PrintWriter writer = null;
        try {
            writer = new PrintWriter(file);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        logger.info("Saving data to: " + filename);
        if (table != null) {
            for (Element row : table.select("tr")) {
                Elements cells = row.select("td");
                Elements cells1 = row.select("img[title]");
                String position = cells.get(0).text();
                String club = cells.get(1).text();
                String countryName = cells1.attr("title");
                String y1 = cells.get(3).text().replace(",", ".");
                String y2 = cells.get(4).text().replace(",", ".");
                String y3 = cells.get(5).text().replace(",", ".");
                String y4 = cells.get(6).text().replace(",", ".");
                String y5 = cells.get(7).text().replace(",", ".");
                String sum = cells.get(8).text().replace(",", ".");
                String countryRank = cells.get(9).text().replace(",", ".");
                String line = String.format("%s,%s,%s,%s,%s,%s,%s,%s,%s,%s", position, club, countryName, y1, y2, y3, y4, y5, sum, countryRank);
                writer.println(line);
                count++;
            }
        }
        writer.close();
    }
    /**
     * Checking data with country which was clicked on the map, then adding info to sortable table
     */
    public void CheckingData() throws FileNotFoundException {
        String filename = "dataClubs.csv";
        File file = new File(filename);
        Object[][] data = new Object[count - 1][10];
        int row = -1;
        int lineNumber = 0;
        Scanner scanner = new Scanner(file);
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            lineNumber++;
            if (lineNumber >= 2) {
                row++;
                String[] fields = line.split(",");
                data[row][0] = Integer.parseInt(fields[0]);
                data[row][1] = fields[1]; // club
                data[row][2] = fields[2];//contry
                data[row][3] = Double.parseDouble(fields[3]);
                data[row][4] = Double.parseDouble(fields[4]);
                data[row][5] = Double.parseDouble(fields[5]);
                data[row][6] = Double.parseDouble(fields[6]);
                data[row][7] = Double.parseDouble(fields[7]);
                data[row][8] = Double.parseDouble(fields[8]);
                data[row][9] = Double.parseDouble(fields[9]);
            }
        }
        int j = -1;
        int cntry = 0;
        if (newData != null) {
            for (Object[] newDatum : newData) {
                Arrays.fill(newDatum, null);
            }
        }
        for (row = 0; row < data.length; row++) {
            if (countryName.equals(data[row][2])) {
                cntry++;
            }
        }
        newData = new Object[cntry][10];
        for (row = 0; row < data.length; row++) {
            if (countryName.equals(data[row][2])) {
                j++;
                newData[j][0] = data[row][0];    //poz
                newData[j][1] = data[row][1];    // club
                newData[j][2] = data[row][2];    //country
                newData[j][3] = data[row][3];    //18/19
                newData[j][4] = data[row][4];    //19/20
                newData[j][5] = data[row][5];    //20/21
                newData[j][6] = data[row][6];    //21/22
                newData[j][7] = data[row][7];    //22/23
                newData[j][8] = data[row][8];    //razem
                newData[j][9] = data[row][9];    //krajowy
            }
        }
        scanner.close();
    }

    /**
     * this method is getting data about countries from url then saving data to csv file
     */
    public void GettingCountriesData() throws IOException {
        int dupa = 0;
        String url = "http://www.90minut.pl/ranking_uefa.php";
        Document doc = Jsoup.connect(url).get();
        Elements table = doc.select("table");
        String filename = "dataCountries.csv";
        File file = new File(filename);
        PrintWriter writer = new PrintWriter(file);
        logger.info("Saving data to: " + filename);
        for (Element row : table.select("tr")) {
            Elements cells = row.select("td");
            String position = cells.get(0).text();
            String countryName = cells.get(1).text();
            String year1 = cells.get(2).text().replace(",", ".");    // 18/19
            String year2 = cells.get(3).text().replace(",", ".");    // 19/20
            String year3 = cells.get(4).text().replace(",", ".");    // 20/21
            String year4 = cells.get(5).text().replace(",", ".");    // 21/22
            String year5 = cells.get(6).text().replace(",", ".");    // 22/23
            String sum = cells.get(7).text().replace(",", ".");      //wszystkie sezony
            String team = cells.get(8).text().replace(",", ".");     //ile druzyn
            String line = String.format("%s,%s,%s,%s,%s,%s,%s,%s,%s", position, countryName, year1, year2, year3, year4, year5, sum, team);
            writer.println(line);
            dupa++;
        }
        writer.close();
        Object[] columnNames = {"Poz.", "Kraj", "2018/2019", "2019/2020", "2020/2021", "2021/2022", "2022/2023", "Razem", "Ile drużyn"};
        Object[][] data = new Object[dupa - 1][9];
        int row = -1;
        int lineNumber = 0;
        Scanner scanner = new Scanner(file);
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            lineNumber++;
            if (lineNumber >= 2) {
                row++;
                String[] fields = line.split(",");
                data[row][0] = Integer.parseInt(fields[0]);
                data[row][1] = fields[1];
                data[row][2] = Double.parseDouble(fields[2]);
                data[row][3] = Double.parseDouble(fields[3]);
                data[row][4] = Double.parseDouble(fields[4]);
                data[row][5] = Double.parseDouble(fields[5]);
                data[row][6] = Double.parseDouble(fields[6]);
                data[row][7] = Double.parseDouble(fields[7]);
                data[row][8] = fields[8];
            }
        }
        scanner.close();
        JFrame f = new JFrame();
        f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        f.setTitle("UEFA RANKING");
        DefaultTableModel model = new DefaultTableModel(data, columnNames) {
            @Override
            public Class getColumnClass(int column) {
                return switch (column) {
                    case 0 -> Integer.class;
                    case 2, 3, 4, 5, 6, 7 -> Double.class;
                    default -> String.class;
                };
            }
            @Override
            public boolean isCellEditable(int row, int column) {    //turing to non-editable
                return false;
            }
        };
        JTable table1 = new JTable(model);
        JScrollPane scroll = new JScrollPane(table1);
        table1.getTableHeader().setForeground(Color.white);
        table1.getTableHeader().setBackground(Color.black);
        table1.setAutoCreateRowSorter(true);
        f.add(scroll);
        f.setSize(900, 900);
        f.setVisible(true);
    }
    /**
     * this method is setting country name for (x,y) coordinates to hashmap
     */

}
