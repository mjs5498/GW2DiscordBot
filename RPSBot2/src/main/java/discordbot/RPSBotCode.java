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
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;
import java.util.EnumMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

import com.google.common.util.concurrent.FutureCallback;

import de.btobastian.javacord.DiscordAPI;
import de.btobastian.javacord.Javacord;
import de.btobastian.javacord.entities.message.Message;
import de.btobastian.javacord.listener.message.MessageCreateListener;
import me.nithanim.gw2api.v2.GuildWars2Api;
import me.nithanim.gw2api.v2.api.account.CurrencyBelonging;
import me.nithanim.gw2api.v2.api.achievements.Achievement;
import me.nithanim.gw2api.v2.api.achievements.DailyAchievement;
import me.nithanim.gw2api.v2.api.achievements.DailyAchievement.Type;
import me.nithanim.gw2api.v2.api.characters.SpecializationType;
import me.nithanim.gw2api.v2.api.characters.WornItem;
import me.nithanim.gw2api.v2.api.currencies.CurrenciesResource;
import me.nithanim.gw2api.v2.api.currencies.Currency;
import me.nithanim.gw2api.v2.api.items.ItemInfo;
import me.nithanim.gw2api.v2.api.specializations.Specialization;
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
		//
		
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
		
		api.disconnect();  //call this so there is a clean shutdown of the last. I think this is needed for running from the IDE only
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
                        else if(userMessage.contains("grr")) { 
                        	message.reply("Calm down " + message.getAuthor().getMentionTag());
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
                        		//sql vars
                        		String apiKey = "";
                        		ResultSet rs = null;
                        		Connection con = null;
                        		Statement statement = null;
                        		String query = "SELECT * FROM APIKeys WHERE DiscordID='" + replyName + "'";
                        		
                        		//character vars
                        		String[] characters;//gw2api.characters().get("E8B70E10-CB14-8A47-8A34-CB14B33AB306D8E3941F-3397-4BA8-8632-9889C6CCEB05");
                        		
                        		try {
                        			con = SQLConnector.getConnection();//connect
									statement = con.createStatement();
									rs = statement.executeQuery(query);//search DB for the user. we don't want to set again if they exist
									//colNum = rsmd.getColumnCount();
									if(rs.next()) { //if in database then process the command
										apiKey = rs.getString("APIKey");
										characters = gw2api.characters().get(apiKey);
										String outMessage = "";
		                        		for(int i = 0; i < characters.length; i++) {
		                        			outMessage = outMessage + characters[i] + "\n";
		                        		}
		                        		message.reply(outMessage);
										//message.reply("You are already in the database use '.gw api update' to change your API Key (feature coming soon)");
									}
									else { //user not in database so throw an error
										//addResult = statement.executeUpdate(addString);
										message.reply("You have not set up an API key. Please do so and try again");
									}
									
								} catch (SQLException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								} finally {
									if (con != null) {
										try {
											con.close();
										} catch (SQLException e) {
											e.printStackTrace();
										}
									}
								}	
                        	}
                        	else if(userMessage.contains("character set ")) {
                        		//sql vars
                        		//String apiKey = "";
                        		ResultSet rs = null;
                        		Connection con = null;
                        		Statement statement = null;
                        		String query = "SELECT * FROM APIKeys WHERE DiscordID='" + replyName + "'";
                        		String discord = "'" + replyName + "'";
                        		String character = "'" + message.getContent().substring(18, message.getContent().length()) + "'";//gw2api.characters().get("E8B70E10-CB14-8A47-8A34-CB14B33AB306D8E3941F-3397-4BA8-8632-9889C6CCEB05");
                        		String addString = "UPDATE APIKeys SET LastCharacter =" + character + " WHERE DiscordID =" + discord +";";                    		
                        		try {
                        			int addResult = 0;
                        			con = SQLConnector.getConnection();//connect
									statement = con.createStatement();
									rs = statement.executeQuery(query);//search DB for the user. we don't want to set again if they exist
									//colNum = rsmd.getColumnCount();
									if(rs.next()) { //if in database then process the command
										addResult = statement.executeUpdate(addString);
										message.reply("Your character selection has been set to " + character);
										//message.reply("You are already in the database use '.gw api update' to change your API Key (feature coming soon)");
									}
									else { //user not in database so throw an error
										//addResult = statement.executeUpdate(addString);
										message.reply("You have not set up an API key. Please do so and try again");
									}
									
								} catch (SQLException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								} finally {
									if (con != null) {
										try {
											con.close();
										} catch (SQLException e) {
											e.printStackTrace();
										}
									}
								}	
                        	}
                        	
                        	else if(userMessage.contains("api set ")) {
                        		int addResult = 0;
                        		ResultSet rs = null;
                        		Connection con = null;
                        		Statement statement = null;
                        		//boolean rsEmpty = true;
                        		String apiKey = "'" + message.getContent().substring(12, message.getContent().length()) + "'";//"'E8B70E10-CB14-8A47-8A34-CB14B33AB306D8E3941F-3397-4BA8-8632-9889C6CCEB05'";
                        		String discord = "'" + replyName + "'";
                        		System.out.println(apiKey);
                        		String query = "SELECT * FROM APIKeys WHERE DiscordID='" + replyName + "'";
                        		String addString = "INSERT INTO APIKeys (DiscordID, APIKey) VALUES (" + discord + "," + apiKey + ");";
                        		
                        		
                        		try {
                        			int colNum = 0;
                        			con = SQLConnector.getConnection();//connect
									statement = con.createStatement();
									rs = statement.executeQuery(query);//search DB for the user. we don't want to set again if they exist
									ResultSetMetaData rsmd = rs.getMetaData();
									colNum = rsmd.getColumnCount();
									if(rs.next()) { //if it has next that means the Discord user is already in our database
										message.reply("You are already in the database use '.gw api update' to change your API Key (feature coming soon)");
									}
									else { //if rs doesn't have a next that means the user isn't in the database and we can add their key
										addResult = statement.executeUpdate(addString);
										message.reply("You have been added to the database!");
									}
									
								} catch (SQLException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								} finally {
									if (con != null) {
										try {
											con.close();
										} catch (SQLException e) {
											e.printStackTrace();
										}
									}
								}	
                        	}
                        	else if(userMessage.contains("character selected")) {
                        		String lastCharacter = "";
                        		ResultSet rs = null;
                        		Connection con = null;
                        		Statement statement = null;
                        		String query = "SELECT * FROM APIKeys WHERE DiscordID='" + replyName + "'"; //this returns the entire row on that discord user
                        		
                        		
                        		try {
                        			con = SQLConnector.getConnection();//connect
									statement = con.createStatement();
									rs = statement.executeQuery(query);//search DB for the user. we don't want to set again if they exist
									//colNum = rsmd.getColumnCount();
									if(rs.next()) { //if in database then process the command
										lastCharacter = rs.getString("LastCharacter");

		                        		message.reply("Your selected character is " + lastCharacter );
									}
									else { //user not in database so throw an error
										//addResult = statement.executeUpdate(addString);
										message.reply("You have not set up an API key. Please do so and try again");
									}	
								} catch (SQLException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								} finally {
									if (con != null) {
										try {
											con.close();
										} catch (SQLException e) {
											e.printStackTrace();
										}
									}
								}
                        		
                        	}
                        	else if(userMessage.contains("wallet")) {
                        		String apiKey = "";
                        		ResultSet rs = null;
                        		Connection con = null;
                        		Statement statement = null;
                        		String query = "SELECT * FROM APIKeys WHERE DiscordID='" + replyName + "'"; //this returns the entire row on that discord user
                        			
                        		try {
                            		String lastCharacter = "";
                        			con = SQLConnector.getConnection();//connect
									statement = con.createStatement();
									rs = statement.executeQuery(query);//search DB for the user. we don't want to set again if they exist
									//colNum = rsmd.getColumnCount();
									if(rs.next()) { //if in database then process the command
										lastCharacter = rs.getString("LastCharacter");
										apiKey = rs.getString("APIKey");
										
										CurrencyBelonging[] cb = gw2api.account().wallet(apiKey);
										Currency c;
		                        		CurrenciesResource cr;
		                        		String outMessage = "";
		                        		for(int i = 0; i < cb.length; i++) {
		                        			c = gw2api.currencies().get(cb[i].getId());//cr.get(cb[i].getId());
		                        			
		                        			outMessage = outMessage + c.getName() + ": " + cb[i].getValue() + "\n";
		                        		}
		                        		message.reply(outMessage);
										//me.nithanim.gw2api.v2.api.characters.Character c = gw2api.characters().get(lastCharacter, apiKey);
		                        		//message.reply("Age: " + c.getAge() + " seconds");
									}
									else { //user not in database so throw an error
										//addResult = statement.executeUpdate(addString);
										message.reply("You have not set up an API key. Please do so and try again");
									}
									
								} catch (SQLException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								} finally {
									if (con != null) {
										try {
											con.close();
										} catch (SQLException e) {
											e.printStackTrace();
										}
									}
								}
                        	}
                        	else if(userMessage.contains("age")) {
                        		String lastCharacter = "";
                        		String apiKey = "";
                        		ResultSet rs = null;
                        		Connection con = null;
                        		Statement statement = null;
                        		String query = "SELECT * FROM APIKeys WHERE DiscordID='" + replyName + "'"; //this returns the entire row on that discord user
                        			
                        		try {
                        			con = SQLConnector.getConnection();//connect
									statement = con.createStatement();
									rs = statement.executeQuery(query);//search DB for the user. we don't want to set again if they exist
									//colNum = rsmd.getColumnCount();
									if(rs.next()) { //if in database then process the command
										lastCharacter = rs.getString("LastCharacter");
										apiKey = rs.getString("APIKey");
										
										me.nithanim.gw2api.v2.api.characters.Character c = gw2api.characters().get(lastCharacter, apiKey);
		                        		message.reply("Age: " + c.getAge() + " seconds");
									}
									else { //user not in database so throw an error
										//addResult = statement.executeUpdate(addString);
										message.reply("You have not set up an API key. Please do so and try again");
									}
									
								} catch (SQLException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								} finally {
									if (con != null) {
										try {
											con.close();
										} catch (SQLException e) {
											e.printStackTrace();
										}
									}
								}
                        		
                        	}
                        	else if(userMessage.contains("deaths")) {
                        		String lastCharacter = "";
                        		String apiKey = "";
                        		ResultSet rs = null;
                        		Connection con = null;
                        		Statement statement = null;
                        		String query = "SELECT * FROM APIKeys WHERE DiscordID='" + replyName + "'"; //this returns the entire row on that discord user
                        		
                        		
                        		try {
                        			con = SQLConnector.getConnection();//connect
									statement = con.createStatement();
									rs = statement.executeQuery(query);//search DB for the user. we don't want to set again if they exist
									//colNum = rsmd.getColumnCount();
									if(rs.next()) { //if in database then process the command
										lastCharacter = rs.getString("LastCharacter");
										apiKey = rs.getString("APIKey");
										
										me.nithanim.gw2api.v2.api.characters.Character c = gw2api.characters().get(lastCharacter, apiKey);
		                        		message.reply("Deaths: " + c.getDeaths() );
									}
									else { //user not in database so throw an error
										//addResult = statement.executeUpdate(addString);
										message.reply("You have not set up an API key. Please do so and try again");
									}
									
								} catch (SQLException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								} finally {
									if (con != null) {
										try {
											con.close();
										} catch (SQLException e) {
											e.printStackTrace();
										}
									}
								}
                        		
                        	}
                        	else if(userMessage.contains("dailies")) {
                        		message.reply("Please wait a moment while I wait for a server response");
                        		System.out.println("dailies");
                        		//String apiKey = "";
                        		String outMessage = "";
                        		ResultSet rs = null;
                        		Connection con = null;
                        		Statement statement = null;
                        		String query = "SELECT * FROM APIKeys WHERE DiscordID='" + replyName + "'"; //this returns the entire row on that discord user

                        		Achievement test = gw2api.achievements().get(1);

                				Map<Type, DailyAchievement[]> dailiesMap = gw2api.achievements().getDailyAchievements();
                				Collection<DailyAchievement[]> dailies = dailiesMap.values();
                				Iterator <Type> keySet = dailiesMap.keySet().iterator();
                				DailyAchievement dailyAchiev;
                				while(keySet.hasNext()) {
                					Type dailyType = keySet.next();
                					DailyAchievement[] dailiesTemp = dailiesMap.get(dailyType);
                					outMessage = outMessage  + dailyType + "\n";
                					for(int i = 0; i < dailiesTemp.length; i++) {
                						dailyAchiev = dailiesTemp[i];
                        				//int[] charTraits = dailiesTemp[i];
                        				//for(int j = 0; j < charTraits.length; j++)
                        					outMessage = outMessage + dailyAchiev.getId() + "\n";
                        				//System.out.println(gw2api.traits().get(charSpec.getTraits()));
                        			}
                        			outMessage = outMessage + "\n\n\n";
                        		}
                        		message.reply(outMessage);
                        		outMessage = "";
                			}
                        	//}
                        	else if(userMessage.contains("traits")) {
                        		message.reply("Please wait a moment while I wait for a server response");
                        		System.out.println("traits");
                        		int track = 0;
                        		String outMessage = "";
                        		String lastCharacter = "";
                        		String apiKey = "";
                        		ResultSet rs = null;
                        		Connection con = null;
                        		Statement statement = null;
                        		String query = "SELECT * FROM APIKeys WHERE DiscordID='" + replyName + "'"; //this returns the entire row on that discord user
                        		
                        		String specifiedTraits = "";
                        		
                        		if (userMessage.contains("pve"))
                        			specifiedTraits = "PVE";
                        		else if (userMessage.contains("pvp"))
                        			specifiedTraits = "PVP";
                        		else if (userMessage.contains("wvw"))
                        			specifiedTraits = "WVW";
                        		
                        		
                        		try {
                        			con = SQLConnector.getConnection();//connect
									statement = con.createStatement();
									rs = statement.executeQuery(query);//search DB for the user. we don't want to set again if they exist
									//colNum = rsmd.getColumnCount();
									if(rs.next()) { //if in database then process the command
										lastCharacter = rs.getString("LastCharacter");
										apiKey = rs.getString("APIKey");
                        		
		                        		me.nithanim.gw2api.v2.api.characters.Character c = gw2api.characters().get(lastCharacter, apiKey);//.get("Alistaire Nightfall", "E8B70E10-CB14-8A47-8A34-CB14B33AB306D8E3941F-3397-4BA8-8632-9889C6CCEB05");
		                        		EnumMap<SpecializationType, me.nithanim.gw2api.v2.api.characters.Specialization[]> specializations = c.getSpecializations();
		                        		//System.out.println(specializations.toString());
		                        		Collection<me.nithanim.gw2api.v2.api.characters.Specialization[]> special = specializations.values();
		                        		Iterator<SpecializationType> enumKeySet = specializations.keySet().iterator();
		                        		me.nithanim.gw2api.v2.api.characters.Specialization charSpec; 
		                        		if(specifiedTraits.equals("")) {
			                        		while(enumKeySet.hasNext()){
			                        			SpecializationType sp = enumKeySet.next();
			                        			me.nithanim.gw2api.v2.api.characters.Specialization[] spe = specializations.get(sp);
			                        			//System.out.println(specializations.get(sp));
			                        			//System.out.println(spe[0]); //junk
			                        			System.out.println(sp.name());
			                        			outMessage = outMessage + sp + "\n";//pve pvp wvw
			                        			for(int i = 0; i < spe.length; i++) {
			                        				charSpec = spe[i];
			                        				int[] charTraits = spe[i].getTraits();
			                        				outMessage = outMessage + gw2api.specializations().get(spe[i].getId()).getName() + ": ";
			                        				for(int j = 0; j < charTraits.length; j++)
			                        					outMessage = outMessage + gw2api.traits().get(charTraits[j]).getName() + ", ";
			                        				outMessage = outMessage.substring(0, outMessage.length()-2) + "\n";
			                        				//System.out.println(gw2api.traits().get(charSpec.getTraits()));
			                        			}
			                        			outMessage = outMessage + "\n";
			                        		}
			                        		message.reply(outMessage);
			                        		outMessage = "";
		                        		}
		                        		else {
		                        			while(enumKeySet.hasNext()){
			                        			SpecializationType sp = enumKeySet.next();
			                        			me.nithanim.gw2api.v2.api.characters.Specialization[] spe = specializations.get(sp);
			                        			//System.out.println(specializations.get(sp));
			                        			//System.out.println(spe[0]); //junk
			                        			if(sp.name().equals(specifiedTraits)) {
				                        			outMessage = outMessage + sp + "\n";//pve pvp wvw
				                        			for(int i = 0; i < spe.length; i++) {
				                        				charSpec = spe[i];
				                        				int[] charTraits = spe[i].getTraits();
				                        				outMessage = outMessage + gw2api.specializations().get(spe[i].getId()).getName() + ": ";
				                        				for(int j = 0; j < charTraits.length; j++)
				                        					outMessage = outMessage + gw2api.traits().get(charTraits[j]).getName() + ", ";
				                        				outMessage = outMessage.substring(0, outMessage.length()-2) + "\n";
				                        				//System.out.println(gw2api.traits().get(charSpec.getTraits()));
				                        			}
				                        			//outMessage = outMessage + "\n";
			                        			}
			                        		}
			                        		message.reply(outMessage);
			                        		outMessage = "";
		                        		}
									}
                        		} catch (SQLException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								} finally {
									if (con != null) {
										try {
											con.close();
										} catch (SQLException e) {
											e.printStackTrace();
										}
									}
								}
                        	}
                        	else if(userMessage.contains("gear")) {
                        		System.out.println("gear");
                        		message.reply("Please wait a moment while I wait for a server response");
                        		String lastCharacter = "";
                        		String apiKey = "";
                        		ResultSet rs = null;
                        		Connection con = null;
                        		Statement statement = null;
                        		String query = "SELECT * FROM APIKeys WHERE DiscordID='" + replyName + "'";
                        		
                        		//character vars
                        		
                        		try {
                        			con = SQLConnector.getConnection();//connect
									statement = con.createStatement();
									rs = statement.executeQuery(query);//search DB for the user. we don't want to set again if they exist
									//colNum = rsmd.getColumnCount();
									if(rs.next()) { //if in database then process the command
										lastCharacter = rs.getString("LastCharacter");
										apiKey = rs.getString("APIKey");
										
										me.nithanim.gw2api.v2.api.characters.Character c = gw2api.characters().get(lastCharacter, apiKey);
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
		                        		
		                        		
										//message.reply("You are already in the database use '.gw api update' to change your API Key (feature coming soon)");
									}
									else { //user not in database so throw an error
										//addResult = statement.executeUpdate(addString);
										message.reply("You have not set up an API key. Please do so and try again");
									}
									
								} catch (SQLException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								} finally {
									if (con != null) {
										try {
											con.close();
										} catch (SQLException e) {
											e.printStackTrace();
										}
									}
								}
                        	}
                        }
                    } //how did this become an extra?
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