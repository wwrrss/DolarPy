package me.willermo.dolarpy.models;

/**
 * Created by william on 9/5/15.
 */
public class DolarPy {
  private Maxicambios maxicambios;
  private Cambiosalberdi cambiosalberdi;
  private Cambioschaco cambioschaco;

    public Maxicambios getMaxicambios() {
        return maxicambios;
    }

    public void setMaxicambios(Maxicambios maxicambios) {
        this.maxicambios = maxicambios;
    }

    public Cambiosalberdi getCambiosalberdi() {
        return cambiosalberdi;
    }

    public void setCambiosalberdi(Cambiosalberdi cambiosalberdi) {
        this.cambiosalberdi = cambiosalberdi;
    }

    public Cambioschaco getCambioschaco() {
        return cambioschaco;
    }

    public void setCambioschaco(Cambioschaco cambioschaco) {
        this.cambioschaco = cambioschaco;
    }
}
