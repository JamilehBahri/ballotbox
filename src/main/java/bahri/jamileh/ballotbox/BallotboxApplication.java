package bahri.jamileh.ballotbox;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BallotboxApplication {

    private static final Logger LOGGER =
            LoggerFactory.getLogger(BallotboxApplication.class);

    public static void main(String[] args) {

        SpringApplication.run(BallotboxApplication.class, args);
        LOGGER.info("BallotBox Wait To Received Genesis And Generate Votes And Send It To Candidates  . . . . ");

    }

}
