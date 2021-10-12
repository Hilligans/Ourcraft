package dev.Hilligans.Data.Other;

import dev.Hilligans.Client.MatrixStack;
import dev.Hilligans.Client.Rendering.Textures;
import dev.Hilligans.Client.Rendering.World.StringRenderer;
import dev.Hilligans.Data.Primitives.Tuplet;
import dev.Hilligans.Util.Settings;

import java.util.ArrayList;

public class PlayerList {

    ArrayList<Tuplet<String, Integer>> players = new ArrayList<>();

    public PlayerList(String[] players, int[] ids) {
        addPlayers(players,ids);
    }

    public void sort() {
        players.sort((o1, o2) -> o1.getTypeA().compareToIgnoreCase(o2.typeA));
    }

    public void addPlayers(String[] players, int[] ids) {
        for(int x = 0; x < players.length; x++) {
            this.players.add(new Tuplet<>(players[x],ids[x]));
        }
        sort();
    }

    public void removePlayers(String[] players, int[] ids) {
        for(int x = 0; x < players.length; x++) {
            for(int y = 0; y < this.players.size(); y++) {
                Tuplet<String, Integer> player = this.players.get(y);
                if(player.getTypeA().equals(players[x]) && player.getTypeB() == ids[x]) {
                    this.players.remove(y);
                    break;
                }
            }
        }
    }

    public void render(MatrixStack matrixStack) {
        int stringSize = (int) (StringRenderer.instance.stringHeight * 0.5f);
        int x = Textures.TRANSPARENT_BACKGROUND.drawCenteredXTexture(matrixStack,0, Settings.guiSize * 64,players.size() * stringSize + 2);
        int y = 0;
        for(Tuplet<String, Integer> player : players) {
            StringRenderer.drawString(matrixStack,player.typeA,x,y,0.5f);
            y += stringSize;
        }
    }





}
