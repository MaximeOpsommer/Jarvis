package command;

import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.Response;
import java.util.*;

public class LolRandomCompCommand extends AbstractCommand {

    private final static String CHAMPIONS_JSON_URL = "http://ddragon.leagueoflegends.com/cdn/10.2.1/data/en_US/champion.json";

    private final Map<String, String> randomComp = new HashMap<>();
    private final Random random = new Random();

    private final Map<LolRole, List<String>> championListByRole = new HashMap<>();
    private final Map<String, Set<String>> championListByLane = new HashMap<>();

    private static final String TOP = "TOP";
    private static final String JUNGLE = "JUNGLE";
    private static final String MID = "MID";
    private static final String BOT = "BOT";
    private static final String SUPPORT = "SUPPORT";

    @Override
    public String getCommandName() {
        return "/lolRandomComp";
    }

    @Override
    public void execute(final MessageReceivedEvent event, final String[] commandArgs) {

        final Client client = ClientBuilder.newClient();
        final Response response = client.target(CHAMPIONS_JSON_URL)
                .request()
                .get();
        if (response.getStatus() != Response.Status.OK.getStatusCode()) {
            System.err.println("API champions KO");
            return;
        }

        initChampionListListByRole(response.readEntity(String.class));
        initChampionListByLane();

        final List<String> shuffledLanes = new ArrayList<>(championListByLane.keySet());
        Collections.shuffle(shuffledLanes);
        for (final String lane : shuffledLanes) {
            addRandomChampion(lane, championListByLane.get(lane));
        }

        final String message = String.format("%s: %s\n%s: %s\n%s: %s\n%s: %s\n%s: %s",
                TOP, randomComp.get(TOP),
                JUNGLE, randomComp.get(JUNGLE),
                MID, randomComp.get(MID),
                BOT, randomComp.get(BOT),
                SUPPORT, randomComp.get(SUPPORT));

        final MessageChannel channel = event.getChannel();
        channel.sendMessage(message).queue();
    }

    private void initChampionListListByRole(final String json) {
        for (final LolRole value : LolRole.values()) {
            championListByRole.put(value, new ArrayList<>());
        }

        JSONObject jsonObject = new JSONObject(json);
        JSONObject data = jsonObject.getJSONObject("data");
        for (final String key : data.keySet()) {
            final JSONObject champion = data.getJSONObject(key);
            final JSONArray tags = champion.getJSONArray("tags");
            final List<LolRole> roles = new ArrayList<>();
            for (final Object next : tags) {
                roles.add(LolRole.getRoleByName(next.toString()));
            }
            for (final LolRole role : roles) {
                championListByRole.get(role).add(champion.getString("name"));
            }
        }
    }

    private void initChampionListByLane() {
        HashSet<String> champions = new HashSet<>();
        champions.addAll(championListByRole.get(LolRole.FIGHTER));
        champions.addAll(championListByRole.get(LolRole.MAGE));
        champions.addAll(championListByRole.get(LolRole.TANK));
        championListByLane.put(TOP, champions);

        champions = new HashSet<>();
        champions.addAll(championListByRole.get(LolRole.ASSASSIN));
        champions.addAll(championListByRole.get(LolRole.FIGHTER));
        champions.addAll(championListByRole.get(LolRole.TANK));
        championListByLane.put(JUNGLE, champions);

        champions = new HashSet<>();
        champions.addAll(championListByRole.get(LolRole.ASSASSIN));
        champions.addAll(championListByRole.get(LolRole.MAGE));
        championListByLane.put(MID, champions);

        champions = new HashSet<>(championListByRole.get(LolRole.MARKSMAN));
        championListByLane.put(BOT, champions);

        champions = new HashSet<>(championListByRole.get(LolRole.SUPPORT));
        championListByLane.put(SUPPORT, champions);
    }

    private void addRandomChampion(final String lane, final Set<String> champions) {
        String randomChampion = null;
        while (randomChampion == null || randomComp.containsValue(randomChampion)) {
            randomChampion = champions.toArray(new String[0])[random.nextInt(champions.size())];
        }
        randomComp.put(lane, randomChampion);
    }

    @Override
    public String getDescription() {
        return "Génére une composition de champions LoL au hasard respectant les règles suivantes:\n" +
                "TOP : Combattant ou Mage ou Tank\n" +
                "JGL : Assassin ou Combattant ou Tank\n" +
                "MID : Assassin ou Mage\n" +
                "BOT : Tireur\n" +
                "SUP : Support";
    }

    @Override
    public String getUsage() {
        return "`/lolRandomComp`";
    }
}
