package discordbot;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.util.Collection;
import java.util.EnumMap;
import java.util.Iterator;
import java.util.Random;

import javax.swing.plaf.synth.SynthSeparatorUI;

import com.google.common.util.concurrent.FutureCallback;

import de.btobastian.javacord.DiscordAPI;
import de.btobastian.javacord.Javacord;
import de.btobastian.javacord.entities.message.Message;
import de.btobastian.javacord.listener.message.MessageCreateListener;
import me.nithanim.gw2api.v2.GuildWars2Api;
import me.nithanim.gw2api.v2.api.account.CurrencyBelonging;
import me.nithanim.gw2api.v2.api.characters.SpecializationType;
import me.nithanim.gw2api.v2.api.characters.WornItem;
import me.nithanim.gw2api.v2.api.currencies.CurrenciesResource;
import me.nithanim.gw2api.v2.api.currencies.Currency;
import me.nithanim.gw2api.v2.api.items.ItemInfo;
import me.nithanim.gw2api.v2.api.specializations.Specialization;
import me.nithanim.gw2api.v2.api.traits.Trait;
import me.nithanim.gw2api.v2.common.Item;
import me.nithanim.gw2api.v2.configs.GuildWars2ApiDefaultConfigWithGodaddyFix;

public class RPSBotCode {
	int gamesPlayed = 0;
	int gamesWon = 0;
	int gamesLost = 0;
	int lsgamesPlayed = 0;
	int lsgamesWon = 0;
	int lsgamesLost = 0;
	String statPath ="";
	FileWriter write;
	FileReader reader;
	File statPathFile;
	BufferedReader bufferedReader;
	FileReader fileReader;
	StringBuffer stringBuffer = new StringBuffer();
	String fileLine;
	String fileName = "rpsStats.txt";
	final File writeFile;
	GuildWars2Api gw2api = new GuildWars2Api(new GuildWars2ApiDefaultConfigWithGodaddyFix());
	
