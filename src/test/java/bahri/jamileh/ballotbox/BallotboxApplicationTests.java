package bahri.jamileh.ballotbox;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BallotboxApplicationTests {

    private BallotBoxGenesis ballotBoxGenesis ;

    @Before
    public void init(){
        Map<String,Integer> ballotbox = new HashMap<>();
        ballotbox.put("1" , 1);
        ballotbox.put("2" , 2);

        Set<Integer> candidates = new HashSet<>();
        candidates.add(1);
        candidates.add(2);
        candidates.add(3);
        candidates.add(4);
        candidates.add(5);
        candidates.add(6);
        candidates.add(7);
        candidates.add(8);
        candidates.add(9);
        candidates.add(10);

        ballotBoxGenesis = new BallotBoxGenesis(1,5,ballotbox,
                4, LocalDateTime.now(),LocalDateTime.now(),candidates);
    }

    @Test
    public void testGenerateVotes(){

        Manager manager= new Manager();

        manager.generateAndSendVotes();
    }

    @Test
    public void contextLoads() {
    }
}
