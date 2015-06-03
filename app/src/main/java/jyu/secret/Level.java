package jyu.secret;

import android.graphics.drawable.Drawable;

/**
 * Created by jyu on 15-5-28.
 */
public enum Level {

    NORNAL(0,"普通",R.color.level_normal),
    IMPORTANT(1,"重要",R.color.level_important),
    PRIVATE(2,"私密",R.color.level_private);


    private final int id;
    private final String name;
    private final int color;

    Level(int id, String name, int color) {

        this.id = id;
        this.name = name;
        this.color = color;
    }

    public static Level byId(long id){
        for(Level l:values()){
            if(l.id==id){
                return l;
            }
        }
        return null;
    }

    public String getName() {
        return name;
    }

    public int getColor() {
        return color;
    }
}
