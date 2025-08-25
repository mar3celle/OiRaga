import java.util.ArrayList;
import java.util.List;

public class World {

    private final int width;
    private final int height;           //define altura e largura

    private Player player; //recebe um player

    private final List<Player> bots = new ArrayList<>();
    private final List<Pallet> pallets= new ArrayList<>();

    private static final int BOT_COUNT= 12; //n de outros bots
    private static final int PALLET_COUNT= 1500;    //n de pallets iniciais

    public World(int width, int height){
        this.width= width;
        this.height= height;
    }



}
