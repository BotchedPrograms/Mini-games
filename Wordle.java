import java.util.ArrayList;
import java.util.Scanner;
import java.util.Random;

// Wordle
// Not a perfect recreation, but it suffices
public class Wordle {
  public static void main(String[] args) {
    Scanner scan = new Scanner(System.in);
    String[] words = {"Abort", "About", "Above", "Abuse", "Adapt", "Adopt", "Adult", "Affix", "Agent", "Agile", "Alarm", "Alibi", "Alien", "Allot", "Allow", "Alloy", "Alone", "Alpha", "Amber", "Among", "Anger", "Angle", "Annal", "Anvil", "Apart", "Apple", "Ardor", "Arise", "Armor", "Arrow", "Avoid", "Award", "Awful", "Axiom", "Badge", "Barge", "Basic", "Basis", "Beach", "Beast", "Belch", "Below", "Bench", "Beret", "Berry", "Birth", "Black", "Blade", "Blame", "Bland", "Blank", "Blast", "Bleak", "Blend", "Bless", "Blind", "Blink", "Block", "Blond", "Blood", "Bloom", "Blunt", "Board", "Boast", "Boost", "Booth", "Bored", "Boxer", "Brain", "Brand", "Brave", "Brawn", "Bread", "Break", "Brief", "Bring", "Brink", "Brisk", "Broth", "Brown", "Brunt", "Brush", "Brute", "Budge", "Build", "Bunch", "Buyer", "Cable", "Cache", "Cadre", "Camel", "Canal", "Candy", "Canon", "Carol", "Carry", "Caste", "Catch", "Cater", "Cause", "Chain", "Chair", "Chalk", "Chaos", "Charm", "Chase", "Chasm", "Cheat", "Check", "Chess", "Chest", "Chick", "Chief", "Child", "Chill", "China", "Chirp", "Choir", "Chute", "Civil", "Claim", "Clamp", "Class", "Clean", "Clear", "Click", "Cliff", "Climb", "Cloak", "Clock", "Clone", "Cloth", "Cloud", "Clown", "Clump", "Coach", "Coast", "Color", "Comet", "Conch", "Couch", "Cough", "Court", "Cover", "Cower", "Crack", "Craft", "Crane", "Crash", "Crate", "Crave", "Crawl", "Crazy", "Cream", "Creek", "Creep", "Crest", "Crime", "Croak", "Cross", "Crowd", "Crown", "Crumb", "Crush", "Crust", "Crypt", "Curly", "Curry", "Curve", "Cycle", "Daily", "Dairy", "Dance", "Death", "Deity", "Depth", "Digit", "Diner", "Dirty", "Donut", "Doubt", "Draft", "Drain", "Drama", "Drawn", "Dread", "Dream", "Dress", "Drift", "Drill", "Drink", "Drive", "Drone", "Druid", "Dryer", "Dwarf", "Early", "Earth", "Elate", "Elect", "Emote", "Empty", "Enemy", "Enjoy", "Enter", "Entry", "Equal", "Equip", "Erase", "Erode", "Error", "Evade", "Event", "Exert", "Exist", "Faint", "Faith", "Farce", "Fault", "Fauna", "Favor", "Feign", "Fence", "Fever", "Fiber", "Field", "Fight", "Filth", "Final", "First", "Flame", "Flare", "Flash", "Flask", "Flesh", "Fling", "Flint", "Flirt", "Float", "Flock", "Flood", "Floor", "Flora", "Floss", "Fluff", "Fluid", "Flush", "Flyer", "Focus", "Folly", "Force", "Forge", "Forth", "Found", "Frail", "Frame", "Fraud", "Freak", "Fresh", "Friar", "Front", "Frost", "Froth", "Fruit", "Fudge", "Fungi", "Funny", "Gauge", "Gavel", "Geode", "Ghost", "Given", "Glare", "Glass", "Glide", "Glint", "Globe", "Gloom", "Glory", "Glove", "Gnome", "Golem", "Gorge", "Gouge", "Gourd", "Grace", "Grain", "Grand", "Grant", "Grape", "Grasp", "Grass", "Grate", "Grave", "Gravy", "Great", "Greed", "Green", "Greet", "Grill", "Grime", "Groan", "Gross", "Group", "Guard", "Guess", "Guest", "Guide", "Guilt", "Guise", "Habit", "Happy", "Harsh", "Hatch", "Heard", "Heart", "Heist", "Helix", "Honor", "Horse", "Hotel", "Hound", "House", "Hover", "Human", "Humid", "Hunch", "Hurry", "Hydra", "Igloo", "Image", "Imply", "Index", "Inert", "Input", "Inuit", "Irony", "Issue", "Joint", "Jolly", "Judge", "Juice", "Karma", "Knack", "Knife", "Known", "Krill", "Label", "Labor", "Lance", "Large", "Larva", "Laser", "Latch", "Laugh", "Layer", "Learn", "Least", "Leave", "Ledge", "Leech", "Lemon", "Level", "Lever", "Light", "Limit", "Linen", "Liver", "Livid", "Llama", "Lodge", "Loser", "Lotus", "Loyal", "Lucky", "Lunar", "Lunch", "Lynch", "Magic", "Magma", "Major", "March", "Marry", "Marsh", "Match", "Maxim", "Mayor", "Medal", "Merit", "Metal", "Might", "Model", "Moldy", "Money", "Month", "Moral", "Morph", "Motor", "Mound", "Mount", "Mourn", "Mouth", "Movie", "Mural", "Music", "Naive", "Naval", "Never", "Night", "Noise", "North", "Novel", "Nurse", "Occur", "Offer", "Onion", "Orbit", "Order", "Organ", "Other", "Otter", "Ought", "Overt", "Owner", "Ozone", "Panel", "Papal", "Paper", "Parse", "Party", "Peace", "Penal", "Phase", "Phone", "Photo", "Piano", "Piece", "Pigmy", "Pilot", "Pique", "Pivot", "Pitch", "Pizza", "Place", "Plain", "Plane", "Plant", "Plate", "Plaza", "Plead", "Pluck", "Plume", "Poach", "Point", "Pound", "Power", "Prank", "Press", "Price", "Prime", "Print", "Pride", "Print", "Prize", "Prone", "Prong", "Proof", "Prove", "Purse", "Quack", "Qualm", "Quart", "Queen", "Quest", "Queue", "Quick", "Quiet", "Quill", "Quilt", "Quite", "Quote", "Radar", "Radio", "Rally", "Range", "Rapid", "Ratio", "Ravel", "Reach", "React", "Ready", "Rebel", "Refer", "Reign", "Relax", "Repel", "Reply", "Reset", "Resin", "Retry", "Rhino", "Rhyme", "Ridge", "Right", "Rigor", "Rinse", "Rival", "River", "Roast", "Rocky", "Roost", "Rough", "Round", "Route", "Rover", "Royal", "Rugby", "Ruler", "Rumor", "Rural", "Rusty", "Sapid", "Sassy", "Sauce", "Savvy", "Scale", "Scare", "Scarf", "Scene", "Scent", "Scone", "Scope", "Score", "Scorn", "Scowl", "Scrap", "Screw", "Scrub", "Seize", "Sense", "Shade", "Shaft", "Shall", "Shape", "Shard", "Share", "Shark", "Sharp", "Sheep", "Sheet", "Shift", "Shine", "Shiny", "Shirt", "Shock", "Shoot", "Shore", "Short", "Shove", "Shred", "Shrew", "Shrug", "Sight", "Silly", "Skill", "Skirt", "Skunk", "Slant", "Slave", "Sleep", "Slime", "Sloth", "Small", "Smart", "Smell", "Smelt", "Smile", "Smirk", "Smoke", "Snail", "Snake", "Sneak", "Snore", "Sober", "Solid", "Sonar", "Sound", "South", "Space", "Spare", "Speak", "Spear", "Speed", "Spell", "Spent", "Spice", "Spike", "Spill", "Spine", "Spite", "Split", "Spoil", "Sport", "Spout", "Spurn", "Squad", "Squid", "Stack", "Staff", "Stage", "Stain", "Stalk", "Stall", "Stamp", "Stand", "Stare", "Stark", "Start", "State", "Steak", "Steal", "Steam", "Steel", "Steep", "Steer", "Stern", "Stick", "Still", "Sting", "Stink", "Stock", "Stomp", "Stone", "Stool", "Store", "Storm", "Story", "Stove", "Strap", "Straw", "Strip", "Strum", "Strut", "Stuck", "Study", "Stuff", "Stump", "Style", "Sugar", "Swamp", "Swarm", "Swear", "Swell", "Swift", "Swirl", "Sword", "Syrup", "Table", "Talon", "Taste", "Tenet", "Thank", "Theft", "Theme", "Thick", "Thing", "Think", "Throw", "Tight", "Titan", "Title", "Toast", "Token", "Tooth", "Torso", "Total", "Touch", "Tough", "Towel", "Tower", "Toxic", "Track", "Trade", "Train", "Trash", "Tread", "Treat", "Trend", "Trial", "Trick", "Trite", "Troll", "Truck", "Trust", "Truth", "Trope", "Tulip", "Tunic", "Twirl", "Twist", "Uncle", "Under", "Union", "Unify", "Unity", "Urban", "Vague", "Valid", "Valor", "Value", "Video", "Vigor", "Viral", "Visit", "Voice", "Vouch", "Wagon", "Waist", "Waive", "Waste", "Watch", "Water", "Weary", "Weave", "Wedge", "Whack", "Whale", "Wheel", "While", "Whisk", "White", "Whole", "Width", "Witch", "Woman", "World", "Worry", "Worst", "Worth", "Wound", "Wrath", "Wreck", "Wrong", "Wring", "Wrist", "Write", "Yearn", "Yeast", "Youth"
};
    String letters = "q w e r t y u i o p\n a s d f g h j k l\n  z x c v b n m".toLowerCase();
    Random rand = new Random();
    String input = "";
    ArrayList<String> wordsInput = new ArrayList<String>();
    String answer;
    String retry = "y";
    game: while (retry.equals("y") || retry.equals("yes")) {
      answer = words[rand.nextInt(words.length)].toLowerCase();
      wordsInput.clear();
      System.out.println("Input a word:");
      for (int tries = 0; tries < 6; tries++) {
        while (input.length() != 5) {
          input = scan.nextLine().toLowerCase();
        }
        tryWord: for (int i = 0; i < 5; i++) {
          if (answer.charAt(i) == input.charAt(i)) {
            System.out.print(2);
            continue tryWord;
          }
          for (int j = 0; j < 5; j++) {
            if (answer.charAt(j) == input.charAt(i)) {
              System.out.print(1);
              continue tryWord;
            }
          }
          System.out.print(0);
        }
        System.out.println();
  
        if (input.equals(answer)) {
          if (tries == 0) {
            System.out.println("Congratulations! " + 1 + " try!");
          } else {
            System.out.println("Victory! " + (tries + 1) + " tries");
          }
          input = "";
          retry = "";
          System.out.println("Retry?");
          retry = scan.nextLine().toLowerCase();
          continue game;
        }
        
        wordsInput.add(input);
        tryLetters: for (int i = 0; i < letters.length(); i++) {
          for (int j = 0; j < wordsInput.size(); j++) {
            for (int k = 0; k < 5; k++) {
              if (letters.charAt(i) == wordsInput.get(j).charAt(k)) {
                for (int l = 0; l < 5; l++) {
                  if (letters.charAt(i) == answer.charAt(l)) {
                    System.out.print(letters.substring(i, i+1).toUpperCase());
                    continue tryLetters;
                  }
                }
                System.out.print(" ");
                continue tryLetters;
              }
            }
          }
          System.out.print(letters.charAt(i));
        }
        System.out.println();
        input = "";
      }
      System.out.println(answer);
      System.out.println("Retry?");
      retry = scan.nextLine().toLowerCase();
    }
    scan.close();
  }
}
