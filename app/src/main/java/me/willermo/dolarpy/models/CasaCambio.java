package me.willermo.dolarpy.models;

/**
 * Created by william on 9/5/15.
 */
public abstract class CasaCambio {
    private String compra;
    private String venta;

    public String getCompra() {
        return compra;
    }

    public void setCompra(String compra) {
        this.compra = compra;
    }

    public String getVenta() {
        return venta;
    }

    public void setVenta(String venta) {
        this.venta = venta;
    }
}
