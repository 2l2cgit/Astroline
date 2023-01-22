/*
 * Decompiled with CFR 0.150.
 */
package vip.astroline.client.layout.altMgr;

import com.thealtening.auth.TheAlteningAuthentication;
import com.thealtening.auth.service.AlteningServiceType;
import fr.litarvan.openauth.microsoft.MicrosoftAuthResult;
import fr.litarvan.openauth.microsoft.MicrosoftAuthenticationException;
import fr.litarvan.openauth.microsoft.MicrosoftAuthenticator;
import fr.litarvan.openauth.microsoft.model.response.MinecraftProfile;
import java.awt.Component;
import java.awt.Desktop;
import java.awt.HeadlessException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Session;
import vip.astroline.client.Astroline;
import vip.astroline.client.layout.altMgr.Alt;
import vip.astroline.client.layout.altMgr.AltButton;
import vip.astroline.client.layout.altMgr.AltScrollList;
import vip.astroline.client.layout.altMgr.dialog.DialogWindow;
import vip.astroline.client.layout.altMgr.dialog.impl.AltInputDialog;
import vip.astroline.client.layout.altMgr.dialog.impl.ConfirmDialog;
import vip.astroline.client.layout.altMgr.dialog.impl.InfoDialog;
import vip.astroline.client.layout.altMgr.dialog.impl.InputDialog;
import vip.astroline.client.layout.altMgr.kingAlts.AltJson;
import vip.astroline.client.layout.altMgr.kingAlts.KingAlts;
import vip.astroline.client.layout.altMgr.kingAlts.ProfileJson;
import vip.astroline.client.service.font.FontManager;
import vip.astroline.client.storage.utils.login.LoginUtils;
import vip.astroline.client.storage.utils.render.render.RenderUtil;
import yarukon.oauth.OAuthService;

