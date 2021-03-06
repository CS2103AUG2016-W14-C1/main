//@@author A0144881Y
package seedu.manager.ui;

import com.google.common.eventbus.Subscribe;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import seedu.manager.commons.core.LogsCenter;
import seedu.manager.commons.events.model.ActivityManagerChangedEvent;
import seedu.manager.commons.events.ui.ChangeStorageFileDisplayEvent;
import seedu.manager.commons.util.FxViewUtil;

import org.controlsfx.control.StatusBar;

import java.util.Date;
import java.util.logging.Logger;

/**
 * A ui for the status bar that is displayed at the footer of the application.
 */
public class StatusBarFooter extends UiPart {
    private static final String INFO_CHANGE_STORAGE = "Changing data storage location to: %1$s";
    private static final String INFO_LAST_UPDATED = "Setting last updated status to %1$s";
    private static final String MESSAGE_NOT_UPDATED_YET = "Not updated yet in this session";
    private static final Logger logger = LogsCenter.getLogger(StatusBarFooter.class);
    private StatusBar syncStatus;
    private StatusBar saveLocationStatus;

    private GridPane mainPane;

    @FXML
    private AnchorPane saveLocStatusBarPane;

    @FXML
    private AnchorPane syncStatusBarPane;

    private AnchorPane placeHolder;

    private static final String FXML = "StatusBarFooter.fxml";

    public static StatusBarFooter load(Stage stage, AnchorPane placeHolder, String saveLocation) {
        StatusBarFooter statusBarFooter = UiPartLoader.loadUiPart(stage, placeHolder, new StatusBarFooter());
        statusBarFooter.configure(saveLocation);
        return statusBarFooter;
    }

    public void configure(String saveLocation) {
        addMainPane();
        addSyncStatus();
        setSyncStatus(MESSAGE_NOT_UPDATED_YET);
        addSaveLocation();
        setSaveLocation("./" + saveLocation);
        registerAsAnEventHandler(this);
    }

    private void addMainPane() {
        FxViewUtil.applyAnchorBoundaryParameters(mainPane, 0.0, 0.0, 0.0, 0.0);
        placeHolder.getChildren().add(mainPane);
    }

    private void setSaveLocation(String location) {
        this.saveLocationStatus.setText(location);
    }

    private void addSaveLocation() {
        this.saveLocationStatus = new StatusBar();
        FxViewUtil.applyAnchorBoundaryParameters(saveLocationStatus, 0.0, 0.0, 0.0, 0.0);
        saveLocStatusBarPane.getChildren().add(saveLocationStatus);
    }

    private void setSyncStatus(String status) {
        this.syncStatus.setText(status);
    }

    private void addSyncStatus() {
        this.syncStatus = new StatusBar();
        FxViewUtil.applyAnchorBoundaryParameters(syncStatus, 0.0, 0.0, 0.0, 0.0);
        syncStatusBarPane.getChildren().add(syncStatus);
    }

    @Override
    public void setNode(Node node) {
        mainPane = (GridPane) node;
    }

    @Override
    public void setPlaceholder(AnchorPane placeholder) {
        this.placeHolder = placeholder;
    }

    @Override
    public String getFxmlPath() {
        return FXML;
    }

    @Subscribe
    public void handleActivityManagerChangedEvent(ActivityManagerChangedEvent abce) {
        String lastUpdated = (new Date()).toString();
        logger.info(LogsCenter.getEventHandlingLogMessage(abce, String.format(INFO_LAST_UPDATED, lastUpdated)));
        setSyncStatus("Last Updated: " + lastUpdated);
    }
    
    @Subscribe
    public void handleActivityManagerChangedStorageFile(ChangeStorageFileDisplayEvent event){
    	logger.info(LogsCenter.getEventHandlingLogMessage(event, String.format(INFO_CHANGE_STORAGE, event.file)));
    	setSaveLocation(event.file);
    }
}
