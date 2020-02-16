package me.xorrad.practice.elo;

import org.bukkit.entity.Player;

public class EloSys {
    
    public static void replaceElo(Player winner, Player looser, String mode) {
        if (EloConfig.getElo(winner, mode) < EloConfig.getElo(looser, mode)) {
            int elow = EloConfig.getElo(looser, mode) - EloConfig.getElo(winner, mode);
            if (elow >= 0 && elow <= 24) {
                EloConfig.addElo(winner, mode, 16);
                EloConfig.removeElo(looser, mode, 16);
                winner.sendMessage ("§eElo Changes: §a" + winner.getName() + " +16 (" + EloConfig.getElo(winner, mode) + ") §c" + looser.getName() + " -16 (" + EloConfig.getElo(looser, mode) + ")");
                looser.sendMessage ("§eElo Changes: §a" + winner.getName() + " +16 (" + EloConfig.getElo(winner, mode) + ") §c" + looser.getName() + " -16 (" + EloConfig.getElo(looser, mode) + ")");
            }
            if (elow >= 25 && elow <= 49) {
                EloConfig.addElo(winner, mode, 17);
                EloConfig.removeElo(looser, mode, 17);
                winner.sendMessage ("§eElo Changes: §a" + winner.getName() + " +17 (" + EloConfig.getElo(winner, mode) + ") §c" + looser.getName() + " -17 (" + EloConfig.getElo(looser, mode) + ")");
                looser.sendMessage ("§eElo Changes: §a" + winner.getName() + " +17 (" + EloConfig.getElo(winner, mode) + ") §c" + looser.getName() + " -17 (" + EloConfig.getElo(looser, mode) + ")");
            }
            if (elow >= 50 && elow <= 74) {
                EloConfig.addElo(winner, mode, 18);
                EloConfig.removeElo(looser, mode, 18);
                winner.sendMessage ("§eElo Changes: §a" + winner.getName() + " +18 (" + EloConfig.getElo(winner, mode) + ") §c" + looser.getName() + " -18 (" + EloConfig.getElo(looser, mode) + ")");
                looser.sendMessage ("§eElo Changes: §a" + winner.getName() + " +18 (" + EloConfig.getElo(winner, mode) + ") §c" + looser.getName() + " -18 (" + EloConfig.getElo(looser, mode) + ")");
            }
            if (elow >= 75 && elow <= 99) {
                EloConfig.addElo(winner, mode, 19);
                EloConfig.removeElo(looser, mode, 19);
                winner.sendMessage ("§eElo Changes: §a" + winner.getName() + " +19 (" + EloConfig.getElo(winner, mode) + ") §c" + looser.getName() + " -19 (" + EloConfig.getElo(looser, mode) + ")");
                looser.sendMessage ("§eElo Changes: §a" + winner.getName() + " +19 (" + EloConfig.getElo(winner, mode) + ") §c" + looser.getName() + " -19 (" + EloConfig.getElo(looser, mode) + ")");
            }
            if (elow >= 100 && elow <= 124) {
                EloConfig.addElo(winner, mode, 20);
                EloConfig.removeElo(looser, mode, 20);
                winner.sendMessage ("§eElo Changes: §a" + winner.getName() + " +20 (" + EloConfig.getElo(winner, mode) + ") §c" + looser.getName() + " -20 (" + EloConfig.getElo(looser, mode) + ")");
                looser.sendMessage ("§eElo Changes: §a" + winner.getName() + " +20 (" + EloConfig.getElo(winner, mode) + ") §c" + looser.getName() + " -20 (" + EloConfig.getElo(looser, mode) + ")");
            }
            if (elow >= 125 && elow <= 149) {
                EloConfig.addElo(winner, mode, 21);
                EloConfig.removeElo(looser, mode, 21);
                winner.sendMessage ("§eElo Changes: §a" + winner.getName() + " +21 (" + EloConfig.getElo(winner, mode) + ") §c" + looser.getName() + " -21 (" + EloConfig.getElo(looser, mode) + ")");
                looser.sendMessage ("§eElo Changes: §a" + winner.getName() + " +21 (" + EloConfig.getElo(winner, mode) + ") §c" + looser.getName() + " -21 (" + EloConfig.getElo(looser, mode) + ")");
            }
            if (elow >= 150 && elow <= 174) {
                EloConfig.addElo(winner, mode, 22);
                EloConfig.removeElo(looser, mode, 22);
                winner.sendMessage ("§eElo Changes: §a" + winner.getName() + " +22 (" + EloConfig.getElo(winner, mode) + ") §c" + looser.getName() + " -22 (" + EloConfig.getElo(looser, mode) + ")");
                looser.sendMessage ("§eElo Changes: §a" + winner.getName() + " +22 (" + EloConfig.getElo(winner, mode) + ") §c" + looser.getName() + " -22 (" + EloConfig.getElo(looser, mode) + ")");
            }
            if (elow >= 175 && elow <= 199) {
                EloConfig.addElo(winner, mode, 23);
                EloConfig.removeElo(looser, mode, 23);
                winner.sendMessage ("§eElo Changes: §a" + winner.getName() + " +23 (" + EloConfig.getElo(winner, mode) + ") §c" + looser.getName() + " -23 (" + EloConfig.getElo(looser, mode) + ")");
                looser.sendMessage ("§eElo Changes: §a" + winner.getName() + " +23 (" + EloConfig.getElo(winner, mode) + ") §c" + looser.getName() + " -23 (" + EloConfig.getElo(looser, mode) + ")");
            }
            if (elow >= 200 && elow <= 224) {
                EloConfig.addElo(winner, mode, 24);
                EloConfig.removeElo(looser, mode, 24);
                winner.sendMessage ("§eElo Changes: §a" + winner.getName() + " +24 (" + EloConfig.getElo(winner, mode) + ") §c" + looser.getName() + " -24 (" + EloConfig.getElo(looser, mode) + ")");
                looser.sendMessage ("§eElo Changes: §a" + winner.getName() + " +24 (" + EloConfig.getElo(winner, mode) + ") §c" + looser.getName() + " -24 (" + EloConfig.getElo(looser, mode) + ")");
            }
            if (elow >= 225 && elow <= 249) {
                EloConfig.addElo(winner, mode, 25);
                EloConfig.removeElo(looser, mode, 25);
                winner.sendMessage ("§eElo Changes: §a" + winner.getName() + " +25 (" + EloConfig.getElo(winner, mode) + ") §c" + looser.getName() + " -25 (" + EloConfig.getElo(looser, mode) + ")");
                looser.sendMessage ("§eElo Changes: §a" + winner.getName() + " +25 (" + EloConfig.getElo(winner, mode) + ") §c" + looser.getName() + " -25 (" + EloConfig.getElo(looser, mode) + ")");
            }
            if (elow >= 250 && elow <= 274) {
                EloConfig.addElo(winner, mode, 26);
                EloConfig.removeElo(looser, mode, 26);
                winner.sendMessage ("§eElo Changes: §a" + winner.getName() + " +26 (" + EloConfig.getElo(winner, mode) + ") §c" + looser.getName() + " -26 (" + EloConfig.getElo(looser, mode) + ")");
                looser.sendMessage ("§eElo Changes: §a" + winner.getName() + " +26 (" + EloConfig.getElo(winner, mode) + ") §c" + looser.getName() + " -26 (" + EloConfig.getElo(looser, mode) + ")");
            }
            if (elow >= 275 && elow <= 299) {
                EloConfig.addElo(winner, mode, 27);
                EloConfig.removeElo(looser, mode, 27);
                winner.sendMessage ("§eElo Changes: §a" + winner.getName() + " +27 (" + EloConfig.getElo(winner, mode) + ") §c" + looser.getName() + " -27 (" + EloConfig.getElo(looser, mode) + ")");
                looser.sendMessage ("§eElo Changes: §a" + winner.getName() + " +27 (" + EloConfig.getElo(winner, mode) + ") §c" + looser.getName() + " -27 (" + EloConfig.getElo(looser, mode) + ")");
            }
            if (elow > 299) {
                EloConfig.addElo(winner, mode, 28);
                EloConfig.removeElo(looser, mode, 28);
                winner.sendMessage ("§eElo Changes: §a" + winner.getName() + " +28 (" + EloConfig.getElo(winner, mode) + ") §c" + looser.getName() + " -28 (" + EloConfig.getElo(looser, mode) + ")");
                looser.sendMessage ("§eElo Changes: §a" + winner.getName() + " +28 (" + EloConfig.getElo(winner, mode) + ") §c" + looser.getName() + " -28 (" + EloConfig.getElo(looser, mode) + ")");
            }
        }
        else {
            int elow = EloConfig.getElo(winner, mode) - EloConfig.getElo(looser, mode);
            if (elow >= 0 && elow <= 24) {
                EloConfig.addElo(winner, mode, 16);
                EloConfig.removeElo(looser, mode, 16);
                winner.sendMessage ("§eElo Changes: §a" + winner.getName() + " +16 (" + EloConfig.getElo(winner, mode) + ") §c" + looser.getName() + " -16 (" + EloConfig.getElo(looser, mode) + ")");
                looser.sendMessage ("§eElo Changes: §a" + winner.getName() + " +16 (" + EloConfig.getElo(winner, mode) + ") §c" + looser.getName() + " -16 (" + EloConfig.getElo(looser, mode) + ")");
            }
            if (elow >= 25 && elow <= 49) {
                EloConfig.addElo(winner, mode, 15);
                EloConfig.removeElo(looser, mode, 15);
                winner.sendMessage ("§eElo Changes: §a" + winner.getName() + " +15 (" + EloConfig.getElo(winner, mode) + ") §c" + looser.getName() + " -15 (" + EloConfig.getElo(looser, mode) + ")");
                looser.sendMessage ("§eElo Changes: §a" + winner.getName() + " +15 (" + EloConfig.getElo(winner, mode) + ") §c" + looser.getName() + " -15 (" + EloConfig.getElo(looser, mode) + ")");
            }
            if (elow >= 50 && elow <= 74) {
                EloConfig.addElo(winner, mode, 14);
                EloConfig.removeElo(looser, mode, 14);
                winner.sendMessage ("§eElo Changes: §a" + winner.getName() + " +14 (" + EloConfig.getElo(winner, mode) + ") §c" + looser.getName() + " -14 (" + EloConfig.getElo(looser, mode) + ")");
                looser.sendMessage ("§eElo Changes: §a" + winner.getName() + " +14 (" + EloConfig.getElo(winner, mode) + ") §c" + looser.getName() + " -14 (" + EloConfig.getElo(looser, mode) + ")");
            }
            if (elow >= 75 && elow <= 99) {
                EloConfig.addElo(winner, mode, 13);
                EloConfig.removeElo(looser, mode, 13);
                winner.sendMessage ("§eElo Changes: §a" + winner.getName() + " +13 (" + EloConfig.getElo(winner, mode) + ") §c" + looser.getName() + " -13 (" + EloConfig.getElo(looser, mode) + ")");
                looser.sendMessage ("§eElo Changes: §a" + winner.getName() + " +13 (" + EloConfig.getElo(winner, mode) + ") §c" + looser.getName() + " -13 (" + EloConfig.getElo(looser, mode) + ")");
            }
            if (elow >= 100 && elow <= 124) {
                EloConfig.addElo(winner, mode, 12);
                EloConfig.removeElo(looser, mode, 12);
                winner.sendMessage ("§eElo Changes: §a" + winner.getName() + " +12 (" + EloConfig.getElo(winner, mode) + ") §c" + looser.getName() + " -12 (" + EloConfig.getElo(looser, mode) + ")");
                looser.sendMessage ("§eElo Changes: §a" + winner.getName() + " +12 (" + EloConfig.getElo(winner, mode) + ") §c" + looser.getName() + " -12 (" + EloConfig.getElo(looser, mode) + ")");
            }
            if (elow >= 125 && elow <= 149) {
                EloConfig.addElo(winner, mode, 11);
                EloConfig.removeElo(looser, mode, 11);
                winner.sendMessage ("§eElo Changes: §a" + winner.getName() + " +11 (" + EloConfig.getElo(winner, mode) + ") §c" + looser.getName() + " -11 (" + EloConfig.getElo(looser, mode) + ")");
                looser.sendMessage ("§eElo Changes: §a" + winner.getName() + " +11 (" + EloConfig.getElo(winner, mode) + ") §c" + looser.getName() + " -11 (" + EloConfig.getElo(looser, mode) + ")");
            }
            if (elow >= 150 && elow <= 174) {
                EloConfig.addElo(winner, mode, 10);
                EloConfig.removeElo(looser, mode, 10);
                winner.sendMessage ("§eElo Changes: §a" + winner.getName() + " +10 (" + EloConfig.getElo(winner, mode) + ") §c" + looser.getName() + " -10 (" + EloConfig.getElo(looser, mode) + ")");
                looser.sendMessage ("§eElo Changes: §a" + winner.getName() + " +10 (" + EloConfig.getElo(winner, mode) + ") §c" + looser.getName() + " -10 (" + EloConfig.getElo(looser, mode) + ")");
            }
            if (elow >= 175 && elow <= 199) {
                EloConfig.addElo(winner, mode, 9);
                EloConfig.removeElo(looser, mode, 9);
                winner.sendMessage ("§eElo Changes: §a" + winner.getName() + " +9 (" + EloConfig.getElo(winner, mode) + ") §c" + looser.getName() + " -9 (" + EloConfig.getElo(looser, mode) + ")");
                looser.sendMessage ("§eElo Changes: §a" + winner.getName() + " +9 (" + EloConfig.getElo(winner, mode) + ") §c" + looser.getName() + " -9 (" + EloConfig.getElo(looser, mode) + ")");
            }
            if (elow >= 200 && elow <= 224) {
                EloConfig.addElo(winner, mode, 8);
                EloConfig.removeElo(looser, mode, 8);
                winner.sendMessage ("§eElo Changes: §a" + winner.getName() + " +8 (" + EloConfig.getElo(winner, mode) + ") §c" + looser.getName() + " -8 (" + EloConfig.getElo(looser, mode) + ")");
                looser.sendMessage ("§eElo Changes: §a" + winner.getName() + " +8 (" + EloConfig.getElo(winner, mode) + ") §c" + looser.getName() + " -8 (" + EloConfig.getElo(looser, mode) + ")");
            }
            if (elow >= 225 && elow <= 249) {
                EloConfig.addElo(winner, mode, 7);
                EloConfig.removeElo(looser, mode, 7);
                winner.sendMessage ("§eElo Changes: §a" + winner.getName() + " +7 (" + EloConfig.getElo(winner, mode) + ") §c" + looser.getName() + " -7 (" + EloConfig.getElo(looser, mode) + ")");
                looser.sendMessage ("§eElo Changes: §a" + winner.getName() + " +7 (" + EloConfig.getElo(winner, mode) + ") §c" + looser.getName() + " -75 (" + EloConfig.getElo(looser, mode) + ")");
            }
            if (elow >= 250 && elow <= 274) {
                EloConfig.addElo(winner, mode, 6);
                EloConfig.removeElo(looser, mode, 6);
                winner.sendMessage ("§eElo Changes: §a" + winner.getName() + " +6 (" + EloConfig.getElo(winner, mode) + ") §c" + looser.getName() + " -6 (" + EloConfig.getElo(looser, mode) + ")");
                looser.sendMessage ("§eElo Changes: §a" + winner.getName() + " +6 (" + EloConfig.getElo(winner, mode) + ") §c" + looser.getName() + " -6 (" + EloConfig.getElo(looser, mode) + ")");
            }
            if (elow >= 275 && elow <= 299) {
                EloConfig.addElo(winner, mode, 5);
                EloConfig.removeElo(looser, mode, 5);
                winner.sendMessage ("§eElo Changes: §a" + winner.getName() + " +5 (" + EloConfig.getElo(winner, mode) + ") §c" + looser.getName() + " -5 (" + EloConfig.getElo(looser, mode) + ")");
                looser.sendMessage ("§eElo Changes: §a" + winner.getName() + " +5 (" + EloConfig.getElo(winner, mode) + ") §c" + looser.getName() + " -5 (" + EloConfig.getElo(looser, mode) + ")");
            }
            if (elow > 299) {
                EloConfig.addElo(winner, mode, 4);
                EloConfig.removeElo(looser, mode, 4);
                winner.sendMessage ("§eElo Changes: §a" + winner.getName() + " +4 (" + EloConfig.getElo(winner, mode) + ") §c" + looser.getName() + " -4 (" + EloConfig.getElo(looser, mode) + ")");
                looser.sendMessage ("§eElo Changes: §a" + winner.getName() + " +4 (" + EloConfig.getElo(winner, mode) + ") §c" + looser.getName() + " -4 (" + EloConfig.getElo(looser, mode) + ")");
            }
        }
    }
}

