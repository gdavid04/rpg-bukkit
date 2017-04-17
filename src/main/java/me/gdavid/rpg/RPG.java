package me.gdavid.rpg;

import java.util.ArrayList;
import java.util.Random;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;

public final class RPG extends JavaPlugin implements Listener {
	
	Inventory classGUI;
	
	@Override
    public void onEnable() {
		classGUI = Bukkit.createInventory(null, 9, getConfig().getString("lang.select-class"));
		ArrayList<String> warriorlore = (ArrayList<String>) getConfig().getStringList("lang.warrior-desc");
		createDisplay(Material.IRON_SWORD, classGUI, 0, getConfig().getString("lang.warrior"), warriorlore);
		ArrayList<String> archerlore = (ArrayList<String>) getConfig().getStringList("lang.archer-desc");
		createDisplay(Material.BOW, classGUI, 1, getConfig().getString("lang.archer"), archerlore);
		this.saveDefaultConfig();
		getServer().getPluginManager().registerEvents(this, this);
	}
	
	public static void createDisplay(Material material, Inventory inv, int Slot, String name, String lore) {
		ItemStack item = new ItemStack(material);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(name);
		ArrayList<String> Lore = new ArrayList<String>();
		Lore.add(lore);
		meta.setLore(Lore);
		item.setItemMeta(meta);
		inv.setItem(Slot, item); 
	}
	
	public static void createDisplay(Material material, Inventory inv, int Slot, String name, ArrayList<String> lore) {
		ItemStack item = new ItemStack(material);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(name);
		meta.setLore(lore);
		item.setItemMeta(meta);
		inv.setItem(Slot, item); 
	}
	
