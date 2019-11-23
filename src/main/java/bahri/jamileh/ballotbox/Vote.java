package bahri.jamileh.ballotbox;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Set;


public class Vote {

    final private int electionId;

    final private String voteId;

    final private String ballotBoxId;

    final private LocalDateTime issuedTime;

    final private Set<Integer> candidates;


    public Vote(int electionId, String voteId, String ballotBoxId, LocalDateTime issuedTime, Set<Integer> candidates) {
        this.electionId = electionId;
        this.voteId = voteId;
        this.ballotBoxId = ballotBoxId;
        this.issuedTime = issuedTime;
        this.candidates = candidates;
    }

    public int getElectionId() {
        return electionId;
    }

    public String getVoteId() {
        return voteId;
    }

    public String getBallotBoxId() {
        return ballotBoxId;
    }

    public LocalDateTime getIssuedTime() {
        return issuedTime;
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vote votes = (Vote) o;
        return Objects.equals(getVoteId(), votes.getVoteId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getVoteId());
    }

    @Override
    public String toString() {
        return "Vote{" +
                ", electionId=" + electionId +
                ", voteId='" + voteId + '\'' +
                ", ballotBoxId='" + ballotBoxId + '\'' +
                ", issuedTime=" + issuedTime +
                ", candidates=" + candidates +
                '}';
    }
}
