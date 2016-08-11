package org.dplib.display;

import javafx.fxml.Initializable;
import javafx.scene.chart.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import org.dplib.Result;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by george on 04/08/16.
 */
public class FXController
implements Initializable
{

    public TextArea editor;
    public Button solve;
    public BarChart executionTimeGraph;
    public BarChart instructionCountGraph;
    private Result result;
    private boolean resultAlreadySet = false;

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
        if(!resultAlreadySet)
        {
            setExecutionTimeGraph();
            setInstructionCountGraph();
            resultAlreadySet = true;
        }
    }

    private void setExecutionTimeGraph()
    {
        executionTimeGraph.setTitle("Execution time");
        executionTimeGraph.getYAxis().setLabel("Execution Time (ms)");
        XYChart.Series model = createSeries("Model", result.getModelExecutionTime());
        XYChart.Series user = createSeries("User", result.getUserExecutionTime());
        executionTimeGraph.getData().setAll(model, user);
        executionTimeGraph.getYAxis().setAnimated(false);
        executionTimeGraph.getXAxis().setAnimated(false);
        executionTimeGraph.setAnimated(false);
    }

    private void setInstructionCountGraph()
    {
        instructionCountGraph.setTitle("Instructions");
        instructionCountGraph.getYAxis().setLabel("Instructions (millions)");
        XYChart.Series model = createSeries("Model", result.getModelInstructionCount() / 1000000);
        XYChart.Series user = createSeries("User", result.getUserInstructionCount() / 1000000);
        instructionCountGraph.getData().setAll(model, user);
        instructionCountGraph.getYAxis().setAnimated(false);
        instructionCountGraph.getXAxis().setAnimated(false);
        instructionCountGraph.setAnimated(false);
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
