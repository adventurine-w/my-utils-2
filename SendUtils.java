package org.adv.clickerflex.ultimate_utils.server_side;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.function.Predicate;

import static org.adv.clickerflex.ultimate_utils.Common.centerChat;
import static org.adv.clickerflex.utils.generic.ColorUtils.mmColored;

public class SendUtils {
    public static void sendLegacy(Player p,  String message){
        p.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
    }
    public static void send(Player p,  Component message){
        send(p, message, null, 0, 0);
    }
    public static void send(Player p,  String mm){
        send(p, mmColored(mm), null, 0, 0);
    }
    public static void send(Player p, Component message, Sound sound){
        send(p, message, sound, 1, 1);
    }
    public static void send(Player p, String mm, Sound sound){
        send(p, mmColored(mm), sound, 1, 1);
    }
    public static void send(Player p, String mm, Sound sound, float volume, float pitch){send(p, mmColored(mm), sound, volume, pitch);}
    public static void send(Player p, Component message, Sound sound, float volume, float pitch) {
        p.sendMessage(message);
        if (sound != null) {
            playSound(p, sound, volume, pitch);
        }
    }

    public static void sendCenter(Player p, Component message) {sendCenter(p, message, null, 0, 0);}
    public static void sendCenter(Player p, String mm) {sendCenter(p, mm, null, 0, 0);}
    public static void sendCenter(Player p, Component message, Sound sound) {sendCenter(p, message, sound, 1, 1);}
    public static void sendCenter(Player p, String mm, Sound sound) {sendCenter(p, mm, sound, 1, 1);}
    public static void sendCenter(Player p, String mm, Sound sound, float volume, float pitch) {
        sendCenter(p, mmColored(mm), sound, volume, pitch);
    }
    public static void sendCenter(Player p, Component message, Sound sound, float volume, float pitch) {send(p, centerChat(message), sound, volume, pitch);}





    public static void playSound(Player p, Sound sound, Location soundLoc){
        p.playSound(soundLoc, sound, 1f, 1f);
    }
    public static void playSound(Player p, Sound sound, float volume, float pitch, Location soundLoc){
        float first = Math.min(volume, 5f);
        if (first > 0) p.playSound(soundLoc, sound, first, pitch);

        // Handle extra tiers
        float remaining = volume - 5f;
        while (remaining > 0) {
            float thisTierValue = Math.min(5f, remaining);
            float scaled = getTieredVolume(thisTierValue, 0.3f, 0.8f);
            p.playSound(soundLoc, sound, scaled, pitch);
            remaining -= 5f;
        }
    }
    public static void playSound(Player p, Sound sound){
        playSound(p, sound, 1f, 1f);
    }
    public static void playSound(Player p, Sound sound, float volume, float pitch){
        float first = Math.min(volume, 5f);
        if (first > 0) p.playSound(p, sound, first, pitch);

        // Handle extra tiers
        float remaining = volume - 5f;
        while (remaining > 0) {
            float thisTierValue = Math.min(5f, remaining);
            float scaled = getTieredVolume(thisTierValue, 0.3f, 0.8f);
            p.playSound(p, sound, scaled, pitch);
            remaining -= 5f;
        }
    }
    public static float getTieredVolume(float v, float base, float step) {
        v = v-1;
        return base*(Math.min(v,1)) + v*step;
    }






    public static void broadcast(Component message) {
        broadcast(message, null, 0, 0, p -> true);
    }

    public static void broadcast(String mm) {
        broadcast(mmColored(mm), null, 0, 0, p -> true);
    }

    public static void broadcast(Component message, Sound sound) {
        broadcast(message, sound, 1, 1, p -> true);
    }

    public static void broadcast(String mm, Sound sound) {
        broadcast(mmColored(mm), sound, 1, 1, p -> true);
    }

    public static void broadcast(String mm, Sound sound, float volume, float pitch) {
        broadcast(mmColored(mm), sound, volume, pitch, p -> true);
    }

    public static void broadcast(Component message, Sound sound, float volume, float pitch) {
        broadcast(message, sound, volume, pitch, p -> true);
    }

    public static void broadcast(String mm, Predicate<Player> filter) {
        broadcast(mmColored(mm), null, 0, 0, filter);
    }

    public static void broadcast(Component message, Predicate<Player> filter) {
        broadcast(message, null, 0, 0, filter);
    }

    public static void broadcast(String mm, Sound sound, Predicate<Player> filter) {
        broadcast(mmColored(mm), sound, 1, 1, filter);
    }

    public static void broadcast(Component message, Sound sound, Predicate<Player> filter) {
        broadcast(message, sound, 1, 1, filter);
    }

    public static void broadcast(String mm, Sound sound, float volume, float pitch, Predicate<Player> filter) {
        broadcast(mmColored(mm), sound, volume, pitch, filter);
    }

    public static void broadcast(Component message, Sound sound, float volume, float pitch, Predicate<Player> filter) {
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (filter.test(p)) {
                p.sendMessage(message);
                if (sound != null) {
                    p.playSound(p, sound, volume, pitch);
                }
            }
        }
    }
}
