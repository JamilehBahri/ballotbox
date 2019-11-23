package bahri.jamileh.ballotbox;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.*;

public class VotePublisher {

  private static final Logger LOGGER =
      LoggerFactory.getLogger(VotePublisher.class);

  private String clientId;
  private Connection connection;
  private Session session;
  private MessageProducer messageProducer;

  public void create(String clientId, String topicName)
      throws JMSException {
    this.clientId = clientId;

    // create a Connection Factory
    ConnectionFactory connectionFactory =
        new ActiveMQConnectionFactory(
            ActiveMQConnection.DEFAULT_BROKER_URL);

    // create a Connection
    connection = connectionFactory.createConnection();
    connection.setClientID(clientId);

    // create a Session
    session =
        connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

    // create the Topic to which messages will be sent
    Topic topic = session.createTopic(topicName);

    // create a MessageProducer for sending messages
    messageProducer = session.createProducer(topic);
  }

  public void closeConnection() throws JMSException {
    connection.close();
  }

  public void send_Votes(String VoteJson)
      throws JMSException {

    // create a JMS TextMessage
    TextMessage textMessage = session.createTextMessage(VoteJson);

    // send the message to the topic destination
    messageProducer.send(textMessage);

    LOGGER.debug(clientId + ": sent Vote with text='{}'", VoteJson);
  }
}
