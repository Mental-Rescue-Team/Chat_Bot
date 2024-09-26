package MentalCare.ChatBot.domain.Member.Role;

public enum Role {
    ADMIN("this is admin"),
    USER("this is user");

    private final String description;

    Role(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
