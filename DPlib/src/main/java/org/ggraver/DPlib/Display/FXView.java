//package org.ggraver.DPlib.Display;
//
//import javafx.application.Application;
//import javafx.fxml.FXMLLoader;
//import javafx.scene.Parent;
//import javafx.scene.Scene;
//import javafx.stage.Stage;
//import org.ggraver.DPlib.IO;
//
//import java.net.URL;
//import java.util.List;
//
///**
// * Created by george on 04/08/16.
// */
//public class FXView
//extends Application
//{
//
//    private Result result;
//
//    @Override
//    public void init()
//            throws Exception
//    {
//        IO io = new IO();
//        List<String> args = getParameters().getRaw();
//
//    }
//
//    @Override
//    public void start(Stage stage)
//    throws Exception
//    {
//        try {
//            URL fxmlURL = getClass().getClassLoader().getResource("gui.fxml");
//            assert fxmlURL != null;
//            FXMLLoader fxmlLoader = new FXMLLoader(fxmlURL);
//            Parent root = fxmlLoader.load();
//            FXController controller = fxmlLoader.getController();
//            controller.loadJavaFile();
//            controller.setResult(result);
//
//            stage.setTitle("DPLib");
//            stage.setScene(new Scene(root, 800, 600));
//            stage.setResizable(false);
//            stage.show();
//        }
//        catch (Exception e)
//        {
//            e.printStackTrace();
//            System.exit(1);
//        }
//    }
//
//}
