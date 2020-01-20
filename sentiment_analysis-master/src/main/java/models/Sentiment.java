package models;

import javax.annotation.Generated;

@Generated("io.t28.json2java.core.JavaConverter")
@SuppressWarnings("all")

public class Sentiment {
    private final Probability probability;

    private final String label;

    public Sentiment(Probability probability, String label) {
        this.probability = probability;
        this.label = label;
    }

    public Probability getProbability() {
        return probability;
    }

    public String getLabel() {
        return label;
    }

}
