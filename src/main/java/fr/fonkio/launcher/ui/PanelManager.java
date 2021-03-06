package fr.fonkio.launcher.ui;

import fr.arinonia.arilibfx.AriLibFX;
import fr.flowarg.flowupdater.utils.builderapi.BuilderException;
import fr.fonkio.launcher.Main;
import fr.fonkio.launcher.MvWildLauncher;
import fr.fonkio.launcher.launcher.Launcher;
import fr.fonkio.launcher.ui.panel.Panel;
import fr.fonkio.launcher.ui.panels.PanelMain;
import fr.fonkio.launcher.ui.panels.PanelLogin;
import fr.fonkio.launcher.ui.panels.includes.TopPanel;
import fr.fonkio.launcher.utils.HttpRecup;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.*;

public class PanelManager {

    private final Stage stage;
    private final TopPanel topPanel;
    private final GridPane centerPanel = new GridPane();
    private Double xOffset;
    private Double yOffset;
    PanelLogin panelLogin;
    boolean loginInit = false;
    PanelMain panelMain;
    boolean homeInit = false;
    fr.fonkio.launcher.utils.MainPanel currentPanel;
    Launcher launcher;

    public PanelManager(Stage stage) throws URISyntaxException, BuilderException, MalformedURLException {
        topPanel = new TopPanel(stage);
        this.stage = stage;
        launcher = new Launcher(this);
        panelMain = new PanelMain(stage, this);
        try {
            panelLogin = new PanelLogin(stage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void init() {
        this.stage.setTitle(MvWildLauncher.SERVEUR_NAME + " Launcher");
        this.stage.setMinWidth(1280);
        this.stage.setWidth(1280);
        this.stage.setMinHeight(720);
        this.stage.setHeight(720);
        this.stage.initStyle(StageStyle.UNDECORATED);
        this.stage.centerOnScreen();
        this.stage.getIcons().add(new Image(Main.class.getResource("/logoNBG.png").toExternalForm()));
        this.stage.show();

        GridPane layout = new GridPane();
        layout.setStyle(AriLibFX.setResponsiveBackground(Main.class.getResource("/fondLauncher.png").toExternalForm()));
        this.stage.setScene(new Scene(layout));
        this.stage.setResizable(false);

        RowConstraints topPanelConstraints = new RowConstraints();
        topPanelConstraints.setValignment(VPos.TOP);
        topPanelConstraints.setMinHeight(25);
        topPanelConstraints.setMaxHeight(25);
        layout.getRowConstraints().addAll(topPanelConstraints, new RowConstraints());
        layout.add(this.topPanel.getLayout(), 0, 0);
        this.topPanel.init(this);
        layout.add(this.centerPanel, 0, 1);
        GridPane.setVgrow(this.centerPanel, Priority.ALWAYS);
        GridPane.setHgrow(this.centerPanel, Priority.ALWAYS);
        //ResizeHelper.addResizeListener(this.stage);
        layout.setOnMousePressed(event -> {
            xOffset = stage.getX() - event.getScreenX();
            yOffset = stage.getY() - event.getScreenY();
        });
        layout.setOnMouseDragged(event -> {
            stage.setX(event.getScreenX() + xOffset);
            stage.setY(event.getScreenY() + yOffset);
        });
        checkVersion();
        showPanel(fr.fonkio.launcher.utils.MainPanel.LOGIN);

        MvWildLauncher.updatePresence(null, "Connexion au launcher ...", "mvwildlogo", null);
    }

    public void showPanel(fr.fonkio.launcher.utils.MainPanel name) {
        Panel panel = null;
        boolean changementPanel = true;
        boolean initPanel = true;
        switch (name) {
            case HOME:
                panel = panelMain;
                changementPanel = currentPanel.equals(fr.fonkio.launcher.utils.MainPanel.LOGIN);
                initPanel = !homeInit;
                homeInit = true;
                currentPanel = fr.fonkio.launcher.utils.MainPanel.HOME;
                break;
            case PARAMETRES:
                panel = panelMain;
                changementPanel = currentPanel.equals(fr.fonkio.launcher.utils.MainPanel.LOGIN);
                initPanel = !homeInit;
                homeInit = true;
                currentPanel = fr.fonkio.launcher.utils.MainPanel.PARAMETRES;
                break;
            case LOGIN:
                currentPanel = fr.fonkio.launcher.utils.MainPanel.LOGIN;
                initPanel = !loginInit;
                loginInit = true;
                panel = panelLogin;
                panelLogin.showPanel();
                break;
        }
        if (panel != null){
            if(changementPanel) {
                this.centerPanel.getChildren().clear();
                this.centerPanel.getChildren().add(panel.getLayout());
            }
            if(initPanel) {
                panel.init(this);
            }
            panel.onShow();
        }

    }

    public Stage getStage() {
        return stage;
    }
    public void setInstallButtonText(String s) {
        if (homeInit) {
            panelMain.setInstallButtonText(s);
        }
    }

    public void setDisableInstallButton(boolean b) {
        if (homeInit) {
            panelMain.setDisableInstall(b);
        }
    }
    public void setProgress(float avancee, float fin) {
        if (homeInit) {
            panelMain.setProgress(avancee, fin);
        }
    }
    public void setPseudo(String pseudo) {
        this.launcher.setPseudo(pseudo);
        if(homeInit) {
            panelMain.setPseudo(pseudo);
        }
    }

    public void setStatus(String s) {
        if(homeInit) {
            panelMain.setStatus(s);
        }
    }

    public String getRAM() {
        return this.launcher.getRAM();
    }

    public void setRAM(double ramD) {
        this.launcher.setRAM(ramD);
    }

    public String getPseudo() {
        return this.launcher.getPseudo();
    }

    public String getPseudoTextField() {
        if (loginInit) {
            return panelLogin.getPseudoTextField();
        }
        return "";
    }

    public void connexion() {
        this.launcher.connexion();
    }

    public String checkVersion() {
        String version = HttpRecup.getVersion(MvWildLauncher.SITE_URL +"launcher/version.php");
        if (version != null && (!HttpRecup.offline) && !version.equals(MvWildLauncher.LAUNCHER_VERSION)) {
            return version;
        }
        return null;
    }

    public void install() {
        this.launcher.install();
    }

    public String getVersion() {
        return this.launcher.getVersion();
    }

    public String getForgeVersion() {
        return this.launcher.getForgeVersion();
    }

    public void setDRP(boolean selected) {
        this.launcher.setDRP(selected);
    }

    public Boolean getDRP() {
        return this.launcher.getDRP();
    }

    public void resetLauncher() {
        launcher.resetLauncher();
    }
    public boolean containsModsFolder() {
        return launcher.containsModsFolder();
    }
}
