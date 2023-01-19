import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import javax.swing.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

public class App extends GUI{
    /**
     * logger for App class
     */
    final static Logger logger = LogManager.getLogger(App.class);
    public App() {
        try {
            long startTime = System.nanoTime();
            logger.trace("[RUN] Setting country names");
            SettingCountries();
            logger.trace("[RUN] Getting club data");
            GettingClubData();
            logger.trace("[RUN] Getting countries data");
            GettingCountriesData();
            logger.trace("[GUI] Initializing EuropeMap");
            initUI();
            long elapsedTime = System.nanoTime() - startTime;
           logger.info("execution time: "+TimeUnit.SECONDS.convert(elapsedTime, TimeUnit.NANOSECONDS));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    /**
     * entry point to application
     * @param args CLI arguments
     */
    public static void main(String[] args) {
        Runnable r = () -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            App o = new App();
            JFrame f = new JFrame("EUROPE MAP");
            f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            f.setLocationByPlatform(true);
            f.setContentPane(o.getUI());
            f.setResizable(false);
            f.pack();
            f.setVisible(true);
        };
        SwingUtilities.invokeLater(r);

    }
    public void SettingCountries() {
        pathMap.put(
                Collections.unmodifiableList(Arrays.asList(124.0, 14.0)),
                "Islandia"
        );
        pathMap.put(
                Collections.unmodifiableList(Arrays.asList(19.0, 528.0)),
                "Portugalia"
        );
        pathMap.put(
                Collections.unmodifiableList(Arrays.asList(42.0, 514.0)),
                "Hiszpania"
        );
        pathMap.put(
                Collections.unmodifiableList(Arrays.asList(210.0, 640.0)),
                "Hiszpania" //mallorca
        );
        pathMap.put(
                Collections.unmodifiableList(Arrays.asList(157.0, 413.0)),
                "Francja"
        );
        pathMap.put(
                Collections.unmodifiableList(Arrays.asList(317.0, 598.0)),
                "Francja"
        );
        pathMap.put(
                Collections.unmodifiableList(Arrays.asList(158.0, 235.0)),
                "Anglia"
        );
        pathMap.put(
                Collections.unmodifiableList(Arrays.asList(424.0, 361.0)),
                "Polska"
        );
        pathMap.put(
                Collections.unmodifiableList(Arrays.asList(311.0, 366.0)),
                "Niemcy"
        );
        pathMap.put(
                Collections.unmodifiableList(Arrays.asList(301.0, 517.0)),
                "Włochy"
        );
        pathMap.put(
                Collections.unmodifiableList(Arrays.asList(305.0, 634.0)),
                "Włochy"
        );
        pathMap.put(
                Collections.unmodifiableList(Arrays.asList(370.0, 701.0)),
                "Włochy"    //sycylia
        );
        pathMap.put(
                Collections.unmodifiableList(Arrays.asList(267.0, 410.0)),
                "Belgia"
        );
        pathMap.put(
                Collections.unmodifiableList(Arrays.asList(292.0, 375.0)),
                "Holandia"
        );
        pathMap.put(
                Collections.unmodifiableList(Arrays.asList(300.0, 432.0)),
                "Luksemburg"
        );
        pathMap.put(
                Collections.unmodifiableList(Arrays.asList(394.0, 439.0)),
                "Czechy"
        );
        pathMap.put(
                Collections.unmodifiableList(Arrays.asList(114.0, 299.0)),
                "Irlandia"
        );
        pathMap.put(
                Collections.unmodifiableList(Arrays.asList(155.0, 288.0)),
                "Irlandia Północna"
        );
        pathMap.put(
                Collections.unmodifiableList(Arrays.asList(341.0, 83.0)),
                "Norwegia"
        );
        pathMap.put(
                Collections.unmodifiableList(Arrays.asList(399.0, 64.0)),
                "Szwecja"
        );
        pathMap.put(
                Collections.unmodifiableList(Arrays.asList(500.0, 50.0)),
                "Finlandia"
        );
        pathMap.put(
                Collections.unmodifiableList(Arrays.asList(292.0, 375.0)),
                "Holandia"
        );
        pathMap.put(
                Collections.unmodifiableList(Arrays.asList(300.0, 432.0)),
                "Luksemburg"
        );
        pathMap.put(
                Collections.unmodifiableList(Arrays.asList(394.0, 439.0)),
                "Czechy"
        );
        pathMap.put(
                Collections.unmodifiableList(Arrays.asList(267.0, 410.0)),
                "Belgia"
        );
        pathMap.put(
                Collections.unmodifiableList(Arrays.asList(292.0, 375.0)),
                "Holandia"
        );
        pathMap.put(
                Collections.unmodifiableList(Arrays.asList(300.0, 432.0)),
                "Luksemburg"
        );
        pathMap.put(
                Collections.unmodifiableList(Arrays.asList(394.0, 439.0)),
                "Czechy"
        );
        pathMap.put(
                Collections.unmodifiableList(Arrays.asList(358.0, 294.0)),
                "Dania"
        );
        pathMap.put(
                Collections.unmodifiableList(Arrays.asList(341.0, 483.0)),
                "Austria"
        );
        pathMap.put(
                Collections.unmodifiableList(Arrays.asList(301.0, 498.0)),
                "Szwajcaria"
        );
        pathMap.put(
                Collections.unmodifiableList(Arrays.asList(412.0, 521.0)),
                "Słowenia"
        );
        pathMap.put(
                Collections.unmodifiableList(Arrays.asList(422.0, 534.0)),
                "Chorwacja"
        );
        pathMap.put(
                Collections.unmodifiableList(Arrays.asList(434.0, 560.0)),
                "Bośnia i Hercegowina"
        );
        pathMap.put(
                Collections.unmodifiableList(Arrays.asList(466.0, 600.0)),
                "Czarnogóra"
        );
        pathMap.put(
                Collections.unmodifiableList(Arrays.asList(485.0, 619.0)),
                "Albania"
        );
        pathMap.put(
                Collections.unmodifiableList(Arrays.asList(514.0, 706.0)),
                "Grecja"
        );
        pathMap.put(
                Collections.unmodifiableList(Arrays.asList(500.0, 637.0)),
                "Grecja"    //podzielila sie na 2idk
        );
        pathMap.put(
                Collections.unmodifiableList(Arrays.asList(511.0, 631.0)),
                "Macedonia Północna"
        );
        pathMap.put(
                Collections.unmodifiableList(Arrays.asList(482.0, 543.0)),
                "Serbia"
        );
        pathMap.put(
                Collections.unmodifiableList(Arrays.asList(498.0, 604.0)),
                "Kosowo"
        );
        pathMap.put(
                Collections.unmodifiableList(Arrays.asList(534.0, 579.0)),
                "Bułgaria"
        );
        pathMap.put(
                Collections.unmodifiableList(Arrays.asList(504.0, 495.0)),
                "Rumunia"
        );
        pathMap.put(
                Collections.unmodifiableList(Arrays.asList(590.0, 486.0)),
                "Mołdawia"
        );
        pathMap.put(
                Collections.unmodifiableList(Arrays.asList(449.0, 491.0)),
                "Węgry"
        );
        pathMap.put(
                Collections.unmodifiableList(Arrays.asList(454.0, 471.0)),
                "Słowacja"
        );
        pathMap.put(
                Collections.unmodifiableList(Arrays.asList(534.0, 393.0)),
                "Ukraina"
        );
        pathMap.put(
                Collections.unmodifiableList(Arrays.asList(540.0, 335.0)),
                "Białoruś"
        );
        pathMap.put(
                Collections.unmodifiableList(Arrays.asList(497.0, 355.0)),
                "Rosja"
        );
        pathMap.put(
                Collections.unmodifiableList(Arrays.asList(509.0, 316.0)),
                "Litwa"
        );
        pathMap.put(
                Collections.unmodifiableList(Arrays.asList(514.0, 300.0)),
                "Łotwa"
        );
        pathMap.put(
                Collections.unmodifiableList(Arrays.asList(562.0, 1.0)),
                "Rosja"
        );
        pathMap.put(
                Collections.unmodifiableList(Arrays.asList(204.0, 575.0)),
                "Andora"
        );
        pathMap.put(
                Collections.unmodifiableList(Arrays.asList(536.0, 257.0)),
                "Estonia"
        );
        pathMap.put(
                Collections.unmodifiableList(Arrays.asList(392.0, 336.0)),
                "Dania"
        );
        pathMap.put(
                Collections.unmodifiableList(Arrays.asList(379.0, 338.0)),
                "Dania"
        );


    }
}
