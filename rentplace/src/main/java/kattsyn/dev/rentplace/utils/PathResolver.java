package kattsyn.dev.rentplace.utils;

import kattsyn.dev.rentplace.enums.ImageType;

public class PathResolver {
    public static String resolvePath(ImageType imageType, long entityId) {
        return switch (imageType){
            case USER -> "/users/" + entityId + "/";
            case PROPERTY -> "/properties/" + entityId + "/";
            case FACILITY -> "/facilities/" + entityId + "/";
            case CATEGORY -> "/categories/" + entityId + "/";
        };
    }
}
