package net.ayato.tksmod.item;

public class Steel_Ingot extends AbstractTKSItem{
    public static final String ID = "steel_ingot";
    public Steel_Ingot() {
        super(new Properties().stacksTo(64));
    }

    @Override
    protected String getDescripted() {
        return ID;
    }
}