	public RPSBotCode(String token){
		final DiscordAPI api = Javacord.getApi(token, true);
		gw2api.destroy();
		gw2api = new GuildWars2Api(new GuildWars2ApiDefaultConfigWithGodaddyFix());
		//final String[] rpsMoves = {"Rock!", "Paper!", "Scissors!"};
		//final String[] rpslsMoves = {"Rock!", "Paper!", "Scissors!", "Lizard!", "Spock!"};
		
		
		//set up reading the stat file
		try {
			statPath = RPSBotCode.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
			statPath = URLDecoder.decode(statPath, "UTF-8");
			statPathFile = new File(statPath, fileName);
			fileReader = new FileReader(statPathFile);
			bufferedReader = new BufferedReader(fileReader);
			//read the stat info
			fileLine = bufferedReader.readLine();
			gamesPlayed = Integer.parseInt(fileLine);
			fileLine = bufferedReader.readLine();
			gamesWon = Integer.parseInt(fileLine);
			fileLine = bufferedReader.readLine();
			gamesLost = Integer.parseInt(fileLine);
			fileLine = bufferedReader.readLine();
			lsgamesPlayed = Integer.parseInt(fileLine);
			fileLine = bufferedReader.readLine();
			lsgamesWon = Integer.parseInt(fileLine);
			fileLine = bufferedReader.readLine();
			lsgamesLost = Integer.parseInt(fileLine);
			//close the file
			fileReader.close();
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("CANNOT ACCESS SAVE FILE");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		writeFile = new File(statPath,fileName);
		//write = new FileWriter(writeFile);
		
		api.disconnect();  //call this so there is a clean shutdown of the last
		api.connect(new FutureCallback<DiscordAPI>() {
			public void onSuccess(DiscordAPI api) {
				//register listener
				api.registerListener(new MessageCreateListener() {
                    public void onMessageCreate(DiscordAPI api, Message message) {
                        // check the content of the message
                    	Random rand = new Random();
                    	String replyName = message.getAuthor().getMentionTag();
                    	int moveNum = 0;
                    	int userMoveNum = 0;
                    	String userMessage = message.getContent().toLowerCase();
                    	String userName = message.getAuthor().getName();
                        if (message.getContent().equalsIgnoreCase("ping")) {
                            // reply to the message
                        	//if(message.get){
                        	//	message.reply("PONG");
                        	//}
                        	if(userName.equalsIgnoreCase("smiffy") || userName.equalsIgnoreCase("smiffs"))//.getId().equalsIgnoreCase("smiffy"))
                        	{
                        		message.reply(replyName + " pong!");
                        	}
                        	else
                        		message.reply(replyName + " pong");
                        }
                        else if(message.getContent().equals(".rps"))
                        		message.reply(replyName + " Rock Paper Scissors!");
                        else if(message.getContent().equals(".rps stats")) {
                        	message.reply(replyName + " Rock Paper Scissors Bot Stats\nGames Played: " + gamesPlayed + "\nGames Won: " + gamesWon + "\nGames Lost: " + gamesLost + "\nGames Tied: " + (gamesPlayed - gamesLost - gamesWon));
                        }
                        else if(message.getContent().equals(".rpsls stats")) {
                        	message.reply(replyName + " Rock Paper Scissors Lizard Spock Bot Stats\nGames Played: " + lsgamesPlayed + "\nGames Won: " + lsgamesWon + "\nGames Lost: " + lsgamesLost + "\nGames Tied: " + (lsgamesPlayed - lsgamesLost - lsgamesWon));
                        }
                        else if(userMessage.contains(".rps ")) {
                        	
                    		/*if(userName.equalsIgnoreCase("tofu") || userName.equalsIgnoreCase("thetofu611"))
                    		{
                    			if(userMessage.contains("rock")) {
                    				message.reply(replyName + " Paper! You Lose!");
                    				gamesPlayed++;
                    				gamesWon++;
                    			}
                    			else if(userMessage.contains("paper")) {
                    				message.reply(replyName + " Scissors! You Lose!");
                    				gamesPlayed++;
                    				gamesWon++;
                    			}
                    			else if(userMessage.contains("scissors")) {
                    				message.reply(replyName + " Rock! You Lose!");
                    				gamesPlayed++;
                    				gamesWon++;
                    			}
                    			else 
                    				message.reply("You must choose rock, paper, or scissors");
                    		}*/
                        	if (userMessage.contains(".rps ")){
                    			if(userMessage.contains("rock"))
                    				userMoveNum = 0;
                    			else if(userMessage.contains("paper"))
                    				userMoveNum = 1;
                    			else if(userMessage.contains("scissors"))
                    				userMoveNum = 2;
                    			else 
                    				userMoveNum = -1;
                    			
                    			moveNum = rand.nextInt((3));
                    			if(userMoveNum != -1) {
                    				if (userMoveNum == 0) {
                    					switch(moveNum) {
                    						case 0:
                    							message.reply(replyName + " Rock! It's a tie!");
                    							gamesPlayed++;
                    							break;
                    						case 1:
                    							message.reply(replyName + " Paper! You Lose!");
                    							gamesPlayed++;
                    							gamesWon++;
                    							
                    							break;
                    						case 2:
                    							message.reply(replyName + " Scissors! You Win!");
                    							gamesPlayed++;
                    							gamesLost++;
                    							break;
                    					}
                    				}
                    				else if (userMoveNum == 1) {
                    					switch(moveNum) {
                    						case 0:
                    							message.reply(replyName + " Rock! You win!");
                    							gamesPlayed++;
                    							gamesLost++;
                    							break;
                    						case 1:
                    							message.reply(replyName + " Paper! It's a tie!");
                    							gamesPlayed++;
                    							break;
                    						case 2:
                    							message.reply(replyName + " Scissors! You Lose!");
                    							gamesPlayed++;
                    							gamesWon++;
                    							break;
                    					}
                    				}
                    				else if (userMoveNum == 2) {
                    					switch(moveNum) {
                    						case 0:
                    							message.reply(replyName + " Rock! You lose!");
                    							gamesPlayed++;
                    							gamesWon++;
                    							break;
                    						case 1:
                    							message.reply(replyName + " Paper! You Win!");
                    							gamesPlayed++;
                    							gamesLost++;
                    							break;
                    						case 2:
                    							message.reply(replyName + " Scissors! It's a tie!");
                    							gamesPlayed++;
                    							break;
                    					}
                    				}
                    				//after playing a round save the results
                    				writeStats();
                    			}
                    			else {
                    				message.reply(replyName + " You must choose rock, paper, or scissors");
                    			}
                    		}
                    	}
                        else if(userMessage.contains(".rpsls ")) {
                    		//message.reply(replyName + " Rock Paper Scissors Lizard Spock is coming soon!");

                    			if(userMessage.contains("rock"))
                    				userMoveNum = 0;
                    			else if(userMessage.contains("paper"))
                    				userMoveNum = 1;
                    			else if(userMessage.contains("scissors"))
                    				userMoveNum = 2;
                    			else if(userMessage.contains("lizard"))
                    				userMoveNum = 3;
                    			else if(userMessage.contains("spock"))
                    				userMoveNum = 4;
                    			else 
                    				userMoveNum = -1;
                    			
                    			moveNum = rand.nextInt((5));
                    			if(userMoveNum != -1) {
                    				if (userMoveNum == 0) {
                    					switch(moveNum) {
                    						case 0:
                    							message.reply(replyName + " Rock! It's a tie!");
                    							lsgamesPlayed++;
                    							break;
                    						case 1:
                    							message.reply(replyName + " Paper! Paper covers Rock! You Lose!");
                    							lsgamesPlayed++;
                    							lsgamesWon++;
                    							
                    							break;
                    						case 2:
                    							message.reply(replyName + " Scissors! Rock crushes Scissors! You Win!");
                    							lsgamesPlayed++;
                    							lsgamesLost++;
                    							break;
                    						case 3:
                    							message.reply(replyName + " Lizard! Rock crushes Lizard! You Win!");
                    							lsgamesPlayed++;
                    							lsgamesLost++;
                    							break;
                    						case 4:
                    							message.reply(replyName + " Spock! Spock vaporizes Rock! You Lose!");
                    							lsgamesPlayed++;
                    							lsgamesWon++;
                    							break;
                    					}
                    				}
                    				else if (userMoveNum == 1) {
                    					switch(moveNum) {
                    						case 0:
                    							message.reply(replyName + " Rock! Paper covers Rock! You win!");
                    							lsgamesPlayed++;
                    							lsgamesLost++;
                    							break;
                    						case 1:
                    							message.reply(replyName + " Paper! It's a tie!");
                    							lsgamesPlayed++;
                    							break;
                    						case 2:
                    							message.reply(replyName + " Scissors! Scissors cut Paper! You Lose!");
                    							lsgamesPlayed++;
                    							lsgamesWon++;
                    							break;
                    						case 3:
                    							message.reply(replyName + " Lizard! Lizard eats Paper! You Lose!");
                    							lsgamesPlayed++;
                    							lsgamesWon++;
                    							break;
                    						case 4:
                    							message.reply(replyName + " Spock! Paper disproves Spock! You Win!");
                    							lsgamesPlayed++;
                    							lsgamesLost++;
                    							break;
                    					}
                    				}
                    				else if (userMoveNum == 2) {
                    					switch(moveNum) {
                    						case 0:
                    							message.reply(replyName + " Rock! Rock crushes Scissors! You lose!");
                    							lsgamesPlayed++;
                    							lsgamesWon++;
                    							break;
                    						case 1:
                    							message.reply(replyName + " Paper! Scissors cut Paper! You Win!");
                    							lsgamesPlayed++;
                    							lsgamesLost++;
                    							break;
                    						case 2:
                    							message.reply(replyName + " Scissors! It's a tie!");
                    							lsgamesPlayed++;
                    							break;
                    						case 3:
                    							message.reply(replyName + " Lizard! Scissors decapitates Lizard! You Win!");
                    							lsgamesPlayed++;
                    							lsgamesLost++;
                    							break;
                    						case 4:
                    							message.reply(replyName + " Spock! Spock smashes Scissors! You Lose!");
                    							lsgamesPlayed++;
                    							lsgamesLost++;
                    							break;
                    					}
                    				}
                    				else if (userMoveNum == 3) {
                    					switch(moveNum) {
                    						case 0:
                    							message.reply(replyName + " Rock! Rock crushes Lizard! You Lose!");
                    							lsgamesPlayed++;
                    							lsgamesWon++;
                    							break;
                    						case 1:
                    							message.reply(replyName + " Paper! Lizard eats Paper! You Win!");
                    							lsgamesPlayed++;
                    							lsgamesLost++;
                    							break;
                    						case 2:
                    							message.reply(replyName + " Scissors! Scissors decapitates Lizard! You Lose");
                    							lsgamesPlayed++;
                    							lsgamesWon++;
                    							break;
                    						case 3:
                    							message.reply(replyName + " Lizard! I think they're getting freaky! It's a tie!");
                    							lsgamesPlayed++;
                    							break;
                    						case 4:
                    							message.reply(replyName + " Spock! Lizard Poisons Spock! You Win!");
                    							lsgamesPlayed++;
                    							lsgamesLost++;
                    							break;
                    					}
                    				}
                    				else if (userMoveNum == 4) {
                    					switch(moveNum) {
                    						case 0:
                    							message.reply(replyName + " Rock! Spock vaporizes Rock! You Win!");
                    							lsgamesPlayed++;
                    							lsgamesLost++;
                    							break;
                    						case 1:
                    							message.reply(replyName + " Paper! Paper disproves Spock! You Lose!");
                    							lsgamesPlayed++;
                    							lsgamesWon++;
                    							break;
                    						case 2:
                    							message.reply(replyName + " Scissors! Spock smashes Scissors! You Win!");
                    							lsgamesPlayed++;
                    							lsgamesWon++;
                    							break;
                    						case 3:
                    							message.reply(replyName + " Lizard! Lizard poisons Spock! You Lose!");
                    							lsgamesPlayed++;
                    							lsgamesWon++;
                    							break;
                    						case 4:
                    							message.reply(replyName + " Spock! Which timeline is this?! It's a tie !");
                    							lsgamesPlayed++;
                    							break;
                    					}
                    				}
                    				//after playing a round save the results
                    				writeStats();
                    			}
                    			else {
                    				message.reply(replyName + " You must choose rock, paper, scissors, lizard or spock");
                    			}
                    	}
                        else if(userMessage.startsWith(".gw ")) {
                        	if(userMessage.contains("characters")) {
                        		String[] characters = gw2api.characters().get("E8B70E10-CB14-8A47-8A34-CB14B33AB306D8E3941F-3397-4BA8-8632-9889C6CCEB05");
                        		String outMessage = "";
                        		for(int i = 0; i < characters.length; i++) {
                        			outMessage = outMessage + characters[i] + "\n";
                        		}
                        		message.reply(outMessage);
                        	}
                        	else if(userMessage.contains("wallet")) {
                        		CurrencyBelonging[] cb = gw2api.account().wallet("E8B70E10-CB14-8A47-8A34-CB14B33AB306D8E3941F-3397-4BA8-8632-9889C6CCEB05");
                        		
                        		Currency c;
                        		CurrenciesResource cr;
                        		
                        		String outMessage = "";
                        		for(int i = 0; i < cb.length; i++) {
                        			c = gw2api.currencies().get(cb[i].getId());//cr.get(cb[i].getId());
                        			
                        			outMessage = outMessage + c.getName() + ": " + cb[i].getValue() + "\n";
                        		}
                        		message.reply(outMessage);
                        	}
                        	else if(userMessage.contains("age")) {
                        		me.nithanim.gw2api.v2.api.characters.Character c = gw2api.characters().get("Alistaire Nightfall", "E8B70E10-CB14-8A47-8A34-CB14B33AB306D8E3941F-3397-4BA8-8632-9889C6CCEB05");
                        		message.reply("Age: " + c.getAge() + " seconds");
                        	}
                        	else if(userMessage.contains("traits")) {
                        		String outMessage = "";
                        		me.nithanim.gw2api.v2.api.characters.Character c = gw2api.characters().get("Alistaire Nightfall", "E8B70E10-CB14-8A47-8A34-CB14B33AB306D8E3941F-3397-4BA8-8632-9889C6CCEB05");
                        		EnumMap<SpecializationType, me.nithanim.gw2api.v2.api.characters.Specialization[]> specializations = c.getSpecializations();
                        		//System.out.println(specializations.toString());
                        		Collection<me.nithanim.gw2api.v2.api.characters.Specialization[]> special = specializations.values();
                        		Iterator<SpecializationType> enumKeySet = specializations.keySet().iterator();
                        		me.nithanim.gw2api.v2.api.characters.Specialization charSpec; 
                        		while(enumKeySet.hasNext()){
                        			SpecializationType sp = enumKeySet.next();
                        			me.nithanim.gw2api.v2.api.characters.Specialization[] spe = specializations.get(sp);
                        			//System.out.println(specializations.get(sp));
                        			//System.out.println(spe[0]); //junk
                        			outMessage = outMessage + sp + "\n";//pve pvp wvw
                        			for(int i = 0; i < spe.length; i++) {
                        				charSpec = spe[i];
                        				int[] charTraits = spe[i].getTraits();
                        				for(int j = 0; j < charTraits.length; j++)
                        					outMessage = outMessage + gw2api.traits().get(charTraits[j]).getName() + "\n";
                        				//System.out.println(gw2api.traits().get(charSpec.getTraits()));
                        			}
                        			outMessage = outMessage + "\n\n\n";
                        		}
                        		message.reply(outMessage);
                        		outMessage = "";
                        	}
                        	else if(userMessage.contains("gear")) {
                        		me.nithanim.gw2api.v2.api.characters.Character c = gw2api.characters().get("Alistaire Nightfall", "E8B70E10-CB14-8A47-8A34-CB14B33AB306D8E3941F-3397-4BA8-8632-9889C6CCEB05");
                        		WornItem[] wi = c.getEquipment();
                        		String outMessage = "";
                        		ItemInfo item;
                        		int[] itemUpgrades;
                        		int[] itemInfusion;
                        		for(int i = 0; i < wi.length; i++) {
                        			item = gw2api.items().get(wi[i].getId());
                        			itemUpgrades = wi[i].getUpgrades();//gw2api.items().get(wi[i].getUpgrades());
                        			itemInfusion = wi[i].getInfusions();//gw2api.items().get(wi[i].getInfusions());
                        			outMessage = outMessage + item.getName() + " " + item.getRarity() + " ";
                        			if(itemUpgrades!= null)
                        				for(int j = 0; j < itemUpgrades.length; j++) {
                        					
                        					outMessage = " " + outMessage + gw2api.items().get(itemUpgrades[j]).getName();
                        				}
                        			if(itemInfusion!= null)
	                        			for(int j = 0; j < itemInfusion.length; j++) {
	                        				outMessage = " " + outMessage + gw2api.items().get(itemInfusion[j]).getName();
	                        			}
                        			outMessage = outMessage + "\n";
                        		}
                        		message.reply(outMessage);
                        	}
                        	else if(userMessage.contains("traits")) {
                        		me.nithanim.gw2api.v2.api.characters.Character c = gw2api.characters().get("Alistaire Nightfall", "E8B70E10-CB14-8A47-8A34-CB14B33AB306D8E3941F-3397-4BA8-8632-9889C6CCEB05");
                        		
                        	}
                        }
                        
                    }
                });
            }

            public void onFailure(Throwable t) {
                t.printStackTrace();
            }
        });
		Runtime.getRuntime().addShutdownHook(new Thread() {
			public void run() {
				api.disconnect();
				System.out.println("Shutdown Hook is running !"); 
				} 
			});
		System.out.println("Application Terminating ...");
    }
	
	public void writeStats() {
		try {
				write = new FileWriter(writeFile);
				write.write(gamesPlayed + "\r\n");
				write.write(gamesWon + "\r\n");
				write.write(gamesLost + "\r\n");
				write.write(lsgamesPlayed + "\r\n");
				write.write(lsgamesWon + "\r\n");
				write.write(lsgamesLost + "\r\n");
				write.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}

}