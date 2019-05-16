import game.Player;
import game.Team;
import org.w3c.dom.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;

public class NtbblTeamReader {
    static Team loadTeam(String inputFileContent) {
        Team team = new Team();

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

        try {
            DocumentBuilderFactory factory =
                    DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = null;

            builder = factory.newDocumentBuilder();

            ByteArrayInputStream input = new ByteArrayInputStream(
                    teamTableString.getBytes("UTF-8"));
            Document doc = builder.parse(input);
            Element docElement = doc.getDocumentElement();
            NodeList rows = docElement.getChildNodes();

            // skip the first row
            for (int i = 0; i < rows.getLength(); i++) {
                Node playerRow = rows.item(i);
                if (playerRow.getNodeType() == Node.ELEMENT_NODE) {

                    NamedNodeMap attributes = playerRow.getAttributes();
                    if (attributes.getNamedItem("BGCOLOR") != null)
                    {
                        // skip header row
                        continue;
                    }

                    Element p = (Element) playerRow;
                    NodeList playerProperties = p.getElementsByTagName("TD");
                    if (playerProperties.getLength() != 16){
                        continue;
                    }

                    String pos = playerProperties.item(2).getTextContent();
                    Player player = new Player(pos);
                    String num = playerProperties.item(0).getTextContent();
                    player.setNumber(num);
                    team.getPlayers().add(player);
                }
            }
            // get team players
        } catch (Exception e) {
            e.printStackTrace();
        }

        team.getPlayers().add(new Player("Human Blitzer (Blue)"));

        return team;
    }
}
