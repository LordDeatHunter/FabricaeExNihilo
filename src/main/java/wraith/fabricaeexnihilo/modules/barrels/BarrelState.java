package wraith.fabricaeexnihilo.modules.barrels;

public enum BarrelState {
    EMPTY,
    FLUID,
    ITEM,
    COMPOST;

    public static BarrelState byId(String id) {
        return switch (id) {
            case "fluid" -> FLUID;
            case "item" -> ITEM;
            case "compost" -> COMPOST;
            default -> EMPTY;
        };
    }

    public String getId() {
        return switch (this) {
            case EMPTY -> "empty";
            case FLUID -> "fluid";
            case ITEM -> "item";
            case COMPOST -> "compost";
        };
    }
}
