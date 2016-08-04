package org.ggraver.DPlib;

import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by george on 04/08/16.
 */
public class Controller
implements Initializable
{

    public TextArea editor;
    public Button solve;
    public BarChart executionTimeGraph;
    public BarChart instructionCountGraph;
    public Result result;
    public Result oldResult;

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        editor.setText("INITIALISED\n");
    }

    public void loadJavaFile ()
    {
        editor.appendText("Loaded java file!");
    }

    public void setGraphs()
    {
        setExecutionTimeGraph();
//        setInstructionCountGraph();
    }

    private void setExecutionTimeGraph()
    {
        if(!(oldResult == result))
        {
            executionTimeGraph.setTitle("Execution time");
            executionTimeGraph.getYAxis().setLabel("Execution Time (ms)");
            XYChart.Series model = createSeries("Model", result.getModelExecutionTime());
            XYChart.Series user = createSeries("Model", result.getUserExecutionTime());
            executionTimeGraph.getData().setAll(model, user);
            oldResult = result;
        }
    }

    private void setInstructionCountGraph()
    {
//        this.instructionCountGraph = new Graph(new CategoryAxis(),
//                                               new NumberAxis(),
//                                               "Instructions",
//                                               "Number of Instructions Executed",
//                                               result.getModelInstructionCount(),
//                                               result.getUserInstructionCount());
    }

    private XYChart.Series createSeries(String name, long value)
    {
        XYChart.Series s = new XYChart.Series();
        s.setName(name);
        XYChart.Data<String, Long> data = new XYChart.Data<>("", value);
        s.getData().add(data);
        return s;
    }

    public void setResult(Result result)
    {
        this.result = result;
    }

}
