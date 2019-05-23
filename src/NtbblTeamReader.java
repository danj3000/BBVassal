import game.Player;
import game.Team;
import org.w3c.dom.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;

public class NtbblTeamReader {
    static Team loadTeam(String inputFileContent) {
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
                        if ("Race:".equalsIgnoreCase(playerProperties.item(1).getTextContent()))
                        {
                            team.setRace(playerProperties.item(2).getTextContent().trim());
                        }
                        // set team name
                        if ("Team:".equalsIgnoreCase(playerProperties.item(1).getTextContent()))
                        {
                            team.setName(playerProperties.item(2).getTextContent().trim());
                        }
                        // set team coach
                        if ("Coach:".equalsIgnoreCase(playerProperties.item(1).getTextContent()))
                        {
                            team.setCoach(playerProperties.item(2).getTextContent().trim());
                        }
                        continue;
                    }

                    String pos = playerProperties.item(2).getTextContent();
                    Player player = new Player(pos);
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

    private static String fixHtmlInput(String inputFileContent) {
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
}
