import game.Player;
import game.Team;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NtbblTeamReaderTest {
    private String input = "";

    @BeforeEach
    void setUp() {
input = "\n" +
        "<TABLE BORDER=1>\n" +
        "        <TR BGCOLOR='SILVER'>\n" +
        "                <TH>#</TH>\n" +
        "                <TH>PLAYER'S NAME</TH>\n" +
        "                <TH>POSITION</TH>\n" +
        "                <TH>MA</TH>\n" +
        "                <TH>ST</TH>\n" +
        "                <TH>AG</TH>\n" +
        "                <TH>AV</TH>\n" +
        "                <TH>SKILLS</TH>\n" +
        "                <TH>INJ</TH>\n" +
        "                <TH>COMP</TH>\n" +
        "                <TH>TD</TH>\n" +
        "                <TH>INT</TH>\n" +
        "                <TH>CAS</TH>\n" +
        "                <TH>MVP</TH>\n" +
        "                <TH>SPP</TH>\n" +
        "                <TH>VALUE</TH>\n" +
        "        </TR>\n" +
        "        <TR ALIGN=CENTER>\n" +
        "                <TD>1</TD>\n" +
        "                <TD>Unnamed</TD>\n" +
        "                <TD>Beastman</TD>\n" +
        "                <TD>6</TD>\n" +
        "                <TD>3</TD>\n" +
        "                <TD>3</TD>\n" +
        "                <TD>8</TD>\n" +
        "                <TD>Horns</TD>\n" +
        "                <TD></TD>\n" +
        "                <TD>0</TD>\n" +
        "                <TD>0</TD>\n" +
        "                <TD>0</TD>\n" +
        "                <TD>0</TD>\n" +
        "                <TD>0</TD>\n" +
        "                <TD>0</TD>\n" +
        "                <TD>60 kgp</TD>\n" +
        "        </TR>\n" +
        "        <TR ALIGN=CENTER>\n" +
        "                <TD>2</TD>\n" +
        "                <TD>Unnamed</TD>\n" +
        "                <TD>Beastman</TD>\n" +
        "                <TD>6</TD>\n" +
        "                <TD>3</TD>\n" +
        "                <TD>3</TD>\n" +
        "                <TD>8</TD>\n" +
        "                <TD>Horns</TD>\n" +
        "                <TD></TD>\n" +
        "                <TD>0</TD>\n" +
        "                <TD>0</TD>\n" +
        "                <TD>0</TD>\n" +
        "                <TD>0</TD>\n" +
        "                <TD>0</TD>\n" +
        "                <TD>0</TD>\n" +
        "                <TD>60 kgp</TD>\n" +
        "        </TR>\n" +
        "        <TR ALIGN=CENTER>\n" +
        "                <TD>3</TD>\n" +
        "                <TD>Unnamed</TD>\n" +
        "                <TD>Beastman</TD>\n" +
        "                <TD>6</TD>\n" +
        "                <TD>3</TD>\n" +
        "                <TD>3</TD>\n" +
        "                <TD>8</TD>\n" +
        "                <TD>Horns</TD>\n" +
        "                <TD></TD>\n" +
        "                <TD>0</TD>\n" +
        "                <TD>0</TD>\n" +
        "                <TD>0</TD>\n" +
        "                <TD>0</TD>\n" +
        "                <TD>0</TD>\n" +
        "                <TD>0</TD>\n" +
        "                <TD>60 kgp</TD>\n" +
        "        </TR>\n" +
        "        <TR ALIGN=CENTER>\n" +
        "                <TD>4</TD>\n" +
        "                <TD>Unnamed</TD>\n" +
        "                <TD>Beastman</TD>\n" +
        "                <TD>6</TD>\n" +
        "                <TD>3</TD>\n" +
        "                <TD>3</TD>\n" +
        "                <TD>8</TD>\n" +
        "                <TD>Horns</TD>\n" +
        "                <TD></TD>\n" +
        "                <TD>0</TD>\n" +
        "                <TD>0</TD>\n" +
        "                <TD>0</TD>\n" +
        "                <TD>0</TD>\n" +
        "                <TD>0</TD>\n" +
        "                <TD>0</TD>\n" +
        "                <TD>60 kgp</TD>\n" +
        "        </TR>\n" +
        "        <TR ALIGN=CENTER>\n" +
        "                <TD>5</TD>\n" +
        "                <TD>Unnamed</TD>\n" +
        "                <TD>Beastman</TD>\n" +
        "                <TD>6</TD>\n" +
        "                <TD>3</TD>\n" +
        "                <TD>3</TD>\n" +
        "                <TD>8</TD>\n" +
        "                <TD>Horns</TD>\n" +
        "                <TD></TD>\n" +
        "                <TD>0</TD>\n" +
        "                <TD>0</TD>\n" +
        "                <TD>0</TD>\n" +
        "                <TD>0</TD>\n" +
        "                <TD>0</TD>\n" +
        "                <TD>0</TD>\n" +
        "                <TD>60 kgp</TD>\n" +
        "        </TR>\n" +
        "        <TR ALIGN=CENTER>\n" +
        "                <TD>6</TD>\n" +
        "                <TD>Unnamed</TD>\n" +
        "                <TD>Beastman</TD>\n" +
        "                <TD>6</TD>\n" +
        "                <TD>3</TD>\n" +
        "                <TD>3</TD>\n" +
        "                <TD>8</TD>\n" +
        "                <TD>Horns</TD>\n" +
        "                <TD></TD>\n" +
        "                <TD>0</TD>\n" +
        "                <TD>0</TD>\n" +
        "                <TD>0</TD>\n" +
        "                <TD>0</TD>\n" +
        "                <TD>0</TD>\n" +
        "                <TD>0</TD>\n" +
        "                <TD>60 kgp</TD>\n" +
        "        </TR>\n" +
        "        <TR ALIGN=CENTER>\n" +
        "                <TD>7</TD>\n" +
        "                <TD>Unnamed</TD>\n" +
        "                <TD>Beastman</TD>\n" +
        "                <TD>6</TD>\n" +
        "                <TD>3</TD>\n" +
        "                <TD>3</TD>\n" +
        "                <TD>8</TD>\n" +
        "                <TD>Horns</TD>\n" +
        "                <TD></TD>\n" +
        "                <TD>0</TD>\n" +
        "                <TD>0</TD>\n" +
        "                <TD>0</TD>\n" +
        "                <TD>0</TD>\n" +
        "                <TD>0</TD>\n" +
        "                <TD>0</TD>\n" +
        "                <TD>60 kgp</TD>\n" +
        "        </TR>\n" +
        "        <TR ALIGN=CENTER>\n" +
        "                <TD>10</TD>\n" +
        "                <TD>Unnamed</TD>\n" +
        "                <TD>Chaos Warrior</TD>\n" +
        "                <TD>5</TD>\n" +
        "                <TD>4</TD>\n" +
        "                <TD>3</TD>\n" +
        "                <TD>9</TD>\n" +
        "                <TD></TD>\n" +
        "                <TD></TD>\n" +
        "                <TD>0</TD>\n" +
        "                <TD>0</TD>\n" +
        "                <TD>0</TD>\n" +
        "                <TD>0</TD>\n" +
        "                <TD>0</TD>\n" +
        "                <TD>0</TD>\n" +
        "                <TD>100 kgp</TD>\n" +
        "        </TR>\n" +
        "        <TR ALIGN=CENTER>\n" +
        "                <TD>11</TD>\n" +
        "                <TD>Unnamed</TD>\n" +
        "                <TD>Chaos Warrior</TD>\n" +
        "                <TD>5</TD>\n" +
        "                <TD>4</TD>\n" +
        "                <TD>3</TD>\n" +
        "                <TD>9</TD>\n" +
        "                <TD></TD>\n" +
        "                <TD></TD>\n" +
        "                <TD>0</TD>\n" +
        "                <TD>0</TD>\n" +
        "                <TD>0</TD>\n" +
        "                <TD>0</TD>\n" +
        "                <TD>0</TD>\n" +
        "                <TD>0</TD>\n" +
        "                <TD>100 kgp</TD>\n" +
        "        </TR>\n" +
        "        <TR ALIGN=CENTER>\n" +
        "                <TD>12</TD>\n" +
        "                <TD>Unnamed</TD>\n" +
        "                <TD>Chaos Warrior</TD>\n" +
        "                <TD>5</TD>\n" +
        "                <TD>4</TD>\n" +
        "                <TD>3</TD>\n" +
        "                <TD>9</TD>\n" +
        "                <TD></TD>\n" +
        "                <TD></TD>\n" +
        "                <TD>0</TD>\n" +
        "                <TD>0</TD>\n" +
        "                <TD>0</TD>\n" +
        "                <TD>0</TD>\n" +
        "                <TD>0</TD>\n" +
        "                <TD>0</TD>\n" +
        "                <TD>100 kgp</TD>\n" +
        "        </TR>\n" +
        "        <TR ALIGN=CENTER>\n" +
        "                <TD>30</TD>\n" +
        "                <TD>Unnamed</TD>\n" +
        "                <TD>Minotaur Lord</TD>\n" +
        "                <TD>5</TD>\n" +
        "                <TD>5</TD>\n" +
        "                <TD>2</TD>\n" +
        "                <TD>8</TD>\n" +
        "                <TD>Loner, Frenzy, Horns, Mighty Blow, Thick Skull, Wild Animal, Leader\n" +
        "                </TD>\n" +
        "                <TD></TD>\n" +
        "                <TD>0</TD>\n" +
        "                <TD>0</TD>\n" +
        "                <TD>0</TD>\n" +
        "                <TD>0</TD>\n" +
        "                <TD>0</TD>\n" +
        "                <TD>0</TD>\n" +
        "                <TD>150 kgp</TD>\n" +
        "        </TR>\n" +
        "        <TR ALIGN=CENTER>\n" +
        "                <TD></TD>\n" +
        "                <TD ALIGN=RIGHT BGCOLOR='SILVER'>Team:</TD>\n" +
        "                <TD COLSPAN=5>Angrytown</TD>\n" +
        "                <TD ALIGN=RIGHT BGCOLOR='SILVER' COLSPAN=5>RE-ROLLS:</TD>\n" +
        "                <TD>2</TD>\n" +
        "                <TD COLSPAN=2 BGCOLOR='SILVER'> x 60 kgp =</TD>\n" +
        "                <TD>120 Kgp</TD>\n" +
        "        </TR>\n" +
        "        <TR ALIGN=CENTER>\n" +
        "                <TD></TD>\n" +
        "                <TD ALIGN=RIGHT BGCOLOR='SILVER'>Race:</TD>\n" +
        "                <TD COLSPAN=5> Chaos</TD>\n" +
        "                <TD ALIGN=RIGHT BGCOLOR='SILVER' COLSPAN=5>FAN FACTOR:</TD>\n" +
        "                <TD>0</TD>\n" +
        "                <TD COLSPAN=2 BGCOLOR='SILVER'> x 10 kgp =</TD>\n" +
        "                <TD>0 kgp</TD>\n" +
        "        </TR>\n" +
        "        <TR ALIGN=CENTER>\n" +
        "                <TD></TD>\n" +
        "                <TD ALIGN=RIGHT BGCOLOR='SILVER'>Team Value:</TD>\n" +
        "                <TD COLSPAN=5>99</TD>\n" +
        "                <TD ALIGN=RIGHT BGCOLOR='SILVER' COLSPAN=5>ASSISTANT COACHES:</TD>\n" +
        "                <TD>0</TD>\n" +
        "                <TD COLSPAN=2 BGCOLOR='SILVER'> x 10 kgp =</TD>\n" +
        "                <TD>0 kgp&nbsp</TD>\n" +
        "        </TR>\n" +
        "        <TR ALIGN=CENTER>\n" +
        "                <TD></TD>\n" +
        "                <TD ALIGN=RIGHT BGCOLOR='SILVER'>Bank:</TD>\n" +
        "                <TD COLSPAN=5>&nbsp</TD>\n" +
        "                <TD ALIGN=RIGHT BGCOLOR='SILVER' COLSPAN=5>CHEERLEADERS:</TD>\n" +
        "                <TD>0</TD>\n" +
        "                <TD COLSPAN=2 BGCOLOR='SILVER'> x 10 kgp =</TD>\n" +
        "                <TD>0 Kgp</TD>\n" +
        "        </TR>\n" +
        "        <TR ALIGN=CENTER>\n" +
        "                <TD></TD>\n" +
        "                <TD ALIGN=RIGHT BGCOLOR='SILVER'>Coach:</TD>\n" +
        "                <TD COLSPAN=5>danj3000</TD>\n" +
        "                <TD ALIGN=RIGHT BGCOLOR='SILVER' COLSPAN=5>APOTHECARY:</TD>\n" +
        "                <TD>&nbsp0</TD>\n" +
        "                <TD COLSPAN=2 BGCOLOR='SILVER'> x50 kgp =</TD>\n" +
        "                <TD>0 Kgp&nbsp</TD>\n" +
        "        </TR>\n" +
        "        <TR ALIGN=CENTER>\n" +
        "                <TD></TD>\n" +
        "                <TD ALIGN=RIGHT BGCOLOR='SILVER'>Email:</TD>\n" +
        "                <TD COLSPAN=5>&nbsp</TD>\n" +
        "                <TD ALIGN=RIGHT BGCOLOR='SILVER' COLSPAN=5> </TD>\n" +
        "                <TD> </TD>\n" +
        "                <TD COLSPAN=2 BGCOLOR='SILVER'>Treasury:</TD>\n" +
        "                <TD>10 kgp</TD>\n" +
        "        </TR>\n" +
        "</TABLE>\n";
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void loadTeam() {
        Team team = NtbblTeamReader.loadTeam(input);
        assertNotNull(team);
        assertEquals(11, team.getPlayers().size());
        assertEquals("Beastman", team.getPlayers().get(0).getPosition());
        Player player = team.getPlayers().get(1);
        assertEquals("2", player.getNumber());
        assertEquals("Chaos", team.getRace());
        assertEquals("danj3000", team.getCoach());
        assertEquals("Angrytown", team.getName());
        assertEquals(6, player.getMovement());
        assertEquals(3, player.getStrength());
        assertEquals(3, player.getAgility());
        assertEquals(8, player.getArmour());
        assertEquals("Unnamed", player.getName());
        assertEquals("Horns", player.getSkills());

    }
}