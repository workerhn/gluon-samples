/**
 * Copyright (c) 2016, Gluon
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 * notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 *     * Neither the name of Gluon, any associated website, nor the
 * names of its contributors may be used to endorse or promote products
 * derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL GLUON BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.gluonhq.gonative;

import com.gluonhq.gonative.views.MainView;
import com.gluonhq.gonative.views.PicturesView;
import com.gluonhq.charm.glisten.application.MobileApplication;
import com.gluonhq.charm.glisten.control.Avatar;
import com.gluonhq.charm.glisten.control.NavigationDrawer;
import com.gluonhq.charm.glisten.control.NavigationDrawer.Item;
import com.gluonhq.charm.glisten.layout.layer.SidePopupView;
import com.gluonhq.charm.glisten.mvc.View;
import com.gluonhq.charm.glisten.visual.MaterialDesignIcon;
import com.gluonhq.charm.glisten.visual.Swatch;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import com.gluonhq.gonative.views.AccelView;
import com.gluonhq.gonative.views.VibrationView;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javafx.beans.value.ChangeListener;
import javafx.scene.Node;

public class GoNative extends MobileApplication {

    public static final String MAIN_VIEW = HOME_VIEW;
    public static final String PICTURES_VIEW = "Pictures View";
    public static final String ACCEL_VIEW = "Accelerometer View";
    public static final String VIBRATION_VIEW = "Vibration View";
    public static final String MENU_LAYER = "Side Menu";
    
    private final Map<String, String> itemsMap = new HashMap<>();
    
    private final ChangeListener<Node> listener = (obs, oldItem, newItem) -> {
            hideLayer(MENU_LAYER);
            Iterator<Map.Entry<String, String>> iterator = itemsMap.entrySet().iterator();
            while (iterator.hasNext()) {
                final Map.Entry<String, String> next = iterator.next();
                if (next.getValue().equals(((Item) newItem).getTitle())) {
                    switchView(next.getKey());
                    break;
                }
            }
        };
    
    @Override
    public void init() {
        itemsMap.put(MAIN_VIEW, "Main");
        itemsMap.put(PICTURES_VIEW, "Pictures");
        itemsMap.put(ACCEL_VIEW, "Accelerometer");
        itemsMap.put(VIBRATION_VIEW, "Vibration");
        
        addViewFactory(MAIN_VIEW, () -> (View) new MainView().getView());
        addViewFactory(PICTURES_VIEW, () -> (View) new PicturesView().getView());
        addViewFactory(ACCEL_VIEW, () -> (View) new AccelView().getView());
        addViewFactory(VIBRATION_VIEW, () -> (View) new VibrationView().getView());
        
        NavigationDrawer drawer = new NavigationDrawer();
        
        NavigationDrawer.Header header = new NavigationDrawer.Header("Gluon Mobile",
                "Multi View Project",
                new Avatar(21, new Image(GoNative.class.getResourceAsStream("/icon.png"))));
        drawer.setHeader(header);
        
        final Item primaryItem = new Item(itemsMap.get(MAIN_VIEW), MaterialDesignIcon.HOME.graphic());
        final Item picturesItem = new Item(itemsMap.get(PICTURES_VIEW), MaterialDesignIcon.CAMERA.graphic());
        final Item accelItem = new Item(itemsMap.get(ACCEL_VIEW), MaterialDesignIcon.LEAK_ADD.graphic());
        final Item vibrationItem = new Item(itemsMap.get(VIBRATION_VIEW), MaterialDesignIcon.VIBRATION.graphic());
        drawer.getItems().addAll(primaryItem, picturesItem, accelItem, vibrationItem);
        
        primaryItem.setSelected(true);
        drawer.selectedItemProperty().addListener(listener);
        
        viewProperty().addListener((obs, ov, nv) -> {
            for (Node node : drawer.getItems()) {
                NavigationDrawer.Item item = (NavigationDrawer.Item) node;
                if (itemsMap.get(nv.getName()).equals(item.getTitle())) {
                    drawer.selectedItemProperty().removeListener(listener);
                    drawer.setSelectedItem(node);
                    item.setSelected(true);
                    drawer.selectedItemProperty().addListener(listener);
                } else {
                    item.setSelected(false);
                }
            }
        });
        
        addLayerFactory(MENU_LAYER, () -> new SidePopupView(drawer));
    }

    @Override
    public void postInit(Scene scene) {
        Swatch.BLUE.assignTo(scene);

        scene.getStylesheets().add(GoNative.class.getResource("style.css").toExternalForm());
        ((Stage) scene.getWindow()).getIcons().add(new Image(GoNative.class.getResourceAsStream("/icon.png")));
    }
}
