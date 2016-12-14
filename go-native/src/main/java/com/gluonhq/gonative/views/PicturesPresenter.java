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
package com.gluonhq.gonative.views;

import com.gluonhq.charm.glisten.animation.BounceInRightTransition;
import com.gluonhq.charm.glisten.application.MobileApplication;
import com.gluonhq.charm.glisten.control.AppBar;
import com.gluonhq.charm.glisten.control.CardPane;
import com.gluonhq.charm.glisten.mvc.View;
import com.gluonhq.charm.glisten.visual.MaterialDesignIcon;
import com.gluonhq.gonative.GoNative;
import com.gluonhq.gonative.GoNativePlatformFactory;
import com.gluonhq.gonative.NativeService;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;

public class PicturesPresenter {

    @FXML
    private View pictures;
    
    @FXML
    private CardPane cardPane;
    
    private ImageView imageView;

    public void initialize() {
        final NativeService nativeService = GoNativePlatformFactory.getPlatform().getNativeService();

        pictures.setShowTransitionFactory(BounceInRightTransition::new);
        
        pictures.showingProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                AppBar appBar = MobileApplication.getInstance().getAppBar();
                appBar.setNavIcon(MaterialDesignIcon.MENU.button(e -> 
                        MobileApplication.getInstance().showLayer(GoNative.MENU_LAYER)));
                appBar.setTitleText("Picture Service");
                appBar.getActionItems().addAll(
                        MaterialDesignIcon.CAMERA.button(e -> nativeService.takePicture()),
                        MaterialDesignIcon.COLLECTIONS.button(e -> nativeService.retrievePicture()));
            }
        });
        
        imageView = new ImageView();
        imageView.setImage(nativeService.imageProperty().get());
        imageView.fitWidthProperty().bind(pictures.widthProperty().subtract(60));
        imageView.fitHeightProperty().bind(pictures.heightProperty().subtract(60));
        imageView.setPreserveRatio(true);
        
        cardPane.getCards().add(imageView);
        
        nativeService.imageProperty().addListener((obs, ov, nv) -> imageView.setImage(nv));
    }
    
}
