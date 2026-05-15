package server.atlassian.v1.model.bitbucket;

/**
 * Bitbucket pull request lifecycle states.
 */
public enum PullRequestState {

    /** PR is open and awaiting review or merge. */
    OPEN("OPEN"),

    /** PR has been merged into the target branch. */
    MERGED("MERGED"),

    /** PR was declined / rejected. */
    DECLINED("DECLINED"),

    /** PR was superseded by another PR. */
    SUPERSEDED("SUPERSEDED");

    private final String apiValue;

    PullRequestState(final String apiValue) {
        this.apiValue = apiValue;
    }

    /**
     * Returns the Bitbucket REST API value.
     *
     * @return the API string (e.g., "OPEN", "MERGED")
     */
    public String getApiValue() {
        return apiValue;
    }

    /**
     * Resolves a state from the Bitbucket API value.
     *
     * @param value the API value (case-insensitive)
     * @return the matching state
     * @throws IllegalArgumentException if no match is found
     */
    public static PullRequestState fromApiValue(final String value) {
        for (final PullRequestState state : values()) {
            if (state.apiValue.equalsIgnoreCase(value)) {
                return state;
            }
        }
        throw new IllegalArgumentException("Unknown pull request state: '" + value
                + "'. Valid values: OPEN, MERGED, DECLINED, SUPERSEDED");
    }
}
