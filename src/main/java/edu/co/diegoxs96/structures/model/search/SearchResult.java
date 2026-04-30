package edu.co.diegoxs96.structures.model.search;

public class SearchResult {

    private boolean found;
    private int steps;
    private long time;

    public SearchResult(boolean found, int steps, long time) {
        this.found = found;
        this.steps = steps;
        this.time = time;
    }

    public boolean isFound() {
        return found;
    }

    public int getSteps() {
        return steps;
    }

    public long getTime() {
        return time;
    }
}