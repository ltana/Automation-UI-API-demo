package project.api.helpers;

import io.cucumber.java.ParameterType;

public class ParametersHelper {
    @ParameterType("empty|space")
    public String emptySpaceType(String emptySpaceType) throws Exception {
        switch (emptySpaceType) {
            case "empty" -> {
                return "";
            }
            case "space" -> {
                return " ";
            }
            default -> throw new Exception("Type is not supported");
        }
    }
}
