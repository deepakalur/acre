/**
 *   Copyright 2004-2005 Sun Microsystems, Inc.
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */
package org.acre.visualizer.ui.dashboard;

import org.acre.analytics.HitCountData;
import org.acre.analytics.SnapshotQuery;
import org.acre.analytics.SnapshotsUtil;
//import org.jfree.chart.ChartFactory;
//import org.jfree.chart.ChartPanel;
//import org.jfree.chart.JFreeChart;
//import org.jfree.chart.plot.PlotOrientation;
//import org.jfree.data.category.CategoryDataset;
//import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.JFrame;
import java.util.ArrayList;
import java.util.List;

/**
 * User: Deepak Alur
 * Date: May 2, 2005
 * Time: 12:05:30 PM
 */
public class AcreChartUtil {


    public static void main(String args[]) {

//        JFreeChart chart;
//        CategoryDataset dataset = createDataset();
//
//        chart = ChartFactory.createBarChart(
//                "Pattern Analytics",         // chart title
//                "Patterns",               // domain axis label
//                "# Hits",                  // range axis label
//                dataset,                  // data
//                PlotOrientation.VERTICAL, // orientation
//                true,                     // include legend
//                true,                     // tooltips?
//                false                     // URLs?
//            );
//
//        ChartPanel cp = new ChartPanel(chart);
//        JFrame f = new JFrame("Chart");
//        f.getContentPane().add(cp);
//        f.pack();
//        f.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
//        f.setVisible(true);
    }

//    private static CategoryDataset createDataset() {
//
//        // row keys...
//        String series1 = "T1";
//        String series2 = "T2";
//        String series3 = "T3";
//
//        // column keys...
//        String category1 = "Pattern 1";
//        String category2 = "Pattern 2";
//        String category3 = "Pattern 3";
//        String category4 = "Pattern 4";
//        String category5 = "Pattern 5";
//
//        // create the dataset...
//        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
//
//        dataset.addValue(1.0, series1, category1);
//        dataset.addValue(4.0, series1, category2);
//        dataset.addValue(3.0, series1, category3);
//        dataset.addValue(5.0, series1, category4);
//        dataset.addValue(5.0, series1, category5);
//
//        dataset.addValue(5.0, series2, category1);
//        dataset.addValue(7.0, series2, category2);
//        dataset.addValue(6.0, series2, category3);
//        dataset.addValue(8.0, series2, category4);
//        dataset.addValue(4.0, series2, category5);
//
//        dataset.addValue(4.0, series3, category1);
//        dataset.addValue(3.0, series3, category2);
//        dataset.addValue(2.0, series3, category3);
//        dataset.addValue(3.0, series3, category4);
//        dataset.addValue(6.0, series3, category5);
//
//        return dataset;
//    }
//
//    public static CategoryDataset createDataset(String [] patternNames, SnapshotQuery query) {
//
//        // create the dataset...
//        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
//
//        for (int pn=0; pn < patternNames.length; pn++) {
//            String patternName = patternNames[pn];
//
//            List hits = SnapshotsUtil.getPatternHitCounts(patternName, query);
//
//            // for each pattern, you will have Tn hits
//            // construct series
//            ArrayList series = new ArrayList();
//            for (int i=0; i < hits.size(); i++) {
//                HitCountData hcd = (HitCountData) hits.get(i);
//                series.add(hcd.getVersion());
//                dataset.addValue(hcd.getHitCount(), hcd.getVersion(), patternName);
//            }
//
////            // construct keys
////            String category = patternName;
////
////            for (int i=0; i < series.size(); i++) {
////                Integer hit = (Integer) hits.get(i);
////                dataset.addValue(hit, (String)series.get(i), category);
////            }
//        }
//
//        return dataset;
//    }
//
//    public static JFreeChart createChart (String title,
//                                          String xAxisLabel,
//                                          String yAxisLabel,
//                                          CategoryDataset dataset) {
//
//        JFreeChart chart = ChartFactory.createBarChart(
//                title,         // chart title
//                xAxisLabel,               // domain axis label
//                yAxisLabel,                  // range axis label
//                dataset,                  // data
//                PlotOrientation.VERTICAL, // orientation
//                true,                     // include legend
//                true,                     // tooltips?
//                false                     // URLs?
//            );
//
////        Range range = new Range(0, 100);
////        chart.getCategoryPlot().getRangeAxis().setRange(range);
//
//        return chart;
//    }
}
