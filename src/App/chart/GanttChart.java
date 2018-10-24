
package App.chart;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.GregorianCalendar;

import App.Connect;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import App.chart.GanttChartController.ExtraData;

public class GanttChart extends Application {

    //String[] Dates = new String[] {"28-08-2018", "04-09-2018", "12-09-2018"};
    ArrayList<Date> ProjectDates = new ArrayList<Date>();
    ArrayList<Date> TaskDates = new ArrayList<Date>();
    //private ObservableList<String> Date;
    ArrayList<String> TaskNames = new ArrayList<String>();
    int count = 0;

    String[] colors =  new String[] {"status-red", "status-green", "status-blue", "status-voilet", "status-pink", "status-orange", "status-brown"};

    @Override
    public void start(Stage stage) throws Exception {

        try {
            Connect connect =new Connect();
            Connection connection=connect.getConnection();

            Statement statement = connection.createStatement();
            String sql = "SELECT task_name,task_start_date,task_end_date FROM project_task WHERE project_id = "+1;
            ResultSet rs = statement.executeQuery(sql);

            while (rs.next()) {
                String TaskName = rs.getString("task_name");
                Date StartDate =  rs.getDate("task_start_date");
                Date EndDate = rs.getDate("task_end_date");
                TaskNames.add(TaskName);
                TaskDates.add(StartDate);
                TaskDates.add(EndDate);
                ++count;
            }
            rs.close();
            statement.close();
        }
        catch (Exception e){
            e.printStackTrace();
        }

        stage.setTitle("Gantt Chart Sample");
        //String[] machines = new String[] { "Machine 1", "Machine 2", "Machine 3" };

        final DateAxis xAxis = new DateAxis();
        final CategoryAxis yAxis = new CategoryAxis();

        //String date = new SimpleDateFormat("12/12/2018");
        final GanttChartController<Date,String> chart = new GanttChartController<>(xAxis,yAxis);
        xAxis.setLabel("");
        xAxis.setTickLabelFill(Color.CHOCOLATE);
        xAxis.setTickLabelGap(10);
        xAxis.setLowerBound(TaskDates.get(0));
        xAxis.setUpperBound(TaskDates.get(TaskDates.size()-1));
        double asd = xAxis.averageTickGap();
        System.out.println(asd);

        xAxis.setAutoRanging(false);

        xAxis.setTickLabelRotation(90);

        //Date d2 = new Date(2018,9,26);
        //System.out.println(b);
        //System.out.println(c);

        yAxis.setLabel("");
        yAxis.setTickLabelFill(Color.CHOCOLATE);
        yAxis.setTickLabelGap(10);
        yAxis.setCategories(FXCollections.observableList(TaskNames));

        chart.setTitle("Machine Monitoring");
        chart.setLegendVisible(false);
        chart.setBlockHeight(50);

        xAxis.setTickLength(15);
        xAxis.setMaxWidth(1000);
        xAxis.setMinWidth(1000);


        int j = 0;
        int k = 1;


        for (int i =0; i<count; i++) {
            double length = xAxis.getDisplayPositionDate(TaskDates.get(j), TaskDates.get(k));
            XYChart.Series series = new XYChart.Series();
            series.getData().add(new XYChart.Data(TaskDates.get(j), TaskNames.get(i), new ExtraData( length, colors[i])));
            chart.getData().add(series);
            j+=2; k+=2;
        }

        xAxis.getWidth();
        System.out.println(xAxis.getWidth());
        //length = xAxis.getDisplayPositionDate(TaskDates.get(0), TaskDates.get(1));
        //XYChart.Series series1 = new XYChart.Series();
        //series1.getData().add(new XYChart.Data(TaskDates.get(0), TaskNames.get(0), new ExtraData(length, "status-red")));

        //chart.getData().addAll(series1);


        //series1.getData().add(new XYChart.Data(Date, machine, new ExtraData( 8, "status-red")));
        /*
        machine = TaskNames.get(1);
        Date = this.Date.get(2);
        XYChart.Series series2 = new XYChart.Series();
        series2.getData().add(new XYChart.Data(Date, machine, new ExtraData( 1, "status-green")));

        machine = TaskNames.get(2);
        Date = this.Date.get(4);
        XYChart.Series series3 = new XYChart.Series();
        series3.getData().add(new XYChart.Data(Date, machine, new ExtraData( 110, "status-blue")));

        chart.getData().addAll(series1, series2, series3);
        */


        //long length = DAYS.between(LocalDate.of(2014, Month.JULY, 4), LocalDate.of(2014, Month.JULY, 12));
        //System.out.println(length);

        /*
        ObservableList<XYChart.Series<Date, Number>> series = FXCollections.observableArrayList();

        ObservableList<XYChart.Data<Date, Number>> series2Data = FXCollections.observableArrayList();
        series2Data.add(new XYChart.Data<Date, Number>(new GregorianCalendar(2014, 1, 13).getTime(), 9));
        series2Data.add(new XYChart.Data<Date, Number>(new GregorianCalendar(2014, 2, 27).getTime(), 8));

        series.add(new XYChart.Series<>("Series2", series2Data));
        NumberAxis numberAxis = new NumberAxis();
        DateAxis dateAxis = new DateAxis();

        LineChart<Date, Number> lineChart = new LineChart<Date, Number>(dateAxis, numberAxis, series);
        */



        chart.getStylesheets().add(getClass().getResource("ganttchart.css").toExternalForm());

        Scene scene  = new Scene(chart,1200,600);
        stage.setScene(scene);
        stage.show();
        double lengthm = xAxis.getWidth();
        //System.out.println(lengthm);
    }

    public static void main(String[] args) {
        launch(args);
    }
}