/*
 * Copyright © 2015, 9x Media Pvt. Ltd.
 * Written under contract by Robosoft Technologies Pvt. Ltd.
 */

package com.ninexe.ui.managers;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.text.TextUtils;

import java.util.HashMap;
import java.util.Map;


public class FontManager {
    private static FontManager sFontManager;

    private AssetManager mAssetManager;

    private Map<String, Typeface> fonts;

    private FontManager(AssetManager _mgr) {
        mAssetManager = _mgr;
        fonts = new HashMap<String, Typeface>();
    }

    public static FontManager getInstance(Context context) {
        if (sFontManager == null) {
            sFontManager = new FontManager(context.getAssets());
        }
        return sFontManager;
    }

    public Typeface getFont(String asset) {
        if (fonts.containsKey(asset))
            return fonts.get(asset);

        Typeface font = null;

        try {
            font = Typeface.createFromAsset(mAssetManager, asset);
            fonts.put(asset, font);
        } catch (Exception e) {

        }

        if (font == null) {
            try {
                String fixedAsset = fixAssetFilename(asset);
                font = Typeface.createFromAsset(mAssetManager, fixedAsset);
                fonts.put(asset, font);
                fonts.put(fixedAsset, font);
            } catch (Exception e) {

            }
        }

        return font;
    }

    private String fixAssetFilename(String asset) {
        // Empty font filename?
        // Just return it. We can't help.
        if (TextUtils.isEmpty(asset))
            return asset;

        // Make sure that the font ends in '.ttf' or '.ttc'
        if ((!asset.endsWith(".ttf")) && (!asset.endsWith(".ttc")))
            asset = String.format("%s.ttf", asset);

        return asset;
    }
}