public class GuiAltMgr
extends GuiScreen {
    public GuiScreen parent;
    public AltScrollList list;
    public static CopyOnWriteArrayList<Alt> alts = new CopyOnWriteArrayList();
    public ArrayList<AltButton> cButtons = new ArrayList();
    public Alt selectedAlt = null;
    public String apiString = "King Alts: API Key not set";
    public static int premiumAlts = 0;
    public static int crackedAlts = 0;
    public DialogWindow popupDialog;
    public static OAuthService oAuthService = new OAuthService();

    public GuiAltMgr(GuiScreen parent) {
        this.parent = parent;
    }

    @Override
    public void initGui() {
        this.cButtons.clear();
        this.cButtons.add(new AltButton(0, this.width / 2 - 120, this.height - 52, 57.0f, 20.0f, "Use"));
        this.cButtons.add(new AltButton(1, (float)(this.width / 2) - 60.0f, this.height - 52, 117.0f, 20.0f, "Direct Login"));
        this.cButtons.add(new AltButton(2, this.width / 2 - 120, this.height - 28, 57.0f, 20.0f, "Star"));
        this.cButtons.add(new AltButton(3, this.width / 2 - 60, this.height - 28, 57.0f, 20.0f, "Add"));
        this.cButtons.add(new AltButton(4, this.width / 2, this.height - 28, 57.0f, 20.0f, "Edit"));
        this.cButtons.add(new AltButton(5, this.width / 2 + 60, this.height - 52, 57.0f, 20.0f, "Delete"));
        this.cButtons.add(new AltButton(6, this.width / 2 + 60, this.height - 28, 57.0f, 20.0f, "Back"));
        this.cButtons.add(new AltButton(7, 5.0f, 5.0f, 110.0f, 20.0f, "Import Alts"));
        this.cButtons.add(new AltButton(8, this.width - 85, 5.0f, 80.0f, 20.0f, "Clear All"));
        this.cButtons.add(new AltButton(9, this.width - 85 - 85, 5.0f, 80.0f, 20.0f, "King Alts"));
        if (KingAlts.API_KEY.length() > 3) {
            this.cButtons.add(new AltButton(10, 5.0f, 30.0f, 110.0f, 20.0f, "Add Alt by King Alts"));
        }
        this.cButtons.add(new AltButton(13, this.width / 2 - 200, this.height - 28, 77.0f, 20.0f, "Microsoft Login"));
        this.list = new AltScrollList(this, alt -> {
            this.selectedAlt = alt;
        }, alt -> this.doClickButton(this.cButtons.get(0)));
        new Thread("King Alts"){

            @Override
            public void run() {
                if (KingAlts.API_KEY != null && KingAlts.API_KEY.length() > 3) {
                    GuiAltMgr.this.apiString = "King Alts Profile Loading...";
                    ProfileJson json = KingAlts.getProfile();
                    GuiAltMgr.this.apiString = json.getMessage() != null ? "\u00a7cERROR: " + json.getMessage() : "You have generated " + json.getGeneratedToday() + " alt" + (json.getGeneratedToday() > 1 ? "s" : "") + " today.";
                }
            }
        }.start();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        if (this.popupDialog != null && this.popupDialog.destroy) {
            this.popupDialog = null;
        }
        RenderUtil.drawImg(new ResourceLocation("astroline/images/bg.jpg"), 0.0, 0.0, this.width, this.height);
        FontManager.sans16.drawCenteredString("Current name: " + Minecraft.getMinecraft().getSession().getUsername() + (Object)((Object)EnumChatFormatting.WHITE) + " (" + (this.mc.getSession().getSessionType() == Session.Type.LEGACY ? "Cracked" : "Premium") + ")", (float)this.width / 2.0f, 5.0f, -1);
        FontManager.sans16.drawCenteredString("Premium: " + premiumAlts + ", Cracked: " + crackedAlts, (float)this.width / 2.0f, 15.0f, -1);
        FontManager.sans16.drawCenteredString(this.apiString, (float)this.width / 2.0f, 25.0f, -1);
        for (AltButton button : this.cButtons) {
            button.drawButton(mouseX, mouseY);
        }
        if (this.list != null) {
            this.list.doDraw((float)this.width / 2.0f - 120.0f, 40.0f, 237.0f, this.height - 100, mouseX, mouseY);
        }
        if (this.popupDialog != null) {
            RenderUtil.drawRect(0.0f, 0.0f, this.width, this.height, -2013265920);
            this.popupDialog.draw(mouseX, mouseY);
        }
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    public void updateScreen() {
        this.cButtons.get((int)0).isEnabled = !alts.isEmpty() && this.selectedAlt != null;
        this.cButtons.get((int)2).isEnabled = !alts.isEmpty() && this.selectedAlt != null;
        this.cButtons.get((int)4).isEnabled = !alts.isEmpty() && this.selectedAlt != null;
        boolean bl = this.cButtons.get((int)5).isEnabled = !alts.isEmpty() && this.selectedAlt != null;
        if (this.cButtons.get((int)2).isEnabled) {
            if (this.selectedAlt != null) {
                this.cButtons.get((int)2).displayString = this.selectedAlt.isStarred() ? "Unstar" : "Star";
            }
        } else {
            this.cButtons.get((int)2).displayString = "Star";
        }
        if (this.selectedAlt != null && !alts.contains(this.selectedAlt)) {
            this.selectedAlt = null;
        }
        if (this.popupDialog != null) {
            this.popupDialog.updateScreen();
        }
    }

    @Override
    public void handleMouseInput() throws IOException {
        if (this.popupDialog == null) {
            this.list.handleMouseInput();
        }
        super.handleMouseInput();
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        if (this.popupDialog == null) {
            for (AltButton button : this.cButtons) {
                if (!button.isHovered() || !button.isEnabled) continue;
                button.playPressSound(this.mc.getSoundHandler());
                this.doClickButton(button);
            }
            this.list.onClick(mouseX, mouseY, mouseButton);
        } else {
            this.popupDialog.mouseClick(mouseX, mouseY, mouseButton);
        }
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int state) {
        super.mouseReleased(mouseX, mouseY, state);
    }

    public void doClickButton(AltButton button) {
        switch (button.buttonID) {
            case 0: {
                new Thread(() -> {
                    this.apiString = "Logging in...";
                    if (this.selectedAlt.getEmail().contains("@alt.com")) {
                        TheAlteningAuthentication.theAltening().updateService(AlteningServiceType.THEALTENING);
                        String reply = LoginUtils.login(this.selectedAlt.getEmail(), "THEALTENING");
                        this.apiString = "" + reply;
                        if (reply == null) {
                            this.apiString = "Logged as " + this.mc.getSession().getUsername();
                            this.selectedAlt.setChecked(this.mc.getSession().getUsername());
                        }
                        return;
                    }
                    if (this.selectedAlt.isCracked()) {
                        LoginUtils.changeCrackedName(this.selectedAlt.getEmail());
                        this.apiString = "Logged as " + this.mc.getSession().getUsername();
                    } else {
                        Object reply = null;
                        MicrosoftAuthenticator authenticator = new MicrosoftAuthenticator();
                        try {
                            MicrosoftAuthResult result = null;
                            try {
                                result = authenticator.loginWithCredentials(this.selectedAlt.getEmail(), this.selectedAlt.getPassword());
                            }
                            catch (MicrosoftAuthenticationException e) {
                                throw new RuntimeException(e);
                            }
                            MinecraftProfile profile = result.getProfile();
                            Minecraft.getMinecraft().session = new Session(profile.getName(), profile.getId(), result.getAccessToken(), "microsoft");
                            System.out.println("Logged in with " + Minecraft.getMinecraft().session.getUsername());
                            Astroline.currentAlt = new String[]{this.selectedAlt.getEmail(), this.selectedAlt.getPassword()};
                        }
                        catch (RuntimeException e) {
                            e.printStackTrace();
                        }
                        this.apiString = reply;
                        if (reply == null) {
                            this.apiString = "Logged as " + this.mc.getSession().getUsername();
                            this.selectedAlt.setChecked(this.mc.getSession().getUsername());
                        }
                    }
                }).start();
                break;
            }
            case 1: {
                this.popupDialog = new DirectLoginDialog();
                this.popupDialog.makeCenter(this.width, this.height);
                break;
            }
            case 2: {
                Alt alt = alts.get(this.list.getSelectedIndex());
                alt.setStarred(!alt.isStarred());
                GuiAltMgr.sortAlts();
                break;
            }
            case 3: {
                this.popupDialog = new AddAltDialog();
                this.popupDialog.makeCenter(this.width, this.height);
                break;
            }
            case 4: {
                this.popupDialog = new EditAltDialog(this.selectedAlt);
                this.popupDialog.makeCenter(this.width, this.height);
                break;
            }
            case 5: {
                this.popupDialog = new DeleteAltDialog(this.selectedAlt);
                this.popupDialog.makeCenter(this.width, this.height);
                break;
            }
            case 6: {
                this.mc.displayGuiScreen(this.parent);
                break;
            }
            case 7: {
                new Thread(new Runnable(){

                    @Override
                    public void run() {
                        JFileChooser fileChooser = new JFileChooser(){

                            @Override
                            protected JDialog createDialog(Component parent) throws HeadlessException {
                                JDialog dialog = super.createDialog(parent);
                                dialog.setAlwaysOnTop(true);
                                return dialog;
                            }
                        };
                        fileChooser.setFileSelectionMode(0);
                        fileChooser.setAcceptAllFileFilterUsed(false);
                        fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("Username:Password format (TXT)", "txt"));
                        int action = fileChooser.showOpenDialog(null);
                        if (action == 0) {
                            try {
                                File file = fileChooser.getSelectedFile();
                                BufferedReader load = new BufferedReader(new FileReader(file));
                                String line = "";
                                while ((line = load.readLine()) != null) {
                                    String[] data = line.split(":");
                                    alts.add(new Alt(data[0], data[1], data.length == 3 ? data[2] : null));
                                }
                                load.close();
                                GuiAltMgr.sortAlts();
                            }
                            catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }).start();
                break;
            }
            case 8: {
                this.popupDialog = new ClearAllDialog();
                this.popupDialog.makeCenter(this.width, this.height);
                break;
            }
            case 9: {
                this.popupDialog = new KingAltInputDialog();
                this.popupDialog.makeCenter(this.width, this.height);
                break;
            }
            case 10: {
                new Thread(() -> {
                    this.apiString = "Generating Alt...";
                    AltJson json = KingAlts.getAlt();
                    if (json.getMessage() != null) {
                        this.apiString = "\u00a7cERROR: " + json.getMessage();
                    } else {
                        alts.add(0, new Alt(json.getEmail(), json.getPassword(), null));
                        GuiAltMgr.sortAlts();
                        ProfileJson profile = KingAlts.getProfile();
                        this.apiString = profile.getMessage() != null ? "\u00a7cERROR: " + profile.getMessage() : "You have generated " + profile.getGeneratedToday() + " alt" + (profile.getGeneratedToday() > 1 ? "s" : "") + " today.";
                    }
                }).start();
                break;
            }
            case 12: {
                this.openUrl("https://kw.baoziwl.com");
                break;
            }
            case 13: {
                this.popupDialog = new MicrosoftLoginDialog();
                this.popupDialog.makeCenter(this.width, this.height);
                new Thread(() -> oAuthService.authWithNoRefreshToken()).start();
            }
        }
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        if (this.popupDialog != null) {
            this.popupDialog.keyTyped(typedChar, keyCode);
        }
        if (keyCode == 1 && this.popupDialog == null) {
            this.mc.displayGuiScreen(this.parent);
        }
        super.keyTyped(typedChar, keyCode);
    }

    public static void sortAlts() {
        CopyOnWriteArrayList<Alt> newAlts = new CopyOnWriteArrayList<Alt>();
        premiumAlts = 0;
        crackedAlts = 0;
        for (Alt value : alts) {
            if (!value.isStarred()) continue;
            newAlts.add(value);
        }
        for (Alt alt : alts) {
            if (alt.isCracked() || alt.isStarred()) continue;
            newAlts.add(alt);
        }
        for (Alt alt : alts) {
            if (!alt.isCracked() || alt.isStarred()) continue;
            newAlts.add(alt);
        }
        for (int i = 0; i < newAlts.size(); ++i) {
            for (int i2 = 0; i2 < newAlts.size(); ++i2) {
                if (i == i2 || !((Alt)newAlts.get(i)).getEmail().equals(((Alt)newAlts.get(i2)).getEmail()) || ((Alt)newAlts.get(i)).isCracked() != ((Alt)newAlts.get(i2)).isCracked()) continue;
                newAlts.remove(i2);
            }
        }
        for (Alt newAlt : newAlts) {
            if (newAlt.isCracked()) {
                ++crackedAlts;
                continue;
            }
            ++premiumAlts;
        }
        alts = newAlts;
    }

    public void openUrl(String url) {
        try {
            Desktop desktop = Desktop.getDesktop();
            desktop.browse(new URI(url));
        }
        catch (Exception ex) {
            System.out.println("An error occurred while trying to open a url!");
        }
    }

    public class MicrosoftLoginDialog
    extends InfoDialog {
        public MicrosoftLoginDialog() {
            super(0.0f, 0.0f, 200.0f, 80.0f, "Microsoft Login", "Unknown State!");
        }

        @Override
        public void draw(int mouseX, int mouseY) {
            super.draw(mouseX, mouseY);
            this.message = GuiAltMgr.oAuthService.message;
            this.buttonText = this.message.contains("Successfully logged in with account") ? "OK" : "Cancel";
        }
    }

    public class KingAltInputDialog
    extends InputDialog {
        public KingAltInputDialog() {
            super(0.0f, 0.0f, 200.0f, 80.0f, "King Alts", KingAlts.API_KEY, "API Key");
            this.acceptButtonText = "Save API Key";
        }

        @Override
        public void acceptAction() {
            KingAlts.setApiKey(this.dtf.getText());
            super.acceptAction();
        }
    }

    public class DirectLoginDialog
    extends AltInputDialog {
        String displayText;

        public DirectLoginDialog() {
            super(0.0f, 0.0f, 200.0f, 80.0f, "Direct login", Minecraft.getMinecraft().getSession().getUsername(), "Username / Email:Password");
        }

        @Override
        public void acceptAction() {
            new Thread(() -> {
                this.title = "Logging in...";
                if (this.cef.getText().contains("@alt.com")) {
                    TheAlteningAuthentication.theAltening().updateService(AlteningServiceType.THEALTENING);
                    this.displayText = LoginUtils.login(this.cef.getText().split(":")[0], "Flux");
                    if (this.displayText != null) {
                        this.title = "[ERR TheAltening] " + this.displayText;
                    } else {
                        this.destroy();
                    }
                    return;
                }
                if (this.cef.getText().split(":").length <= 1) {
                    LoginUtils.changeCrackedName(this.cef.getText());
                    this.displayText = null;
                } else {
                    String email = this.cef.getText().split(":")[0];
                    String password = this.cef.getText().split(":")[1];
                    MicrosoftAuthenticator authenticator = new MicrosoftAuthenticator();
                    try {
                        MicrosoftAuthResult result = null;
                        try {
                            result = authenticator.loginWithCredentials(email, password);
                        }
                        catch (MicrosoftAuthenticationException e) {
                            throw new RuntimeException(e);
                        }
                        MinecraftProfile profile = result.getProfile();
                        Minecraft.getMinecraft().session = new Session(profile.getName(), profile.getId(), result.getAccessToken(), "microsoft");
                        System.out.println("Logged in with " + Minecraft.getMinecraft().session.getUsername());
                        Astroline.currentAlt = new String[]{email, password};
                    }
                    catch (RuntimeException e) {
                        throw new RuntimeException(e);
                    }
                }
                if (this.displayText == null) {
                    this.destroy();
                }
            }).start();
        }
    }

    public class ClearAllDialog
    extends ConfirmDialog {
        public ClearAllDialog() {
            super(0.0f, 0.0f, 200.0f, 60.0f, "Are you sure you want to remove EVERY alt?", (Object)((Object)EnumChatFormatting.RED) + "All alt" + (Object)((Object)EnumChatFormatting.WHITE) + " will be lost forever!");
        }

        @Override
        public void acceptAction() {
            alts.clear();
            GuiAltMgr.sortAlts();
            super.acceptAction();
        }
    }

    public class DeleteAltDialog
    extends ConfirmDialog {
        public Alt targetAlt;

        public DeleteAltDialog(Alt targetAlt) {
            super(0.0f, 0.0f, 200.0f, 60.0f, "Are you sure to delete this alt?", "\"" + targetAlt.getNameOrEmail() + "\" will be lost forever!");
            float stringWid = FontManager.sans16.getStringWidth(this.message) + 20.0f;
            this.width = stringWid < 140.0f ? 140.0f : (float)((int)stringWid);
            this.targetAlt = targetAlt;
        }

        @Override
        public void acceptAction() {
            alts.remove(this.targetAlt);
            GuiAltMgr.sortAlts();
            super.acceptAction();
        }
    }

    public class EditAltDialog
    extends AltInputDialog {
        public Alt targetAlt;
        String displayText;

        public EditAltDialog(Alt targetAlt) {
            super(0.0f, 0.0f, 200.0f, 80.0f, "Edit this alt", targetAlt.isCracked() ? targetAlt.getNameOrEmail() : targetAlt.getEmail() + ":" + targetAlt.getPassword(), "Username / Email:Password");
            this.targetAlt = targetAlt;
            this.acceptButtonText = "Save";
        }

        @Override
        public void acceptAction() {
            if (this.cef.getText().split(":").length <= 1) {
                alts.set(alts.indexOf(this.targetAlt), new Alt(this.cef.getText(), null, null, this.targetAlt.isStarred()));
                this.displayText = null;
            } else {
                String email = this.cef.getText().split(":")[0];
                String password = this.cef.getText().split(":")[1];
                this.displayText = LoginUtils.login(email, password);
                if (this.displayText == null) {
                    alts.set(alts.indexOf(this.targetAlt), new Alt(email, password, GuiAltMgr.this.mc.getSession().getUsername(), this.targetAlt.isStarred()));
                } else {
                    this.title = "[ERR] " + this.displayText;
                }
            }
            if (this.displayText == null) {
                GuiAltMgr.sortAlts();
                this.destroy();
            }
        }
    }

    public class AddAltDialog
    extends AltInputDialog {
        public String displayText;

        public AddAltDialog() {
            super(0.0f, 0.0f, 200.0f, 80.0f, "Add an alt", Minecraft.getMinecraft().getSession().getUsername(), "Username / Email:Password");
            this.displayText = null;
        }

        @Override
        public void acceptAction() {
            new Thread(() -> {
                if (this.cef.getText().contains("@alt.com")) {
                    TheAlteningAuthentication.theAltening().updateService(AlteningServiceType.THEALTENING);
                    this.displayText = LoginUtils.login(this.cef.getText().split(":")[0], "Astroline");
                    if (this.displayText == null) {
                        alts.add(0, new Alt(this.cef.getText().split(":")[0], "Astroline", GuiAltMgr.this.mc.getSession().getUsername()));
                        GuiAltMgr.sortAlts();
                        this.destroy();
                    } else {
                        this.title = "[ERR TheAltening]" + this.displayText;
                    }
                    return;
                }
                if (this.cef.getText().split(":").length <= 1) {
                    alts.add(0, new Alt(this.cef.getText(), null, null));
                    this.displayText = null;
                } else {
                    String email = this.cef.getText().split(":")[0];
                    String password = this.cef.getText().split(":")[1];
                    this.displayText = LoginUtils.login(email, password);
                    if (this.displayText == null) {
                        alts.add(0, new Alt(email, password, GuiAltMgr.this.mc.getSession().getUsername()));
                    } else {
                        this.title = "[ERR] " + this.displayText;
                    }
                }
                if (this.displayText == null) {
                    GuiAltMgr.sortAlts();
                    this.destroy();
                }
            }).start();
        }
    }
}

