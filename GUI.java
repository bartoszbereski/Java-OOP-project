import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.imageio.IIOException;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Area;
import java.awt.geom.GeneralPath;
import java.awt.geom.PathIterator;
import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class GUI extends Data{
    protected static final int SIZE = 750;
    protected JFrame f = new JFrame();
    protected JComponent ui = null;
    /**
     * logger for GUI class
     */
    final static Logger logger = LogManager.getLogger(GUI.class);
    protected BufferedImage image;
    protected Area area;
    protected ArrayList<Shape> shapeList;
    protected boolean firstFrame = true;
    protected final HashMap<List<Double>, String> pathMap = new HashMap<>();
    protected final JLabel output = new JLabel();
    /**
     * Initializing the user interface of the program
     */
    public final void initUI() throws Exception {
        if (ui != null) {
            return;
        }
        URL url = new URL("https://preview.redd.it/zjx2bu1q7fi51.png?width=960&crop=smart&auto=webp&s=e6b2ce960b45fbda866cafce4227566f0564d9ed");
        logger.info("[RUN] Connecting to map image: " + url);
        try {
            image = ImageIO.read(url);
        }catch (IIOException exception){
            logger.error("Can't connect: " + url);
        }
        area = getOutline(Color.WHITE, image, 12);
        shapeList = separateShapeIntoRegions(area);
        ui = new JPanel(new BorderLayout(4, 4));
        ui.setBorder(new EmptyBorder(4, 4, 4, 4));
        output.addMouseMotionListener(new GUI.MousePositionListener());
        ui.add(output);
        refresh();
        output.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Point p = e.getPoint();
                for (Shape shape : shapeList) {
                    if (shape.contains(p)) {
                        //System.out.println(shape.getBounds().getX()+"Y: "+ shape.getBounds().getY());
                        if (pathMap.get(Arrays.asList(shape.getBounds().getX(), shape.getBounds().getY())) == null) {
                            logger.warn("[RUN] Country does not exist");
                        } else {
                            countryName = pathMap.get(Arrays.asList(shape.getBounds().getX(), shape.getBounds().getY()));
                            logger.info("[RUN] Current country: " + countryName);
                            try {
                                logger.info("[RUN] Checking country info for: " + countryName);
                                CheckingData();
                            } catch (FileNotFoundException ex) {
                                throw new RuntimeException(ex);
                            }
                            if (firstFrame) {
                                createFrame();
                                firstFrame = false;
                            } else {
                                f.dispose();   //closing old frame before creating new one
                                createFrame();
                            }
                        }
                        break;
                    }
                }
            }
        });
    }
    /**
     * Returns object representing the outline or shape of the area of the image that has the target color
     */
    public Area getOutline(Color target, BufferedImage bi, int tolerance) {
        // construct the GeneralPath
        GeneralPath gp = new GeneralPath();

        boolean cont = false;
        for (int xx = 0; xx < bi.getWidth(); xx++) {
            for (int yy = 0; yy < bi.getHeight(); yy++) {
                if (isIncluded(new Color(bi.getRGB(xx, yy)), target, tolerance)) {
                    if (cont) {
                        gp.lineTo(xx, yy);
                        gp.lineTo(xx, yy + 1);
                        gp.lineTo(xx + 1, yy + 1);
                        gp.lineTo(xx + 1, yy);
                        gp.lineTo(xx, yy);
                    } else {
                        gp.moveTo(xx, yy);
                    }
                    cont = true;
                } else {
                    cont = false;
                }
            }
            cont = false;
        }
        gp.closePath();

        // construct the Area from the GP & return it
        return new Area(gp);
    }
    /**
     * Creates a frame which contain table with information about clubs from Europe
     */
    public void createFrame() {
        EventQueue.invokeLater(() -> {
            logger.info("[RUN] Creating frame for " + countryName);
            f = new JFrame(countryName);
            f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            f.setTitle(countryName + " ranking");
            DefaultTableModel model = new DefaultTableModel(newData, columnNamesClubs) {
                @Override
                public boolean isCellEditable(int row, int column) {    //turing to non-editable
                    return false;
                }
                @Override
                public Class getColumnClass(int column) {               //sorting table by value
                    return switch (column) {
                        case 0 -> Integer.class;
                        case 3, 4, 5, 6, 7, 8, 9 -> Double.class;
                        default -> String.class;
                    };
                }
            };
            JTable table1 = new JTable(model);
            JScrollPane scroll = new JScrollPane(table1);
            table1.getColumnModel().getColumn(1).setPreferredWidth(160);
            table1.getTableHeader().setForeground(Color.white);
            table1.getTableHeader().setBackground(Color.black);
            table1.setAutoCreateRowSorter(true);
            f.add(scroll);
            f.setSize(900, 250);
            f.setVisible(true);
        });
    }

    /**
     * @param shape
     * @return returns an ArrayList of Shape objects
     */
    public static ArrayList<Shape> separateShapeIntoRegions(Shape shape) {
        ArrayList<Shape> regions = new ArrayList<>();
        PathIterator pi = shape.getPathIterator(null);
        GeneralPath gp = new GeneralPath();
        while (!pi.isDone()) {
            double[] coords = new double[6];
            int pathSegmentType = pi.currentSegment(coords);
            int windingRule = pi.getWindingRule();
            gp.setWindingRule(windingRule);
            if (pathSegmentType == PathIterator.SEG_MOVETO) {
                gp = new GeneralPath();
                gp.setWindingRule(windingRule);
                gp.moveTo(coords[0], coords[1]);
            } else if (pathSegmentType == PathIterator.SEG_LINETO) {
                gp.lineTo(coords[0], coords[1]);
            } else if (pathSegmentType == PathIterator.SEG_QUADTO) {
                gp.quadTo(coords[0], coords[1], coords[2], coords[3]);
            } else if (pathSegmentType == PathIterator.SEG_CUBICTO) {
                gp.curveTo(
                        coords[0], coords[1],
                        coords[2], coords[3],
                        coords[4], coords[5]);
            } else if (pathSegmentType == PathIterator.SEG_CLOSE) {
                gp.closePath();
                regions.add(new Area(gp));
            } else {
                System.err.println("Unexpected value! " + pathSegmentType);
            }

            pi.next();
        }

        return regions;
    }

    /**
     * Class is to listen for mouse events and refresh the display when the mouse is moved
     */
    class MousePositionListener implements MouseMotionListener {
        @Override
        public void mouseDragged(MouseEvent e) {
            // do nothing
        }

        @Override
        public void mouseMoved(MouseEvent e) {
            refresh();
        }
    }

    /**
     * @param target
     * @param pixel
     * @param tolerance
     * @return returns a boolean value, true if the pixel color is included in the range of the target color and false otherwise
     */
    public static boolean isIncluded(Color target, Color pixel, int tolerance) {
        int rT = target.getRed();
        int gT = target.getGreen();
        int bT = target.getBlue();
        int rP = pixel.getRed();
        int gP = pixel.getGreen();
        int bP = pixel.getBlue();
        return ((rP - tolerance <= rT) && (rT <= rP + tolerance)
                && (gP - tolerance <= gT) && (gT <= gP + tolerance)
                && (bP - tolerance <= bT) && (bT <= bP + tolerance));
    }

    /**
     * method updates the map image on the GUI by creating a new BufferedImage then drawing the original map image on it
     */
    protected void refresh() {
        output.setIcon(new ImageIcon(getImage()));
    }

    /**
     * @return method returns the updated image to be displayed on the GUI.
     */
    protected BufferedImage getImage() {
        BufferedImage bi = new BufferedImage(SIZE, SIZE, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = bi.createGraphics();
        g.drawImage(image, 0, 0, output);
        g.setColor(Color.ORANGE);
        g.fill(area);
        g.setColor(Color.black);
        g.draw(area);
        try {
            Point p = MouseInfo.getPointerInfo().getLocation();
            Point p1 = output.getLocationOnScreen();
            int x = p.x - p1.x;
            int y = p.y - p1.y;
            Point pointOnImage = new Point(x, y);
            for (Shape shape : shapeList) {
                if (shape.contains(pointOnImage)) {
                    g.setColor(Color.DARK_GRAY);
                    g.fill(shape);
                    break;
                }
            }
        } catch (Exception doNothing) {
            //System.out.println("nothnig");
        }

        g.dispose();

        return bi;
    }

    /**
     * @return the JComponent object that represents the user interface of the program
     */
    public JComponent getUI() {
        return ui;
    }
}
