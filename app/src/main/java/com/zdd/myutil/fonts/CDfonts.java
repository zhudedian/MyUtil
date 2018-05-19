package com.zdd.myutil.fonts;

import android.support.annotation.StringDef;

/**
 * Created by yd on 2018/5/18.
 */

@StringDef({
        CDfonts.SPADES,
        CDfonts.CLUBS,
        CDfonts.DIAMONDS,
        CDfonts.HEART
})

public @interface CDfonts {

    String SPADES = "\ue917";

    String CLUBS = "\ue918";

    String DIAMONDS = "\ue919";

    String HEART = "\ue9da";

}