	@EventHandler
	public void onInventoryClick(InventoryClickEvent event) {
		Player player = (Player) event.getWhoClicked(); // The player that clicked the item
		//ItemStack clicked = event.getCurrentItem(); // The item that was clicked
		Inventory inventory = event.getInventory(); // The inventory that was clicked in
		if (inventory.getName().equals(getConfig().getString("lang.select-class"))) {
			if (event.getSlot() == 0) {
				this.getConfig().set("players." + player.getName() + ".class", "warrior");
				this.getConfig().set("players." + player.getName() + ".level", 1);
				this.getConfig().set("players." + player.getName() + ".xp", 0);
				this.saveConfig();
				player.sendMessage(ChatColor.GOLD + "[RPG] " + ChatColor.GREEN + "Your class has been changed to warrior");
				player.closeInventory();
			} else if (event.getSlot() == 1) {
				this.getConfig().set("players." + player.getName() + ".class", "archer");
				this.getConfig().set("players." + player.getName() + ".level", 1);
				this.getConfig().set("players." + player.getName() + ".xp", 0);
				this.saveConfig();
				player.sendMessage(ChatColor.GOLD + "[RPG] " + ChatColor.GREEN + "Your class has been changed to archer");
				player.closeInventory();
			}
			event.setCancelled(true);
		}
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("rpg")) {
			if (args.length == 1) {
				if (args[0].equalsIgnoreCase("class") && sender instanceof Player) {
					if (this.getConfig().isSet("players." + ((Player)sender).getName() + ".class") == false) {
						((Player) sender).openInventory(classGUI);
					} else {
						sender.sendMessage(ChatColor.GOLD + "[RPG] " + ChatColor.RED + "You have already chosed a class!");
					}
				} else if (args[0].equalsIgnoreCase("stats") && sender instanceof Player) {
					if (this.getConfig().isSet("players." + ((Player)sender).getName() + ".class") == true) {
						sender.sendMessage(ChatColor.GOLD + "[RPG] " + ChatColor.WHITE + "Your stats:");
						sender.sendMessage(ChatColor.GOLD + "[RPG] " + ChatColor.WHITE + "Class: " + this.getConfig().getString("players." + ((Player)sender).getName().toString() + ".class"));
						sender.sendMessage(ChatColor.GOLD + "[RPG] " + ChatColor.WHITE + "Level " + this.getConfig().getInt("players." + ((Player)sender).getName().toString() + ".level"));
						if (this.getConfig().getInt("players." + ((Player)sender).getName().toString() + ".level") < this.getConfig().getInt("max-level")) {
							sender.sendMessage(ChatColor.GOLD + "[RPG] " + ChatColor.WHITE + "XP: " + this.getConfig().getInt("players." + ((Player)sender).getName() + ".xp") + "/" + xpToNext(((Player)sender).getName()));
						} else {
							sender.sendMessage(ChatColor.GOLD + "[RPG] " + ChatColor.GREEN + "You have reached the maximum level!");
						}
					} else {
						sender.sendMessage(ChatColor.GOLD + "[RPG] " + ChatColor.RED + "Please chose class before viewing your stats!");
					}
				}
			} else if (args.length == 0) {
				sender.sendMessage(ChatColor.DARK_BLUE + "RPG 2.4.0");
				sender.sendMessage(ChatColor.DARK_BLUE + "Created by: Gabor David (GDavid)");
				sender.sendMessage(ChatColor.DARK_BLUE + "Thank you for using my plugin!");
				sender.sendMessage(ChatColor.DARK_BLUE + "========");
				sender.sendMessage(ChatColor.GOLD + "[RPG] " + ChatColor.GREEN + "Help");
				sender.sendMessage(ChatColor.GOLD + "[RPG] " + ChatColor.GREEN + "/rpg class - chose class");
				sender.sendMessage(ChatColor.GOLD + "[RPG] " + ChatColor.GREEN + "/rpg stats - your stats");
			}/* else if (args.length == 2 && args[0].equalsIgnoreCase("class") && sender instanceof Player) {
				/*if (this.getConfig().isSet("players." + ((Player)sender).getName() + ".class") == false) {
					if (args[1].equalsIgnoreCase("warrior")) {
						this.getConfig().set("players." + ((Player)sender).getName() + ".class", "warrior");
						this.getConfig().set("players." + ((Player)sender).getName() + ".level", 1);
						this.getConfig().set("players." + ((Player)sender).getName() + ".xp", 0);
						this.saveConfig();
						sender.sendMessage(ChatColor.GOLD + "[RPG] " + ChatColor.GREEN + "Your class has been changed to warrior");
					} else if (args[1].equalsIgnoreCase("archer")) {
						this.getConfig().set("players." + ((Player)sender).getName() + ".class", "archher");
						this.getConfig().set("players." + ((Player)sender).getName() + ".level", 1);
						this.getConfig().set("players." + ((Player)sender).getName() + ".xp", 0);
						this.saveConfig();
						sender.sendMessage(ChatColor.GOLD + "[RPG] " + ChatColor.GREEN + "Your class has been changed to archer.");
					} else {
						sender.sendMessage(ChatColor.GOLD + "[RPG] " + ChatColor.RED + "This class doesn't exist! /rpg class for classes");
					}
				} else {
					sender.sendMessage(ChatColor.GOLD + "[RPG] " + ChatColor.RED + "You have already selected a class!");
				}
			}*/
		}
		return false; 
	}
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		if (this.getConfig().isSet("players." + event.getPlayer().getName() + ".class") == false) {
			event.getPlayer().sendMessage(ChatColor.GOLD + "[RPG] " + ChatColor.RED + getConfig().getString("lang.no-class"));
		}
		event.getPlayer().setMaxHealth(maxHealth(event.getPlayer().getName()));
	}
	
	@EventHandler
	public void playerHitPlayerEvent(EntityDamageByEntityEvent event) {
		try {
		if (event.isCancelled()) {
			return;
		}
		Entity damager = event.getDamager();
		Entity damaged  = event.getEntity();
		double crit = 1;
		if (getRandom(0, 10) > 8) {
			crit = 1.4 + (getRandom(0, 6) / (double)10);
		}
		if (damager instanceof Player) {
			if (this.getConfig().isSet("players." + ((Player)damager).getName() + ".class")) {
				switch (this.getConfig().getString("players." + ((Player)damager).getName() + ".class")) {
				case "warrior":
					event.setDamage(event.getDamage() + 1 + (this.getConfig().getInt("players." + ((Player)damager).getName() + ".level") * this.getConfig().getDouble("levelup-atk-increase")) * crit);
					((Player)damager).sendMessage(ChatColor.GOLD + "[RPG] " + (crit < 1.4 ? ChatColor.GRAY : ChatColor.GOLD) + "You dealt " + event.getDamage() + " HP damage!");
					giveXp((int) event.getDamage(), ((Player)damager).getName());
					break;
				case "archer":
					//archer has no melee damage boost
					break;
				}
			}
		} else if (damager instanceof Arrow) {
			if (((Arrow)damager).getShooter() instanceof Player) {
				Player shooter = (Player) ((Arrow)damager).getShooter();
				
				if (this.getConfig().isSet("players." + (shooter.getName() + ".class"))) {
					switch (this.getConfig().getString("players." + shooter.getName() + ".class")) {
					case "warrior":
						//warrior has no ranged boost
						break;
					case "archer":
						event.setDamage(event.getDamage() + 1 + (this.getConfig().getInt("players." + shooter.getName() + ".level") * this.getConfig().getDouble("levelup-atk-increase")) * crit);
						((Player)damager).sendMessage(ChatColor.GOLD + "[RPG] " + (crit < 1.4 ? ChatColor.GRAY : ChatColor.GOLD) + "Your arrow dealt " + event.getDamage() + " HP damage!");
						giveXp((int) event.getDamage(), shooter.getName());
						break;
					}
				}
			}
		}
		} catch (Exception e) {
			
		}
	}
	
	public int getRandom(int lower, int upper) {
	    return new Random().nextInt((upper - lower) + 1) + lower;
	}
	
	public void giveXp(int amount, String name) {
		Player damager = this.getServer().getPlayer(name);
		if (/*perm check*/ true){
			if (this.getConfig().getInt("players." + name + ".level") >= this.getConfig().getInt("max-level")) return;
			this.getConfig().set("players." + name + ".xp", this.getConfig().getInt("players." + name + ".xp") + amount);
			if (this.getConfig().getInt("players." + name + ".xp") > xpToNext(name)) {
				this.getConfig().set("players." + name + ".xp", this.getConfig().getInt("players." + name + ".xp") - xpToNext(name));
				this.getConfig().set("players." + name + ".level", this.getConfig().getInt("players." + name + ".level") + 1);
				//getServer().getPlayer(name).sendMessage(ChatColor.GOLD + "[RPG] Level up! Level: " + this.getConfig().getInt("players." + name + ".level"));
				getServer().getPlayer(name).setMaxHealth(maxHealth(name));
				getServer().getPlayer(name).setHealth(getServer().getPlayer(name).getMaxHealth());
				getServer().getPlayer(name).sendTitle(getConfig().getString("lang.level-up"), getConfig().getString("lang.level") + " " + this.getConfig().getInt("players." + name + ".level"));
			}
			this.saveConfig();
		}
	}
	
    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
    	event.getPlayer().setMaxHealth(maxHealth(event.getPlayer().getName()));
    	event.getPlayer().setHealth(event.getPlayer().getMaxHealth());
    }
    
    private double maxHealth(String name) {
		return (this.getConfig().getInt("players." + name + ".level")-1) * this.getConfig().getDouble("levelup-hp-increase") + 20;
	}

	private int xpToNext(String name) {
		return ((this.getConfig().getInt("players." + name + ".level")-1) * this.getConfig().getInt("levelup-xp-increase")) + this.getConfig().getInt("levelup-xp-base");
	}

	@Override
    public void onDisable() {
        
    }
}