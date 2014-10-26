package me.mike.bukkit.jetpack;

import java.io.File;
import java.util.List;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

public class Jetpack{
	
	File statsFile;
	FileConfiguration stats;
	Plugin plugin = (Plugin) this;
	
	public Jetpack(int fuel2, List<String> lore, ItemMeta meta, Player player) {
		
	}
	
	@SuppressWarnings("deprecation")
	public void removeFuel(final int fuel,final int end,final List<String> lore,final ItemStack jetpack,final FileConfiguration stats,final Player player){
		
		plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
            public void run() {
            	//int runFuel = fuel, runEnd = end;
            	List<String> runLore = lore;
            	ItemStack runJetpack = jetpack;
            	Player runPlayer = player;
            	runLore.remove(end);
        		runLore.add(fuel + "");
        		ItemMeta meta = jetpack.getItemMeta();
        		meta.setLore(lore);
        		runJetpack.setItemMeta(meta);
        		stats.set(player.getName() + ".fuelLevel", fuel);
        		saveFile();
        		runPlayer.updateInventory();
            }
        }, 0, 20);
		
		}
		/*
		 * I think I should use a task scheduler and use a do{}while loop to run it only when i need it. or using a self-canceling scheduler.
		 * BukkitTask task = new ExampleTask(this.plugin).runTaskLater(this.plugin, 20);
		 * 
		 */
	
	
	public void saveFile(){
		try{
			stats.save(statsFile);
		} catch (Exception e) {
			
		}
	}

	
}
