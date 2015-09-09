package me.willermo.dolarpy.models;

import java.util.Date;

/**
 * Created by william on 9/5/15.
 */
public class MainObject {
    private DolarPy dolarpy;

    private Date updated;

    public DolarPy getDolarpy() {
        return dolarpy;
    }

    public void setDolarpy(DolarPy dolarpy) {
        this.dolarpy = dolarpy;
    }

    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }
}
