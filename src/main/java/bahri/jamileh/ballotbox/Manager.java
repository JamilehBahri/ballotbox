package bahri.jamileh.ballotbox;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedTransferQueue;
import java.util.concurrent.TransferQueue;

@Component("ballotboxmanager")
public class Manager {

    private  final Logger LOGGER = LoggerFactory.getLogger(Manager.class);

    private TransferQueue<String> transferQueue = new LinkedTransferQueue<>();

    private Thread generateVotesThread = new Thread(this::generateAndSendVotes);

    @Value("${BallotBox.genesis.topicname}")
    private String ballotBoxGenesisTopic;

    @Value("${Vote.topicname}")
    private String voteTopic;

    @Value("${boxid}")
    private int ballotBoxId;

    @Autowired
    private JmsTemplate jmsTemplate;

    private BallotBoxGenesis ballotBoxGenesis = new BallotBoxGenesis();

    private  Set<Integer> candidates ;

    private CountDownLatch latch = new CountDownLatch(2);

    public CountDownLatch getLatch() {
        return latch;
    }


    @PostConstruct
    public void init(){
        generateVotesThread.start();
    }

    @PreDestroy
    public void finishing(){
        generateVotesThread.interrupt();
        try {
            generateVotesThread.join(30000);
        }catch (InterruptedException e){
            e.printStackTrace();
        }
    }

    @JmsListener(destination = "${BallotBox.genesis.topicname}")
    public void receiveGenesis(String message) {

        try {
            LOGGER.info("'Ballot Box ' received message='{}'", message);
            transferQueue.transfer(message);
        }catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void generateAndSendVotes () {

        try {
            ballotBoxGenesis = convertJsonToObject(transferQueue.take());
            int offset = ballotBoxGenesis.getMaxGenerateVotes()/ballotBoxGenesis.getBallotBox().size();
            int startvoteNumber= ballotBoxGenesis.getBallotBox().get(ballotBoxId+"");

            for (int i = startvoteNumber ; i < startvoteNumber + offset ; i++) {
                Vote vote = new Vote(ballotBoxGenesis.getElectionId(), i + "",
                        ballotBoxId + "", LocalDateTime.now(),
                        generateRandomCandidate(
                                ballotBoxGenesis.getCandidateChoose(), ballotBoxGenesis.getCandidates())
                );
                sendVotes(convertObjectToJson(vote));
                LOGGER.debug("BallotBox : '{}'  send vote Id = '{}' to candidates", i, vote.getVoteId());
            }
        }catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void sendVotes(String vote) throws InterruptedException{

        Thread.sleep(1000);
        jmsTemplate.convertAndSend(voteTopic, vote);
        LOGGER.info("sending vote='{}'  from ballot box Id='{} to destination='{}'", vote,ballotBoxId,voteTopic );
    }

    public  String convertObjectToJson(Object obj){
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();
        String jsonString = gson.toJson(obj);
        System.out.println(jsonString);
        return jsonString;
    }

    public  BallotBoxGenesis convertJsonToObject(String jsonString){

        if(jsonString !=null){
            GsonBuilder gsonBuilder = new GsonBuilder();
            Gson gson = gsonBuilder.create();
            return gson.fromJson(jsonString,BallotBoxGenesis.class);

        } else
            return null;

    }

    public Set<Integer> generateRandomCandidate(int numberChoose , Set<Integer> candidateList){
        candidates =new HashSet<>();
        for (int j = 1 ; j<= numberChoose;j++) {
            int sizeSet = candidateList.size();
            int item = new Random().nextInt(sizeSet);
            candidates.add(item);
        }
        return candidates;
    }

}
