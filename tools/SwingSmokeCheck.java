import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicReference;

public class SwingSmokeCheck {
    private static File output;
    private static JFrame frame;
    private static void capture(String name) throws Exception {
        BufferedImage image=new BufferedImage(frame.getWidth(),frame.getHeight(),BufferedImage.TYPE_INT_RGB);
        Graphics2D g=image.createGraphics();frame.paintAll(g);g.dispose();
        ImageIO.write(image,"png",new File(output,name+".png"));
    }
    private static int visibleCells(Container panel) {
        int n=0;
        for(Component c:panel.getComponents()) {
            if(c instanceof JPanel && ((JPanel)c).getBorder()!=null && c.getWidth()>0 && c.getHeight()>0)n++;
            if(c instanceof Container)n+=visibleCells((Container)c);
        }
        return n;
    }
    private static JButton button(Container parent) {
        for(Component c:parent.getComponents()) {
            if(c instanceof JButton)return (JButton)c;
            if(c instanceof Container){JButton b=button((Container)c);if(b!=null)return b;}
        }
        return null;
    }
    private static JButton namedButton(Container parent,String text) {
        for(Component c:parent.getComponents()) {
            if(c instanceof JButton&&((JButton)c).getText().equals(text))return (JButton)c;
            if(c instanceof Container){JButton b=namedButton((Container)c,text);if(b!=null)return b;}
        }
        return null;
    }
    public static void main(String[] args) throws Exception {
        output=new File(args[0]);output.mkdirs();
        AtomicReference<Throwable> error=new AtomicReference<>();
        Thread.setDefaultUncaughtExceptionHandler((t,e)->{e.printStackTrace();error.set(e);});
        try {
            Ex3.main(new String[0]);
            SwingUtilities.invokeAndWait(()->{
                for(Frame f:Frame.getFrames())if(f instanceof Ex3)frame=(JFrame)f;
                if(frame==null)throw new IllegalStateException("Ex3 window did not open");
                frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
                System.out.println("APP OPENED: "+frame.getTitle()+" "+frame.getWidth()+"x"+frame.getHeight());
            });
            JTabbedPane tabs=(JTabbedPane)frame.getContentPane().getComponent(0);
            System.out.println("TABS: "+tabs.getTabCount());
            for(int i=0;i<tabs.getTabCount();i++) {
                final int index=i;
                SwingUtilities.invokeAndWait(()->tabs.setSelectedIndex(index));
                Thread.sleep(100);
                SwingUtilities.invokeAndWait(()->{
                    Container panel=(Container)tabs.getComponentAt(index);
                    System.out.println("TAB "+index+": "+tabs.getTitleAt(index)+" visible cells="+visibleCells(panel));
                    if(visibleCells(panel)!=420)throw new AssertionError("Missing grid cells in "+tabs.getTitleAt(index));
                    try{capture("tab-"+index);}catch(Exception e){throw new RuntimeException(e);}
                });
            }
            for(int i=0;i<tabs.getTabCount();i++) {
                if(!tabs.getTitleAt(i).toLowerCase().startsWith("fill"))continue;
                final int index=i;
                SwingUtilities.invokeAndWait(()->{
                    tabs.setSelectedIndex(index);
                    Container panel=(Container)tabs.getComponentAt(index);
                    JButton fill=button(panel);
                    if(fill==null)throw new IllegalStateException("Missing Fill button");
                    fill.doClick();
                });
                Thread.sleep(150);
                SwingUtilities.invokeAndWait(()->{
                    Container panel=(Container)tabs.getComponentAt(index);
                    System.out.println("AFTER FILL "+index+": cells="+visibleCells(panel)+" button remains="+(button(panel)!=null));
                    if(visibleCells(panel)!=420 || button(panel)==null)throw new AssertionError("Fill damaged the grid or controls");
                    try{capture("tab-"+index+"-after-fill");}catch(Exception e){throw new RuntimeException(e);}
                    button(panel).doClick();
                    JButton reset=namedButton(panel,"Reset");
                    if(reset==null)throw new AssertionError("Missing Reset button");
                    reset.doClick();
                });
                Thread.sleep(100);
                SwingUtilities.invokeAndWait(()->{
                    Container panel=(Container)tabs.getComponentAt(index);
                    if(visibleCells(panel)!=420 || button(panel)==null)throw new AssertionError("Reset damaged the grid or controls");
                    System.out.println("AFTER RESET "+index+": cells="+visibleCells(panel)+" controls retained=true");
                    try{capture("tab-"+index+"-after-reset");}catch(Exception e){throw new RuntimeException(e);}
                });
            }
            if(error.get()!=null)throw new RuntimeException("Uncaught GUI error",error.get());
            System.out.println("GUI SMOKE CHECK COMPLETED: all tabs visited, both Fill actions executed.");
        } finally {
            SwingUtilities.invokeAndWait(()->{for(Window w:Window.getWindows())w.dispose();});
        }
    }
}
