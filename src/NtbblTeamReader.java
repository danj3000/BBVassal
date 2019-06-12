import game.Player;
import game.Team;
import org.w3c.dom.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;
import java.util.HashMap;

public class NtbblTeamReader implements ITeamReader {
    HashMap<String, String> aMap = new HashMap<String, String>();

    private String fixHtmlInput(String inputFileContent) {
        int startIndex = inputFileContent.indexOf("<TABLE");
        int endIndex = inputFileContent.indexOf("</TABLE>") + 8;
        String teamTableString = inputFileContent.substring(startIndex, endIndex);
        // remove unquoted attributes (html)
        teamTableString = teamTableString.replace("BORDER=1", "");
        teamTableString = teamTableString.replace("ALIGN=CENTER", "");
        teamTableString = teamTableString.replace("ALIGN=RIGHT", "");
        teamTableString = teamTableString.replace("COLSPAN=5", "");
        teamTableString = teamTableString.replace("COLSPAN=2", "");
        teamTableString = teamTableString.replace("&nbsp", "");
        return teamTableString;
    }

    public Team loadTeam(String inputFileContent) {
        Team team = new Team();

        String teamTableString = fixHtmlInput(inputFileContent);

        try {
            // load doc into xml parser
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            ByteArrayInputStream input = new ByteArrayInputStream(teamTableString.getBytes("UTF-8"));
            Document doc = builder.parse(input);

            Element docElement = doc.getDocumentElement();
            NodeList rows = docElement.getChildNodes();

            // skip the first row
            for (int i = 0; i < rows.getLength(); i++) {
                Node playerRow = rows.item(i);
                if (playerRow.getNodeType() == Node.ELEMENT_NODE) {

                    NamedNodeMap attributes = playerRow.getAttributes();
                    // skip header row
                    if (attributes.getNamedItem("BGCOLOR") != null) { continue; }

                    Element p = (Element) playerRow;
                    NodeList playerProperties = p.getElementsByTagName("TD");
                    if (playerProperties.getLength() != 16){
                        // set race
                        String key = playerProperties.item(1).getTextContent();
                        String value = playerProperties.item(2).getTextContent().trim();
                        if ("Race:".equalsIgnoreCase(key))
                        {
                            if (value.equalsIgnoreCase("elf"))
                                value = "Pro Elf";
                            team.setRace(value);
                            initPositionMap(value);
                        }
                        // set team name
                        if ("Team:".equalsIgnoreCase(key))
                        {
                            team.setName(value);
                        }
                        // set team coach
                        if ("Coach:".equalsIgnoreCase(key))
                        {
                            team.setCoach(value);
                        }
                        continue;
                    }

                    // get player position
                    String pos = playerProperties.item(2).getTextContent();
                    String position = translatePosition(pos, team.getRace());

                    // create player
                    Player player = new Player(position);
                    player.setNumber(playerProperties.item(0).getTextContent());
                    player.setName(playerProperties.item(1).getTextContent());
                    int MA = Integer.parseInt(playerProperties.item(3).getTextContent());
                    player.setMovement(MA);
                    int ST = Integer.parseInt(playerProperties.item(4).getTextContent());
                    player.setStrength(ST);
                    int AG = Integer.parseInt(playerProperties.item(5).getTextContent());
                    player.setAgility(AG);
                    int AV = Integer.parseInt(playerProperties.item(6).getTextContent());
                    player.setArmour(AV);
                    player.setSkills(playerProperties.item(7).getTextContent());
                    team.getPlayers().add(player);
                }
            }
            // get team players
        } catch (Exception e) {
            e.printStackTrace();
        }

        return team;
    }

    private void initPositionMap(String race) {
        aMap.put("Minotaur Lord", "Minotaur");
        // Norse
        aMap.put("Norse Werewolf", "Norse Ulfwerener");
        aMap.put("Necromantic", "Necro");
        aMap.put("Halfling Hero", "Halfling");
        aMap.put("Chaos Troll", "Troll");
        aMap.put("Chaos Ogre", "Ogre");
        aMap.put("River Troll", "Troll");

        // Journeymen
        aMap.put(" [J]", "");
        if (race.equalsIgnoreCase("Norse")) {
            aMap.put("Blitzer", "Beserker"); // note spelling mistake
            aMap.put("Catcher", "Runner");
        }
    }

    private String translatePosition(String pos, String race) {
        String position = pos;
        for (String s :
                aMap.keySet()) {
            position = position.replace(s, aMap.get(s));
        }
        return position;
    }
}
