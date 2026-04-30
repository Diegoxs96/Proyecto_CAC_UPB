module UPB_CAC {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.rmi;

    opens edu.co.diegoxs96 to javafx.fxml;
    opens edu.co.diegoxs96.Client.Controller to javafx.fxml;
    opens edu.co.diegoxs96.Server.Controller to javafx.fxml;
    opens edu.co.diegoxs96.Server.Model to javafx.fxml, java.rmi;
    opens edu.co.diegoxs96.Server.View to javafx.fxml;

    exports edu.co.diegoxs96;
    exports edu.co.diegoxs96.Client.Controller;
    exports edu.co.diegoxs96.Server.Model;
}
