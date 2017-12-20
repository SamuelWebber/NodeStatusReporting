import java.io.*;
import java.util.*;
public class NodeStatusReporting {
	static ArrayList<String> characters = new ArrayList<String>();
	static HashMap<String,String> alive = new HashMap<String,String>();
	static HashMap<String,String> dead = new HashMap<String,String>();
	static HashMap<String,String> unknown = new HashMap<String,String>();
	static ArrayList<String> ambiguous = new ArrayList<String>();

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		if (args.length > 0) {
			File file = new File(args[0]);
			BufferedReader buffer = null;
			try {
				String notification;
				buffer = new BufferedReader(new FileReader(file.getCanonicalPath()));
				while ((notification = buffer.readLine()) != null) {
					String[] details = notification.split(" ");
					if (details.length > 3) {
						String character = details[2];
						if (!characters.contains(character)) {
							characters.add(character);
						}
						if (details[3].equalsIgnoreCase("HELLO")) {
							if (checkClashingNotifications(notification, character, true)) {
								removeCharacter(character);
								unknown.put(character, notification);
							} else {
								if (checkTimesNotifications(notification,character,true)) {
									removeCharacter(character);
									removeUnknowns(character);
									alive.put(character, notification);
								}
							}
						} else if (details[3].equalsIgnoreCase("LOST")) {
							if (details.length > 4) {
								if (checkClashingNotifications(notification,character,true)) {
									removeCharacter(character);
									unknown.put(character, notification);
								} else {
									if (checkTimesNotifications(notification,character,true)) {
										removeCharacter(character);
										removeUnknowns(character);
										alive.put(character, notification);
									}
								}
								String character2 = details[4];
								if (!characters.contains(character2)) {
									characters.add(character2);
								}
								if (checkClashingNotifications(notification,character2,false)) {
									removeCharacter(character2);
									unknown.put(character2, notification);
								} else {
									if (checkTimesNotifications(notification,character2,false)) {
										removeCharacter(character2);
										removeUnknowns(character);
										dead.put(character2, notification);
									}
								}
							} else {
								System.err.println("Error: Invalid Arguments!");
							}
						} else if (details[3].equalsIgnoreCase("FOUND")) {
							if (details.length > 4) {
								if (checkClashingNotifications(notification,character,true)) {
									removeCharacter(character);
									unknown.put(character, notification);
								} else {
									if (checkTimesNotifications(notification,character,true)) {
										removeCharacter(character);
										removeUnknowns(character);
										alive.put(character, notification);
									}
								}
								String character2 = details[4];
								if (!characters.contains(character2)) {
									characters.add(character2);
								}
								if (checkClashingNotifications(notification,character2,true)) {
									removeCharacter(character2);
									unknown.put(character2, notification);
								} else {
									if (checkTimesNotifications(notification,character2,true)) {
										removeCharacter(character2);
										removeUnknowns(character);
										alive.put(character2, notification);
									}
								}
							} else {
								System.err.println("Error: Invalid Arguments!");
							}
						} else {
							System.err.println("Error: Invalid Arguments!");
						}
					} else {
						System.err.println("Error: Invalid Number of Notification Arguments");
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			for (int i = 0; i < characters.size(); i++) {
				String character =  characters.get(i);
				if (alive.containsKey(character)) {
					String notification = character + " ALIVE ";
					String[] details = alive.get(character).split(" ");
					for (int j = 0; j < details.length-1; j++) {
						if (j != 1) {
							notification += details[j] + " ";
						}
					}
					notification += details[details.length-1];
					System.out.println(notification);
				}
				if (dead.containsKey(character)) {
					String notification = character + " DEAD ";
					String[] details = dead.get(character).split(" ");
					for (int j = 0; j < details.length-1; j++) {
						if (j != 1) {
							notification += details[j] + " ";
						}
					}
					notification += details[details.length-1];
					System.out.println(notification);
				}
				if (unknown.containsKey(character)) {
					String notification = character + " UNKNOWN ";
					String[] details = unknown.get(character).split(" ");
					for (int j = 0; j < details.length-1; j++) {
						if (j != 1) {
							notification += details[j] + " ";
						}
					}
					notification += details[details.length-1];
					System.out.println(notification);
					printUnknownNotifications(character);
				}
			}
		} else {
			System.err.println("Error: Invalid Number of Arguments");
			System.exit(0);
		}
	}
	
	public static void removeCharacter(String character) {
		if (dead.containsKey(character)) {
			dead.remove(character);
		} else if (alive.containsKey(character)) {
			alive.remove(character);
		} else if (unknown.containsKey(character)) {
			unknown.remove(character);
		}
	}
	
	public static boolean isClashing(String oldNotification, String notification) {
		String[] oldDetails = oldNotification.split(" ");
		String[] details = notification.split(" ");
		double oldTime = Double.parseDouble(oldDetails[1]);
		double newTime = Double.parseDouble(details[1]);
		double difference = Math.abs(newTime-oldTime);
		if (difference <= 50) {
			return true;
		} else {
			return false;
		}
	}
	
	public static boolean checkClashingNotifications(String notification, String character, boolean isAlive) {
		if (isAlive) {
			if (unknown.containsKey(character) || dead.containsKey(character)) {
				String oldNotification = "";
				if (unknown.containsKey(character)) {
					oldNotification = unknown.get(character);
				} else {
					oldNotification = dead.get(character);
				}
				if (isClashing(oldNotification,notification)) {
					if (dead.containsKey(character)) {
						ambiguous.add(character + " UNKNOWN " + oldNotification);
					}
					ambiguous.add(character + " UNKNOWN " + notification);
					return true;
				} else {
					return false;
				}
			} else {
				return false;
			}
		} else {
			if (unknown.containsKey(character) || alive.containsKey(character)) {
				String oldNotification = "";
				if (unknown.containsKey(character)) {
					oldNotification = unknown.get(character);
				} else {
					oldNotification = alive.get(character);
				}
				if (isClashing(oldNotification,notification)) {
					if (alive.containsKey(character)) {
						ambiguous.add(character + " UNKNOWN " + oldNotification);
						
					}
					ambiguous.add(character + " UNKNOWN " + notification);
					return true;
				} else {
					return false;
				}
			} else {
				return false;
			}
		}
	}
	
	public static boolean checkTimesNotifications(String notification, String character, boolean isAlive) {
		if (isAlive) {
			if (unknown.containsKey(character) || dead.containsKey(character)) {
				String oldNotification = "";
				if (unknown.containsKey(character)) {
					oldNotification = unknown.get(character);
				} else {
					oldNotification = dead.get(character);
				}
				String[] oldDetails = oldNotification.split(" ");
				String[] details = notification.split(" ");
				if (Double.parseDouble(oldDetails[1]) < Double.parseDouble(details[1])) {
					return true;
				} else {
					return false;
				}
			} else {
				return true;
			}
		} else {
			if (unknown.containsKey(character) || alive.containsKey(character)) {
				String oldNotification = "";
				if (unknown.containsKey(character)) {
					oldNotification = unknown.get(character);
				} else {
					oldNotification = alive.get(character);
				}
				String[] oldDetails = oldNotification.split(" ");
				String[] details = notification.split(" ");
				if (Double.parseDouble(oldDetails[1]) < Double.parseDouble(details[1])) {
					return true;
				} else {
					return false;
				}
			} else {
				return true;
			}
		}
	}
	
	public static void removeUnknowns(String character) {
		for (int i = ambiguous.size()-1; i >= 0; i--) {
			if (ambiguous.get(i).contains(character + " UNKNOWN")) {
				ambiguous.remove(i);
			}
		}
	}
	
	public static void printUnknownNotifications(String character) {
		for (int i = 0; i < ambiguous.size(); i++) {
			if (ambiguous.get(i).contains(character + " UNKNOWN")) {
				System.out.println(ambiguous.get(i));
			}
		}
	}
}