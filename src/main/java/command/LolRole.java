package command;

public enum LolRole {

    ASSASSIN,
    FIGHTER,
    MAGE,
    MARKSMAN,
    SUPPORT,
    TANK;

    public static LolRole getRoleByName(final String roleName) {
        for (final LolRole value : values()) {
            if (value.name().equalsIgnoreCase(roleName)) {
                return value;
            }
        }
        return null;
    }

}
