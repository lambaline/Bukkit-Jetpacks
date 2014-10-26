package me.mike.bukkit.jetpack;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin{

	File statsFile;
	FileConfiguration stats;
	
	public void onEnable(){
		getLogger().info("Plugin enabled!");
		getServer().getPluginManager().registerEvents(new LoginListener(), this);
		String type;
		
		statsFile = new File(getDataFolder(), "jetpacks.yml");
		stats = YamlConfiguration.loadConfiguration(statsFile);
		
		type = "iron";
		ShapedRecipe ironJetpack = new ShapedRecipe(new ItemStack(Jetpacks(type)))
			.shape("* *","*&*","/ /")
			.setIngredient('*', Material.IRON_INGOT)
			.setIngredient('&', Material.IRON_CHESTPLATE)
			.setIngredient('/', Material.BUCKET);
		type = "gold";
		ShapedRecipe GoldJetpack = new ShapedRecipe(new ItemStack(Jetpacks(type)))
			.shape("* *","*&*","/ /")
			.setIngredient('*', Material.GOLD_INGOT)
			.setIngredient('&', Material.GOLD_CHESTPLATE)
			.setIngredient('/', Material.BUCKET);
		type = "diamond";
		ShapedRecipe diamondJetpack = new ShapedRecipe(new ItemStack(Jetpacks(type)))
			.shape("* *","*&*","/ /")
			.setIngredient('*', Material.DIAMOND)
			.setIngredient('&', Material.DIAMOND_CHESTPLATE)
			.setIngredient('/', Material.BUCKET);
		
		getServer().addRecipe(ironJetpack);
		getServer().addRecipe(GoldJetpack);
		getServer().addRecipe(diamondJetpack);
	}

	private ItemStack Jetpacks(String type) {
		ItemStack jetpack = new ItemStack(Material.CHAINMAIL_CHESTPLATE);
		
		ItemMeta meta = (ItemMeta) jetpack.getItemMeta();
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("A genuine jetpack!");//0
		lore.add("Maximum fuel: ");//1
		if(type == "iron"){
			jetpack = new ItemStack(Material.IRON_CHESTPLATE);
			meta.setDisplayName("Iron Jetpack");
			jetpack.setAmount(-1);
			lore.add("1000");//2
		}
		else if(type == "gold")
		{
			jetpack = new ItemStack(Material.GOLD_CHESTPLATE);
			meta.setDisplayName(ChatColor.GOLD + "Golden Jetpack");
			lore.add("10000");
		}
		else if(type == "diamond"){
			jetpack = new ItemStack(Material.DIAMOND_CHESTPLATE);
			meta.setDisplayName(ChatColor.AQUA + "Diamond Jetpack");
			lore.add("100000");
		}
		
		lore.add(ChatColor.DARK_PURPLE + "Fuel: ");
		lore.add("0");
		meta.setLore(lore);
		jetpack.setItemMeta(meta);
		return jetpack;
	}

	public void saveFile(){
		try{
			stats.save(statsFile);
		} catch (Exception e) {
			
		}
	}
	
	public void onDisable(){
		getLogger().info("Plugin disabled!");
	}
	
	@SuppressWarnings("deprecation")
	public void hoverModeSubtractor(int fuel, List<String> lore,
			ItemMeta meta, ItemStack jetpack, int end, Player player){
		fuel = fuel - 5;
		lore.remove(end);
		lore.add(fuel + "");
		meta.setLore(lore);
		jetpack.setItemMeta(meta);
		stats.set(player.getName() + ".fuelLevel", fuel);
		saveFile();
		player.updateInventory();
		
	}
	
	final class LoginListener implements Listener{
		
		@SuppressWarnings("deprecation")
		@EventHandler
		public void onPlayerMove(final PlayerMoveEvent move)
		{
			
			Player player = move.getPlayer();
			ItemStack jetpack = player.getInventory().getChestplate(); //this gets the player's inventory and the chestplate they're wearing
			ItemMeta meta = jetpack.getItemMeta();
			List<String> lore = meta.getLore();
			int end = lore.size() - 1;
			int fuel = stats.getInt(player.getName() + ".fuelLevel");
			
			/*new BukkitRunnable(){
				public void run()
				{
					Player player = move.getPlayer();
					ItemStack jetpack = player.getInventory().getChestplate(); //this gets the player's inventory and the chestplate they're wearing
					ItemMeta meta = jetpack.getItemMeta();
					List<String> lore = meta.getLore();
					int end = lore.size() - 1;
					int fuel = stats.getInt(player.getName() + ".fuelLevel");
					if(lore.get(0).equals("A genuine jetpack!"))
					{
						if(player.getVelocity().length() == 0.0 && fuel >= 0 && player.isFlying() == false)
						{
							
						}
					}
				}
			}.runTaskLater(this, 20);*/
			//BukkitTask task = new removeFuel(this).runTaskLater(this, 20);
			if(lore.get(0).equals("A genuine jetpack!"))
			{
				//player.sendMessage("You can fly!");
				if(player.isFlying() == true)
				{
					fuel = fuel - 10;
					lore.remove(end);
					lore.add(fuel + "");
					meta.setLore(lore);
					jetpack.setItemMeta(meta);
					stats.set(player.getName() + ".fuelLevel", fuel);
					saveFile();
					player.updateInventory();
					Jetpack jetpack1 = new Jetpack(fuel, lore, meta, player);
					if(fuel <= 0)
					{
						player.setAllowFlight(false);
						player.sendMessage(ChatColor.RED + "You have run out of fuel!");
					}
					if(player.isFlying() == true)
					{
						Location loc = player.getLocation();
						Firework firework = player.getWorld().spawn(loc, Firework.class);
				        FireworkMeta data = (FireworkMeta) firework.getFireworkMeta();
				       // data.addEffects(FireworkEffect.builder().withColor(Color.GREEN).with(Type.BALL_LARGE).build());
				       // data.set
				        data.setPower(0);
				        
				        firework.setFireworkMeta(data);
				        firework.detonate();
					}
					//player.sendMessage("velocity = " + player.getVelocity().length());
					//player.sendMessage("Fly statis  = " + player.getAllowFlight());
					
					if(player.getVelocity().length() == 0.0 && (fuel>0) && player.isFlying() == true)
					{
						do{
							jetpack1.removeFuel(fuel, end, lore, jetpack, stats, player);
						}while(player.isFlying() && fuel >= 0 && player.getVelocity().length() == 0.0);
					}
					
					/*do
					{
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						
					}while((player.getVelocity().length() == 0.0) && ((fuel > 0) && (player.isFlying() == true)));*/ //velocity == 0, fuel is greater or equal to 0, player is flying,
																												//no,				fuel = 100,					player is flying
				}
				if(player.isFlying() == false && fuel > 0)
				{
					player.setAllowFlight(true);
				}
			}			
		}
		

		@SuppressWarnings("deprecation")
		@EventHandler
		public void onPlayerClick(final PlayerInteractEvent click)
		{
			Player player = click.getPlayer();	
			ItemStack tempfuel = player.getItemInHand();
			Material fuel = tempfuel.getType();
			int amtOfFuel;
			int statsFuel = stats.getInt(player.getName() + ".fuelLevel");
			
			ItemStack armor = player.getInventory().getChestplate();
			ItemMeta meta = armor.getItemMeta();

			int maxFuel = 0 ;
		
			//boolean hasFuel = null; //this detects if the jetpack has fuel or not
			if(meta.getLore().get(0).equals("A genuine jetpack!"))
			{
				//player.sendMessage("You're wearing a geniune jetpack!");
				if((fuel == Material.COAL)||(fuel == Material.BLAZE_ROD))
				{
						
					if(click.getAction() == Action.RIGHT_CLICK_AIR || click.getAction() == Action.RIGHT_CLICK_BLOCK)
					{
						//player.sendMessage("Youve right clicked!");
					
						if(fuel == Material.BLAZE_ROD)
							//player.sendMessage("with a blaze rod!");
						{
							if(armor.getType() == Material.DIAMOND_CHESTPLATE)
							{
								maxFuel = 100000;
							}
							if(armor.getType() == Material.GOLD_CHESTPLATE)
							{
								maxFuel = 10000;
							}
							if(armor.getType() == Material.IRON_CHESTPLATE)
							{
								maxFuel = 1000;
							}
							player.sendMessage("maxFuel = " + maxFuel);
							int amtofitems = tempfuel.getAmount(); //getting amount of items in players hand
							amtOfFuel = amtofitems * 1000;
							statsFuel = statsFuel + amtOfFuel;
							player.getInventory().removeItem(tempfuel);
							
							if(statsFuel >= maxFuel)
							{
								statsFuel = maxFuel;	//this should stop statsfuel from going above maxfuel, but idk...
							}
							
							List<String> lore = meta.getLore();
							int end = lore.size() - 1;					
							
							player.sendMessage("Amt of fuel = " + amtOfFuel);
							player.sendMessage("statsfuel = " + statsFuel);
							
							stats.set(player.getName() + ".fuelLevel", statsFuel);
							
							lore.remove(end);
							lore.add(statsFuel + "");
							meta.setLore(lore);
							armor.setItemMeta(meta);
							
							player.updateInventory();
							saveFile();
						}
						/*if(fuel == Material.COAL) IRRELEVENT get ^ working first
						{
							int amtofitems = tempfuel.getAmount(); //getting amount of items in players hand
							amtOfFuel = amtofitems * 100;
							statsFuel = statsFuel + amtOfFuel;
							player.sendMessage("Amt of fuel = " + amtOfFuel);
							player.sendMessage("statsfuel = " + statsFuel);
							hasFuel = true;
							stats.set(player.getName() + ".fuelLevel", statsFuel);
							stats.set(player.getName() + ".hasFuel", hasFuel);
							
							List<String> lore = meta.getLore();
							player.sendMessage("Lore = " + lore);
							int end = lore.size() - 1;
							lore.remove(end);
							lore.add(statsFuel + "");
							meta.setLore(lore);
							armor.setItemMeta(meta);
							
							player.updateInventory();
							saveFile();
						}*/
					}
				}
			}
		}

		
	}
}
	