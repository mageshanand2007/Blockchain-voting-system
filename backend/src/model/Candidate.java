package model;
import interfaces.Votable;
public class Candidate implements Votable {
    private String id; private String name; private String party; private String description; private String symbol;
    public Candidate(String id, String name, String party, String description, String symbol) {
        this.id = id; this.name = name; this.party = party; this.description = description; this.symbol = symbol;
    }
    @Override public String getCandidateId() { return id; }
    @Override public String getName() { return name; }
    public String getParty() { return party; }
    public String getDescription() { return description; }
    public String getSymbol() { return symbol; }
}
