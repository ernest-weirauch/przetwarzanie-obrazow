/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wpo_gui;

import java.awt.Dimension;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 *
 * @author Ja
 */
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYBarRenderer;
import org.jfree.data.statistics.HistogramDataset;
import org.jfree.data.xy.IntervalXYDataset;



public class HistogramFrame extends JFrame{
    static double[] red;
    static double[] green;
    static double[] blue;
    static double[] gray;

//    HistogramFrame(double[] r, double[] g, double[] b) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//    }

    public HistogramFrame(/*String title,*/ double[] red, double[] green, double[] blue) {
        //super(/*title*/);
        this.setSize(1000, 600);
        
        HistogramFrame.red = red;
        HistogramFrame.green = green;
        HistogramFrame.blue = blue;

        JFreeChart jfreechart = ChartFactory.createHistogram("RGB Histogram", "pixel value", "count", createRGBDataset(), PlotOrientation.VERTICAL, true, true, false);
        XYPlot xyplot = (XYPlot) jfreechart.getPlot();
        ValueAxis domainAxis = xyplot.getDomainAxis();
        domainAxis.setRange(0, 256);
//        xyplot.setForegroundAlpha(0.85F);
//        XYBarRenderer xybarrenderer = (XYBarRenderer) xyplot.getRenderer();
//        xybarrenderer.setDrawBarOutline(false);
        JPanel jpanel = new ChartPanel(jfreechart);
        jpanel.setPreferredSize(new Dimension(1000, 600));
        setContentPane(jpanel);
        
    }
    
        public HistogramFrame(/*String title,*/ double[] gray) {
        //super(/*title*/);
        this.setSize(1000, 600);
        
        HistogramFrame.gray = gray;

        JFreeChart jfreechart = ChartFactory.createHistogram("Gray Histogram", "pixel value", "count", createGrayDataset(), PlotOrientation.VERTICAL, true, true, false);
        XYPlot xyplot = (XYPlot) jfreechart.getPlot();
        ValueAxis domainAxis = xyplot.getDomainAxis();
        domainAxis.setRange(0, 256);
//        xyplot.setForegroundAlpha(0.85F);
//        XYBarRenderer xybarrenderer = (XYBarRenderer) xyplot.getRenderer();
//        xybarrenderer.setDrawBarOutline(false);
        JPanel jpanel = new ChartPanel(jfreechart);
        jpanel.setPreferredSize(new Dimension(1000, 600));
        setContentPane(jpanel);
        
    }

    private static IntervalXYDataset createRGBDataset() {
        HistogramDataset histogramDataset = new HistogramDataset();
        histogramDataset.addSeries("Red histogram", red, 256);
        histogramDataset.addSeries("Blue histogram", green, 256);
        histogramDataset.addSeries("Green histogram", blue, 256);

        return histogramDataset;
    }
    
    private static IntervalXYDataset createGrayDataset(){
        HistogramDataset histogramDataset = new HistogramDataset();
        histogramDataset.addSeries("Gray histogram", gray, 256);
        return histogramDataset;
        
    }

}
