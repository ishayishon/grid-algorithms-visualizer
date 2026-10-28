import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Ex3 extends JFrame {


    public static void main(String[] args) {
        int[][] map1 = {
                {3, 1, 3, 1, 1, 3, 2, 3, 3, 3, 3, 1, 0, 2, 3, 2, 0, 2, 0, 3},
                {0, 2, 1, 3, 2, 2, 2, 3, 2, 1, 3, 1, 1, 3, 0, 0, 0, 0, 1, 1},
                {2, 0, 0, 3, 3, 3, 2, 3, 1, 2, 3, 1, 0, 0, 0, 3, 2, 2, 0, 2},
                {1, 0, 3, 3, 3, 1, 2, 1, 2, 3, 2, 1, 0, 3, 0, 0, 2, 1, 1, 0},
                {0, 1, 3, 1, 2, 0, 2, 0, 2, 1, 3, 2, 0, 3, 0, 2, 1, 3, 3, 2},
                {1, 1, 3, 1, 0, 3, 0, 3, 2, 0, 3, 1, 0, 0, 0, 2, 1, 3, 1, 3},
                {1, 1, 0, 1, 2, 1, 2, 0, 0 ,0, 2, 2, 2, 0, 0, 0, 0, 2, 3, 2},
                {2, 2, 1, 2, 1, 0, 3, 0, 1, 0, 0, 1, 0, 1, 2, 2, 0, 1, 0, 1},
                {2, 0, 2, 3, 2, 2, 3, 0, 2, 0, 0, 2, 3, 0, 0, 0, 3, 3, 0, 3},
                {0, 2, 0, 2, 1, 0, 0, 0, 0, 0, 2, 1, 0, 3, 2, 0, 1, 1, 3, 3},
                {1, 0, 2, 0, 1, 1, 0, 0, 3, 0, 1, 3, 3, 1, 1, 3, 1, 1, 0, 3},
                {3, 1, 3, 2, 2, 2, 1, 2, 3, 1, 0, 0, 0, 1, 3, 2, 1, 0, 3, 0},
                {3, 1, 2, 1, 1, 3, 3, 2, 1, 0, 1, 1, 1, 3, 2, 2, 3, 1, 1, 2},
                {2, 3, 0, 0, 2, 2, 1, 0, 3, 0, 0, 2, 2, 1, 1, 2, 3, 0, 0, 1},
                {2, 0, 0, 3, 3, 3, 3, 3, 0, 3, 1, 1, 1, 2, 0, 2, 3, 0, 1, 1},
                {0, 0, 1, 3, 1, 1, 1, 1, 3, 0, 2, 2, 0, 1, 2, 2, 3, 2, 0, 0},
                {0, 0, 1, 0, 0, 0, 1, 0, 2, 2, 2, 3, 2, 3, 1, 0, 3, 1, 1, 0},
                {3, 0, 3, 1, 3, 1, 0, 2, 3, 2, 0, 3, 2, 2, 0, 3, 2, 0, 2, 1},
                {2, 1, 2, 0, 1, 0, 1, 2, 1, 2, 1, 2, 1, 3, 0, 1, 0, 0, 1, 1},
                {0, 1, 1, 1, 1, 1, 0, 2, 1, 3, 2, 1, 1, 3, 0, 0, 0, 0, 0, 1},
                {1, 0, 2, 1, 1, 0, 0, 2, 1, 3, 3, 2, 0, 1, 0, 1, 0, 2, 0, 3}

        };

        Map2D map = new Map(map1); map.setCyclic(false);        //Defining the maps and creating a circular and non-circular map
        Map2D mapCy = new Map(map1); mapCy.setCyclic(true);

        Map2D mapFill = new Map(map1); mapFill.setCyclic(true);
        Map2D mapFillNotCy = new Map(map1); mapFillNotCy.setCyclic(false);

        Map2D number = mapCy.numberOfConnectedComponentsMap(0);
        Map2D numberNoCyclic = map.numberOfConnectedComponentsMap(0);

        Pixel2D start = new Index2D(5, 5);
        Map2D distances = mapCy.allDistance(start, 0);
        Map2D distancesNoCy = map.allDistance(start, 0); distancesNoCy.setCyclic(false);
        distances.setPixel(start, -3);
        distancesNoCy.setPixel(start, -3);


        Map2D mapShortestPathTwoPoints = new Map(map1); mapShortestPathTwoPoints.setCyclic(true);
        Map2D mapShortestPathTwoPointsNoCy = new Map(map1); mapShortestPathTwoPointsNoCy.setCyclic(false);


        Map2D mapShortestPath = new Map(map1); mapShortestPath.setCyclic(true);
        Map2D mapShortestPathNoCy = new Map(map1);  mapShortestPathNoCy.setCyclic(false);

         Pixel2D p1 = new Index2D(2, 5);            //Selecting points and inserting them into the array
         Pixel2D p2 = new Index2D(15, 15);
         Pixel2D p3 = new Index2D(15, 2);
         Pixel2D p4 = new Index2D(19, 3);
         Pixel2D p5 = new Index2D(10, 2);

        Pixel2D[] shortestPathTwoPoints = mapShortestPathTwoPoints.shortestPath(p2, p1, 0);
        Pixel2D[] shortestPathTwoPointsNoCy = mapShortestPathTwoPointsNoCy.shortestPath(p2, p1, 0);

       Pixel2D[] points = new Pixel2D[5];

        points[0] = p1;
        points[1] = p2;
        points[2] = p3;
        points[3] = p4;
        points[4] = p5;


        //Setting a special color for all the pixels of the path
        for (int i=0; i<shortestPathTwoPoints.length; i++){
            mapShortestPathTwoPoints.setPixel(shortestPathTwoPoints[i], -5);
        }
        for (int i=0; i<shortestPathTwoPointsNoCy.length; i++){
            mapShortestPathTwoPointsNoCy.setPixel(shortestPathTwoPointsNoCy[i], -5);
        }


        // Setting a special color for the pixels of the beginning and end of the path
        mapShortestPathTwoPoints.setPixel(p1, -4);
        mapShortestPathTwoPoints.setPixel(p2, -4);

        mapShortestPathTwoPointsNoCy.setPixel(p1, -4);
        mapShortestPathTwoPointsNoCy.setPixel(p2, -4);


        Pixel2D[] shortestPath = mapShortestPath.shortestPath(points,0);
        Pixel2D[] shortestPathNoCy = mapShortestPathNoCy.shortestPath(points,0);

        //Setting a special color for the pixels  need to reach and a special color for the path pixels
        for (int i=0; i<shortestPath.length; i++){
            mapShortestPath.setPixel(shortestPath[i], -5);
        };
        for (int i=0; i<points.length; i++){
            mapShortestPath.setPixel(points[i], -4);
        };

        for (int i=0; i<shortestPathNoCy.length; i++){
            mapShortestPathNoCy.setPixel(shortestPathNoCy[i], -5);
        };
        for (int i=0; i<points.length; i++){
            mapShortestPathNoCy.setPixel(points[i], -4);
        };

        SwingUtilities.invokeLater(() -> new Ex3 (map, mapShortestPath, mapShortestPathTwoPoints, mapFill, number, distances, numberNoCyclic,
                                                    mapShortestPathNoCy, mapShortestPathTwoPointsNoCy, mapFillNotCy, distancesNoCy));
    }

    public Ex3(Map2D map, Map2D mapShortestPath, Map2D mapShortestPathTwoPoints, Map2D mapFill, Map2D number, Map2D distances,Map2D numberNoCyclic,
               Map2D mapShortestPathNoCy, Map2D mapShortestPathTwoPointsNoCy, Map2D mapFillNotCy,Map2D distancesNoCy) {

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setTitle("Grid Algorithms Visualizer");


        //Creating map tabs

        JTabbedPane tabbedPane = new JTabbedPane();

        JPanel panel1 = new JPanel();
        drawMat(map, panel1,false);
        tabbedPane.addTab("Map", panel1);

        JPanel panel2 = new JPanel(); distances.setCyclic(true);
        drawMatDistance(distances, panel2, true);
        tabbedPane.addTab("Distances (cyclic)", panel2);

        JPanel panel3 = new JPanel();
        drawMatDistance(distancesNoCy, panel3, true);
        tabbedPane.addTab("Distances (bounded)", panel3);

        JPanel panel4 = new JPanel();
        drawMat(mapShortestPath, panel4, true);
        tabbedPane.addTab("Multi-stop route (cyclic)", panel4);

        JPanel panel5 = new JPanel();
        drawMat(mapShortestPathNoCy, panel5, true);
        tabbedPane.addTab("Multi-stop route (bounded)", panel5);

        JPanel panel6 = new JPanel();
        drawMat(mapShortestPathTwoPoints, panel6, true);
        tabbedPane.addTab("Shortest path (cyclic)", panel6);

        JPanel panel7 = new JPanel();
        drawMat(mapShortestPathTwoPointsNoCy, panel7, true);
        tabbedPane.addTab("Shortest path (bounded)", panel7);

        JPanel panel8 = createFillPanel(mapFill);
        tabbedPane.addTab("Fill (cyclic)", panel8);
        JPanel panel9 = createFillPanel(mapFillNotCy);

        JPanel panel10 = new JPanel();
        drawMat(number, panel10, true);
        tabbedPane.addTab("Components (cyclic)", panel10);


        JPanel panel11 = new JPanel();
        drawMat(numberNoCyclic, panel11, true);
        tabbedPane.addTab("Components (bounded)", panel11);


        tabbedPane.addTab("Fill (bounded)", panel9);
        getContentPane().add(tabbedPane);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private JPanel createFillPanel(Map2D map) {
        int[][] initial = map.getMap();
        JPanel panel = new JPanel(new BorderLayout());
        JPanel grid = new JPanel();
        JPanel controls = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton fill = new JButton("Fill region");
        JButton reset = new JButton("Reset");
        JLabel status = new JLabel("Fill the preset connected area, or reset the example.");
        controls.add(fill);
        controls.add(reset);
        drawMatFill(map, grid, false);
        fill.addActionListener(event -> {
            int count = map.fill(new Index2D(5, 12), 6);
            grid.removeAll();
            drawMatFill(map, grid, false);
            grid.revalidate();
            grid.repaint();
            status.setText("Filled " + count + " cells");
        });
        reset.addActionListener(event -> {
            map.init(initial);
            grid.removeAll();
            drawMatFill(map, grid, false);
            grid.revalidate();
            grid.repaint();
            status.setText("Example reset");
        });
        panel.add(controls, BorderLayout.NORTH);
        panel.add(grid, BorderLayout.CENTER);
        panel.add(status, BorderLayout.SOUTH);
        return panel;
    }

    private void fillMap(Map2D mapFill, JPanel panel8) {
        drawMatFill(mapFill, panel8, false);}


    private void drawMat(Map2D map, JPanel panel, boolean showValue) {
        int rows = map.getHeight();
        int cols = map.getWidth();

        panel.setLayout(new GridLayout(rows, cols));

        // Define border color
        Color borderColor = Color.BLACK;

        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < cols; x++) {
                JPanel pixelPanel = new JPanel();
                if (map.getPixel(x, y) == 0 || map.getPixel(x, y) == -1) {
                    pixelPanel.setBackground(Color.GRAY);
                } else if (map.getPixel(x, y) == 1) {
                    pixelPanel.setBackground(Color.GREEN);
                } else if (map.getPixel(x, y) == 2) {
                    pixelPanel.setBackground(Color.YELLOW);
                } else if (map.getPixel(x, y) == 3) {
                    pixelPanel.setBackground(Color.ORANGE);
                } else if (map.getPixel(x, y) == 4) {
                    pixelPanel.setBackground(Color.LIGHT_GRAY);
                } else if (map.getPixel(x, y) == 5) {
                    pixelPanel.setBackground(Color.CYAN);
                } else if (map.getPixel(x, y) == 6) {
                    pixelPanel.setBackground(Color.RED);
                } else if (map.getPixel(x, y) == -4) {
                    pixelPanel.setBackground(Color.BLUE);
                } else if (map.getPixel(x, y) == -5) {
                    pixelPanel.setBackground(Color.PINK);
                } else {
                    pixelPanel.setBackground(Color.WHITE);
                }
                pixelPanel.setBorder(BorderFactory.createLineBorder(borderColor)); // Add border
                panel.add(pixelPanel);

                // Check if to display the value
                if (showValue) {
                    JLabel valueLabel = new JLabel(Integer.toString(map.getPixel(x, y)));
                    pixelPanel.add(valueLabel);
                }
            }
        }
    }

        //Functions to display the map according to the need of each function
        private void drawMatDistance(Map2D map, JPanel panel, boolean showValue) {
            int rows = map.getHeight();
            int cols = map.getWidth();

            panel.setLayout(new GridLayout(rows, cols));

            // Define border color
            Color borderColor = Color.BLACK;

            for (int y = 0; y < rows; y++) {
                for (int x = 0; x < cols; x++) {
                    JPanel pixelPanel = new JPanel();
                    if (map.getPixel(x,y) == -1) {
                        pixelPanel.setBackground(Color.GRAY);
                    } else if (map.getPixel(x,y) == -3) {
                        pixelPanel.setBackground(Color.BLUE);
                    } else {
                        pixelPanel.setBackground(Color.GREEN);
                    }
                    pixelPanel.setBorder(BorderFactory.createLineBorder(borderColor)); // Add border

                    panel.add(pixelPanel);
                    // Check if to display the value
                    if (showValue) {
                        JLabel valueLabel = new JLabel(Integer.toString(map.getPixel(x,y)));
                        pixelPanel.add(valueLabel);
                    }
                }
            }

    }

    private void drawMatFill(Map2D mapFill, JPanel panel , boolean showValue) {
        int rows = mapFill.getHeight();
        int cols = mapFill.getWidth();

        panel.setLayout(new GridLayout(rows, cols));

        // Define border color
        Color borderColor = Color.BLACK;

        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < cols; x++) {
                JPanel pixelPanel = new JPanel();
                if (mapFill.getPixel(x, y) == 0) {
                    pixelPanel.setBackground(Color.GRAY);
                } else if (mapFill.getPixel(x, y) == 1) {
                    pixelPanel.setBackground(Color.GREEN);
                } else if (mapFill.getPixel(x, y) == 2) {
                    pixelPanel.setBackground(Color.YELLOW);
                } else if (mapFill.getPixel(x, y) == 3) {
                    pixelPanel.setBackground(Color.ORANGE);
                } else if (mapFill.getPixel(x, y) == 4) {
                    pixelPanel.setBackground(Color.LIGHT_GRAY);
                } else if (mapFill.getPixel(x, y) == 5) {
                    pixelPanel.setBackground(Color.CYAN);
                } else if (mapFill.getPixel(x, y) == 6) {
                    pixelPanel.setBackground(Color.RED);
                } else if (mapFill.getPixel(x, y) == -4) {
                    pixelPanel.setBackground(Color.BLUE);
                } else if (mapFill.getPixel(x, y) == -5) {
                    pixelPanel.setBackground(Color.PINK);
                } else {
                    pixelPanel.setBackground(Color.WHITE);
                }
                pixelPanel.setBorder(BorderFactory.createLineBorder(borderColor)); // Add border

                panel.add(pixelPanel);
                // Check if to display the value
                if (showValue) {
                    JLabel valueLabel = new JLabel(Integer.toString(mapFill.getPixel(x, y)));
                    pixelPanel.add(valueLabel);
                }
            }
        }
    }
}
